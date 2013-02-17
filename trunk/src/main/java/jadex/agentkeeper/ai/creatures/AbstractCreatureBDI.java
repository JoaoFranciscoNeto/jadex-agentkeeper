package jadex.agentkeeper.ai.creatures;

import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.ai.creatures.troll.TrollBDI.PerformPatrol;
import jadex.bdiv3.annotation.Body;
import jadex.bdiv3.annotation.Deliberation;
import jadex.bdiv3.annotation.Goal;
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
@Plan(trigger = @Trigger(goals = AbstractCreatureBDI.PerformOccupyLair.class), body = @Body(OccupyLairPlan.class))})
public class AbstractCreatureBDI extends AbstractBeingBDI
{
	/**
	 * Position of the Lair for this Creature
	 */
	protected Vector2Int	myLairPosition;

	/**
	 * The agent body.
	 */
	@AgentBody
	@Override
	public void body()
	{
		super.body();
		agent.dispatchTopLevelGoal(new PerformOccupyLair());
	}

	/**
	 * Goal that lets the Creature occupy a bed.
	 */
	
	@Goal(excludemode = MGoal.EXCLUDE_WHEN_SUCCEEDED, succeedonpassed = true, 
			deliberation=@Deliberation(inhibits={PerformIdle.class, PerformPatrol.class}))
	public class PerformOccupyLair
	{
	}
	


}
