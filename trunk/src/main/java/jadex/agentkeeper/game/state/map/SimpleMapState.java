package jadex.agentkeeper.game.state.map;

import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.TileInfo;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.EnumSet;
import java.util.HashMap;


/**
 * Just a first pre-implementation of the Type State, that only holds the
 * Positions for every Type on the Map
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
public class SimpleMapState
{
	private HashMap<MapType, HashMap<Vector2Int, Object>> typesList = new HashMap<MapType, HashMap<Vector2Int, Object>>();



	public SimpleMapState(EnumSet<MapType> enumSetRange)
	{
		for(MapType type : enumSetRange)
		{
			this.typesList.put(type, new HashMap<Vector2Int, Object>());
		}
	}

	/**
	 * add a specific Type
	 * 
	 * @param location
	 * @param type
	 */
	public void addType(Vector2Int location, Object object)
	{
		TileInfo info = (TileInfo)object;
		HashMap<Vector2Int, Object> myList = this.typesList.get(info.getMapType());
		myList.put(location, object);
	}
	
	/**
	 * Get the specific Informations from a type
	 * 
	 * @param type
	 * @return the HashMap
	 */
	public synchronized HashMap<Vector2Int, Object> getTypes(MapType type)
	{
		return this.typesList.get(type);
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

		return null;
	}


}
