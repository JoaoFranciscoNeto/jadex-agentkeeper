package jadex.agentkeeper.ai.base;

import jadex.agentkeeper.util.ISpaceStrings;
import jadex.bridge.service.types.clock.IClockService;
import jadex.extension.envsupport.environment.AbstractTask;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector2Double;


/**
 * Move an object towards a destination.
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
public class MoveTask extends AbstractTask
{
	// -------- constants --------

	/** The destination property. */
	public static final String	PROPERTY_TYPENAME		= "move";

	/** The destination property. */
	public static final String	PROPERTY_DESTINATION	= "destination";

	/** The speed property of the moving object (units per second). */
	public static final String	PROPERTY_SPEED			= "speed";

	/** Backup Exit for Problems */
	private int					maxCounter = 0;

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

		IVector2 idis = (IVector2)getProperty(PROPERTY_DESTINATION);

		double speed = ((Number)getProperty(PROPERTY_SPEED)).doubleValue();

		double gamespeed = (Double)space.getProperty(ISpaceStrings.GAME_SPEED);

		double maxdist = progress * gamespeed * speed * 0.001;

		IVector2 loc = (IVector2)obj.getProperty(Space2D.PROPERTY_POSITION);

		double dist = ((Space2D)space).getDistance(loc, idis).getAsDouble();
		IVector2 newloc;

		Vector2Double destination = new Vector2Double(idis.getXAsDouble(), idis.getYAsDouble());

		if(dist > 0)
		{

			// Todo: how to handle border conditions!?
			newloc = (Vector2Double)(dist <= maxdist ? destination.copy() : destination.copy().subtract(loc).normalize().multiply(maxdist).add(loc));

			((Space2D)space).setPosition(obj.getId(), newloc);

		}
		else
		{
			setFinished(space, obj, true);
		}

		if(maxCounter>60)
		{
//			System.out.println("- - - - -  MOVE TASK HARD EXIT - - - - -");
			setFinished(space, obj, true);
		}
		

		maxCounter++;


	}
}
