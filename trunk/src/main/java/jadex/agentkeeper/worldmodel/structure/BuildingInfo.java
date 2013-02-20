package jadex.agentkeeper.worldmodel.structure;

import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.WalkType;


public abstract class BuildingInfo extends TileInfo
{
	protected MapType[]	neighbours	= new MapType[]{mapType}; 
	
	public BuildingInfo(MapType mapType)
	{
		super(mapType);
		this.hasOwner = true;
		this.hitpoints = 30;
	}

	public MapType[] getNeighbors()
	{
		return neighbours;
	}

}
