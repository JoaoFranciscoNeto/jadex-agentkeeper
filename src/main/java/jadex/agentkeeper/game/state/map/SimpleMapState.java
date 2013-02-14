package jadex.agentkeeper.game.state.map;

import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Just a first pre-implementation of the Map State, that only holds the Positions for every MapType on the Map
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
public class SimpleMapState
{
	private HashMap<MapType, ArrayList<Vector2Int>> mapTypes = new HashMap<MapType, ArrayList<Vector2Int>>();
	
	
	public SimpleMapState()
	{
		for(MapType type : MapType.values() )
		{
			this.mapTypes.put(type, new ArrayList<Vector2Int>());
		}

	}

	public void addMapType(Vector2Int location, MapType type)
	{
		
		ArrayList<Vector2Int> myList = this.mapTypes.get(type);
		myList.add(location);

		System.out.println("typemapcount: " + type + " " + mapTypes.get(type).size());
	}
	
	
	public int getMapTypeCount(String type)
	{
		return mapTypes.get(type).size();
	}

	
	

}
