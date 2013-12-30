package jadex.agentkeeper.game.state.map;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jadex.agentkeeper.init.map.process.InitMapProcess;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.TileInfo;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.Vector2Int;

public class TileChanger {
	
	
	private static Map<String, Object> parameters;
	SimpleMapState mapstate;
	private Grid2D environment;
	
	public TileChanger(Grid2D environment){
		this.environment = environment;
		mapstate = (SimpleMapState)environment.getProperty(ISpaceStrings.BUILDING_STATE);
		parameters = new HashMap<String,Object>();
	}
	
	public TileChanger addParameter(String key, Object value){
		parameters.put(key, value);
		return this;
	}
	
	
	
	public void changeTile(Vector2Int targetPosition, MapType newType,  List<MapType> oldTypes){
		Collection<ISpaceObject> spaceObjectsByGridPosition = environment.getSpaceObjectsByGridPosition(targetPosition, null);
		for(ISpaceObject spaceObject : spaceObjectsByGridPosition) {
			boolean isRightType = false;
			for(MapType oldType : oldTypes){
				if(oldType.toString().equals(spaceObject.getType())){
					isRightType = true;
					break;
				}
			}
			if(isRightType){
				addNewTileToTileMap(newType, targetPosition);
				ISpaceObject justcreated = environment.createSpaceObject(newType.name(), parameters, null);
				((TileInfo)justcreated.getProperty(ISObjStrings.PROPERTY_TILEINFO)).setSpaceObjectId(justcreated.getId());
				environment.destroySpaceObject(spaceObject.getId());

				System.out.println("spaceObject.getType(): "+spaceObject.getType());
			}
		}
		
	}
	
	public void addNewTileToTileMap(MapType newType, Vector2Int targetPosition){
		Object tile;
		try {
			tile = newType.getPojo().getDeclaredConstructor(MapType.class).newInstance(newType);
			parameters.put(ISObjStrings.PROPERTY_TILEINFO, tile);
			mapstate.addType((Vector2Int)targetPosition, tile);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
	}

}
