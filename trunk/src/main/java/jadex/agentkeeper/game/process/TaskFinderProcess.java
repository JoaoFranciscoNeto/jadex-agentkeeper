package jadex.agentkeeper.game.process;

import jadex.agentkeeper.game.state.missions.Auftragsverwalter;
import jadex.agentkeeper.util.ISO;
import jadex.agentkeeper.util.Neighborhood;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.TileInfo;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.SimplePropertyObject;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.ISpaceProcess;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.Set;

/**
 * Simple Space Process who is responsible for find automaticly in background
 * new Tasks for Imps, like claim Sectors.
 * 
 * @author jens.hantke
 */
public class TaskFinderProcess extends SimplePropertyObject implements ISpaceProcess {

	private static final int DELAY_RESET_COUNT = 60*1;

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
		this.timestamp = clock.getTime();
		this.delta = DELAY_RESET_COUNT-5;
	}

	public void execute(IClockService clock, IEnvironmentSpace space) {
		updateProgress(clock);

		if (delta > DELAY_RESET_COUNT) {
			delta = 0;
			findNotClaimedSectorsAndCreateNewTask();
			findNotClaimedWallsAndCreateNewTask();
		}
	}

	private void findNotClaimedSectorsAndCreateNewTask() {
		try{
		Object[] allSObj = environment.getSpaceObjects();
		for (int i = 0; i < allSObj.length; i++) {
			SpaceObject sobj = (SpaceObject) allSObj[i];
			TileInfo tileInfo = TileInfo.getTileInfo(sobj, TileInfo.class);
			if (tileInfo != null && MapType.DIRT_PATH.equals(tileInfo.getMapType())) {
				Auftragsverwalter auftraege = (Auftragsverwalter) environment.getProperty("auftraege");
				Vector2Double vector2Double = (Vector2Double) sobj.getProperty(ISO.Properties.DOUBLE_POSITION);
				auftraege.neuerAuftrag(Auftragsverwalter.BESETZEN, new Vector2Int(vector2Double.getXAsInteger(), vector2Double.getYAsInteger()));
			}
		}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void findNotClaimedWallsAndCreateNewTask() {
		try{
		System.out.println("findNotClaimedWallsAndCreateNewTask_start");
		Object[] allSObj = environment.getSpaceObjects();
		for (int j = 0; j < allSObj.length; j++) {
			SpaceObject sobj = (SpaceObject) allSObj[j];
			TileInfo tileInfo = TileInfo.getTileInfo(sobj, TileInfo.class);
			if (tileInfo != null && MapType.CLAIMED_PATH.equals(tileInfo.getMapType())) {
				Vector2Int vector2Int = (Vector2Int) sobj.getProperty(ISO.Properties.INTPOSITION);
				
				Set<TileInfo> test = Neighborhood.getNeighborTiles(vector2Int,environment);

				for(TileInfo neighbourTile :  test) {
					if(neighbourTile != null &&  neighbourTile.getMapType().equals(MapType.ROCK)) {
						ISpaceObject test2 = environment.getSpaceObject(neighbourTile.getSpaceObjectId());
						boolean clicked = (Boolean) test2.getProperty(ISO.Properties.CLICKED);
						if( !clicked){
							test2.setProperty(ISO.Properties.LOCKED, false);
						}
						Auftragsverwalter auftraege = (Auftragsverwalter) environment.getProperty(ISO.Objects.TaskList);
						
						
						auftraege.neuerAuftrag(Auftragsverwalter.VERSTAERKEWAND, (Vector2Int) test2.getProperty(ISO.Properties.INTPOSITION));
					}
				}
			}
		}
		System.out.println("findNotClaimedWallsAndCreateNewTask");
		} catch(Exception e){
			e.printStackTrace();
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
