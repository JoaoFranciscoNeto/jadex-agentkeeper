package jadex.agentkeeper.init.map.process;

import jadex.agentkeeper.game.state.creatures.SimpleCreatureState;
import jadex.agentkeeper.game.state.map.SimpleMapState;
import jadex.agentkeeper.game.state.missions.Auftragsverwalter;
import jadex.agentkeeper.game.state.player.SimplePlayerState;
import jadex.agentkeeper.game.userinput.UserEingabenManager;
import jadex.agentkeeper.game.userinput.magicSpells.ImpCreationSpell;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISO;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.WalkType;
import jadex.agentkeeper.worldmodel.structure.TileInfo;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.SimplePropertyObject;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceProcess;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public abstract class AInitMapProcess extends SimplePropertyObject implements ISpaceProcess, IMap, ISObjStrings
{

	public static Map<String, String>			loadMapMapping;

	public SimpleCreatureState					creatureState;

	public SimpleMapState						buildingState;

	public SimplePlayerState					playerState;

	public UserEingabenManager					uem;

	public static final Map<String, MapType>	TILE_MAP	= new HashMap<String, MapType>();
	static
	{
		TILE_MAP.put("1F", MapType.HATCHERY);
		TILE_MAP.put("1G", MapType.DUNGEONHEART);
		TILE_MAP.put("1C", MapType.TREASURY);
		TILE_MAP.put("1F", MapType.HATCHERY);
		TILE_MAP.put("1D", MapType.LAIR);
		TILE_MAP.put("1E", MapType.PORTAL);
		TILE_MAP.put("1I", MapType.TRAININGROOM);
		TILE_MAP.put("1H", MapType.LIBRARY);
		TILE_MAP.put("1X", MapType.TORTURE);

		TILE_MAP.put("Ob", MapType.IMPENETRABLE_ROCK);
		TILE_MAP.put("Oc", MapType.ROCK);
		TILE_MAP.put("1B", MapType.REINFORCED_WALL);
		TILE_MAP.put("Og", MapType.GOLD);
		TILE_MAP.put("Oh", MapType.GEMS);
		TILE_MAP.put("Od", MapType.DIRT_PATH);
		TILE_MAP.put("1A", MapType.CLAIMED_PATH);
		TILE_MAP.put("Oe", MapType.WATER);
		TILE_MAP.put("Of", MapType.LAVA);
		TILE_MAP.put("Oh", MapType.HEROTILE);

	}

	// -------- attributes --------

	/** The last tick. */
	protected double							lasttick;

	protected void loadAndSetupMissions(Grid2D grid)
	{
		// grid.getBorderMode();
		// Initialize the field.
		try
		{
			Auftragsverwalter auftragsverwalter = new Auftragsverwalter(grid);

			grid.setProperty("auftraege", auftragsverwalter);

			uem = new UserEingabenManager(grid);

			grid.setProperty(ISO.Objects.UserInputManager, uem);
			
			
			grid.setProperty(ISO.Objects.ImpCreationSpell, new ImpCreationSpell(grid));

			this.creatureState = new SimpleCreatureState();
			this.buildingState = new SimpleMapState(MapType.values());
			this.playerState = new SimplePlayerState(1);
			grid.setProperty(ISpaceStrings.CREATURE_STATE, this.creatureState);
			grid.setProperty(ISpaceStrings.BUILDING_STATE, this.buildingState);
			grid.setProperty(ISpaceStrings.PLAYER_STATE, this.playerState);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * This method will be executed by the object before the process is removed
	 * from the execution queue.
	 * 
	 * @param clock The clock.
	 * @param space The space this process is running in.
	 */
	public void shutdown(IEnvironmentSpace space)
	{
		System.out.println("- - - Init Map Process done - - -");
	}

	/**
	 * Executes the environment process
	 * 
	 * @param clock The clock.
	 * @param space The space this process is running in.
	 */
	public void execute(IClockService clock, IEnvironmentSpace space)
	{
		System.out.println("- - - Init Map Process starting - - -");
	}

	public static Vector2Int convertToIntPos(IVector2 pos)
	{
		int xrund = (int)Math.round(pos.getXAsDouble());
		int yrund = (int)Math.round(pos.getYAsDouble());
		Vector2Int temp = new Vector2Int(xrund, yrund);
		return temp;
	}


	@Deprecated
	public static Set<SpaceObject> getNeighborBlocksInRange(IVector2 ziel, int range, Grid2D grid, MapType[] types)
	{
		if(types != null)
		{
			String[] stringtypes = new String[types.length];
			for(int i = 0; i < types.length; i++)
			{
				stringtypes[i] = types[i].toString();
			}
			if(stringtypes != null)
			{
				return grid.getNearGridObjects(ziel, range, stringtypes);
			}
		}
		return null;

	}

	@Deprecated
	public static SpaceObject getSolidTypeAtPos(IVector2 pos, Grid2D gridext)
	{
		SpaceObject ret = null;
		ret = getFieldTypeAtPos(pos, gridext);
		if(ret == null)
		{
			ret = getBuildingTypeAtPos(pos, gridext);
		}
		return ret;
	}

	@Deprecated
	public static SpaceObject getFieldTypeAtPos(IVector2 pos, Grid2D gridext)
	{
		for(MapType type : MapType.getOnlySolids())
		{
			Collection sobjs = gridext.getSpaceObjectsByGridPosition(pos, type.toString());
			if(sobjs != null)
			{
				return (SpaceObject)sobjs.iterator().next();
			}

		}
		return null;
	}

	@Deprecated
	public static SpaceObject getBuildingTypeAtPos(IVector2 pos, Grid2D gridext)
	{
		for(MapType type : MapType.getOnlyBuildings())
		{
			Collection sobjs = gridext.getSpaceObjectsByGridPosition(pos, type.toString());
			if(sobjs != null)
			{
				return (SpaceObject)sobjs.iterator().next();
			}

		}
		return null;
	}

}
