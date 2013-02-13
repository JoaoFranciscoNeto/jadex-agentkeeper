package jadex.agentkeeper.worldmodel.buildingtypes;

import jadex.agentkeeper.worldmodel.BuildingInfo;

public abstract class ACenterBuildingInfo extends BuildingInfo
{
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
