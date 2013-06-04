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
import jadex.agentkeeper.worldmodel.structure.solid.ClaimedPathInfo;
import jadex.agentkeeper.worldmodel.structure.solid.DefaultTileInfo;
import jadex.agentkeeper.worldmodel.structure.solid.DirtPathInfo;
import jadex.agentkeeper.worldmodel.structure.solid.GoldInfo;
import jadex.agentkeeper.worldmodel.structure.solid.ImpenetrableRockInfo;
import jadex.agentkeeper.worldmodel.structure.solid.RockInfo;
import jadex.agentkeeper.worldmodel.structure.solid.WaterInfo;

import java.util.EnumSet;

public enum MapType
{
	// Unknown, need for MapEditor issues
	UNKNOWN(TypeVariant.SOLIDMAP, WalkType.IMPASSABLE, UnknownInfo.class),
	
	// Buildings
	HATCHERY(TypeVariant.BUILDING, WalkType.PASSABLE, HatcheryInfo.class), 
	LAIR(TypeVariant.BUILDING, WalkType.PASSABLE, LairInfo.class), 
	LIBRARY(TypeVariant.BUILDING, WalkType.PASSABLE, LibraryInfo.class), 
	PORTAL(TypeVariant.BUILDING, WalkType.PASSABLE, PortalInfo.class), 
	TORTURE(TypeVariant.BUILDING, WalkType.PASSABLE, TortureInfo.class),
	TREASURY(TypeVariant.BUILDING, WalkType.PASSABLE, TreasuryInfo.class), 
	DUNGEONHEART(TypeVariant.BUILDING, WalkType.PASSABLE, DungeonHeartInfo.class), 
	TRAININGROOM(TypeVariant.BUILDING, WalkType.PASSABLE, TrainingRoomInfo.class), 
	
	//Solid Types
	IMPENETRABLE_ROCK(TypeVariant.SOLIDMAP, WalkType.IMPASSABLE, ImpenetrableRockInfo.class),
	ROCK(TypeVariant.SOLIDMAP, WalkType.IMPASSABLE, RockInfo.class),
	REINFORCED_WALL(TypeVariant.SOLIDMAP, WalkType.IMPASSABLE, RockInfo.class),
	GOLD(TypeVariant.SOLIDMAP, WalkType.IMPASSABLE, GoldInfo.class),
	GOLD_DROPED(TypeVariant.SOLIDMAP, WalkType.PASSABLE , DefaultTileInfo.class),
	DIRT_PATH(TypeVariant.SOLIDMAP , WalkType.PASSABLE , DirtPathInfo.class),
	CLAIMED_PATH(TypeVariant.SOLIDMAP , WalkType.PASSABLE , ClaimedPathInfo.class),
	GEMS(TypeVariant.SOLIDMAP , WalkType.IMPASSABLE , DefaultTileInfo.class),
	WATER(TypeVariant.SOLIDMAP , WalkType.IMPASSABLE ,  WaterInfo.class),
	LAVA(TypeVariant.SOLIDMAP, WalkType.PASSABLE, WaterInfo.class),
	HEROTILE(TypeVariant.SOLIDMAP, WalkType.PASSABLE , DefaultTileInfo.class);

	private Class<?> pojo;
	private TypeVariant variant;
	private WalkType walkType;
	
	private MapType(TypeVariant variant, WalkType walkType, Class<?> pojo)
	{
		this.variant = variant;
		this.pojo = pojo;
		this.walkType = walkType;
	}
	
	/**
	 * @return  get an enum subset of the Movables
	 */
	public static EnumSet<MapType> getOnlyMovables()
	{
		EnumSet<MapType> set1 = getOnlyBuildings();
		set1.addAll(EnumSet.of(DIRT_PATH, GEMS));
	    return set1;
	}
	
	/**
	 * @return  get an enum subset of the Solids
	 */
	public static EnumSet<MapType> getOnlySolids()
	{
	    return EnumSet.range(IMPENETRABLE_ROCK, HEROTILE);
	}
	
	 /**
	   * @return  get an enum subset of the Buildings
	   */
	public static EnumSet<MapType> getOnlyBuildings()
	{
	    return EnumSet.range(HATCHERY, TRAININGROOM);
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

	/**
	 * @return the walkType
	 */
	public WalkType getWalkType()
	{
		return walkType;
	}

	/**
	 * @param walkType the walkType to set
	 */
	public void setWalkType(WalkType walkType)
	{
		this.walkType = walkType;
	}

}
