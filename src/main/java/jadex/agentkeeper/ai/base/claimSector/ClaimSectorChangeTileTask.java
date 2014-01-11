package jadex.agentkeeper.ai.base.claimSector;

import jadex.agentkeeper.game.state.map.TileChanger;
import jadex.agentkeeper.game.state.missions.Task;
import jadex.agentkeeper.game.state.missions.TaskPoolManager;
import jadex.agentkeeper.game.state.missions.TaskType;
import jadex.agentkeeper.game.state.player.SimplePlayerState;
import jadex.agentkeeper.util.ISO;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.Neighborhood;
import jadex.agentkeeper.util.Neighborcase;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.bridge.service.types.clock.IClockService;
import jadex.extension.envsupport.environment.AbstractTask;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ClaimSectorChangeTileTask extends AbstractTask {

	private static final int DELAY_RESET_COUNT = 100;

	/** The destination property. */
	public static final String PROPERTY_TYPENAME = "claimSectorChangeTileTask";

	/** The destination property. */
	public static final String PROPERTY_DESTINATION = "destination";


	private SpaceObject currentTaskSpaceObject;
	
	@Override
	public void execute(IEnvironmentSpace environment, ISpaceObject obj, long progress, IClockService clock) {
		
		Vector2Int targetPosition = (Vector2Int)getProperty(PROPERTY_DESTINATION);
		
		SimplePlayerState playerState = (SimplePlayerState) environment.getProperty(ISO.Objects.PLAYER_STATE);
		Collection<ISpaceObject> spaceObjectsByGridPosition = ((Grid2D) environment).getSpaceObjectsByGridPosition(targetPosition, null);
		for (ISpaceObject spaceObject : spaceObjectsByGridPosition) {
			for (MapType mapType : MapType.getOnlySolids()) {
				if (mapType.toString().equals(spaceObject.getType())) {
					currentTaskSpaceObject = (SpaceObject) spaceObject;
				}
			}
		}
		
		TileChanger tilechanger = new TileChanger((Grid2D) environment);
		
		String neighborhood = (String) currentTaskSpaceObject.getProperty(ISO.Properties.NEIGHBORHOOD);
		tilechanger.addParameter("bearbeitung", new Integer(0)).addParameter(ISO.Properties.STATUS, "byImpCreated").addParameter(ISO.Properties.CLICKED, false)
				.addParameter(ISO.Properties.LOCKED, false).addParameter(ISO.Properties.NEIGHBORHOOD, neighborhood)
				.addParameter(ISO.Properties.INTPOSITION, targetPosition)
				.addParameter(ISO.Properties.DOUBLE_POSITION, new Vector2Double(targetPosition.getXAsDouble(), targetPosition.getYAsDouble()))
				.changeTile(targetPosition, MapType.CLAIMED_PATH, new ArrayList<MapType>(Arrays.asList(MapType.DIRT_PATH)));

		// imp stop claiming the sector ground
		obj.setProperty(ISObjStrings.PROPERTY_STATUS, "Idle");
		
		playerState.addClaimedSector();
		
		TaskPoolManager taskPoolManager = (TaskPoolManager) environment.getProperty(TaskPoolManager.PROPERTY_NAME);
		for(ISpaceObject neighbour : Neighborhood.getNeighborSpaceObjects(targetPosition, (Grid2D) environment, Neighborcase.getDefault() )) {
			if(Neighborhood.isClaimableWall(neighbour) && !(Boolean)neighbour.getProperty(ISO.Properties.CLICKED)) {
				taskPoolManager.addConnectedTask(TaskType.CLAIM_WALL, (Vector2Int) neighbour.getProperty(ISO.Properties.INTPOSITION));
			}
		}
		taskPoolManager.finishTask((Task)getProperty("Task"));
		
		this.setFinished(environment, obj, true);
	}

}
