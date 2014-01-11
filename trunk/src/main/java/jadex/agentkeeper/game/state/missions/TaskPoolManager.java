package jadex.agentkeeper.game.state.missions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jadex.agentkeeper.util.ISO;
import jadex.agentkeeper.view.selection.SelectionArea;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.math.Vector2Int;

public class TaskPoolManager implements ITaskPoolManager {
	
	public DiggingSelectorTaskCreator diggingSelectorTaskCreator;

	public static final String PROPERTY_NAME = TaskPoolManager.class.getName();
	
	List<Task> unfinishedTasks = new ArrayList<Task>();
	
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
		boolean hasTask = false;
		for (TaskType type : TaskType.values()) {
			if (type.getName().equals(taskToAdd.getTaskType())) {
				if (taskToAdd.getTargetPosition() != null) {
					taskPool.addTask(type, taskToAdd);
					hasTask = true;
				} else {
					System.out.println("TaskPoolManager: Task with no TargetPosition was tried to add.");
					throw new NullPointerException("TaskPoolManager: Task with no TargetPosition was tried to add.");
				}
			}
		}
		if(!hasTask){
			System.out.println("TaskPoolManager: Unknown TaskType.");
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
					unfinishedTasks.add(taskInNeighbourHood);
					return taskInNeighbourHood;
				}
				Task withNearestTaskWithGreatPrio = taskPool.getNextTaskToPositionButDependOnPriority(position);
				unfinishedTasks.add(withNearestTaskWithGreatPrio);
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
	
	public synchronized void finishTask(Task finishedTask){
		unfinishedTasks.remove(finishedTask);
//		System.out.println("Task:"+finishedTask+" finished");
	}

	public int getTaskListSize() {
		return TaskPool.getCountedTasks();
	}
	
	public synchronized int getWorkableTaskListSize() {
//		System.out.println(taskPool.countWorkableCountedTasks() +" "+ TaskPool.getWorkableCountedTasks());
		return taskPool.countWorkableCountedTasks();
	}
	
	public void updateReachableSelectedSectors(Set<ISpaceObject> set) {
		for(ISpaceObject spaceObject : set){
			Vector2Int updatedSectorPosition = (Vector2Int) spaceObject.getProperty(ISO.Properties.INTPOSITION);
			if(diggingSelectorTaskCreator.isSectorDiggalbe(updatedSectorPosition)){
				taskPool.setReachable(TaskType.DIG_SECTOR, updatedSectorPosition);
			}
		}
	}
	
	public synchronized boolean hasTaskOnPosition(Vector2Int askedPosition){
		boolean positionHasTask = taskPool.hasTaskOnPosition(askedPosition);
		for(Task unfinischedTask : unfinishedTasks) {
			if(unfinischedTask != null) {
				if(unfinischedTask.getTargetPosition().equals(askedPosition)) {
					positionHasTask = true;
				}
			}
		}
		return positionHasTask;	
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
