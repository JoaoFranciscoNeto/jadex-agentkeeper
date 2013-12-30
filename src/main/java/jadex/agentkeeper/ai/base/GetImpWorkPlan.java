package jadex.agentkeeper.ai.base;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.ai.AbstractBeingBDI.AchieveMoveToSector;
import jadex.agentkeeper.ai.creatures.AbstractCreatureBDI;
import jadex.agentkeeper.ai.enums.PlanType;
import jadex.agentkeeper.game.state.map.SimpleMapState;
import jadex.agentkeeper.game.state.map.TileChanger;
import jadex.agentkeeper.game.state.missions.Task;
import jadex.agentkeeper.game.state.missions.TaskPoolManager;
import jadex.agentkeeper.util.ISO;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.Neighborhood;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.TileInfo;
import jadex.agentkeeper.worldmodel.structure.building.HatcheryInfo;
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
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector2Int;

@Plan
public class GetImpWorkPlan {

	private static final String IMP_LOCAL_TASK = "ImpLocalTask";

	@PlanCapability
	protected AbstractBeingBDI capa;

	@PlanAPI
	protected IPlan iplan;

	@PlanAPI
	protected IPlan rplan;

	private Object mtaskid;

	private Object digtaskid;
	
	Grid2D environment;

	@PlanBody
	public IFuture<Void> body() {

		// System.out.println("plan body claim sector");
		final Future<Void> retb = new Future<Void>();

		TaskPoolManager taskPoolManager = (TaskPoolManager) capa.getEnvironment().getProperty(TaskPoolManager.PROPERTY_NAME);
		if (taskPoolManager != null && taskPoolManager.getTaskListSize() > 0) {
			
			System.out.println(capa.getMySpaceObject().getId());
			Task newImpTask = taskPoolManager.calculateAndReturnNextTask(new Vector2Int(capa.getMyPosition().getXAsInteger(), capa.getMyPosition().getYAsInteger()));
			environment = capa.getEnvironment();
			capa.getMySpaceObject().setProperty(IMP_LOCAL_TASK, newImpTask);

			System.out.println(capa.getMySpaceObject().getId());
			reachTargetDestination(newImpTask).addResultListener(new DelegationResultListener<Void>(retb));

		} else {
			retb.setResult(null);
		}
		return retb;
	}

	private IFuture<Void> reachTargetDestination(final Task currentImpTask) {
		final Future<Void> ret = new Future<Void>();

		final Vector2Int currentImpTaskPosition = currentImpTask.getTargetPosition();

		Collection<ISpaceObject> spaceObjectsByGridPosition = environment.getSpaceObjectsByGridPosition(currentImpTask.getTargetPosition(), null);
		for(ISpaceObject spaceObject : spaceObjectsByGridPosition){
			System.out.println("spaceObject.getType()2: "+spaceObject.getType());
		}
		
		if (currentImpTaskPosition != null) {
			Vector2Int reachableSectorForDigingInt =  null;
			for(ISpaceObject spaceObject : Neighborhood.getNeighborSpaceObjects(currentImpTaskPosition, environment)) {
				System.out.println(spaceObject);
				if(spaceObject.getType().equals(MapType.CLAIMED_PATH.getName()) || spaceObject.getType().equals(MapType.DIRT_PATH.getName()) ) {
					reachableSectorForDigingInt = (Vector2Int) spaceObject.getProperty(ISO.Properties.INTPOSITION);
					break;
				}
			}
			if(reachableSectorForDigingInt != null){
				IFuture<AchieveMoveToSector> fut = rplan.dispatchSubgoal(capa.new AchieveMoveToSector(reachableSectorForDigingInt));
	
				fut.addResultListener(new ExceptionDelegationResultListener<AbstractCreatureBDI.AchieveMoveToSector, Void>(ret) {
					public void customResultAvailable(AbstractCreatureBDI.AchieveMoveToSector amt) {
						digSector(currentImpTask).addResultListener(new DelegationResultListener<Void>(ret) {
							public void customResultAvailable(Void result) {
								
								
								TileChanger.changeTile(environment, currentImpTask.getTargetPosition());
								capa.getMySpaceObject().setProperty(ISObjStrings.PROPERTY_STATUS, "Idle");
								ret.setResult(null);
							}
						});
					}
				});
			} else {
				//TODO: fail!! sector from task should be reachable for destroy => catch
				System.out.println("fail!! sector from task should be reachable for destroy ");
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
