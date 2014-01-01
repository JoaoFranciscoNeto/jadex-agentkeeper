package jadex.agentkeeper.ai.base;

import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.ai.AbstractBeingBDI.AchieveMoveToSector;
import jadex.agentkeeper.ai.creatures.AbstractCreatureBDI;
import jadex.agentkeeper.game.state.map.TileChanger;
import jadex.agentkeeper.game.state.missions.Task;
import jadex.agentkeeper.game.state.missions.TaskPoolManager;
import jadex.agentkeeper.util.ISO;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.Neighborhood;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;
import jadex.bdiv3.runtime.IPlan;
import jadex.commons.IResultCommand;
import jadex.commons.future.DelegationResultListener;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Plan
public class GetImpWorkPlan {

	private static final String IMP_LOCAL_TASK = "ImpLocalTask";

	@PlanCapability
	protected AbstractBeingBDI capa;

	@PlanAPI
	protected IPlan iplan;

	@PlanAPI
	protected IPlan rplan;

	private Object digtaskid;

	private Grid2D environment;
	
	private SpaceObject currentTaskSpaceObject;

	@PlanBody
	public IFuture<Void> body() {

		// System.out.println("plan body claim sector");
		final Future<Void> retb = new Future<Void>();

		TaskPoolManager taskPoolManager = (TaskPoolManager) capa.getEnvironment().getProperty(TaskPoolManager.PROPERTY_NAME);
		if (taskPoolManager != null && taskPoolManager.getWorkableTaskListSize() > 0) {

			Task newImpTask = taskPoolManager.calculateAndReturnNextTask(new Vector2Int(capa.getMyPosition().getXAsInteger(), capa.getMyPosition().getYAsInteger()));
			if(newImpTask != null) {
				System.out.println(newImpTask);
				environment = capa.getEnvironment();
				capa.getMySpaceObject().setProperty(IMP_LOCAL_TASK, newImpTask);
	
				reachTargetDestination(newImpTask).addResultListener(new DelegationResultListener<Void>(retb));
			}
			else {
				retb.setResult(null);
			}
		} else {
			retb.setResult(null);
		}
		return retb;
	}



	private IFuture<Void> reachTargetDestination(final Task currentImpTask) {
		final Future<Void> ret = new Future<Void>();

		final Vector2Int currentImpTaskPosition = currentImpTask.getTargetPosition();

		Collection<ISpaceObject> spaceObjectsByGridPosition = environment.getSpaceObjectsByGridPosition(currentImpTask.getTargetPosition(), null);
		for (ISpaceObject spaceObject : spaceObjectsByGridPosition) {
			for (MapType mapType : MapType.getOnlySolids()) {
				if (mapType.toString().equals(spaceObject.getType())) {
					currentTaskSpaceObject = (SpaceObject) spaceObject;
				}
			}
		}

		if (currentImpTaskPosition != null) {
			Vector2Int reachableSectorForDigingInt = null;
			
			// get the position from which the imp can walk to and dig
			for (ISpaceObject spaceObject : Neighborhood.getNeighborSpaceObjects(currentImpTaskPosition, environment)) {
				if (Neighborhood.isWalkableForDigging(spaceObject)) {
					reachableSectorForDigingInt = (Vector2Int) spaceObject.getProperty(ISO.Properties.INTPOSITION);
					break;
				}
			}
			// went to the position where the imp can dig from
			if (reachableSectorForDigingInt != null) {
				IFuture<AchieveMoveToSector> reachSectorToDigFrom = rplan.dispatchSubgoal(capa.new AchieveMoveToSector(reachableSectorForDigingInt));

				reachSectorToDigFrom.addResultListener(new ExceptionDelegationResultListener<AbstractCreatureBDI.AchieveMoveToSector, Void>(ret) {
					public void customResultAvailable(AbstractCreatureBDI.AchieveMoveToSector amt) {
						digSector(currentImpTask).addResultListener(new DelegationResultListener<Void>(ret) {
							public void customResultAvailable(Void result) {
								// add new Tile and remove the old, to break the wall
								TileChanger tilechanger = new TileChanger(environment);
								String neighborhood = (String) currentTaskSpaceObject.getProperty(ISO.Properties.NEIGHBORHOOD);
								tilechanger.addParameter("bearbeitung", new Integer(0)).addParameter(ISO.Properties.STATUS, "byImpCreated").addParameter(ISO.Properties.CLICKED, false)
										   .addParameter(ISO.Properties.LOCKED, false).addParameter(ISO.Properties.NEIGHBORHOOD, neighborhood)
										   .addParameter(ISO.Properties.INTPOSITION, currentImpTaskPosition).addParameter(ISO.Properties.DOUBLE_POSITION, new Vector2Double(currentImpTaskPosition.getXAsDouble(), currentImpTaskPosition.getYAsDouble()))
										   .changeTile(currentImpTask.getTargetPosition(), MapType.DIRT_PATH,
										new ArrayList<MapType>(Arrays.asList(MapType.ROCK, MapType.GOLD, MapType.REINFORCED_WALL)));
								
								// imp stop digging
								capa.getMySpaceObject().setProperty(ISObjStrings.PROPERTY_STATUS, "Idle");
								
								// Neighbour update their tile as well, cause they now need to render a wall
								Neighborhood.updateMyNeighborsComplexField(currentImpTask.getTargetPosition(), environment);
								
								TaskPoolManager taskPoolManager = (TaskPoolManager) capa.getEnvironment().getProperty(TaskPoolManager.PROPERTY_NAME);
								taskPoolManager.updateReachableSelectedSectors(Neighborhood.getNeighborSpaceObjects(currentImpTaskPosition, environment));
								
								ret.setResult(null);
							}
						});
					}
				});
			} else {
				// TODO: fail!! sector from task should be reachable for destroy
				// => catch
				System.out.println("fail!! sector from task should be reachable for destroy ");
				capa.getMySpaceObject().setProperty(ISObjStrings.PROPERTY_STATUS, "Idle");
				rplan.abort();
			}
		} else {
			System.out.println("Task has no Int Position");
			rplan.abort();
		}
		return ret;
	}

	private IFuture<Void> digSector(final Task currentImpTask) {
		final Future<Void> ret = new Future<Void>();

		Map<String, Object> props = new HashMap<String, Object>();
		props.put(DigSectorTask.PROPERTY_DESTINATION, currentImpTask.getTargetPosition());
		props.put(DigSectorTask.PROPERTY_SPEED, capa.getMySpeed());

		digtaskid = capa.getEnvironment().createObjectTask(DigSectorTask.PROPERTY_TYPENAME, props, capa.getMySpaceObject().getId());
		iplan.invokeInterruptable(new IResultCommand<IFuture<Void>, Void>() {
			public IFuture<Void> execute(Void args) {
				return capa.getEnvironment().waitForTask(digtaskid, capa.getMySpaceObject().getId());
			}
		}).addResultListener(new DelegationResultListener<Void>(ret));

		return ret;
	}
}
