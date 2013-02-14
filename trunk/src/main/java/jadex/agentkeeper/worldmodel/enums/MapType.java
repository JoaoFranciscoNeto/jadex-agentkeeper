package jadex.agentkeeper.worldmodel.enums;

import jadex.agentkeeper.worldmodel.UnknownInfo;
import jadex.agentkeeper.worldmodel.buildingtypes.DungeonHeartInfo;
import jadex.agentkeeper.worldmodel.buildingtypes.HatcheryInfo;
import jadex.agentkeeper.worldmodel.buildingtypes.LairInfo;
import jadex.agentkeeper.worldmodel.buildingtypes.LibraryInfo;
import jadex.agentkeeper.worldmodel.buildingtypes.PortalInfo;
import jadex.agentkeeper.worldmodel.buildingtypes.TortureInfo;
import jadex.agentkeeper.worldmodel.buildingtypes.TrainingRoomInfo;
import jadex.agentkeeper.worldmodel.buildingtypes.TreasuryInfo;
import jadex.agentkeeper.worldmodel.solidtypes.DefaultTileInfo;
import jadex.agentkeeper.worldmodel.solidtypes.DirtInfo;
import jadex.agentkeeper.worldmodel.solidtypes.WaterInfo;

public enum MapType
{
	// Unknown, need for MapEditor issues
	UNKNOWN(UnknownInfo.class),
	
	// Buildings
	HATCHERY(HatcheryInfo.class), 
	LAIR(LairInfo.class), 
	LIBRARY(LibraryInfo.class), 
	PORTAL(PortalInfo.class), 
	TORTURE(TortureInfo.class),
	TREASURY(TreasuryInfo.class), 
	DUNGEONHEART(DungeonHeartInfo.class), 
	TRAININGROOM(TrainingRoomInfo.class), 
	
	//Solid Types
	IMPENETRABLE_ROCK(DirtInfo.class),
	ROCK(DirtInfo.class),
	REINFORCED_WALL(DirtInfo.class),
	GOLD(DirtInfo.class),
	GOLD_DROPED(DirtInfo.class),
	DIRT_PATH(DefaultTileInfo.class),
	CLAIMED_PATH(DefaultTileInfo.class),
	GEMS(DefaultTileInfo.class),
	WATER(WaterInfo.class),
	LAVA(WaterInfo.class),
	HEROTILE(DefaultTileInfo.class);

	private Class<?> pojo;
	
	private MapType(Class<?> pojo)
	{
		this.pojo = pojo;
	}

	public Class< ? > getPojo()
	{
		return pojo;
	}

	public void setPojo(Class< ? > pojo)
	{
		this.pojo = pojo;
	}

}
