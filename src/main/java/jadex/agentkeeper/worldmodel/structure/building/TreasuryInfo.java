package jadex.agentkeeper.worldmodel.structure.building;


import jadex.agentkeeper.game.state.buildings.Treasury;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.BuildingInfo;


public class TreasuryInfo extends BuildingInfo
{
	
	public static final int MAX_AMOUNT = 3000;
	private int amount = 0;

	public TreasuryInfo(MapType mapType)
	{
		super(mapType);
		this.hitpoints = 30;
		Treasury.totalPossibleAmount += MAX_AMOUNT;
		// TODO: Amount may greater than MaxAmount ? o.O
//		this.amount = Math.round(((float)Math.random())*4000);
	}

	/**
	 * @return the amount
	 */
	public int getAmount()
	{
		return amount;
	}

	/**
	 * @param newamount the amount to set
	 * 
	 */
	public void setAmount(int newamount)
	{
		int oldAmount = this.amount;
		if(oldAmount > newamount) {
			Treasury.currentAmount -= (oldAmount-newamount);
		} else if (oldAmount == newamount) {
			Treasury.currentAmount -= oldAmount;
		} else {
		    Treasury.currentAmount += (newamount-oldAmount);
		}
		this.amount = newamount;
	}
	
	public void addAmount(int amount)
	{
		// TODO: MaxAmount > old amount + new amount , return tail?
		Treasury.currentAmount += amount;
		this.amount+=amount;
	}
	
	public void removeAmount(int amount)
	{
		if(this.amount>=amount){
			Treasury.currentAmount -= amount;
			this.amount-=amount;
		}
	}

}
