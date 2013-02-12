package jadex.agentkeeper.worldmodel.buildingtypes;

import jadex.agentkeeper.init.map.process.IMap;
import jadex.agentkeeper.worldmodel.BuildingInfo;

public class LairInfo extends BuildingInfo {
	
	private static final String[] NEIGHBORS = new String[] { IMap.GOLD };
	
	public LairInfo()
	{
		this.hitpoints = 30;
	}

	@Override
	public String[] getNeighbors() {
		
		return NEIGHBORS;
	}

}
