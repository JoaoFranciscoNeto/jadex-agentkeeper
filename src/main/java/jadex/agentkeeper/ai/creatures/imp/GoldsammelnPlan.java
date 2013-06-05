package jadex.agentkeeper.ai.creatures.imp;

import jadex.agentkeeper.ai.pathfinding.AStarSearch;
import jadex.agentkeeper.game.state.map.SimpleMapState;
import jadex.agentkeeper.game.state.missions.Auftragsverwalter;
import jadex.agentkeeper.game.state.player.SimplePlayerState;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.TileInfo;
import jadex.agentkeeper.worldmodel.structure.building.TreasuryInfo;
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Plan fürs Gold Sammeln
 * 
 * @author 8reichel
 * @author 7willuwe
 */
@SuppressWarnings("serial")
public class GoldsammelnPlan extends ImpPlan
{

	public static final int	ABBAUZEIT	= 15;

	public static final int	AUFNEHMZEIT	= 2;
	
	

	public GoldsammelnPlan()
	{
		_verbrauchsgrad = 0;
	}


	@Override
	public void aktion()
	{
		ladAuftrag();
		
		
		waitFor(1000);
		erreicheZiel(_zielpos, true);

		_avatar.setProperty("status", "Idle");
		waitFor(2000);
		IVector2 treasurypos = findeNaechsteSchatztruhe(_mypos.copy());
		SimpleMapState g = (SimpleMapState)grid.getProperty(ISpaceStrings.BUILDING_STATE);
		TreasuryInfo hinfo = (TreasuryInfo)g.getTileAtPos((Vector2Int)treasurypos);
		grid.getSpaceObject(hinfo.getSpaceObjectId()).setProperty(ISObjStrings.PROPERTY_STATUS, "almostThere");
		
		setze(_zielpos, MapType.DIRT_PATH, true);
		auftragsverwalter.neuerAuftrag(Auftragsverwalter.BESETZEN, _zielpos);
		
		erreicheZiel(treasurypos, true);
		_avatar.setProperty("status", "Idle");
		waitFor(2000);
		
		
		SimplePlayerState pstate = (SimplePlayerState)grid.getProperty(ISpaceStrings.PLAYER_STATE);
		pstate.addGold(3000);

		grid.getSpaceObject(hinfo.getSpaceObjectId()).setProperty(ISObjStrings.PROPERTY_STATUS, "hasGold");

		_ausfuehr = false;
	}

	/**
	 * @param mypos
	 * @return
	 */
	private IVector2 findeNaechsteSchatztruhe(IVector2 mypos)
	{
		
		SimpleMapState g = (SimpleMapState)grid.getProperty(ISpaceStrings.BUILDING_STATE);

		HashMap<Vector2Int, TileInfo> treasuries = g.getTypes(MapType.TREASURY);

		// Set<Vector2Int> truhsen = ;

		ArrayList<Vector2Int> truhen = new ArrayList<Vector2Int>(treasuries.keySet());

		AStarSearch suche;
		int minGKosten = Integer.MAX_VALUE;
		int gKosten;
		IVector2 tmp, minKostenPunkt = null;
		for(Vector2Int vec : truhen)
		{
			TreasuryInfo hinfo = (TreasuryInfo)g.getTileAtPos((Vector2Int)vec);
			
			if(grid.getSpaceObject(hinfo.getSpaceObjectId()).getProperty(ISObjStrings.PROPERTY_STATUS).equals("Nothing"))
			{
				suche = new AStarSearch(mypos, vec, grid, true);
				gKosten = suche.gibPfadKosten();
				tmp = vec;
				if(gKosten < minGKosten)
				{
					minGKosten = gKosten;
					minKostenPunkt = tmp;
				}
			}
		}
		return minKostenPunkt;
	}

	@Override
	public void aborted()
	{
		// _auftragsverwalter.neuerAuftrag( _auf );
	}

}
