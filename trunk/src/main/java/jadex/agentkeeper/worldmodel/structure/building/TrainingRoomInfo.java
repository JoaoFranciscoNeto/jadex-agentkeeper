package jadex.agentkeeper.worldmodel.structure.building;

import jadex.agentkeeper.worldmodel.enums.MapType;

public class TrainingRoomInfo extends ACenterBuildingInfo {
	
	public TrainingRoomInfo(MapType mapType) 
	{
		super(mapType);
		this.hitpoints = isCenter ? 60 : 30;

	}

}
