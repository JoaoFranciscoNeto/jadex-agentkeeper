package jadex.agentkeeper.game.state.map;

import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.HashMap;

/**
 * Just a first pre-implementation of the ImpMapState, mainly for the Imps, to
 * save, share and load their knowledge about what has do be done on the map.
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
public class ImpMapState 
{
	private MissionTypeState[] missions = new MissionTypeState[4];
	private HashMap<String, Integer> missionIdentifier = new HashMap<String, Integer>();
	private MissionTypeState breakWallMissions;
	private MissionTypeState claimMissions;
	private MissionTypeState improveWallMissions;
	private MissionTypeState collectGoldMissions;
	
	
	public ImpMapState()
	{
		breakWallMissions = new MissionTypeState(ImpMission.BREAK_WALL, false);
		claimMissions = new MissionTypeState(ImpMission.CLAIM_SECTOR, true);
		improveWallMissions = new MissionTypeState(ImpMission.IMPROVE_WALL, false);
		collectGoldMissions = new MissionTypeState(ImpMission.COLLECT_GOLD, true);
		missions[0] = breakWallMissions;
		missionIdentifier.put(ImpMission.BREAK_WALL, 0);
		missions[1] = collectGoldMissions;
		missionIdentifier.put(ImpMission.COLLECT_GOLD, 1);
		missions[2] = claimMissions;
		missionIdentifier.put(ImpMission.CLAIM_SECTOR, 2);
		missions[3] = improveWallMissions;
		missionIdentifier.put(ImpMission.IMPROVE_WALL, 3);
	}
	
	public void addMissionSector(Vector2Int sector, String type)
	{
		missions[missionIdentifier.get(type)].addSector(sector);
	}
	
	public ImpMission getClosestMission(Vector2Int position, Grid2D env)
	{
		ImpMission ret = new ImpMission();
		
		for(int i = 0; i<=missions.length;i++)
		{
			if(!missions[i].isEmtpy())
			{
				ret = new ImpMission(missions[i].getType(), missions[i].getClosest(position, env));
			}
		}
		
		return ret;
	}

}
