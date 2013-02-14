package jadex.agentkeeper.worldmodel.structure.building;


import jadex.agentkeeper.worldmodel.enums.MapType;


public class TreasuryInfo extends ACenterBuildingInfo
{
	
	public static final int MAX_AMOUNT = 3000;
	private int amount = 0;

	public TreasuryInfo(MapType mapType)
	{
		super(mapType);
		this.hitpoints = 30;
		this.hitpoints = isCenter ? 60 : 30;
	}

	/**
	 * @return the amount
	 */
	public int getAmount()
	{
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount)
	{
		this.amount = amount;
	}

}
