package jadex.agentkeeper.worldmodel.structure.solid;

import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.SolidInfo;


public class GoldInfo extends SolidInfo
{
	protected int	gold;

	public GoldInfo(MapType mapType)
	{
		super(mapType);
		this.gold = 3000;
		this.isBreakable = true;
		neighbours = new MapType[]{MapType.ROCK, MapType.GOLD, MapType.IMPENETRABLE_ROCK, MapType.REINFORCED_WALL};
	}

	/**
	 * @return the gold
	 */
	public int getGold()
	{
		return gold;
	}

	/**
	 * @param gold the gold to set
	 */
	public void setGold(int gold)
	{
		this.gold = gold;
	}

}
