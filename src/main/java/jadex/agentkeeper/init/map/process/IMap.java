package jadex.agentkeeper.init.map.process;

import jadex.agentkeeper.worldmodel.structure.building.HatcheryInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Constants for field and creature types
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
public interface IMap {
	
	public static final String IMP = "imp";
	public static final String GOBLIN = "goblin";
	public static final String WARLOCK = "warlock";
	public static final String TROLL = "troll";
	public static final String THIEF = "thief";
	
	public static final String[] CREATURE_TYPES = {IMP, GOBLIN, WARLOCK, TROLL, THIEF};
	

}
