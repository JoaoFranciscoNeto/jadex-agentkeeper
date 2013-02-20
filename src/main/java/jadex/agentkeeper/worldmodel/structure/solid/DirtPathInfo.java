package jadex.agentkeeper.worldmodel.structure.solid;

import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.NeighbourType;
import jadex.agentkeeper.worldmodel.enums.WalkType;
import jadex.agentkeeper.worldmodel.structure.SolidInfo;

public class DirtPathInfo extends SolidInfo
{
	
	private static int myQuantity;

	public DirtPathInfo(MapType mapType)
	{
		super(mapType);
		myQuantity++;
		this.hasOwner = true;
		this.neighbourType = NeighbourType.NONE;
	}

	/**
	 * @return the myQuantity
	 */
	public static int getMyQuantity()
	{
		return myQuantity;
	}

	/**
	 * @param myQuantity the myQuantity to set
	 */
	public static void setMyQuantity(int myQuantity)
	{
		DirtPathInfo.myQuantity = myQuantity;
	}

}
