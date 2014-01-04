package jadex.agentkeeper.game.state.map;

import jadex.agentkeeper.worldmodel.enums.CenterType;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.WalkType;
import jadex.agentkeeper.worldmodel.structure.TileInfo;
import jadex.agentkeeper.worldmodel.structure.building.ACenterBuildingInfo;
import jadex.agentkeeper.worldmodel.structure.building.HatcheryInfo;
import jadex.agentkeeper.worldmodel.structure.building.TrainingRoomInfo;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.IVector1;
import jadex.extension.envsupport.math.IVector2;
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
	private HashMap<MapType, HashMap<Vector2Int, TileInfo>>	typesList	= new HashMap<MapType, HashMap<Vector2Int, TileInfo>>();

//	private HashMap<Vector2Int, MapType>					mapTypes	= new HashMap<Vector2Int, MapType>();

	private HashMap<Vector2Int, TileInfo>					mapInfo		= new HashMap<Vector2Int, TileInfo>();


	public SimpleMapState(EnumSet<MapType> enumSetRange)
	{
		for(MapType type : enumSetRange)
		{
			this.typesList.put(type, new HashMap<Vector2Int, TileInfo>());
		}
	}

	public SimpleMapState(MapType[] values)
	{
		for(MapType type : values)
		{
			this.typesList.put(type, new HashMap<Vector2Int, TileInfo>());
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
		HashMap<Vector2Int, TileInfo> myList = this.typesList.get(info.getMapType());
		myList.put(location, (TileInfo)object);
		this.typesList.put(info.getMapType(), myList);

		mapInfo.put(location, (TileInfo)object);


	}
	
	public synchronized void changeType(Vector2Int location, MapType type)
	{
	}

	public synchronized void removeType(Vector2Int location)
	{
		mapInfo.remove(location);
	}


	/**
	 * Finds the closest Hatchery with simple vector distance
	 * 
	 * @param type
	 * @param creaturePos
	 * @return the Position
	 */
	public synchronized Vector2Int getClosestHatcheryWithChickens(Vector2Double creaturePos)
	{
		HashMap<Vector2Int, TileInfo> hatcheries = getTypes(MapType.HATCHERY);
		Set<Vector2Int> hatcherys = hatcheries.keySet();
		ArrayList<Vector2Int> hatcherylist = new ArrayList<Vector2Int>();
		hatcherylist.addAll(hatcherys);

		Vector2Int ret = null;


		Vector1Double lastdistance = new Vector1Double(999999999);
		for(Vector2Int pos : hatcherylist)
		{
			HatcheryInfo info = (HatcheryInfo)getTileAtPos(pos);
			// Is the Hatchery Center? And has it Chickens?
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
	
	String status = "Rotate";
	
	public synchronized Vector2Int getClosestTrainigsRoomWithTrainingObject(Vector2Double creaturePos, Grid2D environment)
	{
		HashMap<Vector2Int, TileInfo> trainingRooms = getTypes(MapType.TRAININGROOM);
		Set<Vector2Int> trainingRoomPositions= trainingRooms.keySet();
		ArrayList<Vector2Int> trainingRoomList = new ArrayList<Vector2Int>();
		trainingRoomList.addAll(trainingRoomPositions);

		Vector2Int ret = null;


		Vector1Double lastdistance = new Vector1Double(999999999);
		for(Vector2Int pos : trainingRoomList)
		{
			for(ISpaceObject spaceObject :  environment.getSpaceObjectsByGridPosition(pos, null)) {
				System.out.println("training?: "+spaceObject);
				if(spaceObject.getProperty("status").equals("Rotate")){
					spaceObject.setProperty("status", "Rota423te2");
				} else{
					spaceObject.setProperty("status", "Rotate");
				}
				
			}
			TrainingRoomInfo info = (TrainingRoomInfo)getTileAtPos(pos);
			// Is the Hatchery Center? And has it Chickens?
			if(((ACenterBuildingInfo)info).getCenterType() == CenterType.CENTER)
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
	public HashMap<Vector2Int, TileInfo> getTypes(MapType type)
	{
		return this.typesList.get(type);
	}


	/**
	 * Get the Type from a position
	 * 
	 * @param pos
	 * @return x
	 */
	public MapType getTypeAtPos(Vector2Int pos)
	{
		return mapInfo.get(pos).getMapType();
		
	}

	/**
	 * Get the Info from a position
	 * 
	 * @param pos
	 * @return x
	 */
	public TileInfo getInfoAtPos(Vector2Int pos)
	{
		return mapInfo.get(pos);
	}

	/**
	 * Get the Type from a position
	 * 
	 * @param pos
	 * @return x
	 */
	public MapType getTypeAtPos(IVector2 pos)
	{
		Vector2Int intpos = new Vector2Int(Math.round(pos.getXAsFloat()), Math.round(pos.getYAsFloat()));
		return mapInfo.get(intpos).getMapType();
	}


	/**
	 * Set a specific type at a Position
	 * 
	 * @param pos
	 * @param object
	 */
	public void setTypeAtPos(Vector2Int pos, Object object)
	{
		TileInfo info = (TileInfo)object;
//		mapTypes.put(pos, info.getMapType());
		mapInfo.put(pos, info);
	}

	/**
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
		if(mapInfo.get(pos) != null)
		{
			ret = mapInfo.get(pos).getMapType().getWalkType() == WalkType.PASSABLE;
		}
		else
		{
			System.out.println("is movable ret null");
			ret = true;
		}
		return ret;
	}


}
