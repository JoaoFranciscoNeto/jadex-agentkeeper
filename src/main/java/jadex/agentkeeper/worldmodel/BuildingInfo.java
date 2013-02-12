package jadex.agentkeeper.worldmodel;

import jadex.agentkeeper.worldmodel.enums.WalkType;

public abstract class BuildingInfo extends TileInfo {

	public BuildingInfo() {
		this.walkType = WalkType.PASSABLE;
		this.hitpoints = 30;
	}

}
