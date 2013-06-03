package jadex.agentkeeper.ai.creatures;

import jadex.agentkeeper.ai.AbstractBeingBDI.AchieveMoveToSector;
import jadex.agentkeeper.ai.creatures.AbstractCreatureBDI.MaintainCreatureAwake;
import jadex.agentkeeper.ai.enums.PlanType;
import jadex.agentkeeper.util.ISObjStrings;
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
import jadex.extension.envsupport.environment.SpaceObject;


public class SleepPlan
{
	
	@PlanCapability
	protected AbstractCreatureBDI	capa;

	@PlanAPI
	protected IPlan					rplan;

	@PlanReason
	protected MaintainCreatureAwake	goal;

	protected SpaceObject			spaceObject;
	
	/**
	 * The plan body.
	 */
	@PlanBody
	public IFuture<Void> body()
	{
		spaceObject = (SpaceObject)capa.getMySpaceObject();
		
		spaceObject.setProperty(ISObjStrings.PROPERTY_GOAL, PlanType.SLEEP);
		
		final Future<Void> ret = new Future<Void>();

		IFuture<AchieveMoveToSector> fut = rplan.dispatchSubgoal(capa.new AchieveMoveToSector(capa.getMyLairPosition()));
		fut.addResultListener(new ExceptionDelegationResultListener<AbstractCreatureBDI.AchieveMoveToSector, Void>(ret)
		{
			public void customResultAvailable(AbstractCreatureBDI.AchieveMoveToSector amt)
			{
				idle(capa.getMyAwakeStatus(), 100.0).addResultListener(new DelegationResultListener<Void>(ret));
			}
		});


		return ret;

	}
	
	
	
	/**
	 * Iterative Method
	 * 
	 * @param it iterator
	 * @return empty result when finished
	 */
	public IFuture<Void> idle(final Double status, final Double target)
	{
		final Future<Void> ret = new Future<Void>();

		if(status <= target)
		{
			
//			System.out.println("- - - > idle < - - - ");

			spaceObject.setProperty(ISObjStrings.PROPERTY_STATUS, "Sleeping");
			rplan.waitFor(100).addResultListener(new DefaultResultListener<Void>()
			{
				public void resultAvailable(Void result)
				{

					idle(capa.getMyAwakeStatus(), target).addResultListener(new DelegationResultListener<Void>(ret));
				}

			});
		}
		else
		{
			spaceObject.setProperty(ISObjStrings.PROPERTY_STATUS, "Idle");
			ret.setResult(null);
		}

		return ret;
	}
}
