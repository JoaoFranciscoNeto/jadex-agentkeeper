package jadex.agentkeeper.worldmodel.structure.building;

import jadex.agentkeeper.worldmodel.enums.CenterPattern;
import jadex.agentkeeper.worldmodel.enums.MapType;

public class DungeonHeartInfo extends ACenterBuildingInfo {
	
	public DungeonHeartInfo(MapType mapType)
	{
		super(mapType);
		this.hitpoints = isCenter? 1000 : 10;
		this.centerPattern = CenterPattern.ONE_BIG_MIDDLE;
	}
}
