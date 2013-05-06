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
import jadex.commons.future.DelegationResultListener;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.environment.ISpaceAction;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.HashMap;
import java.util.Iterator;
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

		// System.out.println("eat plan");

		spaceObject = (SpaceObject)capa.getMySpaceObject();

		environment = capa.getEnvironment();

		// TODO: get this more Elegant
		buildingState = (SimpleMapState)environment.getProperty(ISpaceStrings.BUILDING_STATE);

		reachHatchery(buildingState).addResultListener(new DelegationResultListener<Void>(ret));

		return ret;

	}

	private IFuture<Void> reachHatchery(final SimpleMapState buldingState)
	{
		final Future<Void> ret = new Future<Void>();

		// TODO: Only get closest
		final Vector2Int targetHatchery = buildingState.getClosestHatcheryWithChickens(MapType.HATCHERY, capa.getMyPosition());

		
		if(targetHatchery != null)
		{
			final SpaceObject chicken;
			final HatcheryInfo info = (HatcheryInfo)buildingState.getTileAtPos(targetHatchery);

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

							DefaultResultListener<Void> eatlistener = new DefaultResultListener<Void>()
							{
								public void resultAvailable(Void result)
								{
									ret.setResult(null);
								}

								public void exceptionOccurred(Exception exception)
								{
									// look for another Hatchery
									System.out.println("no chickens anymore");
									reachHatchery(buildingState).addResultListener(new DelegationResultListener<Void>(ret));

								}


							};

							Map params = new HashMap();
							params.put("Monster", spaceObject);
							params.put("Target", info);

							environment.performSpaceAction("eat", params, eatlistener);


						}

					});
				}
			});
		}
		else
		{
			System.out.println("no hatchery with chickens!!");
			rplan.abort();
		}


		return ret;
	}


}
