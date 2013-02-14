package jadex.agentkeeper.worldmodel.structure.solid;

import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.SolidInfo;

public class RockInfo extends SolidInfo
{

	public RockInfo(MapType mapType)
	{
		super(mapType);
		this.isBreakable = true;
		neighbours = new MapType[]{MapType.ROCK, MapType.GOLD, MapType.IMPENETRABLE_ROCK, MapType.REINFORCED_WALL};
	}

}
