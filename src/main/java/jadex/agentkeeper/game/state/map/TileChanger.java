package jadex.agentkeeper.game.state.map;

import java.util.Collection;

import jadex.agentkeeper.init.map.process.InitMapProcess;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.Vector2Int;

public class TileChanger {
	
	
	
	public static void changeTile(Grid2D environment, Vector2Int targetPosition){
		Collection<ISpaceObject> spaceObjectsByGridPosition = environment.getSpaceObjectsByGridPosition(targetPosition, null);
		for(ISpaceObject spaceObject : spaceObjectsByGridPosition){
			System.out.println("spaceObject.getType(): "+spaceObject.getType());
		}
		
	}

}
