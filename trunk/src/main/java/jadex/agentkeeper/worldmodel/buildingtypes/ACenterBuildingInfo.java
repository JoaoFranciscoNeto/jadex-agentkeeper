package jadex.agentkeeper.worldmodel.buildingtypes;

import jadex.agentkeeper.worldmodel.BuildingInfo;
import jadex.agentkeeper.worldmodel.enums.MapType;

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
