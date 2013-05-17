package jadex.agentkeeper.ai.creatures.simple;

import jadex.agentkeeper.game.state.map.SimpleMapState;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.bridge.service.types.clock.IClockService;
import jadex.extension.envsupport.environment.AbstractTask;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector2Double;


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

	private int					propCount			= 0;

	private boolean				iamnew				= true;

	private double				lastduration		= 2.0;

	private Vector2Double		vel					= new Vector2Double(1, 1);

	double						chickenspeed		= 2.4;

	private SimpleMapState		mapState;

	String						status;


	public UpdateChickenTask()
	{

		value = 0;


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

		if(mapState == null)
		{
			mapState = (SimpleMapState)space.getProperty(ISpaceStrings.BUILDING_STATE);
		}

		status = (String)obj.getProperty(PROPERTY_STATUS);

		lastduration = 1.0;

		double delta = (Double)space.getProperty(ISpaceStrings.GAME_SPEED) * progress;

		double movementdelta = delta * chickenspeed * 0.0001;

		


		if(status.equals("Walk"))
		{

			walkAround((Space2D)space, obj, movementdelta);
			lastduration = 1.0 + Math.random() * 2;
		}

		if(status.equals("Sleeping"))
		{
			lastduration = 10.0;
		}

		if(status.equals("Pick"))
		{
			lastduration = 1 + 1 * Math.random();
		}
		
		decreaseProperty(obj, delta * statusChangeSpeed * 2 * Math.random(), lastduration);


	}

	private void walkAround(Space2D space, ISpaceObject obj, double delta)
	{
		Vector2Double newDelta = (Vector2Double)vel.copy().multiply(delta);
		IVector2 oldpos = ((IVector2)obj.getProperty(Space2D.PROPERTY_POSITION));

		IVector2 newpos = oldpos.copy().add(newDelta);

		if(mapState.getTypeAtPos(newpos.copy()) == MapType.HATCHERY)
		{
			obj.setProperty(Space2D.PROPERTY_POSITION, newpos);
		}
		else
		{
			vel = (Vector2Double)vel.multiply(-1);
			newDelta = (Vector2Double)vel.copy().multiply(delta);
			newpos = oldpos.copy().add(newDelta);
			obj.setProperty(Space2D.PROPERTY_POSITION, newpos);
		}


	}

	private void decreaseProperty(ISpaceObject obj, double amount, double lastd)
	{

		if(iamnew)
		{
			iamnew = !iamnew;
			updateProperty(obj, 3);

		}
		else
		{
			if(value > lastd)
			{
				value = 0;

				propCount = Math.round((float)(Math.random() * 10));


				// System.out.println("propCount: " + propCount);
				updateProperty(obj, propCount);

			}

			value = amount + value;
		}


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
				obj.setProperty(PROPERTY_STATUS, "Idle");
			case 3:
				obj.setProperty(PROPERTY_STATUS, "Walk");
				setNewDirection(obj);
				break;
			case 4:
				obj.setProperty(PROPERTY_STATUS, "Walk");
				setNewDirection(obj);
				break;

			default:
				if(!status.equals("Pick"))
				{
					obj.setProperty(PROPERTY_STATUS, "Pick");
				}
				else
				{
					updateProperty(obj, Math.round((float)(Math.random() * 10)));
				}
				break;
		}

	}


	private void setNewDirection(ISpaceObject obj)
	{
		vel = (Vector2Double)vel.redirect(Math.random() * 2 * Math.PI);
	}
}
