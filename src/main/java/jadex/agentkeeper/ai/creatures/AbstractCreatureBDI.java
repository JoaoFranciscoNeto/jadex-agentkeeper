package jadex.agentkeeper.ai.creatures;

import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.ai.AbstractBeingBDI.PerformIdle;
import jadex.agentkeeper.ai.AbstractBeingBDI.PerformIdleForTime;
import jadex.agentkeeper.ai.creatures.troll.TrollBDI.PerformPatrol;
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

	/**
	 * The agent body.
	 */
	@AgentBody
	@Override
	public void body()
	{
		super.body();
		agent.dispatchTopLevelGoal(new PerformOccupyLair());
//		agent.dispatchTopLevelGoal(new MaintainCreatureAwake());
	}

	/**
	 * Goal that lets the Creature occupy a bed.
	 */
	
	@Goal(excludemode = MGoal.EXCLUDE_WHEN_SUCCEEDED, succeedonpassed = true, 
			deliberation=@Deliberation(inhibits={PerformIdle.class, PerformPatrol.class}))
	public class PerformOccupyLair
	{
	}

	/**
	 *  Goal for keeping the Creature awake.
	 */
	@Goal(deliberation=@Deliberation(inhibits={PerformIdle.class, PerformPatrol.class}))
	public class MaintainCreatureAwake
	{
		/**
		 *  When the AwakeStatus is below 10.0
		 *  the Creature will activate this goal.
		 */
		@GoalMaintainCondition(events="myAwakeStatus")
		public boolean checkMaintain()
		{
			
			boolean ret = myAwakeStatus>35.0;
			System.out.println("maintain awake " + myAwakeStatus + " "+ret);
			return ret;
		}
		
		/**
		 *  The target condition determines when
		 *  the goal goes back to idle. 
		 */
		@GoalTargetCondition(events="myAwakeStatus")
		public boolean checkTarget()
		{
			return myAwakeStatus>=100.0;
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
