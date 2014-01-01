package jadex.agentkeeper.ai.base.claimSector;

import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.ai.AbstractBeingBDI.AchieveMoveToSector;
import jadex.agentkeeper.ai.creatures.AbstractCreatureBDI;
import jadex.agentkeeper.ai.imp.ImpBDI;
import jadex.agentkeeper.ai.imp.ImpBDI.AchieveClaimSector;
import jadex.agentkeeper.game.state.map.TileChanger;
import jadex.agentkeeper.game.state.missions.Task;
import jadex.agentkeeper.game.state.missions.TaskPoolManager;
import jadex.agentkeeper.game.state.missions.TaskType;
import jadex.agentkeeper.game.state.player.SimplePlayerState;
import jadex.agentkeeper.util.ISO;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.Neighborcase;
import jadex.agentkeeper.util.Neighborhood;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;
import jadex.bdiv3.annotation.PlanReason;
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
public class ClaimSectorPlan {
	
	private static final String IMP_LOCAL_TASK = "ImpLocalTask";

	@PlanCapability
	protected AbstractBeingBDI capa;
	
	@PlanCapability
	protected ImpBDI impBdi;

	@PlanAPI
	protected IPlan iplan;

	@PlanAPI
	protected IPlan rplan;

	private Object digtaskid;

	private Grid2D environment;

	private SpaceObject currentTaskSpaceObject;
	
	private SimplePlayerState playerState;
	
	@PlanReason
	protected AchieveClaimSector	goal;

	@PlanBody
	public IFuture<Void> body() {
		
		final Future<Void> retb = new Future<Void>();
		
		Task newImpTask = goal.getTarget();
		if (newImpTask != null) {
			System.out.println(newImpTask.getTaskType());
			environment = capa.getEnvironment();
			playerState = (SimplePlayerState) environment.getProperty(ISO.Objects.PLAYER_STATE);
			capa.getMySpaceObject().setProperty(IMP_LOCAL_TASK, newImpTask);

			reachTargetDestination(newImpTask).addResultListener(new DelegationResultListener<Void>(retb));
		} else {
			System.out.println("The Task was null, this should not happen. But we don't break up.");
			retb.setResult(null);
		}
		return retb;
	}

	private IFuture<Void> reachTargetDestination(final Task currentImpTask) {
		final Future<Void> ret = new Future<Void>();

		final Vector2Int currentImpTaskPosition = currentImpTask.getTargetPosition();

		Collection<ISpaceObject> spaceObjectsByGridPosition = environment.getSpaceObjectsByGridPosition(currentImpTask.getTargetPosition(), null);
		for (ISpaceObject spaceObject : spaceObjectsByGridPosition) {
			if (MapType.DIRT_PATH.toString().equals(spaceObject.getType())) {
				currentTaskSpaceObject = (SpaceObject) spaceObject;
			}
		}

		if (currentImpTaskPosition != null) {
			// went to the position where the imp can dig from
			if (currentImpTask.getTargetPosition() != null) {
				IFuture<AchieveMoveToSector> reachSectorToDigFrom = rplan.dispatchSubgoal(capa.new AchieveMoveToSector(currentImpTask.getTargetPosition()));

				reachSectorToDigFrom.addResultListener(new ExceptionDelegationResultListener<AbstractCreatureBDI.AchieveMoveToSector, Void>(ret) {
					public void customResultAvailable(AbstractCreatureBDI.AchieveMoveToSector amt) {
						claimSector(currentImpTask).addResultListener(new DelegationResultListener<Void>(ret) {
							public void customResultAvailable(Void result) {
								// add new Tile and remove the old, claim the sector ground
								TileChanger tilechanger = new TileChanger(environment);
								
								String neighborhood = (String) currentTaskSpaceObject.getProperty(ISO.Properties.NEIGHBORHOOD);
								tilechanger.addParameter("bearbeitung", new Integer(0)).addParameter(ISO.Properties.STATUS, "byImpCreated").addParameter(ISO.Properties.CLICKED, false)
										.addParameter(ISO.Properties.LOCKED, false).addParameter(ISO.Properties.NEIGHBORHOOD, neighborhood)
										.addParameter(ISO.Properties.INTPOSITION, currentImpTaskPosition)
										.addParameter(ISO.Properties.DOUBLE_POSITION, new Vector2Double(currentImpTaskPosition.getXAsDouble(), currentImpTaskPosition.getYAsDouble()))
										.changeTile(currentImpTask.getTargetPosition(), MapType.CLAIMED_PATH, new ArrayList<MapType>(Arrays.asList(MapType.DIRT_PATH)));

								// imp stop claiming the sector ground
								capa.getMySpaceObject().setProperty(ISObjStrings.PROPERTY_STATUS, "Idle");
								
								playerState.addClaimedSector();
								
								TaskPoolManager taskPoolManager = (TaskPoolManager) capa.getEnvironment().getProperty(TaskPoolManager.PROPERTY_NAME);
								for(ISpaceObject neighbour : Neighborhood.getNeighborSpaceObjects(currentImpTaskPosition, environment, Neighborcase.getDefault() )) {
									if(Neighborhood.isClaimableWall(neighbour) && !(Boolean)neighbour.getProperty(ISO.Properties.CLICKED)) {
										taskPoolManager.addConnectedTask(TaskType.CLAIM_WALL, (Vector2Int) neighbour.getProperty(ISO.Properties.INTPOSITION));
									}
								}

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

	private IFuture<Void> claimSector(final Task currentImpTask) {
		final Future<Void> ret = new Future<Void>();

		Map<String, Object> props = new HashMap<String, Object>();
		props.put(ClaimSectorTask.PROPERTY_DESTINATION, currentImpTask.getTargetPosition());
		props.put(ClaimSectorTask.PROPERTY_DIG_SPEED, impBdi.getMyWorkingSpeed());

		digtaskid = capa.getEnvironment().createObjectTask(ClaimSectorTask.PROPERTY_TYPENAME, props, capa.getMySpaceObject().getId());
		iplan.invokeInterruptable(new IResultCommand<IFuture<Void>, Void>() {
			public IFuture<Void> execute(Void args) {
				return capa.getEnvironment().waitForTask(digtaskid, capa.getMySpaceObject().getId());
			}
		}).addResultListener(new DelegationResultListener<Void>(ret));

		return ret;
	}

}
