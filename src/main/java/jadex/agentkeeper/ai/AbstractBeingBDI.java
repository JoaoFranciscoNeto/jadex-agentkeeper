package jadex.agentkeeper.ai;



import jadex.agentkeeper.ai.base.IdleForGivenDuration;
import jadex.agentkeeper.ai.base.IdlePlan;
import jadex.agentkeeper.ai.base.MoveToGridSectorPlan;
import jadex.agentkeeper.ai.base.PatrolPlan;
import jadex.agentkeeper.ai.creatures.AbstractCreatureBDI.PerformOccupyLair;
import jadex.agentkeeper.ai.creatures.troll.TrollBDI.PerformPatrol;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Body;
import jadex.bdiv3.annotation.Deliberation;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalTargetCondition;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Plans;
import jadex.bdiv3.annotation.Trigger;
import jadex.bdiv3.model.MGoal;
import jadex.bridge.modelinfo.IExtensionInstance;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.AgentCreated;


/**
 * Abstract BDI-Agent holds all the similarity (Beliefs, Goals, Plans) from all
 * Beings. A "Being" can be any Creature, Hero or Imp(Worker)
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
@Agent
@Plans({

@Plan(trigger = @Trigger(goals = AbstractBeingBDI.AchieveMoveToSector.class), body = @Body(MoveToGridSectorPlan.class)),
@Plan(trigger=@Trigger(goals=AbstractBeingBDI.PerformIdle.class), body=@Body(PatrolPlan.class)),
//@Plan(trigger=@Trigger(goals=AbstractBeingBDI.PerformIdle.class), body=@Body(IdlePlan.class)),
@Plan(trigger=@Trigger(goals=AbstractBeingBDI.PerformIdleForTime.class), body=@Body(IdleForGivenDuration.class))

})
public class AbstractBeingBDI
{
	/** The bdi agent. Automatically injected */
	@Agent
	protected BDIAgent		agent;

	/** The virtual environment of the Dungeon. */
	protected Grid2D		environment;
	
	/** The virtual SpaceObject of the "Being" in the virtual environment. */
	@Belief
	protected ISpaceObject mySpaceObject;

	/** The position of the "Being". */
	@Belief(dynamic=true)
	protected Vector2Double	myPosition = mySpaceObject==null? null: (Vector2Double)mySpaceObject.getProperty(Space2D.PROPERTY_POSITION);
	
	/** The int-position of the "Being". */
	@Belief(dynamic=true)
	protected Vector2Int myIntPosition = mySpaceObject==null? null: (Vector2Int)mySpaceObject.getProperty(ISObjStrings.PROPERTY_INTPOSITION);

	/** The speed of the "Being". */
	protected float	mySpeed	= 1;
	
	/** The level of the "Being". */
	protected int	myLevel	= 1;
	
	/** The hitpoints of the "Being". */
	protected float	myHitpoints	= 25;

	/**
	 *  Initialize the agent.
	 *  Called at startup.
	 */
	@AgentCreated
	public IFuture<Void>	init()
	{
		final Future<Void>	ret	= new Future<Void>();
		agent.getParentAccess().getExtension("mygc2dspace")
			.addResultListener(new ExceptionDelegationResultListener<IExtensionInstance, Void>(ret)
		{
			public void customResultAvailable(IExtensionInstance ext)
			{
				Grid2D g2d= (Grid2D)ext;
				environment	= g2d;
				setMySpaceObject(environment.getAvatar(agent.getComponentDescription(), agent.getModel().getFullName()));
				
				Vector2Double val1 = (Vector2Double)mySpaceObject.getProperty(Space2D.PROPERTY_POSITION);
				myPosition = val1;
				myPosition = (Vector2Double)mySpaceObject.getProperty(Space2D.PROPERTY_POSITION);
				Object o = mySpaceObject.getProperty(ISObjStrings.PROPERTY_INTPOSITION);
				Vector2Int val2= (Vector2Int)o;
				myIntPosition = val2;

				ret.setResult(null);
			}
		});
		return ret;
	}

	
	
	/**
	 * The agent body.
	 */
	@AgentBody
	public void body()
	{
		agent.dispatchTopLevelGoal(new PerformIdleForTime((int)(2000+Math.random()*5000)));
		agent.dispatchTopLevelGoal(new PerformIdle());
		
//		agent.dispatchTopLevelGoal(new AchieveMoveToSector(new Vector2Int(9,18)));
	}

	/**
	 * The goal is used to move to a specific location ( on the Grid ).
	 */
	@Goal
	public class AchieveMoveToSector
	{
		/** The target. */
		protected Vector2Int	target;

		/**
		 * Create a new goal.
		 * 
		 * @param target The target.
		 */
		public AchieveMoveToSector(Vector2Int target)
		{
			this.target = target;
		}

		/**
		 * The goal is achieved when the position of the being is on the
		 * target sector position.
		 */
		@GoalTargetCondition(events = "myIntPosition")
		public boolean checkTarget()
		{
			boolean ret = target.equals(myIntPosition);
			
//			System.out.print("myIntpos " + myIntPosition + " " );
//			System.out.println("target " + target + " " + ret);

			return ret;
		}

		/**
		 * Get the target.
		 * 
		 * @return The target.
		 */
		public Vector2Int getTarget()
		{
			return this.target;
		}
	}
	
	/**
	 *  Goal that lets the Being perform idle.
	 *  
	 *  Because the Goal has two Plans (IdlePlan and PatrolPlan) and we use randomselection
	 *  the Agent just Idles or Patrols
	 */
	@Goal(excludemode=MGoal.EXCLUDE_NEVER, succeedonpassed=false, randomselection=true)
	public class PerformIdle
	{
	}
	
	/**
	 *  Goal that lets the Being perform idle.
	 *  
	 *  Because the Goal hast two Plans (IdlePlan and PatrolPlan) and we use randomselection
	 *  the Agent just Idles or Patrols
	 */
	@Goal(excludemode=MGoal.EXCLUDE_WHEN_SUCCEEDED, succeedonpassed=true, deliberation=@Deliberation(inhibits={PerformIdle.class, PerformOccupyLair.class, PerformPatrol.class}))
	public class PerformIdleForTime
	{
		private int duration;
		
		public PerformIdleForTime(int duration)
		{
			this.duration = duration;
		}

		/**
		 * @return the duration
		 */
		public int getDuration()
		{
			return duration;
		}

		/**
		 * @param duration the duration to set
		 */
		public void setDuration(int duration)
		{
			this.duration = duration;
		}
	}


	/**
	 * @return the my_position
	 */
	public Vector2Double getMyPosition()
	{
		return myPosition;
	}


	/**
	 * @param my_position the my_position to set
	 */
	public void setMyPosition(Vector2Double my_position)
	{
		this.myPosition = my_position;
	}


	/**
	 * @return the my_speed
	 */
	public float getMySpeed()
	{
		return mySpeed;
	}


	/**
	 * @param my_speed the my_speed to set
	 */
	public void setMySpeed(float my_speed)
	{
		this.mySpeed = my_speed;
	}


	/**
	 * Get the agent.
	 * 
	 * @return The agent.
	 */
	public BDIAgent getAgent()
	{
		return agent;
	}

	/**
	 * @return the environment
	 */
	public Grid2D getEnvironment()
	{
		return environment;
	}

	/**
	 * @param environment the environment to set
	 */
	public void setEnvironment(Grid2D environment)
	{
		this.environment = environment;
	}

	public ISpaceObject getMySpaceObject()
	{
		return mySpaceObject;
	}

	public void setMySpaceObject(ISpaceObject mySpaceObject)
	{
		this.mySpaceObject = mySpaceObject;
	}


	/**
	 * @return the myLevel
	 */
	public int getMyLevel()
	{
		return myLevel;
	}



	/**
	 * @param myLevel the myLevel to set
	 */
	public void setMyLevel(int myLevel)
	{
		this.myLevel = myLevel;
	}



	/**
	 * @return the myHitpoints
	 */
	public float getMyHitpoints()
	{
		return myHitpoints;
	}



	/**
	 * @param myHitpoints the myHitpoints to set
	 */
	public void setMyHitpoints(float myHitpoints)
	{
		this.myHitpoints = myHitpoints;
	}

	



}
