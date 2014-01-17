package jadex.agentkeeper.ai.base.collectGold;

import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.ai.AbstractBeingBDI.AchieveMoveToSector;
import jadex.agentkeeper.ai.base.claimSector.ClaimSectorChangeTileTask;
import jadex.agentkeeper.ai.creatures.AbstractCreatureBDI;
import jadex.agentkeeper.ai.imp.ImpBDI;
import jadex.agentkeeper.ai.imp.ImpBDI.AchieveCollectGold;
import jadex.agentkeeper.ai.imp.ImpBDI.AchieveFillTreasury;
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
public class CollectGoldPlan {

	private static final String IMP_LOCAL_TASK = "ImpLocalTask";

	@PlanCapability
	protected AbstractBeingBDI capa;

	@PlanCapability
	protected ImpBDI impBdi;

	@PlanAPI
	protected IPlan iplan;

	@PlanAPI
	protected IPlan rplan;
	
	@PlanAPI
	protected IPlan splan;
	
	@PlanAPI
	protected IPlan cplan;


	private Object digtaskid, claimtaskid;

	private Grid2D environment;

	private SpaceObject currentTaskSpaceObject;

	@PlanReason
	protected AchieveCollectGold goal;

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
			for (MapType mapType : MapType.getOnlySolids()) {
				if (mapType.toString().equals(spaceObject.getType())) {
					currentTaskSpaceObject = (SpaceObject) spaceObject;
				}
			}
		}

		if (currentImpTaskPosition != null && currentTaskSpaceObject != null) {

			// get the position from which the imp can walk to and dig
//			for (ISpaceObject spaceObject : Neighborhood.getNeighborSpaceObjects(currentImpTaskPosition, environment, Neighborcase.getDefault())) {
//				if (Neighborhood.isWalkableForDigging(spaceObject)) {
//					reachableSectorForDigingInt = (Vector2Int) spaceObject.getProperty(ISO.Properties.INTPOSITION);
//					break;
//				}
//			}
			// went to the position where the imp can dig from
			if (currentImpTask.getTargetPosition() != null) {
				IFuture<AchieveMoveToSector> reachSectorToDigFrom = rplan.dispatchSubgoal(capa.new AchieveMoveToSector(currentImpTask.getTargetPosition()));

				reachSectorToDigFrom.addResultListener(new ExceptionDelegationResultListener<AbstractCreatureBDI.AchieveMoveToSector, Void>(ret) {
					public void customResultAvailable(AbstractCreatureBDI.AchieveMoveToSector amt) {
						collectGold(currentImpTask).addResultListener(new DelegationResultListener<Void>(ret) {
							public void customResultAvailable(Void result) {
								// add new Tile and remove the old, claim the
								// sector ground
								Map<String, Object> props = new HashMap<String, Object>();
								props.put("Task", currentImpTask);
								props.put(ClaimSectorChangeTileTask.PROPERTY_DESTINATION, currentImpTask.getTargetPosition());
								claimtaskid = capa.getEnvironment().createObjectTask(CollectGoldChangeTileTask.PROPERTY_TYPENAME, props, capa.getMySpaceObject().getId());
								cplan.invokeInterruptable(new IResultCommand<IFuture<Void>, Void>() {
									public IFuture<Void> execute(Void args) {
										return capa.getEnvironment().waitForTask(claimtaskid, capa.getMySpaceObject().getId());
									}
								}).addResultListener(new DelegationResultListener<Void>(ret) {
									public void customResultAvailable(Void result) {
										IFuture<AchieveFillTreasury> fillTreasury = cplan.dispatchSubgoal(impBdi.new AchieveFillTreasury(currentImpTask));
										fillTreasury.addResultListener(new ExceptionDelegationResultListener<ImpBDI.AchieveFillTreasury, Void>(ret){

											@Override
											public void customResultAvailable(AchieveFillTreasury result) {
												ret.setResult(null);
											}
										} );
									}
								});
							}
						});
					}
				});
			} else {
				// TODO: fail!! sector from task should be reachable for destroy
				// => catch

				System.out.println("fail!! sector from task should be reachable for clame wall ");
				capa.getMySpaceObject().setProperty(ISObjStrings.PROPERTY_STATUS, "Idle");
				rplan.abort();
			}
		} else {
			System.out.println("Task has no current Object Space or IntPostion (ColledtGold)");
			rplan.abort();
		}
		return ret;
	}

	private IFuture<Void> collectGold(final Task currentImpTask) {
		final Future<Void> ret = new Future<Void>();

		Map<String, Object> props = new HashMap<String, Object>();
		props.put(CollectGoldTask.PROPERTY_DESTINATION, currentImpTask.getTargetPosition());
		props.put(CollectGoldTask.PROPERTY_DIG_SPEED, impBdi.getMyWorkingSpeed());

		digtaskid = capa.getEnvironment().createObjectTask(CollectGoldTask.PROPERTY_TYPENAME, props, capa.getMySpaceObject().getId());
		iplan.invokeInterruptable(new IResultCommand<IFuture<Void>, Void>() {
			public IFuture<Void> execute(Void args) {
				return capa.getEnvironment().waitForTask(digtaskid, capa.getMySpaceObject().getId());
			}
		}).addResultListener(new DelegationResultListener<Void>(ret));

		return ret;
	}
}
