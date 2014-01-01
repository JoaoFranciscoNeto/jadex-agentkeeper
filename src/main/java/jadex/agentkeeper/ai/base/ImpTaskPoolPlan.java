package jadex.agentkeeper.ai.base;

import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.ai.imp.ImpBDI;
import jadex.agentkeeper.ai.imp.ImpBDI.AchieveDigSector;
import jadex.agentkeeper.game.state.missions.Task;
import jadex.agentkeeper.game.state.missions.TaskPoolManager;
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
				System.out.println(newImpTask.getTaskType());
				capa.getMySpaceObject().setProperty(IMP_LOCAL_TASK, newImpTask);
				IFuture<AchieveDigSector> reachSectorToDigFrom = rplan.dispatchSubgoal(impBdi.new AchieveDigSector(newImpTask));
				reachSectorToDigFrom.addResultListener(new ExceptionDelegationResultListener<ImpBDI.AchieveDigSector, Void>(retb){
					@Override
					public void customResultAvailable(AchieveDigSector result) {
						retb.setResult(null);
					}
				} );
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
