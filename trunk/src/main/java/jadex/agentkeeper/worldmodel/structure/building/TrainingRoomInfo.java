package jadex.agentkeeper.worldmodel.structure.building;

import jadex.agentkeeper.worldmodel.enums.CenterPattern;
import jadex.agentkeeper.worldmodel.enums.CenterType;
import jadex.agentkeeper.worldmodel.enums.MapType;


public class TrainingRoomInfo extends ACenterBuildingInfo
{

	public TrainingRoomInfo(MapType mapType)
	{
		super(mapType);
		this.centerPattern = CenterPattern.ONE_BORDER_1;
		this.hitpoints = centerType == CenterType.CENTER ? 60 : 30;
	}
	
	

}
