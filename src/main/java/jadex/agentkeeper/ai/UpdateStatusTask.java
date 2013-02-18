package jadex.agentkeeper.ai;

import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.bridge.service.types.clock.IClockService;
import jadex.extension.envsupport.environment.AbstractTask;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;

import java.util.HashMap;


/**
 * Move an object towards a destination.
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
public class UpdateStatusTask extends AbstractTask implements ISObjStrings
{
	// -------- constants --------

	/** The destination property. */
	public static final String		PROPERTY_TYPENAME	= "updateStatus";

	private static final double		awakeDecrease		= 0.002;

	private static final double		awakeIncrease		= 0.01;

	private static final double		fedDecrease			= 0.001;
	private static final double		fedIncrease			= 0.01;


	/** Save the Temp-Values to set the new Value only when a limit is reached */
	private HashMap<String, Double>	tmpValues;

	public UpdateStatusTask()
	{
		tmpValues = new HashMap<String, Double>();
		tmpValues.put(PROPERTY_AWAKE, 0.0);
		tmpValues.put(PROPERTY_FED, 0.0);
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
	public void execute(IEnvironmentSpace space, ISpaceObject obj, long progress, IClockService clock)
	{

		String status = (String)obj.getProperty(PROPERTY_STATUS);

		double timeDecrease = (Double)space.getProperty(ISpaceStrings.GAME_SPEED) * progress;


		if(status.equals("Sleeping"))
		{
			increaseProperty(obj, PROPERTY_AWAKE, timeDecrease * awakeIncrease);
		}
		else
		{
			decreaseProperty(obj, PROPERTY_AWAKE, timeDecrease * awakeDecrease);
		}
		
		if(status.equals("Eating"))
		{
			increaseProperty(obj, PROPERTY_FED, timeDecrease * fedIncrease);
		}
		else
		{
			decreaseProperty(obj, PROPERTY_FED, timeDecrease * fedDecrease);
		}

		


	}

	private void decreaseProperty(ISpaceObject obj, String prop, double amount)
	{
		tmpValues.put(prop, amount + tmpValues.get(prop));

		if(tmpValues.get(prop) > 5)
		{
			double newValue = (Double)obj.getProperty(prop) - tmpValues.get(prop);
			obj.setProperty(prop, newValue > 0 ? newValue : 0);
			tmpValues.put(prop, 0.0);


		}

	}

	private void increaseProperty(ISpaceObject obj, String prop, double amount)
	{
		tmpValues.put(prop, amount + tmpValues.get(prop));

		if(tmpValues.get(prop) > 5)
		{
			double newValue = (Double)obj.getProperty(prop) + tmpValues.get(prop);
			obj.setProperty(prop, newValue > 0 ? newValue : 0);
			tmpValues.put(prop, 0.0);
		}

	}
}
