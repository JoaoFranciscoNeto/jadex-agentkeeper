package jadex.agentkeeper.ai.creatures.simple;

import java.util.HashMap;
import java.util.Map;

import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.future.IResultListener;
import jadex.extension.envsupport.environment.AbstractTask;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceAction;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.environment.space2d.action.GetPosition;
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
	
	private double newdir;
	
	private IVector2 target;
	
	double chickenspeed = 1.0;
	double maxdist;

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
		

		double delta = (Double)space.getProperty(ISpaceStrings.GAME_SPEED) * progress;
		
		maxdist = delta * (Double)space.getProperty(ISpaceStrings.GAME_SPEED) * chickenspeed * 0.001;

		lastduration = 2.0;
		
		decreaseProperty(obj, delta * statusChangeSpeed, lastduration);
		

		if(obj.getProperty(PROPERTY_STATUS).equals("Walk"))
		{
			walkAround((Space2D)space, obj, delta);
		}

		if(obj.getProperty(PROPERTY_STATUS).equals("Sleeping"))
		{
			lastduration = 50.0;
		}



	}

	private void walkAround(Space2D space, ISpaceObject obj, double delta)
	{
//		System.out.println("walk around");
		IVector2 oldpos = ((IVector2)obj.getProperty(Space2D.PROPERTY_POSITION)).copy();
//		oldpos = oldpos.add(delta / 12000);
		
		// convert to vector
		// normally x=cos(dir) and y=sin(dir)
		// here 0 degree is 12 o'clock and the rotation right

//		double x = Math.sin(newdir);
//		double y = Math.cos(newdir);
		
//		System.out.println("delta" + delta);
		
		
//		System.out.println("newdir" + newdirectionvector);
		
		
		
//		
		IVector2 newpos = target.copy().subtract(oldpos).normalize().multiply(maxdist).add(oldpos);
		
//		System.out.println("oldpos: " + newpos);
		
		obj.setProperty(Space2D.PROPERTY_POSITION, newpos);
		

	}

	private void decreaseProperty(ISpaceObject obj, double amount, double lastd)
	{

		if(iamnew)
		{
			iamnew = !iamnew;
//			updateProperty(obj, 3);
			
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
				obj.setProperty(PROPERTY_STATUS, "Pick");
				break;
			case 3:
				obj.setProperty(PROPERTY_STATUS, "Walk");
				setDirection(obj);
				break;
			case 4:
				obj.setProperty(PROPERTY_STATUS, "Pick");
			case 5:
				obj.setProperty(PROPERTY_STATUS, "Pick");

			default:
				obj.setProperty(PROPERTY_STATUS, "Idle");
				break;
		}

	}


	private void setDirection(ISpaceObject obj)
	{
		
		double dir = ((Number)obj.getProperty("direction")).doubleValue();
		
//		System.out.println("dir" + dir);

		// move
		// change direction slightly
		double factor = 10;
		double rotchange = Math.random()*Math.PI/factor-Math.PI/2/factor;
		
		newdir = dir+rotchange;
		if(newdir<0)
			newdir+=Math.PI*2;
		else if(newdir>Math.PI*2)
			newdir-=Math.PI*2;
		
		double x = Math.sin(newdir);
		double y = -Math.cos(newdir);
		
		
		
		// hack
		obj.setProperty("direction", new Double(newdir));
		
		
		IVector2 oldpos = ((IVector2)obj.getProperty(Space2D.PROPERTY_POSITION)).copy();
		
		target = oldpos.copy().add(new Vector2Double(x,y));
		
		
		
//		System.out.println("newdir" + target);
		
		
		
	}
}
