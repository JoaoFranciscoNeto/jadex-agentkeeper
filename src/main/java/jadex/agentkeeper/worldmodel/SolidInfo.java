package jadex.agentkeeper.worldmodel;

import jadex.agentkeeper.worldmodel.enums.MapType;


public abstract class SolidInfo extends TileInfo
{

	protected static MapType[]	NEIGHBORS	= new MapType[]{mapType};

	private boolean					isBreakable	= false;


	public SolidInfo(MapType mapType)
	{
		super(mapType);
	}


	public MapType[] getNeighbors()
	{
		return NEIGHBORS;
	}

	/**
	 * @return the isBreakable
	 */
	public boolean isBreakable()
	{
		return isBreakable;
	}

	/**
	 * @param isBreakable the isBreakable to set
	 */
	public void setBreakable(boolean isBreakable)
	{
		this.isBreakable = isBreakable;
	}

}
