package jadex.agentkeeper.worldmodel.buildingtypes;

import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.NeighbourType;

public class DungeonHeartInfo extends ACenterBuildingInfo {
	
	public DungeonHeartInfo(MapType mapType)
	{
		super(mapType);
		this.hitpoints = isCenter? 1000 : 10;
		this.neighbourType = NeighbourType.COMPLEX;
	}
}
