package jadex.agentkeeper.ai.base;

import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.ai.AbstractBeingBDI.AchieveMoveToSector;
import jadex.agentkeeper.ai.pathfinding.AStarSearch;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanAborted;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;
import jadex.bdiv3.annotation.PlanFailed;
import jadex.bdiv3.annotation.PlanReason;
import jadex.bdiv3.runtime.IPlan;
import jadex.commons.IResultCommand;
import jadex.commons.future.DelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Move to a Location on the Grid
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
@Plan
public class MoveToGridSectorPlan
{
	@PlanCapability
	protected AbstractBeingBDI				capa;

	@PlanAPI
	protected IPlan					iplan;

	@PlanReason
	protected AchieveMoveToSector	goal;

	private AStarSearch				astar;

	private Iterator<Vector2Int>	path_iterator;
	
	private ISpaceObject spaceObject;
	
	private Object mtaskid;

	// -------- constructors --------

	/**
	 * Create a new plan.
	 */
	public MoveToGridSectorPlan()
	{
//		System.out.println("Created: " + this);
//		 getLogger().info("Created: "+this);
	}

	// -------- methods --------

	/**
	 * The plan body.
	 */
	@PlanBody
	public IFuture<Void> body()
	{
		final Future<Void> ret = new Future<Void>();
		
		spaceObject = capa.getMySpaceObject();

		Vector2Int target = goal.getTarget();
		
		Vector2Double myLoc = capa.getMyPosition();

		// TODO: refractor AStar-Search
		astar = new AStarSearch(myLoc, target, capa.getEnvironment(), true);

		if(astar.istErreichbar())
		{
			spaceObject.setProperty(ISObjStrings.PROPERTY_STATUS, "Walk");

			ArrayList<Vector2Int> path = astar.gibPfadInverted();

			path_iterator = path.iterator();
			
			moveToNextSector(path_iterator).addResultListener(new DelegationResultListener<Void>(ret)
			{
				public void exceptionOccurred(Exception exception)
				{
					ret.setExceptionIfUndone(exception);
				}
			});
		}
		else
		{
			System.out.println("not reachable " + target);
			ret.setException(new RuntimeException("Not reachable: " + target));
		}


		return ret;
	}
	
	/**
	 * Iterative Method
	 * @param it iterator
	 * @return empty result when finished
	 */
	private IFuture<Void> moveToNextSector(final Iterator<Vector2Int> it)
	{
		final Future<Void> ret = new Future<Void>();
		if(it.hasNext())
		{
			final Vector2Int nextTarget = it.next();
			
//			System.out.println("next Target " + nextTarget);

			oneStepToTarget(nextTarget).addResultListener(new DelegationResultListener<Void>(ret)
			{
				public void customResultAvailable(Void result)
				{
					spaceObject.setProperty(ISObjStrings.PROPERTY_INTPOSITION, nextTarget);
					moveToNextSector(path_iterator).addResultListener(new DelegationResultListener<Void>(ret));
				}
			});
		}
		else
		{
			ret.setResult(null);
		}

		return ret;
	}
	
	@PlanFailed
	@PlanAborted
	public void cleanupMoveTask2()
	{
		if(mtaskid != null)
		{
//			System.out.println("cleanup task aborted:"+iplan.getId() + " "+mtaskid);
			capa.getEnvironment().removeObjectTask(mtaskid, spaceObject.getId());
		}
	}
	
	/**
	 * We use the MoveTask for the "moving" in the virtual World.
	 * 
	 * @param nextTarget
	 * @return
	 */
	private IFuture<Void> oneStepToTarget(Vector2Int nextTarget)
	{
		final Future<Void> ret = new Future<Void>();
		
//		System.out.println("spaceObject: " + spaceObject.getType() + spaceObject.getProperty(ISObjStrings.PROPERTY_INTPOSITION));
		
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(MoveTask.PROPERTY_DESTINATION, nextTarget);
		props.put(MoveTask.PROPERTY_SPEED, capa.getMySpeed());

		this.mtaskid = capa.getEnvironment().createObjectTask(MoveTask.PROPERTY_TYPENAME, props, capa.getMySpaceObject().getId());

		
		// wait for task but remain interruptible when goal is abort
		iplan.invokeInterruptable(new IResultCommand<IFuture<Void>, Void>()
		{
			public IFuture<Void> execute(Void args)
			{
				return capa.getEnvironment().waitForTask(mtaskid, capa.getMySpaceObject().getId());
				
			}
		}).addResultListener(new DelegationResultListener<Void>(ret));
		
		return ret;
	}
}
