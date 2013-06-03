package jadex.agentkeeper.worldmodel.structure;

import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.NeighbourType;
import jadex.extension.envsupport.environment.SpaceObject;


public abstract class TileInfo
{
	protected  MapType	mapType;

	protected boolean			hasOwner;

	protected int				hitpoints;

	protected int				owner	= 0;

	protected String			neighbourhood;


	protected boolean			locked;

	protected NeighbourType		neighbourType;
	
	Object spaceObjectId;


	//TODO: Remove MapType from Constructor
	public TileInfo(MapType mapType)
	{
		this.hasOwner = false;
		this.locked = false;
		this.hitpoints = 10;
		this.owner = 0;
		this.neighbourhood = "00000000";
		this.neighbourType = NeighbourType.COMPLEX;
		this.mapType = mapType;
		this.spaceObjectId = null;
	}

	public abstract MapType[] getNeighbors();

	/**
	 * @return the hitpoints
	 */
	public int getHitpoints()
	{
		return hitpoints;
	}

	/**
	 * @param hitpoints the hitpoints to set
	 */
	public void setHitpoints(int hitpoints)
	{
		this.hitpoints = hitpoints;
	}

	/**
	 * @return the owner
	 */
	public int getOwner()
	{
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(int owner)
	{
		this.owner = owner;
	}

	/**
	 * @return the neighbourhood
	 */
	public String getNeighbourhood()
	{
		return neighbourhood;
	}

	/**
	 * @param neighbourhood the neighbourhood to set
	 */
	public void setNeighbourhood(String neighbourhood)
	{
		this.neighbourhood = neighbourhood;
	}

	public static final <T> T getTileInfo(SpaceObject obj, Class<T> type)
	{
		// System.out.println("obk " + obj.getType());
		T ret = null;
		ret = (T)obj.getProperty(ISObjStrings.PROPERTY_TILEINFO);
		return ret;
	}

	/**
	 * @return the mapType
	 */
	public MapType getMapType()
	{
		return mapType;
	}

	/**
	 * @param mapType the mapType to set
	 */
	public void setMapType(MapType mapType)
	{
		this.mapType = mapType;
	}

	/**
	 * @return the locked
	 */
	public boolean isLocked()
	{
		return locked;
	}

	/**
	 * @param locked the locked to set
	 */
	public void setLocked(boolean locked)
	{
		this.locked = locked;
	}

	/**
	 * @return the hasOwner
	 */
	public boolean isHasOwner()
	{
		return hasOwner;
	}

	/**
	 * @param hasOwner the hasOwner to set
	 */
	public void setHasOwner(boolean hasOwner)
	{
		this.hasOwner = hasOwner;
	}

	/**
	 * @return the spaceObjectId
	 */
	public Object getSpaceObjectId()
	{
		return spaceObjectId;
	}

	/**
	 * @param spaceObjectId the spaceObjectId to set
	 */
	public void setSpaceObjectId(Object spaceObjectId)
	{
		this.spaceObjectId = spaceObjectId;
	}
}
