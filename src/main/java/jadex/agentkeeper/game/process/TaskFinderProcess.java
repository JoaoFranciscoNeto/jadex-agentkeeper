package jadex.agentkeeper.game.process;

import jadex.agentkeeper.ai.UpdateStatusTask;
import jadex.agentkeeper.game.state.creatures.SimpleCreatureState;
import jadex.agentkeeper.game.state.map.SimpleMapState;
import jadex.agentkeeper.game.state.missions.Auftragsliste;
import jadex.agentkeeper.game.state.missions.Auftragsverwalter;
import jadex.agentkeeper.game.state.player.SimplePlayerState;
import jadex.agentkeeper.init.map.process.InitMapProcess;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISpaceObject;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.agentkeeper.worldmodel.enums.CenterType;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.TileInfo;
import jadex.agentkeeper.worldmodel.structure.building.ACenterBuildingInfo;
import jadex.agentkeeper.worldmodel.structure.solid.DirtPathInfo;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.SimplePropertyObject;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.IObjectTask;
import jadex.extension.envsupport.environment.ISpaceProcess;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple Space Process who is responsible for find automaticly in background
 * new Tasks for Imps, like claim Sectors.
 * 
 * @author jens.hantke
 */
public class TaskFinderProcess extends SimplePropertyObject implements ISpaceProcess {

	private SimpleMapState mapState;

	private Grid2D environment;

	/** Current time stamp */
	private long timestamp;

	/** The time that has passed according to the environment executor. */
	private long progress;

	private double onePerSecondDelta;

	/** The Delta **/
	private double delta;

	public void start(IClockService clock, IEnvironmentSpace space) {
		this.environment = (Grid2D) space;
		this.mapState = (SimpleMapState) environment.getProperty(ISpaceStrings.BUILDING_STATE);
		this.timestamp = clock.getTime();
		this.delta = 25;
	}

	public void execute(IClockService clock, IEnvironmentSpace space) {
		updateProgress(clock);

		if (delta > 30) {
			delta = 0;
			findNotClaimedSectorsAndCreateNewTask();
		}
	}

	private void findNotClaimedSectorsAndCreateNewTask() {
		Object[] allSObj = environment.getSpaceObjects();
		for (int i = 0; i < allSObj.length; i++) {
			SpaceObject sobj = (SpaceObject) allSObj[i];
			TileInfo tileInfo = TileInfo.getTileInfo(sobj, TileInfo.class);
			if (tileInfo != null && MapType.DIRT_PATH.equals(tileInfo.getMapType())) {
				Auftragsverwalter auftraege = (Auftragsverwalter) environment.getProperty("auftraege");
				Vector2Double vector2Double = (Vector2Double) sobj.getProperty(ISpaceObject.Properties.DOUBLE_POSITION);
				auftraege.neuerAuftrag(Auftragsverwalter.BESETZEN, new Vector2Int(vector2Double.getXAsInteger(), vector2Double.getYAsInteger()));
			}
		}
	}

	private void updateProgress(IClockService clock) {
		long currenttime = clock.getTime();
		this.progress = currenttime - timestamp;
		this.timestamp = currenttime;
		onePerSecondDelta = progress * 0.001;
		delta = delta + onePerSecondDelta;
	}

	public void shutdown(IEnvironmentSpace space) {

	}

}
