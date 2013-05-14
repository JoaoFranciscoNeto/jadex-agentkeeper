package jadex.agentkeeper.worldmodel.structure.building;


import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.extension.envsupport.environment.SpaceObject;

import java.util.Stack;


public class HatcheryInfo extends ACenterBuildingInfo
{

	// tex
	private int					numChickens;

	private int					maxChickens	= 16;

	private Stack<SpaceObject>	chickens	= new Stack<SpaceObject>();

	public HatcheryInfo(MapType mapType)
	{
		super(mapType);
		this.hitpoints = 30;
	}

	public int getMaxChickens()
	{
		return maxChickens;
	}

	public void setMaxChickens(int maxChickens)
	{
		this.maxChickens = maxChickens;
	}

	public boolean hasSpace()
	{
		return numChickens <= maxChickens;
	}

	public void addChicken(SpaceObject chicken)
	{
		chickens.add(chicken);
		numChickens++;
	}


	public SpaceObject getOneChicken()
	{
		SpaceObject ret;
		if(!chickens.isEmpty())
		{
			numChickens--;
			ret = chickens.pop();
		}
		else
		{
			ret = null;
		}
		
		return ret;

	}


	public boolean hasChickens()
	{
		return !chickens.isEmpty();
	}

	/**
	 * @return the numChickens
	 */
	public int getNumChickens()
	{
		return numChickens;
	}

	/**
	 * @param numChickens the numChickens to set
	 */
	public void setNumChickens(int numChickens)
	{
		this.numChickens = numChickens;
	}

}
