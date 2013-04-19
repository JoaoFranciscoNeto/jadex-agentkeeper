package jadex.agentkeeper.ai.creatures;

import jadex.agentkeeper.ai.AbstractBeingBDI.AchieveMoveToSector;
import jadex.agentkeeper.ai.creatures.AbstractCreatureBDI.MaintainCreatureFed;
import jadex.agentkeeper.ai.enums.PlanType;
import jadex.agentkeeper.game.state.map.SimpleMapState;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.building.HatcheryInfo;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;
import jadex.bdiv3.annotation.PlanReason;
import jadex.bdiv3.runtime.IPlan;
import jadex.commons.future.DefaultResultListener;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.environment.ISpaceAction;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.HashMap;
import java.util.Map;

import javax.management.monitor.Monitor;


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

	protected Grid2D				environment;

	/**
	 * The plan body.
	 */
	@PlanBody
	public IFuture<Void> body()
	{
		final Future<Void> ret = new Future<Void>();

		final Monitor mon = new Monitor()
		{

			@Override
			public void stop()
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void start()
			{
				// TODO Auto-generated method stub

			}
		};

		// System.out.println("eat plan");

		spaceObject = (SpaceObject)capa.getMySpaceObject();

		environment = capa.getEnvironment();

		// TODO: get this more Elegant
		buildingState = (SimpleMapState)environment.getProperty(ISpaceStrings.BUILDING_STATE);

		//TODO: Only get closest
		final Vector2Int targetHatchery = buildingState.getClosestHatcheryWithChickens(MapType.HATCHERY, capa.getMyPosition());

		if(targetHatchery != null)
		{
			final SpaceObject chicken;
			final HatcheryInfo info = (HatcheryInfo)buildingState.getTileAtPos(targetHatchery);
			synchronized(mon)
			{
				chicken = info.reserveChicken(environment);

			}
			spaceObject.setProperty(ISObjStrings.PROPERTY_GOAL, PlanType.EAT);
			IFuture<AchieveMoveToSector> fut = rplan.dispatchSubgoal(capa.new AchieveMoveToSector(targetHatchery));
			// System.out.println("- - - - - start walking to bed - - - - - ");
			fut.addResultListener(new ExceptionDelegationResultListener<AbstractCreatureBDI.AchieveMoveToSector, Void>(ret)
			{
				public void customResultAvailable(AbstractCreatureBDI.AchieveMoveToSector amt)
				{
					// System.out.println("at pos");
					rplan.waitFor(100).addResultListener(new DefaultResultListener<Void>()
					{
						public void resultAvailable(Void result)
						{

							// SyncResultListener srl = new
							// SyncResultListener();

							DefaultResultListener<Void> listener = new DefaultResultListener<Void>()
							{
								public void resultAvailable(Void result)
								{
									info.remChicken();
									ret.setResult(null);
								}
							};
							
							
							Map params = new HashMap();
							params.put("Monster", spaceObject);
							params.put("Target", chicken);
							
							
							 environment.performSpaceAction("eat", params, listener);

							 
//							// Agent is a the Hatchery Position
//							if(info.getNumChickens() > 0)
//							{
//								spaceObject.setProperty(ISObjStrings.PROPERTY_FED, 101.0);
//								info.remChicken();
//								environment.destroySpaceObject(chicken.getId());
//								ret.setResult(null);
//							}
//							else
//							{
//								System.out.println("no Chickens left");
//								rplan.abort();
//							}


						}

					});
				}
			});
		}
		else
		{
			System.out.println("no hatchery!!");
			rplan.abort();
		}


		return ret;

	}


}
