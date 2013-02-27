package jadex.agentkeeper.worldmodel.structure.building;

import jadex.agentkeeper.worldmodel.enums.CenterPattern;
import jadex.agentkeeper.worldmodel.enums.CenterType;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.BuildingInfo;

public abstract class ACenterBuildingInfo extends BuildingInfo
{
	protected CenterType centerType = CenterType.UNKNOWN;
	protected CenterPattern centerPattern = CenterPattern.ONE_MIDDLE;
	
	public ACenterBuildingInfo(MapType mapType)
	{
		super(mapType);
	}


	/**
	 * @return the centerType
	 */
	public CenterType getCenterType()
	{
		return centerType;
	}



	/**
	 * @param centerType the centerType to set
	 */
	public void setCenterType(CenterType centerType)
	{
		this.centerType = centerType;
	}



	/**
	 * @return the centerPattern
	 */
	public CenterPattern getCenterPattern()
	{
		return centerPattern;
	}



	/**
	 * @param centerPattern the centerPattern to set
	 */
	public void setCenterPattern(CenterPattern centerPattern)
	{
		this.centerPattern = centerPattern;
	}



	


}
