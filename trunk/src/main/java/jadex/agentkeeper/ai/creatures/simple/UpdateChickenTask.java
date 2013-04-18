package jadex.agentkeeper.ai.creatures.simple;

import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.bridge.service.types.clock.IClockService;
import jadex.extension.envsupport.environment.AbstractTask;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;


/**
 * Update the Chicken
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
public class UpdateChickenTask extends AbstractTask implements ISObjStrings
{
	// -------- constants --------

	/** The destination property. */
	public static final String	PROPERTY_TYPENAME	= "updateStatus";

	private static final double	statusChangeSpeed	= 0.0005;

	private double				value;

	private int					propCount = 0;
	
	private boolean idle = false;

	public UpdateChickenTask()
	{

		value = Math.random() * 50.0;

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


//		obj.setProperty(PROPERTY_STATUS, "Sleeping");
	}

	private void decreaseProperty(ISpaceObject obj, double amount)
	{


		if(value > 5.0)
		{
			value = 0;
			
			if(idle)
			{
				propCount = Math.round((float)(Math.random() * 4));
//				propCount = 0;
			}
			else
			{
				propCount = 3;
//				propCount = 0;
			}
			
			idle = !idle;
			
//			System.out.println("propCount: " + propCount);
			updateProperty(obj, propCount);

		}


		value = amount + value;
	}


	private void updateProperty(ISpaceObject obj, int prop)
	{
		
		switch(prop)
		{
			case 0:
				obj.setProperty(PROPERTY_STATUS, "Sleeping");
				break;
			case 1:
				obj.setProperty(PROPERTY_STATUS, "Paw");
				break;
			case 2:
				obj.setProperty(PROPERTY_STATUS, "Pick");
//				obj.setProperty(Space2D.PROPERTY_POSITION, hpos.add(new Vector2Double(1,1)));
				break;
			case 3:
				obj.setProperty(PROPERTY_STATUS, "Idle");
				break;

			default:
				obj.setProperty(PROPERTY_STATUS, "Walk");
				break;
		}

	}
}
