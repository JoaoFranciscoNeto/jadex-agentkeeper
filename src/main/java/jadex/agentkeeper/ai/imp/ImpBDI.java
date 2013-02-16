package jadex.agentkeeper.ai.imp;

import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.ai.base.PatrolPlan;
import jadex.bdiv3.annotation.Body;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Plans;
import jadex.bdiv3.annotation.Trigger;
import jadex.bdiv3.model.MGoal;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.AgentCreated;

/**
 * The Imp. The main Worker-Creature.
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 *
 */
@Agent
@Plans({

@Plan(trigger=@Trigger(goals=ImpBDI.PerformImpPatrol.class), body=@Body(PatrolPlan.class))
})
public class ImpBDI extends AbstractBeingBDI
{
	
	/** The workingspeed of the "Imp". */
	protected float	myWorkspeed	= 1;


	@AgentBody
	@Override
	public void body()
	{
		agent.dispatchTopLevelGoal(new PerformImpPatrol());
	}
	
	/**
	 *  Initialize the agent.
	 *  Called at startup.
	 *  
	 *  Imp have double Walkspeed from startup
	 *  
	 */
	@AgentCreated
	@Override
	public IFuture<Void>	init()
	{
		super.init();
		final Future<Void>	ret	= new Future<Void>();

		mySpeed = 2;
			
		ret.setResult(null);
		return ret;
	}
	
	/**
	 *  Goal that lets the Imp perform Patrols.
	 *  
	 */
	@Goal(excludemode=MGoal.EXCLUDE_NEVER, succeedonpassed=false)
	public class PerformImpPatrol
	{
		
	}

}
