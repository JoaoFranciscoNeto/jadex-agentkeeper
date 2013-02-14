package jadex.agentkeeper.worldmodel.solidtypes;

import jadex.agentkeeper.worldmodel.SolidInfo;
import jadex.agentkeeper.worldmodel.enums.MapType;

public class RockInfo extends SolidInfo
{

	public RockInfo(MapType mapType)
	{
		super(mapType);
		this.isBreakable = true;
		neighbours = new MapType[]{MapType.ROCK, MapType.GOLD, MapType.IMPENETRABLE_ROCK, MapType.REINFORCED_WALL};
	}

}
