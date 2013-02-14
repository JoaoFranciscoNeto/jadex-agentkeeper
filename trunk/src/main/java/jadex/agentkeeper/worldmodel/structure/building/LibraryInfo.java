package jadex.agentkeeper.worldmodel.structure.building;


import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.NeighbourType;


public class LibraryInfo extends ACenterBuildingInfo
{
	public LibraryInfo(MapType mapType)
	{
		super(mapType);
		this.neighbourType = NeighbourType.NONE;
		this.hitpoints = 30;
		this.hitpoints = isCenter ? 60 : 30;
	}
}
