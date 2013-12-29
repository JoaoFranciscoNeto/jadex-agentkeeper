package jadex.agentkeeper.game.state.player;

import jadex.agentkeeper.view.selection.SelectionMode;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.SpellType;

/**
 * Just a first pre-implementation of the Player State, mainly for the GUI
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
public class SimplePlayerState
{
	private int playerId;
	
	private int claimedSectors = 0;
	
	private double mana;
	
	private int gold;
	
	private boolean showBars;
	
	/* The Building the Player may want to place */
	private MapType mapType;
	
	private SpellType spellType;
	
	/* The Selection Mode */
	private SelectionMode					selectionMode;
	
	public SimplePlayerState(int playerId)
	{
		this.playerId = playerId;
		this.selectionMode = SelectionMode.IMPMODE;
	}

	public int getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
	}
	
	public void addClaimedSector()
	{
		this.claimedSectors++;
	}

	public int getClaimedSectors()
	{
		return claimedSectors;
	}

	public void setClaimedSectors(int claimedSectors)
	{
		this.claimedSectors = claimedSectors;
	}


	public int getGold()
	{
		return gold;
	}

	public void setGold(int gold)
	{
		this.gold = gold;
	}
	
	public void addGold(int amount)
	{
		this.gold = this.gold + amount;
	}
	
	public void removeGold(int amount)
	{
		this.gold = this.gold - amount;
	}

	public double getMana()
	{
		return mana;
	}

	public void setMana(double mana)
	{
		this.mana = mana;
	}

	/**
	 * @return the showBars
	 */
	public boolean isShowBars()
	{
		return showBars;
	}

	/**
	 * @param showBars the showBars to set
	 */
	public void setShowBars(boolean showBars)
	{
		this.showBars = showBars;
	}

	/**
	 * @return the selectionMode
	 */
	public SelectionMode getSelectionMode()
	{
		return selectionMode;
	}

	/**
	 * @param selectionMode the selectionMode to set
	 */
	public void setSelectionMode(SelectionMode selectionMode)
	{
		this.selectionMode = selectionMode;
	}
	
	public void setBuilding(MapType type)
	{
		this.setSelectionMode(SelectionMode.BUILDMODE);
		this.mapType = type;
	}
	
	public void setSpell(SpellType type)
	{
		this.setSelectionMode(SelectionMode.SPELL_MODE);
		this.spellType = type;
	}

	/**
	 * @return the mapType
	 */
	public MapType getMapType()
	{
		return mapType;
	}
}
