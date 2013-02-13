package jadex.agentkeeper.worldmodel.enums;

import jadex.agentkeeper.worldmodel.SolidInfo;
import jadex.agentkeeper.worldmodel.UnknownInfo;
import jadex.agentkeeper.worldmodel.buildingtypes.DungeonHeartInfo;
import jadex.agentkeeper.worldmodel.buildingtypes.HatcheryInfo;
import jadex.agentkeeper.worldmodel.buildingtypes.LairInfo;
import jadex.agentkeeper.worldmodel.buildingtypes.TrainingRoomInfo;

public enum MapType
{
	UNKNOWN("unknown", UnknownInfo.class),
	// Buildings
	HATCHERY("hatchery",HatcheryInfo.class), 
	HATCHERYCENTER("hatcherycenter",HatcheryInfo.class), 
	LAIR("lair",LairInfo.class), 
	DUNGEONHEART("dungeonheart",DungeonHeartInfo.class), 
	DUNGEONHEARTCENTER("dungeonheartcenter",DungeonHeartInfo.class), 
	TRAININGROOM("trainingroom",TrainingRoomInfo.class), 
	TRAININGROOMCENTER("trainingroomcenter",TrainingRoomInfo.class),
	
	//Solid Types
	IMPENETRABLE_ROCK("impenetrable_rock", SolidInfo.class),
	ROCK("rock", SolidInfo.class),
	REINFORCED_WALL("reinforced_wall", SolidInfo.class),
	GOLD("gold", SolidInfo.class),
	GOLD_DROPED("gold_dropped", SolidInfo.class),
	DIRT_PATH("dirt_path", SolidInfo.class),
	CLAIMED_PATH("claimed_path", SolidInfo.class),
	GEMS("gems", SolidInfo.class),
	WATER("water", SolidInfo.class),
	LAVA("lava", SolidInfo.class),
	HEROTILE("herotile", SolidInfo.class);
	
	
	
	
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
