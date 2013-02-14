package jadex.agentkeeper.worldmodel.structure.building;

import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.BuildingInfo;

public class LairInfo extends BuildingInfo {
	
	private long creatureId = -1;
	
	public LairInfo(MapType mapType)
	{
		super(mapType);
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


}
