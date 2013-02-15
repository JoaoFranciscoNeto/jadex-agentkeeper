package jadex.agentkeeper.worldmodel.enums;

import jadex.agentkeeper.worldmodel.structure.UnknownInfo;
import jadex.agentkeeper.worldmodel.structure.building.DungeonHeartInfo;
import jadex.agentkeeper.worldmodel.structure.building.HatcheryInfo;
import jadex.agentkeeper.worldmodel.structure.building.LairInfo;
import jadex.agentkeeper.worldmodel.structure.building.LibraryInfo;
import jadex.agentkeeper.worldmodel.structure.building.PortalInfo;
import jadex.agentkeeper.worldmodel.structure.building.TortureInfo;
import jadex.agentkeeper.worldmodel.structure.building.TrainingRoomInfo;
import jadex.agentkeeper.worldmodel.structure.building.TreasuryInfo;
import jadex.agentkeeper.worldmodel.structure.solid.DefaultTileInfo;
import jadex.agentkeeper.worldmodel.structure.solid.DirtPathInfo;
import jadex.agentkeeper.worldmodel.structure.solid.GoldInfo;
import jadex.agentkeeper.worldmodel.structure.solid.ImpenetrableRockInfo;
import jadex.agentkeeper.worldmodel.structure.solid.RockInfo;
import jadex.agentkeeper.worldmodel.structure.solid.WaterInfo;

public enum MapType
{
	// Unknown, need for MapEditor issues
	UNKNOWN(TypeVariant.SOLIDMAP, UnknownInfo.class),
	
	// Buildings
	HATCHERY(TypeVariant.BUILDING, HatcheryInfo.class), 
	LAIR(TypeVariant.BUILDING, LairInfo.class), 
	LIBRARY(TypeVariant.BUILDING, LibraryInfo.class), 
	PORTAL(TypeVariant.BUILDING, PortalInfo.class), 
	TORTURE(TypeVariant.BUILDING, TortureInfo.class),
	TREASURY(TypeVariant.BUILDING, TreasuryInfo.class), 
	DUNGEONHEART(TypeVariant.BUILDING, DungeonHeartInfo.class), 
	TRAININGROOM(TypeVariant.BUILDING, TrainingRoomInfo.class), 
	
	//Solid Types
	IMPENETRABLE_ROCK(TypeVariant.SOLIDMAP, ImpenetrableRockInfo.class),
	ROCK(TypeVariant.SOLIDMAP, RockInfo.class),
	REINFORCED_WALL(TypeVariant.SOLIDMAP, RockInfo.class),
	GOLD(TypeVariant.SOLIDMAP, GoldInfo.class),
	GOLD_DROPED(TypeVariant.SOLIDMAP, GoldInfo.class),
	DIRT_PATH(TypeVariant.SOLIDMAP, DirtPathInfo.class),
	CLAIMED_PATH(TypeVariant.SOLIDMAP, DefaultTileInfo.class),
	GEMS(TypeVariant.SOLIDMAP, DefaultTileInfo.class),
	WATER(TypeVariant.SOLIDMAP, WaterInfo.class),
	LAVA(TypeVariant.SOLIDMAP, WaterInfo.class),
	HEROTILE(TypeVariant.SOLIDMAP, DefaultTileInfo.class);

	private Class<?> pojo;
	private TypeVariant variant;
	
	private MapType(TypeVariant variant, Class<?> pojo)
	{
		this.variant = variant;
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

	/**
	 * @return the variant
	 */
	public TypeVariant getVariant()
	{
		return variant;
	}

	/**
	 * @param variant the variant to set
	 */
	public void setVariant(TypeVariant variant)
	{
		this.variant = variant;
	}

}
