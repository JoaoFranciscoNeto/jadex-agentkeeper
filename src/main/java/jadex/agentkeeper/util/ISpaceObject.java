package jadex.agentkeeper.util;

public interface ISpaceObject {
	
	interface Properties {
		public static final String NEIGHBORHOOD = "neighborhood";
		public static final String STATUS = "status";
		public static final String CLICKED = "clicked";
		public static final String INTPOSITION = "intPos";
		public static final String DOUBLE_POSITION = "position";
		public static final String TILEINFO = "tileInfo";
		public static final String HITPOINTS = "hitpoints";
		public static final String LEVEL = "level";
		public static final String OWNER = "owner";
		
		public static final String AWAKE = "awake";
		public static final String FED = "fed";
		public static final String HAPPINESS = "happiness";
		
		public static final String MORAL = "moral";
		
		public static final String GOAL = "goal";
	}
	
	interface Objects {
		public final static String CREATURE_STATE = "creatureState";
		public final static String BUILDING_STATE = "buildingState";
		public final static String MAPTYPE_STATE = "maptypeState";
		public final static String MAP_STATE = "mapState";
		public final static String PLAYER_STATE = "playerState";
		public final static String GAME_SPEED = "gameSpeed";
	}

}
