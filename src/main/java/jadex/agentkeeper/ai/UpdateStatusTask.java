package jadex.agentkeeper.ai;

import jadex.agentkeeper.ai.enums.PlanType;
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

	public static final String		PROPERTY_TYPENAME	= "updateStatus";

	private static final double		awakeDecrease		= 0.0005*2.0;

	private static final double		awakeIncrease		= 0.003;

	private static final double		fedDecrease			= 0.001*2.0;



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
	
		
//		System.out.println("status: " + status);

		double timeDecrease = (Double)space.getProperty(ISpaceStrings.GAME_SPEED) * progress;


		if(status.equals("Sleeping"))
		{
//			System.out.println("status sleeping!!");
			increaseProperty(obj, PROPERTY_AWAKE, timeDecrease * awakeIncrease);
		}
		else
		{

			if((Double)obj.getProperty(PROPERTY_AWAKE)>0&&!((PlanType)obj.getProperty(PROPERTY_GOAL) == PlanType.EAT))
			{
				decreaseProperty(obj, PROPERTY_AWAKE, timeDecrease * awakeDecrease);
			}
			
			if((Double)obj.getProperty(PROPERTY_FED)>0&&!((PlanType)obj.getProperty(PROPERTY_GOAL) == PlanType.SLEEP))
			{
				decreaseProperty(obj, PROPERTY_FED, timeDecrease * fedDecrease);
			}
			
		}
		
//		if(status.equals("Eating"))
//		{
//			increaseProperty(obj, PROPERTY_FED, timeDecrease * fedIncrease);
//		}
//		else
//		{
//			if((Double)obj.getProperty(PROPERTY_FED)>0)
//			{
//			decreaseProperty(obj, PROPERTY_FED, timeDecrease * fedDecrease);
//			}
//		}

		


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
//			System.out.println("increase:  " + (Double)obj.getProperty(PROPERTY_AWAKE));
			double newValue = (Double)obj.getProperty(prop) + tmpValues.get(prop);
			obj.setProperty(prop, newValue > 0 ? newValue : 0);
			tmpValues.put(prop, 0.0);
		}

	}
}
