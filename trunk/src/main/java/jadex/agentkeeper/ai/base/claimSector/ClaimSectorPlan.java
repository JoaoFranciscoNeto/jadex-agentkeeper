package jadex.agentkeeper.ai.base.claimSector;

import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.ai.AbstractBeingBDI.AchieveMoveToSector;
import jadex.agentkeeper.ai.creatures.AbstractCreatureBDI;
import jadex.agentkeeper.ai.imp.ImpBDI;
import jadex.agentkeeper.ai.imp.ImpBDI.AchieveClaimSector;
import jadex.agentkeeper.game.state.missions.Task;
import jadex.agentkeeper.util.ISObjStrings;
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
import jadex.extension.envsupport.math.Vector2Int;

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
	protected IPlan cplan;

	@PlanAPI
	protected IPlan rplan;

	private Object claimtaskid;
	
	private Object digtaskid;
	

	private Grid2D environment;

	private SpaceObject currentTaskSpaceObject;
	
	@PlanReason
	protected AchieveClaimSector	goal;

	@PlanBody
	public IFuture<Void> body() {
		
		final Future<Void> retb = new Future<Void>();
		
		Task newImpTask = goal.getTarget();
		if (newImpTask != null) {
			environment = capa.getEnvironment();
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

		if (currentImpTaskPosition != null && currentTaskSpaceObject != null) {
			// went to the position where the imp can dig from
			if (currentImpTask.getTargetPosition() != null) {
				IFuture<AchieveMoveToSector> reachSectorToDigFrom = rplan.dispatchSubgoal(capa.new AchieveMoveToSector(currentImpTask.getTargetPosition()));

				reachSectorToDigFrom.addResultListener(new ExceptionDelegationResultListener<AbstractCreatureBDI.AchieveMoveToSector, Void>(ret) {
					public void customResultAvailable(AbstractCreatureBDI.AchieveMoveToSector amt) {
						claimSector(currentImpTask).addResultListener(new DelegationResultListener<Void>(ret) {
							public void customResultAvailable(Void result) {
								// add new Tile and remove the old, claim the sector ground
								
								Map<String, Object> props = new HashMap<String, Object>();
								props.put("Task", currentImpTask);
								props.put(ClaimSectorChangeTileTask.PROPERTY_DESTINATION, currentImpTask.getTargetPosition());
								claimtaskid = capa.getEnvironment().createObjectTask(ClaimSectorChangeTileTask.PROPERTY_TYPENAME, props, capa.getMySpaceObject().getId());
								cplan.invokeInterruptable(new IResultCommand<IFuture<Void>, Void>() {
									public IFuture<Void> execute(Void args) {
										return capa.getEnvironment().waitForTask(claimtaskid, capa.getMySpaceObject().getId());
									}
								}).addResultListener(new DelegationResultListener<Void>(ret));
								
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
			System.out.println("ClaimSectorPlan: Task has no Int Position");
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
