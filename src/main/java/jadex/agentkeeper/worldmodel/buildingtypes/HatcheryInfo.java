package jadex.agentkeeper.worldmodel.buildingtypes;


import jadex.agentkeeper.worldmodel.enums.MapType;

public class HatcheryInfo extends ACenterBuildingInfo {

	private int numChickens;

	public HatcheryInfo(MapType mapType) 
	{
		super(mapType);
		this.hitpoints = 30;
		this.hitpoints = isCenter ? 60 : 30;
	}

	public int getNumChickens() {
		return numChickens;
	}

	public void setNumChickens(int numChickens) {
		this.numChickens = numChickens;
	}

}
