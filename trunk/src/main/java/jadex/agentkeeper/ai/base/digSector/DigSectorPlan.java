package jadex.agentkeeper.ai.base.digSector;

import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.ai.AbstractBeingBDI.AchieveMoveToSector;
import jadex.agentkeeper.ai.creatures.AbstractCreatureBDI;
import jadex.agentkeeper.ai.imp.ImpBDI;
import jadex.agentkeeper.ai.imp.ImpBDI.AchieveDigSector;
import jadex.agentkeeper.game.state.missions.Task;
import jadex.agentkeeper.util.ISO;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.Neighborhood;
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
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.HashMap;
import java.util.Map;

@Plan
public class DigSectorPlan {
	
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

	private Object digtaskid;
	
	private Object claimtaskid;

	private Grid2D environment;

	
	@PlanReason
	protected AchieveDigSector	goal;

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
								
								Map<String, Object> props = new HashMap<String, Object>();
								props.put(DigSectorTask.PROPERTY_DESTINATION, currentImpTask.getTargetPosition());
								props.put("Task", currentImpTask);
								props.put(DigSectorTask.PROPERTY_DIG_SPEED, impBdi.getMyWorkingSpeed());
								claimtaskid = capa.getEnvironment().createObjectTask(ChangeTileTask.PROPERTY_TYPENAME, props, capa.getMySpaceObject().getId());
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
			System.out.println("DigSecotorPlan: Task has no Int Position");
			rplan.abort();
		}
		return ret;
	}

	private IFuture<Void> digSector(final Task currentImpTask) {
		final Future<Void> ret = new Future<Void>();

		Map<String, Object> props = new HashMap<String, Object>();
		props.put(DigSectorTask.PROPERTY_DESTINATION, currentImpTask.getTargetPosition());
		props.put(DigSectorTask.PROPERTY_DIG_SPEED, impBdi.getMyWorkingSpeed());

		digtaskid = capa.getEnvironment().createObjectTask(DigSectorTask.PROPERTY_TYPENAME, props, capa.getMySpaceObject().getId());
		iplan.invokeInterruptable(new IResultCommand<IFuture<Void>, Void>() {
			public IFuture<Void> execute(Void args) {
				return capa.getEnvironment().waitForTask(digtaskid, capa.getMySpaceObject().getId());
			}
		}).addResultListener(new DelegationResultListener<Void>(ret));

		return ret;
	}

}
