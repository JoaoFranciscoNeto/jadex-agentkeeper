package jadex.agentkeeper.worldmodel.solidtypes;

import jadex.agentkeeper.worldmodel.SolidInfo;
import jadex.agentkeeper.worldmodel.enums.MapType;

public class ImpenetrableRockInfo extends SolidInfo
{

	public ImpenetrableRockInfo(MapType mapType)
	{
		super(mapType);
		this.isBreakable = false;
		neighbours = new MapType[]{MapType.ROCK, MapType.GOLD, MapType.IMPENETRABLE_ROCK, MapType.REINFORCED_WALL};
	}

}
