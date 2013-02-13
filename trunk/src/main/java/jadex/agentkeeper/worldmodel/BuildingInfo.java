package jadex.agentkeeper.worldmodel;

import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.WalkType;


public abstract class BuildingInfo extends TileInfo
{

	protected static MapType[]	NEIGHBORS	= new MapType[]{mapType};

	public BuildingInfo(MapType mapType)
	{
		super(mapType);
		this.walkType = WalkType.PASSABLE;
		this.hitpoints = 30;
	}

	public MapType[] getNeighbors()
	{
		return NEIGHBORS;
	}
}
