package jadex.agentkeeper.game.state.missions;

import jadex.extension.envsupport.math.Vector2Int;

import java.util.HashMap;

public class Task {

	private static HashMap<TaskType, Integer> maximalWorkersOfTask;

	static {
		maximalWorkersOfTask = new HashMap<TaskType, Integer>();
		maximalWorkersOfTask.put(TaskType.CLAIM_SECTOR, new Integer(1));
		maximalWorkersOfTask.put(TaskType.COLLECT_GOLD, new Integer(1));
		maximalWorkersOfTask.put(TaskType.CLAIM_WALL, new Integer(1));
		maximalWorkersOfTask.put(TaskType.DIG_SECTOR, new Integer(1));
		maximalWorkersOfTask.put(TaskType.DESTROY, new Integer(1));
		maximalWorkersOfTask.put(TaskType.ATTACK, new Integer(1));
	}

	public String typeOfTask;
	public String typeOfTarget;
	private int _imps;
	private Vector2Int targetPosition;
	private long targetSpaceObjectID;
	private boolean isConnectedToDungeon = false;

	/**
	 * Task at a non-movable Object, like a wall.
	 * 
	 * @param typ
	 * @param ziel
	 */
	public Task(TaskType typ, Vector2Int ziel) {
		typeOfTask = typ.getName();
		targetPosition = ziel;
		_imps = 0;
	}
	
	/**
	 * Task at a non-movable Object, like a wall.
	 * 
	 * @param typ
	 * @param ziel
	 */
	public Task(String typ, Vector2Int ziel) {
		typeOfTask = typ;
		targetPosition = ziel;
		_imps = 0;
	}

	/**
	 * Task on a movable Object, like a chicken?
	 * 
	 * @param typ
	 *            Typ des Auftrages
	 * @param id
	 *            ID des Zielobjektes
	 * @param position
	 *            Aktuelle Position des Zielobjektes
	 */
	public Task(String typ, Long id) {
		typeOfTask = typ;
		targetSpaceObjectID = id;
	}

	public void setTargetType(String zieltyp) {
		typeOfTarget = zieltyp;
	}

	public String getTargetType() {
		return typeOfTarget;
	}

	public String getTaskType() {
		return typeOfTask;
	}

	public long getTargetSpaceObjectID() {
		return targetSpaceObjectID;
	}

	public boolean isTaskFullOfWorker() {
		if ((maximalWorkersOfTask.get(typeOfTask)).intValue() > _imps) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Only rises the Count of Worker!
	 * 
	 */
	public void addNewWorkerToTask() {
		++_imps;
	}

	/**
	 * Gibt die Zielposition des Auftrags zur�ck.
	 * 
	 * @return ziel
	 */
	public Vector2Int getTargetPosition() {
		return targetPosition;
	}

	public String toString() {
		String ret = typeOfTask + ", Hashcode: " + hashCode();
		ret = ret + " Id: " + targetSpaceObjectID;
		if (targetPosition != null) {
			ret = ret + ", Position: " + targetPosition.getXAsInteger() + "," + targetPosition.getYAsInteger();
		}
		return ret;
	}

	public boolean isConnectedToDungeon() {
		return isConnectedToDungeon;
	}

	public Task setConnectedToDungeon(boolean isConnectedToDungeon) {
		this.isConnectedToDungeon = isConnectedToDungeon;
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		Task otherTask = (Task) obj;
		if(otherTask != null) {
			return this.getTaskType().equals(otherTask.getTaskType()) && this.getTargetPosition().equals(otherTask.getTargetPosition());
		} else {
			return false;
		}
	}

}
