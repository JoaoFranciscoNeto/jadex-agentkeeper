package jadex.agentkeeper.game.process;

import jadex.agentkeeper.ai.UpdateStatusTask;
import jadex.agentkeeper.game.state.creatures.SimpleCreatureState;
import jadex.agentkeeper.game.state.map.SimpleMapState;
import jadex.agentkeeper.game.state.player.SimplePlayerState;
import jadex.agentkeeper.init.map.process.InitMapProcess;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.agentkeeper.worldmodel.enums.CenterType;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.TileInfo;
import jadex.agentkeeper.worldmodel.structure.building.ACenterBuildingInfo;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.SimplePropertyObject;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.IObjectTask;
import jadex.extension.envsupport.environment.ISpaceProcess;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Simple Space Process who is responsible for generating the Creatures from the
 * Portal based on the Buildings the Player have.
 * 
 * @author Philip Willuweit
 */
public class GenerateCreatureProcess extends SimplePropertyObject implements ISpaceProcess
{

	private SimplePlayerState	playerState;

	private SimpleCreatureState	creatureState;

	private SimpleMapState		mapState;

	private Grid2D				environment;

	/** Current time stamp */
	private long				timestamp;

	/** The time that has passed according to the environment executor. */
	private long				progress;

	private double				onePerSecondDelta;

	/** The Delta **/
	private double				delta;

	private Vector2Int			portalcenter;

	public void start(IClockService clock, IEnvironmentSpace space)
	{
		this.environment = (Grid2D)space;
		this.playerState = (SimplePlayerState)environment.getProperty(ISpaceStrings.PLAYER_STATE);
		this.creatureState = (SimpleCreatureState)environment.getProperty(ISpaceStrings.CREATURE_STATE);
		this.mapState = (SimpleMapState)environment.getProperty(ISpaceStrings.BUILDING_STATE);
		this.timestamp = clock.getTime();
		this.delta = 0;

		// First: find the Portalcenter
		HashMap<Vector2Int, TileInfo> portaltiles = mapState.getTypes(MapType.PORTAL);
		for(Map.Entry<Vector2Int, TileInfo> entry : portaltiles.entrySet())
		{
			Vector2Int key = entry.getKey();
			TileInfo value = entry.getValue();
			if(((ACenterBuildingInfo)value).getCenterType() == CenterType.CENTER)
			{
				portalcenter = key;
			}
		}


	}

	public void execute(IClockService clock, IEnvironmentSpace space)
	{
		updateProgress(clock);

		if(delta > 30)
		{
			delta = 0;
			int lairtiles = mapState.getTypes(MapType.LAIR).size();
			int hatcherytiles = mapState.getTypes(MapType.HATCHERY).size();
			int trainingroomtiles = mapState.getTypes(MapType.TRAININGROOM).size();
			int librarytiles = mapState.getTypes(MapType.LIBRARY).size();
			int goblins = creatureState.getCreatureCount(InitMapProcess.GOBLIN);
			int warlocks = creatureState.getCreatureCount(InitMapProcess.WARLOCK);
			int orcs = creatureState.getCreatureCount(InitMapProcess.TROLL);

			if(hatcherytiles>=9&&lairtiles>5&&lairtiles-(goblins+orcs+warlocks)>2)
			{
				if(librarytiles>=9&&librarytiles-warlocks>=9&&warlocks<2)
				{
					createMonster(InitMapProcess.WARLOCK);
				}
				else if(trainingroomtiles>=9&&trainingroomtiles-orcs>=9)
				{
					createMonster(InitMapProcess.TROLL);
				}
				else
				{
					createMonster(InitMapProcess.GOBLIN);
				}

			}
			
		}


	}


	private void createMonster(String type)
	{

		HashMap<String, Object> props = new HashMap<String, Object>();

		String level = "1";
		props.put(ISObjStrings.PROPERTY_LEVEL, level);

		String owner = "1";
		props.put(ISObjStrings.PROPERTY_OWNER, owner);

		// props.put(AngreifPlan.ABKLINGZEIT, new
		// Integer(0));
		props.put(Space2D.PROPERTY_POSITION, portalcenter);

		props.put(ISObjStrings.PROPERTY_INTPOSITION, portalcenter);

		props.put(ISObjStrings.PROPERTY_AWAKE, 100.0);
		props.put(ISObjStrings.PROPERTY_FED, 100.0);
		props.put(ISObjStrings.PROPERTY_HAPPINESS, 100.0);
		// props.put("auftragsverwalter", gegnerauftraege);

		props.put("spieler", new Integer(1));
		creatureState.addCreature(type);


		ArrayList<IObjectTask> list = new ArrayList<IObjectTask>();
		list.add(new UpdateStatusTask());

		// todo: level, owner
		environment.createSpaceObject(type, props, list);
	}

	private void updateProgress(IClockService clock)
	{
		long currenttime = clock.getTime();
		this.progress = currenttime - timestamp;
		this.timestamp = currenttime;
		onePerSecondDelta = progress * 0.001;
		delta = delta + onePerSecondDelta;

	}

	public void shutdown(IEnvironmentSpace space)
	{


	}


}
