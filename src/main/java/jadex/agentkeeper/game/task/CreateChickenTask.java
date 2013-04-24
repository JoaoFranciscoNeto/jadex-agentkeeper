package jadex.agentkeeper.game.task;

import jadex.agentkeeper.ai.creatures.simple.UpdateChickenTask;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.agentkeeper.worldmodel.structure.building.HatcheryInfo;
import jadex.bridge.service.types.clock.IClockService;
import jadex.extension.envsupport.environment.AbstractTask;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.IObjectTask;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.HashMap;


public class CreateChickenTask extends AbstractTask implements ISObjStrings
{
	private static final double	stepSize	= 0.0005;

	private double				delta		= 4;

	private double				deltadelta	= 4;

	public CreateChickenTask()
	{

	}

	public void execute(IEnvironmentSpace space, ISpaceObject obj, long progress, IClockService clock)
	{

		double weightedprogess = (Double)space.getProperty(ISpaceStrings.GAME_SPEED) * progress;

		delta = delta + (stepSize * weightedprogess);


		if(delta > deltadelta)
		{
			createChicken(obj, space);
			delta = 0;

		}

	}

	public void createChicken(ISpaceObject obj, IEnvironmentSpace space)
	{
		HatcheryInfo hinfo = (HatcheryInfo)obj.getProperty(PROPERTY_TILEINFO);
		
		if(hinfo.hasSpace())
		{
			

			HashMap<String, Object> propschick = new HashMap<String, Object>();
			Vector2Double hpos = (Vector2Double)obj.getProperty(Space2D.PROPERTY_POSITION);
			Vector2Double npos = (Vector2Double)hpos.copy().add(new Vector2Double(Math.random() * 2 - 1, Math.random() * 2 - 1));
			propschick.put(Space2D.PROPERTY_POSITION, npos);
			propschick.put(ISObjStrings.PROPERTY_INTPOSITION, (Vector2Int)obj.getProperty(PROPERTY_INTPOSITION));
			propschick.put("spieler", new Integer(1));

			ArrayList<IObjectTask> list2 = new ArrayList<IObjectTask>();
			list2.add(new UpdateChickenTask());

			// todo: level, owner
			SpaceObject sobj = (SpaceObject)space.createSpaceObject("chicken", propschick, list2);
			
			hinfo.addChicken(sobj);
			
			
		}
	}


}
