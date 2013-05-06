package jadex.agentkeeper.game.state.map;

import jadex.agentkeeper.worldmodel.enums.CenterType;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.WalkType;
import jadex.agentkeeper.worldmodel.structure.TileInfo;
import jadex.agentkeeper.worldmodel.structure.building.ACenterBuildingInfo;
import jadex.agentkeeper.worldmodel.structure.building.HatcheryInfo;
import jadex.extension.envsupport.math.IVector1;
import jadex.extension.envsupport.math.Vector1Double;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;


/**
 * Just a first pre-implementation of the Type State, that only holds the
 * Positions for every Type on the Map
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
public class SimpleMapState
{
	private HashMap<MapType, HashMap<Vector2Int, Object>> typesList = new HashMap<MapType, HashMap<Vector2Int, Object>>();
	
	private HashMap<Vector2Int, MapType> mapTypes = new HashMap<Vector2Int, MapType>();
	
	private HashMap<Vector2Int, TileInfo> mapInfo = new HashMap<Vector2Int, TileInfo>();



	public SimpleMapState(EnumSet<MapType> enumSetRange)
	{
		for(MapType type : enumSetRange)
		{
			this.typesList.put(type, new HashMap<Vector2Int, Object>());
		}
	}

	public SimpleMapState(MapType[] values)
	{
		for(MapType type : values)
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
	public synchronized void addType(Vector2Int location, Object object)
	{
		TileInfo info = (TileInfo)object;
		HashMap<Vector2Int, Object> myList = this.typesList.get(info.getMapType());
		myList.put(location, object);
		this.typesList.put(info.getMapType(), myList);
		mapTypes.put(location, info.getMapType());
		mapInfo.put(location, (TileInfo)object);
	}
	
	
	/**
	 *  Finds the closest Hatchery with simple vector distance 
	 * 
	 * @param type
	 * @param creaturePos
	 * @return the Position
	 */
	public synchronized Vector2Int getClosestHatcheryWithChickens(MapType type, Vector2Double creaturePos)
	{
		HashMap<Vector2Int, Object> hatcheries = getTypes(MapType.HATCHERY);
		Set<Vector2Int> hatcherys = hatcheries.keySet();
		ArrayList<Vector2Int> hatcherylist = new ArrayList<Vector2Int>();
		hatcherylist.addAll(hatcherys);

		Vector2Int ret = null;


		Vector1Double lastdistance = new Vector1Double(999999999);
		for(Vector2Int pos : hatcherylist)
		{
			HatcheryInfo info = (HatcheryInfo)getTileAtPos(pos);
			//Is the Hatchery Center? And has it Chickens?
			if(((ACenterBuildingInfo)info).getCenterType() == CenterType.CENTER && info.hasChickens())
			{
				
				Vector1Double distance = (Vector1Double)pos.getDistance(creaturePos);
				
				if(distance.less(lastdistance))
				{
					lastdistance = distance;
					ret = pos;
				}

			}

		}
		return ret;
	}
	
	
	/**
	 * Get the specific Informations from a type
	 * 
	 * @param type
	 * @return the HashMap
	 */
	public HashMap<Vector2Int, Object> getTypes(MapType type)
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
		return mapTypes.get(pos);
	}
	
	/**
	 * 
	 * Get the Type from a position
	 * 
	 * @param pos
	 * @return x
	 */
	public TileInfo getTileAtPos(Vector2Int pos)
	{
		return mapInfo.get(pos);
	}
	
	/**

	 */
	public boolean isMovable(Vector2Int pos)
	{
		boolean ret = false;
		if(mapTypes.get(pos) != null)
		{
			ret =  mapTypes.get(pos).getWalkType()==WalkType.PASSABLE;
		}
		else
		{
			System.out.println("is movable ret null");
			ret = true;
		}
		return ret;
	}


}
