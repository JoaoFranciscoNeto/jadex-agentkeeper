package jadex.agentkeeper.ai.creatures;


import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.ai.AbstractBeingBDI.AchieveMoveToSector;
import jadex.agentkeeper.ai.enums.PlanType;
import jadex.agentkeeper.game.state.map.SimpleMapState;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.building.LairInfo;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * Patrol to random Points
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
public class OccupyLairPlan
{
	@PlanCapability
	protected AbstractCreatureBDI	capa;

	@PlanAPI
	protected IPlan				iplan;

	protected SpaceObject		spaceObject;

	protected Grid2D			environment;

	protected SimpleMapState	buildingState;

	protected LairInfo			lairInfo;

	// -------- constructors --------

	/**
	 * Create a new plan.
	 */
	public OccupyLairPlan()
	{
//		System.out.println("lair plan created");
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
		
		spaceObject.setProperty(ISObjStrings.PROPERTY_GOAL, PlanType.GETLAIR);

		buildingState = (SimpleMapState)environment.getProperty(ISpaceStrings.BUILDING_STATE);

		final Future<Void> retb = new Future<Void>();

		HashMap<Vector2Int, Object> lairs = buildingState.getTypes(MapType.LAIR);

		Set<Vector2Int> lairpos = lairs.keySet();
		
		ArrayList<Vector2Int> lairposlist = new ArrayList<Vector2Int>();
		
		lairposlist.addAll(lairpos);
		
		Collections.shuffle(lairposlist);
		
		Iterator<Vector2Int> it = lairposlist.iterator();
		Vector2Int tmp = null;

		// boolean freeLair = false;


		while(it.hasNext())
		{
			tmp = it.next();
//			System.out.println("whileschritt " + tmp);
			lairInfo = (LairInfo)lairs.get(tmp);
			if(lairInfo.getCreatureId() == -1 && !lairInfo.isLocked())
			{

				lairInfo.setLocked(true);

				moveToLocation(tmp).addResultListener(new DelegationResultListener<Void>(retb));
				break;
			}
		}

		return retb;
	}


	/**
	 * 
	 */
	protected IFuture<Void> moveToLocation(final IVector2 pos)
	{
		final Future<Void> ret = new Future<Void>();

		final Vector2Int posi = new Vector2Int(pos.getXAsInteger(), pos.getYAsInteger());

//		System.out.println("free lair! move and I am  " + posi + " " + spaceobject.getType());
		

			
		IFuture<AchieveMoveToSector> fut = iplan.dispatchSubgoal(capa.new AchieveMoveToSector(posi));
		fut.addResultListener(new ExceptionDelegationResultListener<AbstractBeingBDI.AchieveMoveToSector, Void>(ret)
		{
			public void customResultAvailable(AchieveMoveToSector mtg)
			{
				occupyLair(posi).addResultListener(new DelegationResultListener<Void>(ret));
			}
		});

		return ret;
	}
	
	private IFuture<Void> occupyLair(Vector2Int pos)
	{
//		System.out.println("yeah ! + pos " + pos);
		final Future<Void> ret = new Future<Void>();
		
		lairInfo.setCreatureId((Long)spaceObject.getId());
		Collection<SpaceObject> col = environment.getSpaceObjectsByGridPosition(pos, MapType.LAIR.toString());
		SpaceObject oldlair = col.iterator().next();
		
//		spaceobject.setProperty(ISObjStrings.PROPERTY_AWAKE, 100.0);
		capa.setMyLairPosition(pos);
		
		Map<String, Object> probs = oldlair.getProperties();
		environment.destroySpaceObject(oldlair.getId());
		environment.createSpaceObject(MapType.LAIR.toString(), probs, null);

		ret.setResult(null);
		
		return ret;
	}
}
