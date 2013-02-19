package jadex.agentkeeper.ai.creatures;

import jadex.agentkeeper.ai.AbstractBeingBDI.AchieveMoveToSector;
import jadex.agentkeeper.ai.creatures.AbstractCreatureBDI.MaintainCreatureAwake;
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
	protected AbstractCreatureBDI capa;
	
	@PlanAPI
	protected IPlan rplan;

	@PlanReason
	protected MaintainCreatureAwake goal;
	
	protected SpaceObject spaceObject;
	
	
	
	/**
	 *  The plan body.
	 */
	@PlanBody
	public IFuture<Void> body()
	{
		
		
//		System.out.println("new SleepPlan");
		
		
//		
//		final Future<Void> ret = new Future<Void>();
//		
//		IFuture<AchieveMoveToSector> fut = rplan.dispatchSubgoal(capa.new AchieveMoveToSector(capa.getMyLairPosition()));
//		fut.addResultListener(new ExceptionDelegationResultListener<AbstractCreatureBDI.AchieveMoveToSector, Void>(ret)
//		{
//			public void customResultAvailable(AbstractCreatureBDI.AchieveMoveToSector amt)
//			{
//				System.out.println("|- | - - - - start sleeping - - - - | - |");
//				sleep().addResultListener(new DelegationResultListener<Void>(ret));
//				
//			}
//		});		
//		return ret;
		this.spaceObject = (SpaceObject)capa.getMySpaceObject();
		spaceObject.setProperty(ISObjStrings.PROPERTY_AWAKE, 200.0);
		return IFuture.DONE;
	}
	
	
	
	
	
	/**
	 * Iterative Method
	 * @param it iterator
	 * @return empty result when finished
	 */
	private IFuture<Void> sleep()
	{
		
		final Future<Void> ret = new Future<Void>();
		if(capa.getMyAwakeStatus() < 100.0)
		{
			spaceObject.setProperty(ISObjStrings.PROPERTY_STATUS, "Sleeping");
			rplan.waitFor(500).addResultListener(new DefaultResultListener<Void>()
			{
				public void resultAvailable(Void result)
				{
					sleep().addResultListener(new DelegationResultListener<Void>(ret));
				}
			});
		}
		else
		{
//			System.out.println("- - - - - finished sleeping - - - - - ");
			spaceObject.setProperty(ISObjStrings.PROPERTY_STATUS, "Idle");
			ret.setResult(null);
		}

		return ret;
	}
	
}
