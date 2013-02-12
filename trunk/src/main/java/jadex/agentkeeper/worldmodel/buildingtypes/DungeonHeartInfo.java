package jadex.agentkeeper.worldmodel.buildingtypes;

import jadex.agentkeeper.init.map.process.IMap;
import jadex.agentkeeper.worldmodel.BuildingInfo;
import jadex.agentkeeper.worldmodel.TileInfo;

public class DungeonHeartInfo extends BuildingInfo {
	
	private static final String[] NEIGHBORS = new String[] { IMap.GOLD };
	
	public static String TYPE_NAME = "Hatchery";
	
	public DungeonHeartInfo()
	{
		this.hitpoints = 30;
	}
	
	public String[] getNeighbors()
	{
		return NEIGHBORS;
	}
}
