package jadex.agentkeeper.worldmodel.buildingtypes;

import jadex.agentkeeper.init.map.process.IMap;
import jadex.agentkeeper.worldmodel.BuildingInfo;
import jadex.agentkeeper.worldmodel.enums.WalkType;

public class HatcheryCenterInfo extends BuildingInfo {

	private static final String[] NEIGHBORS = new String[] { IMap.HATCHERY,
			IMap.HATCHERYCENTER };

	public static String TYPE_NAME = "Hatchery";

	public HatcheryCenterInfo() {
		this.hitpoints = 30;
		this.walkType = WalkType.IMPASSABLE;
	}

	public String[] getNeighbors() {
		return NEIGHBORS;
	}
}
