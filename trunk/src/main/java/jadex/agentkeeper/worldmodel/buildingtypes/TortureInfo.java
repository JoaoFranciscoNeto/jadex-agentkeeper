package jadex.agentkeeper.worldmodel.buildingtypes;


import jadex.agentkeeper.worldmodel.enums.MapType;


public class TortureInfo extends ACenterBuildingInfo
{

	public TortureInfo(MapType mapType)
	{
		super(mapType);
		this.hitpoints = isCenter ? 60 : 30;
	}
}
