package jadex.agentkeeper.ai.creatures;

import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Body;
import jadex.bdiv3.annotation.Deliberation;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalMaintainCondition;
import jadex.bdiv3.annotation.GoalTargetCondition;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Plans;
import jadex.bdiv3.annotation.Trigger;
import jadex.bdiv3.model.MGoal;
import jadex.extension.envsupport.math.Vector2Int;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;


/**
 * Abstract BDI-Agent holds all the similarity (Beliefs, Goals, Plans) from all
 * Creatures that Belong to the Player. A "Creature" can be any Creature, like
 * Goblin, Imp, Orc and so on
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
@Agent
@Plans({
@Plan(trigger = @Trigger(goals = AbstractCreatureBDI.MaintainCreatureAwake.class), body = @Body(SleepPlan.class)),
@Plan(trigger = @Trigger(goals = AbstractCreatureBDI.MaintainCreatureFed.class), body = @Body(EatPlan.class)),
@Plan(trigger = @Trigger(goals = AbstractCreatureBDI.PerformOccupyLair.class), body = @Body(OccupyLairPlan.class))})
public class AbstractCreatureBDI extends AbstractBeingBDI
{
	/**
	 * Position of the Lair for this Creature
	 */
	protected Vector2Int	myLairPosition;
	
	/** the awake-status of the "Being" */
	@Belief(dynamic=true)
	protected double myAwakeStatus = mySpaceObject==null? 100.0: (Double)mySpaceObject.getProperty(ISObjStrings.PROPERTY_AWAKE);
	
	/** the fed-status (not Hungry) of the "Being" */
	@Belief(dynamic=true)
	protected double myFedStatus = mySpaceObject==null? 100.0: (Double)mySpaceObject.getProperty(ISObjStrings.PROPERTY_FED);
	
	/** the hapiness-status of the "Being" */
	@Belief(dynamic=true)
	protected double myHappinessStatus = mySpaceObject==null? 100.0: (Double)mySpaceObject.getProperty(ISObjStrings.PROPERTY_HAPPINESS);
	
	/** the moral-status of the "Being" */
	@Belief(dynamic=true)
	protected double myMoralStatus = mySpaceObject==null? 100.0: (Double)mySpaceObject.getProperty(ISObjStrings.PROPERTY_MORAL);

	/**
	 * The agent body.
	 */
	@AgentBody
	@Override
	public void body()
	{
		super.body();
		agent.dispatchTopLevelGoal(new PerformOccupyLair());
		agent.dispatchTopLevelGoal(new MaintainCreatureAwake());
		agent.dispatchTopLevelGoal(new MaintainCreatureFed());
	}

	/**
	 * Goal that lets the Creature occupy a bed.
	 */
	
	@Goal(excludemode = Goal.ExcludeMode.WhenSucceeded, succeedonpassed = true, 
			deliberation=@Deliberation(inhibits={PerformIdle.class,  MaintainCreatureAwake.class, MaintainCreatureFed.class}))
	public class PerformOccupyLair
	{
	}

	/**
	 *  Goal for keeping the Creature awake.
	 */
	@Goal(deliberation=@Deliberation(inhibits={PerformIdle.class, MaintainCreatureFed.class}))
	public class MaintainCreatureAwake
	{
		/**
		 *  When the AwakeStatus is below 5.0
		 *  the Creature will activate this goal.
		 */
		@GoalMaintainCondition(events="myAwakeStatus")
		public boolean checkMaintain()
		{

			return myAwakeStatus>5.0;
		}
		
		/**
		 *  The target condition determines when
		 *  the goal goes back to idle. 
		 */
		@GoalTargetCondition(events="myAwakeStatus")
		public boolean checkTarget()
		{
//			System.out.println("checkTarget" + myAwakeStatus);
			return myAwakeStatus>=100.0;
		}
	}
	
	
	/**
	 *  Goal for keeping the Creature feeded.
	 */
	@Goal(deliberation=@Deliberation(inhibits={PerformIdle.class, MaintainCreatureAwake.class}), retry=true, retrydelay=1000)
	public class MaintainCreatureFed
	{
		/**
		 *  When the FedStatus is below 5.0
		 *  the Creature will activate this goal.
		 */
		@GoalMaintainCondition(events="myFedStatus")
		public boolean checkMaintain()
		{
			return myFedStatus>5.0;
		}
		
		/**
		 *  The target condition determines when
		 *  the goal goes back to idle. 
		 */
		@GoalTargetCondition(events="myFedStatus")
		public boolean checkTarget()
		{
			return myFedStatus>=100.0;
		}
	}
	
	
	
	
	// ------------------- GETTER AND SETTER -------------------

	/**
	 * @return the myLairPosition
	 */
	public Vector2Int getMyLairPosition()
	{
		return myLairPosition;
	}

	/**
	 * @param myLairPosition the myLairPosition to set
	 */
	public void setMyLairPosition(Vector2Int myLairPosition)
	{
		this.myLairPosition = myLairPosition;
	}

	/**
	 * @return the myAwakeStatus
	 */
	public double getMyAwakeStatus()
	{
		return myAwakeStatus;
	}

	/**
	 * @param myAwakeStatus the myAwakeStatus to set
	 */
	public void setMyAwakeStatus(double myAwakeStatus)
	{
		this.myAwakeStatus = myAwakeStatus;
	}

	/**
	 * @return the myFedStatus
	 */
	public double getMyFedStatus()
	{
		return myFedStatus;
	}

	/**
	 * @param myFedStatus the myFedStatus to set
	 */
	public void setMyFedStatus(double myFedStatus)
	{
		this.myFedStatus = myFedStatus;
	}

	/**
	 * @return the myHappinessStatus
	 */
	public double getMyHappinessStatus()
	{
		return myHappinessStatus;
	}

	/**
	 * @param myHappinessStatus the myHappinessStatus to set
	 */
	public void setMyHappinessStatus(double myHappinessStatus)
	{
		this.myHappinessStatus = myHappinessStatus;
	}
	


}
