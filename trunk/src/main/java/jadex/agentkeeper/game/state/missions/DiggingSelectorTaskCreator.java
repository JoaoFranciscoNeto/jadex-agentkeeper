package jadex.agentkeeper.game.state.missions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import jadex.agentkeeper.util.Neighborcase;
import jadex.agentkeeper.util.Neighborhood;
import jadex.agentkeeper.view.selection.SelectionArea;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector2Int;

public class DiggingSelectorTaskCreator {

	private Grid2D grid;

	public DiggingSelectorTaskCreator(Grid2D grid) {
		this.grid = grid;
	}

	public synchronized List<Task> computeSelectedArea(SelectionArea area){
		Vector2Int endvector = area.getWorldend();
		Vector2Int startvector = area.getWorldstart();

		List<Task> resultTaskList = new ArrayList<Task>();

		int x_start = startvector.getXAsInteger();
		int y_start = startvector.getYAsInteger();
		int x_end = endvector.getXAsInteger();
		int y_end = endvector.getYAsInteger();
		
		for(int x = x_start; x <= x_end; x++)
		{
			for(int y = y_start; y <= y_end; y++)
			{
				Vector2Int currentSelectedSektor = new Vector2Int(x, y);
				if(isSectorDiggalbe(currentSelectedSektor)) {
					Task task = new Task(TaskType.DIG_SECTOR,currentSelectedSektor);
					boolean reach = Neighborhood.isSectorReachableForDigging(currentSelectedSektor, grid);
					task.setConnectedToDungeon(reach);
					resultTaskList.add(task);
				}
			}
		}
		
		return resultTaskList;
	}
	public boolean isSectorDiggalbe(Vector2Int currentSelectedSektor){
		Collection<ISpaceObject> spaceObjectsByGridPosition = grid.getSpaceObjectsByGridPosition(currentSelectedSektor, null);
		boolean isSolid = false;
		for(ISpaceObject spaceObject : spaceObjectsByGridPosition){
			for(MapType mapType : MapType.getDiggableWalls()){
				if(mapType.toString().equals(spaceObject.getType())) {
					isSolid = true;
				}
			}
		}
		return isSolid;
	}
	


	public synchronized List<Vector2Int> setNewTasksReachable(IVector2 position) {
		List<Vector2Int> newReachableTasks = new ArrayList<Vector2Int>();
		for (Neighborcase neighborcase : Neighborcase.getSimple()) {
			Vector2Int currentNeighbourTarget = (Vector2Int) position.copy().add(neighborcase.getVector());
			if (Neighborhood.isReachable(currentNeighbourTarget, grid)) {
				newReachableTasks.add(currentNeighbourTarget);
			}
		}
		return newReachableTasks;
	}

}
