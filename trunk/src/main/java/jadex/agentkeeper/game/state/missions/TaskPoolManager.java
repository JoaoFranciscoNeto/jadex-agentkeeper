package jadex.agentkeeper.game.state.missions;

import java.util.List;
import java.util.Set;

import jadex.agentkeeper.util.ISO;
import jadex.agentkeeper.view.selection.SelectionArea;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.math.Vector2Int;

public class TaskPoolManager implements ITaskPoolManager {
	
	public DiggingSelectorTaskCreator diggingSelectorTaskCreator;

	public static final String PROPERTY_NAME = TaskPoolManager.class.getName();
	
	TaskPool taskPool = new TaskPool();
	
	public TaskPoolManager(DiggingSelectorTaskCreator diggingSelectorTaskCreator) {
		this.diggingSelectorTaskCreator = diggingSelectorTaskCreator;
	}
	

	public synchronized void addTask(TaskType typ, Vector2Int position) {
		this.addTask(new Task(typ, position));
	}
	
	public synchronized void addTask(String typ, Vector2Int position) {
		this.addTask(new Task(typ, position));
	}

	public synchronized void addTask(Task taskToAdd) {
		for (TaskType type : TaskType.values()) {
			if (type.getName().equals(taskToAdd.getTaskType())) {
				taskPool.addTask(type, taskToAdd);
			}
		}
	}
	
	/**
	 * Insert a Task in the Pool. Force the state of Connection(true).
	 * 
	 * @param typ TaskType
	 * @param position Vector2Int
	 */
	public synchronized void addConnectedTask(TaskType typ, Vector2Int position) {
		this.addTask(new Task(typ, position).setConnectedToDungeon(true));
	}
	
	
	
	public synchronized void processSelection(SelectionArea area){
		this.addTasks(diggingSelectorTaskCreator.computeSelectedArea(area));
	}

	private void addTasks(List<Task> computeSelectedArea) {
		for (Task task : computeSelectedArea) {
			this.addTask(task);
		}
	}

	boolean loading = false;

	public synchronized Task calculateAndReturnNextTask(Vector2Int position) {
		if (position != null) {
			if (!loading) {
				loading = true;
				
				Task taskInNeighbourHood = taskPool.checkNeighborfields(position);
				if(taskInNeighbourHood != null) {
					loading = false;
					return taskInNeighbourHood;
				}
				Task withNearestTaskWithGreatPrio = taskPool.getNextTaskToPositionButDependOnPriority(position);
				if(withNearestTaskWithGreatPrio != null) {
					loading = false;
					return withNearestTaskWithGreatPrio;
				}
				loading = false;
				return null;

				
			} else {
				return null;
			}

		} else {

			return null;
		}
	}

	public int getTaskListSize() {
		return TaskPool.getCountedTasks();
	}
	
	public int getWorkableTaskListSize() {
		return TaskPool.getWorkableCountedTasks();
	}
	
	public void updateReachableSelectedSectors(Set<ISpaceObject> set) {
		for(ISpaceObject spaceObject : set){
			Vector2Int updatedSectorPosition = (Vector2Int) spaceObject.getProperty(ISO.Properties.INTPOSITION);
			if(diggingSelectorTaskCreator.isSectorDiggalbe(updatedSectorPosition)){
				taskPool.setReachable(TaskType.DIG_SECTOR, updatedSectorPosition);
			}
		}
		
		
	}

}

/**
 * First interface in inner class, if there are more TaskPools it should be in
 * its own file.
 * 
 * @author jens.hantke
 * 
 */
interface ITaskPoolManager {

	public void addTask(Task taskToAdd);

	public int getTaskListSize();

}
