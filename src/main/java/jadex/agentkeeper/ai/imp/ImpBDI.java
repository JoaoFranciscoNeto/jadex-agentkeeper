package jadex.agentkeeper.ai.imp;

import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.ai.AbstractBeingBDI.PerformIdle;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.model.MGoal;
import jadex.micro.annotation.AgentBody;

/**
 * The Imp. The main Worker-Creature.
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 *
 */
public class ImpBDI extends AbstractBeingBDI
{
	
	@AgentBody
	@Override
	public void body()
	{
		agent.dispatchTopLevelGoal(new PerformPatrol());
//		agent.dispatchTopLevelGoal(new AchieveMoveToSector(new Vector2Int(9,18)));
	}
	
	/**
	 *  Goal that lets the Troll perform Patrols.
	 *  
	 */
	@Goal(excludemode=MGoal.EXCLUDE_NEVER, succeedonpassed=false)
	public class PerformPatrol
	{
		
	}

}
