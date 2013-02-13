package jadex.agentkeeper.worldmodel.buildingtypes;

import jadex.agentkeeper.init.map.process.IMap;
import jadex.agentkeeper.worldmodel.BuildingInfo;

public class LairInfo extends BuildingInfo {
	
	private static final String[] NEIGHBORS = new String[] { IMap.GOLD };
	
	private long creatureId = -1;
	
	public LairInfo()
	{
		this.hitpoints = 30;
	}

	/**
	 * @return the creatureId
	 */
	public long getCreatureId() {
		return creatureId;
	}

	/**
	 * @param creatureId the creatureId to set
	 */
	public void setCreatureId(long creatureId) {
		this.creatureId = creatureId;
	}

	public String[] getNeighbors() {
		
		return NEIGHBORS;
	}

}
