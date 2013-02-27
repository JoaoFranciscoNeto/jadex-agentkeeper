package jadex.agentkeeper.ai.base;

import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.ai.AbstractBeingBDI.AchieveMoveToSector;
import jadex.agentkeeper.ai.pathfinding.AStarSearch;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;
import jadex.bdiv3.runtime.IPlan;
import jadex.commons.future.DelegationResultListener;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector2Int;


/**
 * Patrol to random Points
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
public class PatrolPlan
{
	@PlanCapability
	protected AbstractBeingBDI	capa;

	@PlanAPI
	protected IPlan				iplan;

	protected Grid2D			environment;

	protected AStarSearch		astar;

	protected SpaceObject		spaceObject;

	// -------- constructors --------

	/**
	 * Create a new plan.
	 */
	public PatrolPlan()
	{
		// System.out.println("create new patrol plan");
		// getLogger().info("Created: "+this);
	}

	// -------- methods --------

	/**
	 * The plan body.
	 */
	@PlanBody
	public IFuture<Void> body()
	{
		environment = capa.getEnvironment();

		spaceObject = (SpaceObject)capa.getMySpaceObject();

		spaceObject.setProperty(ISObjStrings.PROPERTY_GOAL, "Patrol");

		final Future<Void> ret = new Future<Void>();

		iplan.waitFor(100).addResultListener(new DelegationResultListener<Void>(ret)
		{
			public void customResultAvailable(Void result)
			{
				findRndPos().addResultListener(new DelegationResultListener<Void>(ret));
			}
		});


		return ret;
	}

	private IFuture<Void> findRndPos()
	{
		final Future<Void> ret = new Future<Void>();

		Vector2Int rndpos = (Vector2Int)environment.getRandomGridPosition(Vector2Int.ZERO);

		Vector2Int myloc = (Vector2Int)spaceObject.getProperty(ISObjStrings.PROPERTY_INTPOSITION);

		// TODO: refractor AStar-Search
		AStarSearch astar = new AStarSearch(myloc.copy(), rndpos, environment, true);

		if(astar.istErreichbar())
		{
			// System.out.println("reachable");
			moveToLocation(rndpos).addResultListener(new DelegationResultListener<Void>(ret));
		}
		else
		{
			ret.setResult(null);
		}

		return ret;


	}

	/**
	 * 
	 */
	protected IFuture<Void> moveToLocation(final IVector2 pos)
	{
		final Future<Void> ret = new Future<Void>();

		Vector2Int posi = new Vector2Int(Math.round(pos.getXAsFloat()), Math.round(pos.getYAsFloat()));

		IFuture<AchieveMoveToSector> fut = iplan.dispatchSubgoal(capa.new AchieveMoveToSector(posi));
		fut.addResultListener(new ExceptionDelegationResultListener<AbstractBeingBDI.AchieveMoveToSector, Void>(ret)
		{
			public void customResultAvailable(AchieveMoveToSector mtg)
			{
				// System.out.println("patrol plan finished");
				ret.setResult(null);
			}

			public void exceptionOccurred(Exception e)
			{
				// System.out.println("exception patrol");
				super.exceptionOccurred(e);
			}
		});

		return ret;
	}
}
