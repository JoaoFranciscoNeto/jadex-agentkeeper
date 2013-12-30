package jadex.agentkeeper.game.state.missions;

import java.util.List;

import jadex.agentkeeper.view.selection.SelectionArea;
import jadex.extension.envsupport.math.IVector2;
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

	@Override
	public int getTaskListSize() {
		return TaskPool.getCountedTasks();
	}
	
	public void updateReachableSelectedSectors(IVector2 position){
		taskPool.setReachable(TaskType.DIG_SECTOR, position);
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
