package jadex.agentkeeper.worldmodel.buildingtypes;

import jadex.agentkeeper.worldmodel.enums.MapType;

public class TrainingRoomInfo extends ACenterBuildingInfo {
	
	public TrainingRoomInfo(MapType mapType) 
	{
		super(mapType);
		this.hitpoints = isCenter ? 60 : 30;

	}

}
