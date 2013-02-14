package jadex.agentkeeper.worldmodel.structure.solid;

import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.NeighbourType;
import jadex.agentkeeper.worldmodel.structure.SolidInfo;

public class DefaultTileInfo extends SolidInfo
{

	public DefaultTileInfo(MapType mapType)
	{
		super(mapType);
		this.neighbourType = NeighbourType.NONE;
	}

}
