package jadex.agentkeeper.game.state.buildings;

import jadex.agentkeeper.init.map.process.InitMapProcess;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Just a first pre-implementation of the Building State, mainly for the GUI
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
public class SimpleBuildingState
{
	private HashMap<String, ArrayList<SpaceObject>> buildingCounter = new HashMap<String, ArrayList<SpaceObject>>();
	
	
	public SimpleBuildingState()
	{
		for(int i = 0; i<InitMapProcess.BUILDING_TYPES.length; i++ )
		{
			this.buildingCounter.put(InitMapProcess.BUILDING_TYPES[i], new ArrayList<SpaceObject>());
		}

	}

	public void addBuilding(String type, Vector2Int location, SpaceObject sobj)
	{
		
//		buildings.put(location, new Building(type, sobj));
//		buildingCounter.put(type, buildingCounter.get(type)+1);
		System.out.println("typecount: " + type + " " + buildingCounter.get(type));
	}
	
	
	public int getBuildingCount(String type)
	{
		return buildingCounter.get(type).size();
	}

	
	

}
