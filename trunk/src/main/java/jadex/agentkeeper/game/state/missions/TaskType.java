package jadex.agentkeeper.game.state.missions;


public enum TaskType
{
	
	// Buildings
	DIG_SECTOR(Types.DIG_SECTOR, 2),
	CLAIM_WALL(Types.CLAIM_WALL, 4),
	COLLECT_GOLD(Types.COLLECT_GOLD, 1),
	CLAIM_SECTOR(Types.CLAIM_SECTOR, 3),
	REACH_TARGET(Types.REACH_TARGET, 1),
	DESTROY(Types.DESTROY, 6), 
	ATTACK(Types.ATTACK, 5), /*TODO: If there are anytime Opponents , 5 should be 1*/
	REACH_UNIT(Types.REACH_UNIT, 7);

	interface Types {
		public static final String DIG_SECTOR = "DigSector";
		public static final String CLAIM_WALL = "ClaimWall";
		public static final String COLLECT_GOLD = "CollectGold";
		public static final String CLAIM_SECTOR = "ClaimSector";
		public static final String REACH_TARGET = "ReachTarget";
		public static final String REACH_UNIT = "ReachTarget";
		public static final String DESTROY = "Destroy";
		public static final String ATTACK = "Attach";
	}

	private String name;
	private int priority;
	

	
	private TaskType(String name, int price)
	{
		this.name = name;
		this.setPriority(price); 
	}
	


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public int getPriority() {
		return priority;
	}



	public void setPriority(int priority) {
		this.priority = priority;
	}

}
