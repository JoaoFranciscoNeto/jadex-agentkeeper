package jadex.agentkeeper.worldmodel.buildingtypes;

import jadex.agentkeeper.init.map.process.IMap;
import jadex.agentkeeper.worldmodel.BuildingInfo;
import jadex.agentkeeper.worldmodel.TileInfo;
import jadex.agentkeeper.worldmodel.enums.NeighbourType;

public class DungeonHeartInfo extends ACenterBuildingInfo {
	
	private static final String[] NEIGHBORS = new String[] { IMap.GOLD };
	
	public static String TYPE_NAME = "Hatchery";
	
	public DungeonHeartInfo()
	{
		this.hitpoints = isCenter? 1000 : 10;
		this.neighbourType = NeighbourType.COMPLEX;
	}
	
	public String[] getNeighbors()
	{
		return NEIGHBORS;
	}
}
