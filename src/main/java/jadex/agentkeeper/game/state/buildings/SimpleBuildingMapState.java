package jadex.agentkeeper.game.state.buildings;

import jadex.agentkeeper.init.map.process.InitMapProcess;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Just a first pre-implementation of the Building State, that only holds the Positions for every Building on the Map
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
public class SimpleBuildingMapState
{
	private HashMap<String, ArrayList<SpaceObject>> buildings = new HashMap<String, ArrayList<SpaceObject>>();
	
	
	public SimpleBuildingMapState()
	{
		for(int i = 0; i<InitMapProcess.BUILDING_TYPES.length; i++ )
		{
			this.buildings.put(InitMapProcess.BUILDING_TYPES[i], new ArrayList<SpaceObject>());
		}

	}

	public void addBuilding(String type, Vector2Int location, SpaceObject sobj)
	{
		
//		buildings.put(location, new Building(type, sobj));
//		buildingCounter.put(type, buildingCounter.get(type)+1);
		System.out.println("typecount: " + type + " " + buildings.get(type));
	}
	
	
	public int getBuildingCount(String type)
	{
		return buildings.get(type).size();
	}

	
	

}
