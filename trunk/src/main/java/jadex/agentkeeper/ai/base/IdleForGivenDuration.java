package jadex.agentkeeper.ai.base;

import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.ai.AbstractBeingBDI.PerformIdleForTime;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;
import jadex.bdiv3.annotation.PlanReason;
import jadex.bdiv3.runtime.IPlan;
import jadex.commons.future.DelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.environment.ISpaceObject;

/**
 * Plan to let the Agent hang around.
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 *
 */
public class IdleForGivenDuration
{
	@PlanCapability
	protected AbstractBeingBDI	capa;

	@PlanAPI
	protected IPlan rplan;
	
	@PlanReason
	protected PerformIdleForTime	goal;
	
	ISpaceObject spaceObject;
	
	/**
	 * The plan body.
	 */
	@PlanBody
	public IFuture<Void> body()
	{
		spaceObject = capa.getMySpaceObject();
		
		

		final Future<Void> ret = new Future<Void>();

		setRandomIdleStatus(Math.random()).addResultListener(new DelegationResultListener<Void>(ret));
		
		return ret;
	}

	private IFuture<Void>  setRandomIdleStatus(double random)
	{
		final Future<Void> ret = new Future<Void>();
		
	
		String status = random > 0.5f ? "Idle" : "Dance";
		
		spaceObject.setProperty(ISObjStrings.PROPERTY_STATUS, status);
		
		
		int givenWaittime = goal.getDuration();
				
		long waittime = (long)((Double)capa.getEnvironment().getProperty(ISpaceStrings.GAME_SPEED) * givenWaittime);
		
//		System.out.println("wait for waittime " + waittime);
		
		
		rplan.waitFor(waittime).addResultListener(new DelegationResultListener<Void>(ret)
		{
			public void exceptionOccurred(Exception exception)
			{

				
				
			   System.out.println("exception idle for Given Plan ");


				exception.printStackTrace();
				
				super.exceptionOccurred(exception);
				
			}
			public void customResultAvailable(Void result)
			{
				ret.setResult(null);
			}
		});
		
		
		
		return ret;
		
	}
}
