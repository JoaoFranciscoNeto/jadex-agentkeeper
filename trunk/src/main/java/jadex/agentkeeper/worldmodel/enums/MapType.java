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
	UNKNOWN("unknown", UnknownInfo.class),
	
	// Buildings
	HATCHERY("hatchery",HatcheryInfo.class), 
	LAIR("lair",LairInfo.class), 
	LIBRARY("library",LibraryInfo.class), 
	PORTAL("portal",PortalInfo.class), 
	TORTURE("portal",TortureInfo.class),
	TREASURY("treasury",TreasuryInfo.class), 
	DUNGEONHEART("dungeonheart",DungeonHeartInfo.class), 
	TRAININGROOM("trainingroom",TrainingRoomInfo.class), 
	
	//Solid Types
	IMPENETRABLE_ROCK("impenetrable_rock", DirtInfo.class),
	ROCK("rock", DirtInfo.class),
	REINFORCED_WALL("reinforced_wall", DirtInfo.class),
	GOLD("gold", DirtInfo.class),
	GOLD_DROPED("gold_dropped", DirtInfo.class),
	DIRT_PATH("dirt_path", DefaultTileInfo.class),
	CLAIMED_PATH("claimed_path", DefaultTileInfo.class),
	GEMS("gems", DefaultTileInfo.class),
	WATER("water", WaterInfo.class),
	LAVA("lava", WaterInfo.class),
	HEROTILE("herotile", DefaultTileInfo.class);

	private String name;
	private Class<?> pojo;
	
	private MapType(String name, Class<?> pojo)
	{
		this.name = name;
		this.pojo = pojo;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
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
