package jadex.agentkeeper.game.state.missions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jadex.agentkeeper.util.Neighborcase;
import jadex.agentkeeper.util.Neighborhood;
import jadex.agentkeeper.view.selection.SelectionArea;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector2Int;

public class DiggingSelectorTaskCreator {

	private HashMap<Vector2Int, Task> tiles_notreachable;

	private ArrayList<Task> reachable_list;

	private Grid2D grid;

	public DiggingSelectorTaskCreator(Grid2D grid) {
		this.grid = grid;
		this.tiles_notreachable = new HashMap<Vector2Int, Task>();

		this.reachable_list = new ArrayList<Task>();
	}

	public synchronized List<Task> computeSelectedArea(SelectionArea area){
		boolean ret = false;
		Vector2Int endvector = area.getWorldend();
		Vector2Int startvector = area.getWorldstart();

		List<Task> resultTaskList = new ArrayList<Task>();


//		ArrayList<SpaceObject> selectDigfieldList = new ArrayList<SpaceObject>();
//		ArrayList<SpaceObject> deselecetFiledList = new ArrayList<SpaceObject>();

//		HashMap<Vector2Int, Task> selectedNotReachableTiles = new HashMap<Vector2Int, Task>();
		int x_start = startvector.getXAsInteger();
		int y_start = startvector.getYAsInteger();
		int x_end = endvector.getXAsInteger();
		int y_end = endvector.getYAsInteger();
		for(int x = x_start; x <= x_end; x++)
		{
			for(int y = y_start; y <= y_end; y++)
			{
				Vector2Int currentSelectedSektor = new Vector2Int(x, y);
				Task task = new Task(TaskType.DIG_SECTOR,currentSelectedSektor);
				boolean reach = Neighborhood.isReachableForDestroy(currentSelectedSektor, grid);
				task.setConnectedToDungeon(reach);
				resultTaskList.add(task);
			}
		}
		
		return resultTaskList;
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
