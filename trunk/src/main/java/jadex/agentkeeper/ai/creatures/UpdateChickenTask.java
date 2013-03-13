package jadex.agentkeeper.ai.creatures;

import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.bridge.service.types.clock.IClockService;
import jadex.extension.envsupport.environment.AbstractTask;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;

import java.util.HashMap;


/**
 * Update the Chicken
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
public class UpdateChickenTask extends AbstractTask implements ISObjStrings
{
	// -------- constants --------

	/** The destination property. */
	public static final String		PROPERTY_TYPENAME	= "updateStatus";

	private static final double		statusChangeSpeed	= 0.0005;

	/** Save the Temp-Values to set the new Value only when a limit is reached */
	private HashMap<String, Double>	tmpValues;
	
	private boolean sleep;
	private boolean paw;
	private boolean idle;
	private boolean pick;
	
	private double value;

	public UpdateChickenTask()
	{
		value = Math.random()*50.0;

	}


	// -------- IObjectTask methods --------

	/**
	 * Executes the task. Handles exceptions. Subclasses should implement
	 * doExecute() instead.
	 * 
	 * @param space The environment in which the task is executing.
	 * @param obj The object that is executing the task.
	 * @param progress The time that has passed according to the environment
	 *        executor.
	 */

	// TODO HACK!
	public void execute(IEnvironmentSpace space, ISpaceObject obj, long progress, IClockService clock)
	{


		double timeDecrease = (Double)space.getProperty(ISpaceStrings.GAME_SPEED) * progress;


		decreaseProperty(obj, timeDecrease * statusChangeSpeed);


	}

	private void decreaseProperty(ISpaceObject obj, double amount)
	{
		
		
		
//		System.out.println("value " + value);

		if(!sleep && value > 5.0)
		{
			// double newValue = (Double)obj.getProperty(prop) -
			// tmpValues.get(prop);
			obj.setProperty(PROPERTY_STATUS, "Sleeping");
			sleep=true;
//			System.out.println("sleeping!");
		}
		else if(!paw && value > 10.0)
		{
			obj.setProperty(PROPERTY_STATUS, "Paw");
			paw=true;
//			System.out.println("paw!");

		}
		else if(!pick && value > 15.0)
		{
			obj.setProperty(PROPERTY_STATUS, "Pick");
			pick = true;
			
//			System.out.println("pick!");
		}
		else if(!idle && value > 20.0)
		{
			
			obj.setProperty(PROPERTY_STATUS, "Idle");
			idle = true;
//			System.out.println("idle!");
		}
		else if(value > 25.0)
		{
			value = 0.0;
			idle=false;
			paw=false;
			sleep=false;
			pick=false;
		}
		
		value = amount + value;

	}
}
