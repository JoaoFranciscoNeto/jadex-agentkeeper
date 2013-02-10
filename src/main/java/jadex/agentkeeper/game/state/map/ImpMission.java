package jadex.agentkeeper.game.state.map;

import jadex.extension.envsupport.math.Vector2Int;

public class ImpMission implements IImpMission {
	
	private String type;
	
	private Vector2Int target;
	
	public ImpMission() {
	}

	public ImpMission(String type, Vector2Int target) {
		this.type = type;
		this.target = target;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Vector2Int getTarget() {
		return target;
	}

	public void setTarget(Vector2Int target) {
		this.target = target;
	}

}
