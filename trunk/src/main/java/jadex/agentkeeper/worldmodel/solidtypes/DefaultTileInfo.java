package jadex.agentkeeper.worldmodel.solidtypes;

import jadex.agentkeeper.worldmodel.SolidInfo;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.NeighbourType;

public class DefaultTileInfo extends SolidInfo
{

	public DefaultTileInfo(MapType mapType)
	{
		super(mapType);
		this.neighbourType = NeighbourType.NONE;
	}

}
