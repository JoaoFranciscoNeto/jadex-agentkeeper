package jadex.agentkeeper.worldmodel.enums;


public enum SpellType
{

	
	// Buildings
	ImpCreation("ImpCreation", 300);



	private String name;
	private int cost;
	

	
	private SpellType(String name, int price)
	{
		this.name = name;
		this.cost = price; 
	}
	

	/**
	 * @return the cost
	 */
	public int getCost()
	{
		return cost;
	}

	/**
	 * @param cost the cost to set
	 */
	public void setCost(int cost)
	{
		this.cost = cost;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
