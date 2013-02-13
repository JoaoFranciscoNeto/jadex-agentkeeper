package jadex.agentkeeper.worldmodel;

import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.NeighbourType;

public class UnknownInfo extends TileInfo
{

	public UnknownInfo(MapType mapType)
	{
		super(mapType);
		this.neighbourType = NeighbourType.NONE;
	}
	
	public MapType[] getNeighbors()
	{
		return null;
	}

}
