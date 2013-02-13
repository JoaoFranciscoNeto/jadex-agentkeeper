package jadex.agentkeeper.worldmodel.buildingtypes;

import jadex.agentkeeper.init.map.process.IMap;
import jadex.agentkeeper.worldmodel.BuildingInfo;

public class TrainingRoomInfo extends ACenterBuildingInfo {

	private static final String[] NEIGHBORS = new String[] { IMap.TRAININGROOM,
			IMap.TRAININGROOMCENTER };

	public static String TYPE_NAME = "Hatchery";

	public TrainingRoomInfo() {
		this.hitpoints = isCenter ? 60 : 30;

	}

	public String[] getNeighbors() {
		return NEIGHBORS;
	}
}
