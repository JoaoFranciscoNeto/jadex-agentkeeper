package jadex.agentkeeper.ai.imp;

import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.ai.base.ImpTaskPoolPlan;
import jadex.agentkeeper.ai.base.digSector.DigSectorPlan;
import jadex.agentkeeper.game.state.missions.Task;
import jadex.bdiv3.annotation.Body;
import jadex.bdiv3.annotation.Deliberation;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Plans;
import jadex.bdiv3.annotation.Trigger;
import jadex.extension.envsupport.math.Vector2Int;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

/**
 * The Imp. The main Worker-Creature.
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 *
 */
@Agent
@Plans({
@Plan(trigger=@Trigger(goals=ImpBDI.PerformImpTaskPoolGoal.class), body=@Body(ImpTaskPoolPlan.class)),
@Plan(trigger = @Trigger(goals = ImpBDI.AchieveDigSector.class), body = @Body(DigSectorPlan.class))
})
public class ImpBDI extends AbstractBeingBDI {
	
	/** The workingspeed of the "Imp". */
	protected float	myWorkspeed	= 1;
	
//	@Belief(dynamic=true)
//	protected double myTaskStatus = mySpaceObject==null? 100.0: (Double)mySpaceObject.getProperty(ISObjStrings.PROPERTY_FED);


	
	public ImpBDI()
	{
		this.mySpeed = 4;
		this.myWorkspeed = 2;
	}
	
	@AgentBody
	public void body()
	{
		agent.dispatchTopLevelGoal(new PerformIdle());
		agent.dispatchTopLevelGoal(new PerformImpTaskPoolGoal());
//		agent.dispatchTopLevelGoal(new PerformClaimSector());
//		System.out.println("dispatch");
	}
	
	
	/**
	 *  Goal that let the Imp claim Sectors.
	 *  
	 */
//	@Goal(excludemode=Goal.ExcludeMode.Never, succeedonpassed=false, randomselection=true)
	@Goal(deliberation=@Deliberation(inhibits={PerformIdle.class}), excludemode=Goal.ExcludeMode.Never, retry=true, retrydelay=1000, succeedonpassed=false)
	public class PerformImpTaskPoolGoal
	{


//		@GoalMaintainCondition(beliefs="myTaskStatus")
//		public boolean checkMaintain()
//		{
//			Auftragsverwalter auftraege = (Auftragsverwalter)getMySpaceObject().getProperty("auftraege");
//			Auftrag  auftrag = auftraege.gibDichtestenAuftrag(new Vector2Int(getMyPosition().getXAsInteger(), getMyPosition().getYAsInteger()));
//			System.out.println("test");
//			System.out.println(auftrag!=null);
//			return auftrag != null;
//		}
		
		
	}
	
	/**
	 * The goal is used to move to a specific location ( on the Grid ).
	 */
	@Goal
	public class AchieveDigSector {
		/** The target. */
		protected Task target;

		/**
		 * Create a new goal.
		 * 
		 * @param newImpTask
		 *            The target.
		 */
		public AchieveDigSector(Task newImpTask) {
			this.target = newImpTask;
		}

		/**
		 * Get the target.
		 * 
		 * @return The target.
		 */
		public Task getTarget() {
			return this.target;
		}
	}

}
