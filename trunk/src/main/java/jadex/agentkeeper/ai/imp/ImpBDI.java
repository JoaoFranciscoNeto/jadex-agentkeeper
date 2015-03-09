package jadex.agentkeeper.ai.imp;

import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.ai.base.ImpTaskPoolPlan;
import jadex.agentkeeper.ai.base.claimSector.ClaimSectorPlan;
import jadex.agentkeeper.ai.base.claimWall.ClaimWallPlan;
import jadex.agentkeeper.ai.base.collectGold.CollectGoldPlan;
import jadex.agentkeeper.ai.base.collectGold.FillTreasuryPlan;
import jadex.agentkeeper.ai.base.digSector.DigSectorPlan;
import jadex.agentkeeper.game.state.missions.Task;
import jadex.bdiv3.annotation.Body;
import jadex.bdiv3.annotation.Deliberation;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Plans;
import jadex.bdiv3.annotation.Trigger;
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
@Plan(trigger = @Trigger(goals = ImpBDI.AchieveDigSector.class), body = @Body(DigSectorPlan.class)),
@Plan(trigger = @Trigger(goals = ImpBDI.AchieveClaimSector.class), body = @Body(ClaimSectorPlan.class)),
@Plan(trigger = @Trigger(goals = ImpBDI.AchieveClaimWall.class), body = @Body(ClaimWallPlan.class)),
@Plan(trigger = @Trigger(goals = ImpBDI.AchieveCollectGold.class), body = @Body(CollectGoldPlan.class)),
@Plan(trigger = @Trigger(goals = ImpBDI.AchieveFillTreasury.class), body = @Body(FillTreasuryPlan.class))
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
	}
	
	
	/**
	 *  Goal that let the Imp claim Sectors.
	 *  
	 */
	@Goal(deliberation=@Deliberation(inhibits={PerformIdle.class}), excludemode=Goal.ExcludeMode.Never, retry=true, retrydelay=1000, orsuccess=false)
	public class PerformImpTaskPoolGoal
	{


		
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
	
	/**
	 * The goal is used to move to a specific location ( on the Grid ).
	 */
	@Goal
	public class AchieveClaimSector {
		/** The target. */
		protected Task target;

		/**
		 * Create a new goal.
		 * 
		 * @param newImpTask
		 *            The target.
		 */
		public AchieveClaimSector(Task newImpTask) {
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
	
	/**
	 * The goal is used to move to a specific location ( on the Grid ).
	 */
	@Goal
	public class AchieveClaimWall {
		/** The target. */
		protected Task target;

		/**
		 * Create a new goal.
		 * 
		 * @param newImpTask
		 *            The target.
		 */
		public AchieveClaimWall(Task newImpTask) {
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
	/**
	 * The goal is used to move to a specific location ( on the Grid ).
	 */
	@Goal
	public class AchieveCollectGold {
		/** The target. */
		protected Task target;

		/**
		 * Create a new goal.
		 * 
		 * @param newImpTask
		 *            The target.
		 */
		public AchieveCollectGold(Task newImpTask) {
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
	
	/**
	 * The goal is used to move to a specific location ( on the Grid ).
	 */
	@Goal
	public class AchieveFillTreasury {
		/** The target. */
		protected Task target;

		/**
		 * Create a new goal.
		 * 
		 * @param newImpTask
		 *            The target.
		 */
		public AchieveFillTreasury(Task newImpTask) {
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
	

	public Object getMyWorkingSpeed() {
		return myWorkspeed;
	}

}
