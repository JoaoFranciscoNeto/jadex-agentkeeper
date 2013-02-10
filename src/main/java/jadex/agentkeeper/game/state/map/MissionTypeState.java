package jadex.agentkeeper.game.state.map;

import jadex.agentkeeper.ai.pathfinding.AStarSearch;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;

public class MissionTypeState 
{
	private ArrayList<Vector2Int> sectors;
	private boolean direct;
	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public MissionTypeState(String type, boolean direct)
	{
		sectors = new ArrayList<Vector2Int>();
		this.direct = direct;
		this.type = type;
	}
	
	public void addSector(Vector2Int sector)
	{
		sectors.add(sector);
	}
	
	public boolean isEmtpy()
	{
		return sectors.isEmpty();
	}
	
	public synchronized Vector2Int getClosest(Vector2Int position, Grid2D env)
	{
		Vector2Int ret = null;
		int cost = 10000;
		
		for(Vector2Int sector : sectors)
		{
			AStarSearch search = new AStarSearch(position, sector, env, direct);
			int tmpcost = search.gibPfadKosten();
			
			if(tmpcost>cost)
			{
				cost = tmpcost;
				ret = sector;
			}
		}
		return ret;
	}
	
	public void removeSector(Vector2Int sector)
	{
		if(sectors.contains(sector))
		{
			sectors.remove(sector);
		}
		else
		{
			System.out.println("not a Mission anyway @ MissionTypeState");
		}
		
	}

	public ArrayList<Vector2Int> getSectors() {
		return sectors;
	}

	public void setSectors(ArrayList<Vector2Int> sectors) {
		this.sectors = sectors;
	}

}
