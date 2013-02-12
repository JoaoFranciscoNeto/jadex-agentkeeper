package jadex.agentkeeper.worldmodel.buildingtypes;

import jadex.agentkeeper.init.map.process.IMap;
import jadex.agentkeeper.worldmodel.BuildingInfo;

public class TrainingRoomInfo extends BuildingInfo {
	
	private static final String[] NEIGHBORS = new String[] { IMap.TRAININGROOM, IMap.TRAININGROOMCENTER };
	
	public static String TYPE_NAME = "Hatchery";
	
	
	public TrainingRoomInfo()
	{
		this.hitpoints = 30;
		
	}
	
	public String[] getNeighbors()
	{
		return NEIGHBORS;
	}
}
