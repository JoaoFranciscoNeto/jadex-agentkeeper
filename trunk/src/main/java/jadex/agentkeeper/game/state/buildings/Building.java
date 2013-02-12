package jadex.agentkeeper.game.state.buildings;

import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.math.Vector2Int;


/**
 * Simple dataholding of the Buildungs in the Scene for use in State-related
 * stuff
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
public class Building
{
	private String		type;

	private Vector2Int	location;

	private SpaceObject	spaceobject;


	/**
	 * Constructor for pre-Creation (before SpaceObject creation)
	 * 
	 * @param location
	 * @param type
	 * @param spaceobject
	 */
	public Building(String type, Vector2Int location)
	{
		this.type = type;
		this.location = location;
		this.spaceobject = null;
	}


	/**
	 * @param location
	 * @param type
	 * @param spaceobject
	 */
	public Building(String type, SpaceObject spaceobject)
	{
		this.type = type;
		this.spaceobject = spaceobject;
	}


	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @return the spaceobject
	 */
	public SpaceObject getSpaceobject()
	{
		return spaceobject;
	}

	/**
	 * @param spaceobject the spaceobject to set
	 */
	public void setSpaceobject(SpaceObject spaceobject)
	{
		this.spaceobject = spaceobject;
	}


	public Vector2Int getLocation()
	{
		return location;
	}


	public void setLocation(Vector2Int location)
	{
		this.location = location;
	}

}
