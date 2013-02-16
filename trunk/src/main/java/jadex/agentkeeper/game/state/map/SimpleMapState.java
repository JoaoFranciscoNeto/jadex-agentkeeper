package jadex.agentkeeper.game.state.map;

import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.extension.envsupport.math.Vector2Int;
import jadex.rules.rulesystem.rules.functions.Length;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


/**
 * Just a first pre-implementation of the Type State, that only holds the
 * Positions for every Type on the Map
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
public class SimpleMapState
{
	private HashMap<MapType, ArrayList<Vector2Int>>	typesList	= new HashMap<MapType, ArrayList<Vector2Int>>();


	public SimpleMapState()
	{
		for(MapType type : MapType.values())
		{
			this.typesList.put(type, new ArrayList<Vector2Int>());
		}
	}

	/**
	 * add a specific Type
	 * 
	 * @param location
	 * @param type
	 */
	public void addType(Vector2Int location, MapType type)
	{
		ArrayList<Vector2Int> myList = this.typesList.get(type);
		myList.add(location);
	}
	
	/**
	 * remove a specific Type
	 * 
	 * @param location
	 * @param type
	 */
	public void remType(Vector2Int location, MapType type)
	{
		ArrayList<Vector2Int> myList = this.typesList.get(type);
		myList.remove(location);
//		System.out.println("typecount: " + type + " " + typesList.get(type).size());
	}


	/**
	 * Get the Number of the Type from a special Type
	 * 
	 * @param type
	 * @return the TypeCount
	 */
	public int getTypeCount(String type)
	{
		return typesList.get(type).size();
	}

	
	/**
	 * 
	 * Check if a special Type is at an position
	 * 
	 * @param pos
	 * @param type
	 * @return x
	 */
	public boolean hasTypeAtPos(Vector2Int pos, String type)
	{
		return typesList.get(type).contains(pos);
	}
	
	/**
	 * 
	 * Check if one Types of a set of Types is at an position
	 * 
	 * @param pos
	 * @param type
	 * @return x
	 */
	public boolean hasTypeAtPos(Vector2Int pos, String[] types)
	{
		boolean ret = false;
		for(int i = 0; i < types.length; i++)
		{
			ret = typesList.get(types[i]).contains(pos);
			if(ret == true)
			{
				continue;
			}
		}
		return ret;
		
	}
	
	
	/**
	 * 
	 * Get the Type from a position
	 * 
	 * @param pos
	 * @return x
	 */
	public MapType getTypeAtPos(Vector2Int pos)
	{
		Iterator<Entry<MapType, ArrayList<Vector2Int>>> it = typesList.entrySet().iterator();
		
		while(it.hasNext())
		{
			Entry<MapType, ArrayList<Vector2Int>> entry = it.next();
			
			ArrayList<Vector2Int> list = entry.getValue();
			if(list.contains(pos))
			{
				return entry.getKey();
			}
			
			
		}
		return null;
	}


}
