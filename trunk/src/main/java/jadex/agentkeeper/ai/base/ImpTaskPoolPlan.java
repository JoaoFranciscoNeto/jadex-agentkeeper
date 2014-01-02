package jadex.agentkeeper.ai.base;

import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.ai.imp.ImpBDI;
import jadex.agentkeeper.ai.imp.ImpBDI.AchieveClaimSector;
import jadex.agentkeeper.ai.imp.ImpBDI.AchieveClaimWall;
import jadex.agentkeeper.ai.imp.ImpBDI.AchieveCollectGold;
import jadex.agentkeeper.ai.imp.ImpBDI.AchieveDigSector;
import jadex.agentkeeper.game.state.missions.Task;
import jadex.agentkeeper.game.state.missions.TaskPoolManager;
import jadex.agentkeeper.game.state.missions.TaskType;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;
import jadex.bdiv3.runtime.IPlan;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.math.Vector2Int;

@Plan
public class ImpTaskPoolPlan {

	private static final String IMP_LOCAL_TASK = "ImpLocalTask";

	@PlanCapability
	protected AbstractBeingBDI capa;
	
	@PlanCapability
	protected ImpBDI impBdi;

	@PlanAPI
	protected IPlan iplan;

	@PlanAPI
	protected IPlan rplan;

	@PlanBody
	public IFuture<Void> body() {

		final Future<Void> retb = new Future<Void>();

		TaskPoolManager taskPoolManager = (TaskPoolManager) capa.getEnvironment().getProperty(TaskPoolManager.PROPERTY_NAME);
		if (taskPoolManager != null && taskPoolManager.getWorkableTaskListSize() > 0) {
			Task newImpTask = taskPoolManager.calculateAndReturnNextTask(new Vector2Int(capa.getMyPosition().getXAsInteger(), capa.getMyPosition().getYAsInteger()));
			if (newImpTask != null) {
				capa.getMySpaceObject().setProperty(IMP_LOCAL_TASK, newImpTask);
				if(newImpTask.getTaskType().equals(TaskType.Types.DIG_SECTOR)){
					IFuture<AchieveDigSector> reachSectorToDigFrom = rplan.dispatchSubgoal(impBdi.new AchieveDigSector(newImpTask));
					reachSectorToDigFrom.addResultListener(new ExceptionDelegationResultListener<ImpBDI.AchieveDigSector, Void>(retb){
						@Override
						public void customResultAvailable(AchieveDigSector result) {
							retb.setResult(null);
						}
					} );
				} else if(newImpTask.getTaskType().equals(TaskType.Types.CLAIM_SECTOR)){
					IFuture<AchieveClaimSector> reachSectorToClaimFrom = rplan.dispatchSubgoal(impBdi.new AchieveClaimSector(newImpTask));
					reachSectorToClaimFrom.addResultListener(new ExceptionDelegationResultListener<ImpBDI.AchieveClaimSector, Void>(retb){
						@Override
						public void customResultAvailable(AchieveClaimSector result) {
							retb.setResult(null);
						}
					} );
				} else if(newImpTask.getTaskType().equals(TaskType.Types.CLAIM_WALL)){
					IFuture<AchieveClaimWall> reachWallToClaimFrom = rplan.dispatchSubgoal(impBdi.new AchieveClaimWall(newImpTask));
					reachWallToClaimFrom.addResultListener(new ExceptionDelegationResultListener<ImpBDI.AchieveClaimWall, Void>(retb){

						@Override
						public void customResultAvailable(AchieveClaimWall result) {
							retb.setResult(null);
							
						}
					} );
				} else if(newImpTask.getTaskType().equals(TaskType.Types.COLLECT_GOLD)){
					IFuture<AchieveCollectGold> reachWallToClaimFrom = rplan.dispatchSubgoal(impBdi.new AchieveCollectGold(newImpTask));
					reachWallToClaimFrom.addResultListener(new ExceptionDelegationResultListener<ImpBDI.AchieveCollectGold, Void>(retb){

						@Override
						public void customResultAvailable(AchieveCollectGold result) {
							retb.setResult(null);
							
						}
					} );
				} else {
					retb.setResult(null);
				}
				
			} else {
				System.out.println("The Task was null, this should not happen. But we don't break up.");
				retb.setResult(null);
			}
		} else {
			retb.setResult(null);
		}
		return retb;
	}


}
