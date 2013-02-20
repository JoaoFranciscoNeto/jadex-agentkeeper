package jadex.agentkeeper.worldmodel.structure.solid;

import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.WalkType;
import jadex.agentkeeper.worldmodel.structure.SolidInfo;

public class ImpenetrableRockInfo extends SolidInfo
{

	public ImpenetrableRockInfo(MapType mapType)
	{
		super(mapType);
		this.isBreakable = false;
		neighbours = new MapType[]{MapType.ROCK, MapType.GOLD, MapType.IMPENETRABLE_ROCK, MapType.REINFORCED_WALL};
	}

}
