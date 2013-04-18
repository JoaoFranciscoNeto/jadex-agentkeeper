package jadex.agentkeeper.ai.creatures;

import jadex.agentkeeper.util.ISObjStrings;
import jadex.bridge.service.search.SServiceProvider;
import jadex.bridge.service.types.cms.IComponentDescription;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.SimplePropertyObject;
import jadex.commons.future.IResultListener;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceAction;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.environment.space2d.Space2D;

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
//		System.out.println("eat action: "+parameters);
		
		Grid2D grid = (Grid2D)space;
		IComponentDescription owner = (IComponentDescription)parameters.get(ISpaceAction.ACTOR_ID);
		ISpaceObject avatar = grid.getAvatar(owner);
		final ISpaceObject target = (ISpaceObject)parameters.get(ISpaceAction.OBJECT_ID);
		
		if(null==space.getSpaceObject(target.getId()))
		{
			throw new RuntimeException("No such object in space: "+target);
		}
		
//		if(!avatar.getProperty(Space2D.PROPERTY_POSITION).equals(target.getProperty(Space2D.PROPERTY_POSITION)))
//		{
//			throw new RuntimeException("Can only eat objects at same position.");
//		}
		
		
		space.destroySpaceObject(target.getId());
		
		
		avatar.setProperty(PROPERTY_FED, 101);

		
		return null;
	}
}
