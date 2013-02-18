package jadex.agentkeeper.ai.creatures;

import jadex.agentkeeper.ai.AbstractBeingBDI.AchieveMoveToSector;
import jadex.agentkeeper.ai.creatures.AbstractCreatureBDI.MaintainCreatureAwake;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;
import jadex.bdiv3.annotation.PlanReason;
import jadex.bdiv3.runtime.IPlan;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
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
	
	public SleepPlan()
	{
		System.out.println("sleep plan constructor!!");
	}
	
	/**
	 *  The plan body.
	 */
	@PlanBody
	public IFuture<Void> body()
	{
		this.spaceObject = (SpaceObject)capa.getMySpaceObject();
		
		final Future<Void> ret = new Future<Void>();
		
		IFuture<AchieveMoveToSector> fut = rplan.dispatchSubgoal(capa.new AchieveMoveToSector(capa.getMyLairPosition()));
		fut.addResultListener(new ExceptionDelegationResultListener<AbstractCreatureBDI.AchieveMoveToSector, Void>(ret)
		{
			public void customResultAvailable(AbstractCreatureBDI.AchieveMoveToSector amt)
			{
				System.out.println("finished achive move to");
				sleep().addResultListener(new DelegationResultListener<Void>(ret));
				
			}
		});		
		return ret;
	}
	
	
	
	
	
	/**
	 * Iterative Method
	 * @param it iterator
	 * @return empty result when finished
	 */
	private IFuture<Void> sleep()
	{
		final Future<Void> ret = new Future<Void>();
		if(capa.getMyAwakeStatus() <= 100.0)
		{
			spaceObject.setProperty(ISObjStrings.PROPERTY_STATUS, "Sleeping");
			rplan.waitFor(1000).addResultListener(new DefaultResultListener<Void>()
			{
				public void resultAvailable(Void result)
				{
					sleep().addResultListener(new DelegationResultListener<Void>(ret));
				}
			});
		}
		else
		{
			System.out.println("finished sleeping");
			spaceObject.setProperty(ISObjStrings.PROPERTY_STATUS, "Idle");
			ret.setResult(null);
		}

		return ret;
	}
	
	
	private IFuture<Void> sleepStep()
	{
		final Future<Void> ret = new Future<Void>();
		
		IComponentStep<Void> loadstep = new IComponentStep<Void>()
				{
					public IFuture<Void> execute(IInternalAccess ia) 
					{
						final IComponentStep<Void> self = this;
						
						double awake = (Double)spaceObject.getProperty(ISObjStrings.PROPERTY_AWAKE);
						if(capa.getMyPosition().getDistance(capa.getMyLairPosition()).getAsDouble()<0.1 && awake<100.0)
						{
							System.out.print(" + 5 ");
							spaceObject.setProperty(ISObjStrings.PROPERTY_STATUS, "Sleeping");
							awake	= Math.min(awake + 5, 100.0);
							spaceObject.setProperty(ISObjStrings.PROPERTY_AWAKE, awake);
//							capa.setMyAwakeStatus(awake);
						}
						if(awake>=100.0)
						{
							System.out.println("finished sleep");
							spaceObject.setProperty(ISObjStrings.PROPERTY_STATUS, "Dancing");
							ret.setResult(null);
						}
						else
						{
							rplan.waitFor(100).addResultListener(new DefaultResultListener<Void>()
							{
								public void resultAvailable(Void result)
								{
									capa.getAgent().scheduleStep(self);
								}
							});
						}
						return IFuture.DONE;
					};
				};
				capa.getAgent().scheduleStep(loadstep);
		return ret;
	}
}
