package jadex.agentkeeper.ai.creatures;

import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.worldmodel.structure.building.HatcheryInfo;
import jadex.commons.SimplePropertyObject;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceAction;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;

import java.util.Map;

/**
 *  Action for eating food or another creature.
 */
public class EatAction extends SimplePropertyObject implements ISpaceAction, ISObjStrings
{
	//-------- constants --------
	
	/** The property for the points of a creature. */
	public static final	String	PROPERTY_POINTS	= "points";
	
	//-------- IAgentAction interface --------
	
	/**
	 * Performs the action.
	 * @param parameters parameters for the action
	 * @param space the environment space
	 * @return action return value
	 */
	public Object perform(Map parameters, IEnvironmentSpace space)
	{
//
//		System.out.println("eat action: "+parameters);
		
		Grid2D grid = (Grid2D)space;
		
		final ISpaceObject monster = (ISpaceObject)parameters.get("Monster");
//		IComponentDescription owner = (IComponentDescription)parameters.get(ISpaceAction.ACTOR_ID);
//		ISpaceObject avatar = grid.getAvatar(owner);
		final HatcheryInfo hinfo = (HatcheryInfo)parameters.get("Target");
		
		final SpaceObject targetChicken = hinfo.getOneChicken();
		
		if(null==targetChicken || null==space.getSpaceObject(targetChicken.getId()))
		{
			throw new RuntimeException("No such object in space: " + targetChicken);
		}


//		parameters.get("Chicken") = targetChicken;
		
//		space.destroySpaceObject(targetChicken.getId());
		

//		monster.setProperty(PROPERTY_FED, 101.0);

		
		return targetChicken;
	}
}
