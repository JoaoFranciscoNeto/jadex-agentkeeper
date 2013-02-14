package jadex.agentkeeper.worldmodel.structure.building;

import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.BuildingInfo;

public abstract class ACenterBuildingInfo extends BuildingInfo
{
	
	
	
	public ACenterBuildingInfo(MapType mapType)
	{
		super(mapType);
	}

	protected boolean isCenter;

	/**
	 * @return the isCenter
	 */
	public boolean isCenter() {
		return isCenter;
	}

	/**
	 * @param isCenter the isCenter to set
	 */
	public void setCenter(boolean isCenter) {
		this.isCenter = isCenter;
	}
	


}
