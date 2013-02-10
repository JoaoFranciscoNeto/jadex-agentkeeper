package jadex.agentkeeper.ai.imp;

import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.ai.base.PatrolPlan;
import jadex.agentkeeper.ai.creatures.AbstractCreatureBDI;
import jadex.bdiv3.annotation.Body;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Plans;
import jadex.bdiv3.annotation.Trigger;
import jadex.bdiv3.model.MGoal;
import jadex.bridge.modelinfo.IExtensionInstance;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.environment.space2d.Grid2D;
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
public class ImpBDI extends AbstractCreatureBDI
{
	
	/** The workingspeed of the "Imp". */
	protected float	myWorkspeed	= 1;


	@AgentBody
	@Override
	public void body()
	{
		agent.dispatchTopLevelGoal(new PerformImpPatrol());
//		agent.dispatchTopLevelGoal(new AchieveMoveToSector(new Vector2Int(9,18)));
	}
	
	/**
	 *  Initialize the agent.
	 *  Called at startup.
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
