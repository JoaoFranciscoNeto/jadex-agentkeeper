package jadex.agentkeeper.ai.creatures;

import jadex.agentkeeper.ai.AbstractBeingBDI.AchieveMoveToSector;
import jadex.agentkeeper.ai.creatures.AbstractCreatureBDI.MaintainCreatureFed;
import jadex.agentkeeper.ai.enums.PlanType;
import jadex.agentkeeper.game.state.map.SimpleMapState;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.agentkeeper.worldmodel.enums.CenterType;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.TileInfo;
import jadex.agentkeeper.worldmodel.structure.building.ACenterBuildingInfo;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;
import jadex.bdiv3.annotation.PlanReason;
import jadex.bdiv3.runtime.IPlan;
import jadex.commons.future.DefaultResultListener;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class EatPlan
{

	@PlanCapability
	protected AbstractCreatureBDI	capa;

	@PlanAPI
	protected IPlan					rplan;

	@PlanReason
	protected MaintainCreatureFed	goal;

	protected SpaceObject			spaceObject;

	protected SimpleMapState		buildingState;


	/**
	 * The plan body.
	 */
	@PlanBody
	public IFuture<Void> body()
	{
		final Future<Void> ret = new Future<Void>();
		
		
//		System.out.println("eat plan");
		
		spaceObject = (SpaceObject)capa.getMySpaceObject();
		

		//TODO: get this more Elegant
		buildingState = (SimpleMapState)capa.getEnvironment().getProperty(ISpaceStrings.BUILDING_STATE);

		HashMap<Vector2Int, Object> hatcheries = buildingState.getTypes(MapType.HATCHERY);

		Set<Vector2Int> hatcherys = hatcheries.keySet();
		ArrayList<Vector2Int> hatcherylist = new ArrayList<Vector2Int>();
		hatcherylist.addAll(hatcherys);

		Vector2Int targetHatchery = null;

		for(Vector2Int pos : hatcherylist)
		{
			TileInfo info = buildingState.getTileAtPos(pos);
			if(((ACenterBuildingInfo)info).getCenterType() == CenterType.CENTER)
			{
				
				targetHatchery = pos;
			}

		}

		if(targetHatchery != null)
		{
			spaceObject.setProperty(ISObjStrings.PROPERTY_GOAL, PlanType.EAT);
			IFuture<AchieveMoveToSector> fut = rplan.dispatchSubgoal(capa.new AchieveMoveToSector(targetHatchery));
			// System.out.println("- - - - - start walking to bed - - - - - ");
			fut.addResultListener(new ExceptionDelegationResultListener<AbstractCreatureBDI.AchieveMoveToSector, Void>(ret)
			{
				public void customResultAvailable(AbstractCreatureBDI.AchieveMoveToSector amt)
				{
//					System.out.println("at pos");
					rplan.waitFor(100).addResultListener(new DefaultResultListener<Void>()
					{
						public void resultAvailable(Void result)
						{

//							System.out.println("finish eating");
							spaceObject.setProperty(ISObjStrings.PROPERTY_FED, 101.0);
							ret.setResult(null);
						}

					});
				}
			});
		}
		else
		{
			System.out.println("no hatchery!!");
			ret.setResult(null);
		}


		return ret;

	}


}
