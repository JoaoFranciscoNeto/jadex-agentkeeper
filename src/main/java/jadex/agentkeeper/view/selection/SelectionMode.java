package jadex.agentkeeper.view.selection;

import jadex.agentkeeper.worldmodel.enums.MapType;

public enum SelectionMode
{
	IMPMODE(new MapType[]{MapType.ROCK, MapType.REINFORCED_WALL, MapType.GOLD}),
	BUILDMODE(new MapType[]{MapType.CLAIMED_PATH});
	
	private MapType[] relatedtypes;
	
	private SelectionMode(MapType[] relatedtypes)
	{
		this.relatedtypes = relatedtypes;
	}
	
	public boolean IsSelectionMatchingToMode(MapType type)
	{
		for(int i = 0; i<=this.relatedtypes.length-1; i++ )
		{
			if(this.relatedtypes[i]==type)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the relatedtypes
	 */
	public MapType[] getRelatedtypes()
	{
		return relatedtypes;
	}

	/**
	 * @param relatedtypes the relatedtypes to set
	 */
	public void setRelatedtypes(MapType[] relatedtypes)
	{
		this.relatedtypes = relatedtypes;
	}
}
