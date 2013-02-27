package jadex.agentkeeper.worldmodel.structure.building;


import jadex.agentkeeper.worldmodel.enums.CenterType;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.NeighbourType;


public class PortalInfo extends ACenterBuildingInfo
{

	public PortalInfo(MapType mapType)
	{
		super(mapType);
		this.neighbourType = NeighbourType.NONE;
		this.hitpoints = centerType == CenterType.CENTER ? 3000 : 30;
	}

}
