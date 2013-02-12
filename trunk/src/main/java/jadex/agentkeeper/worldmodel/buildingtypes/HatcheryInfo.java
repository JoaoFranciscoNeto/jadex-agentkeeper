package jadex.agentkeeper.worldmodel.buildingtypes;

import jadex.agentkeeper.init.map.process.IMap;
import jadex.agentkeeper.worldmodel.BuildingInfo;

public class HatcheryInfo extends BuildingInfo {
	
	private static final String[] NEIGHBORS = new String[] { IMap.HATCHERY, IMap.HATCHERYCENTER };
	
	public static String TYPE_NAME = "Hatchery";
	
	private int numChickens;
	
	public int getNumChickens() {
		return numChickens;
	}

	public void setNumChickens(int numChickens) {
		this.numChickens = numChickens;
	}

	public HatcheryInfo()
	{
		this.hitpoints = 30;
	}
	
	public String[] getNeighbors()
	{
		return NEIGHBORS;
	}
}
