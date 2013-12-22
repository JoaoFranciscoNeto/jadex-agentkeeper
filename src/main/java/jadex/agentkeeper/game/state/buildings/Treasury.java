package jadex.agentkeeper.game.state.buildings;

import jadex.agentkeeper.game.state.map.SimpleMapState;
import jadex.agentkeeper.init.map.process.InitMapProcess;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.TileInfo;
import jadex.agentkeeper.worldmodel.structure.building.TreasuryInfo;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
/**
 * Manage Treasury-Tile:
 *  * reduce money after spend money(for example: create buildings)
 * 
 * @author jens.hantke
 *
 */
public class Treasury {

	public static void synchronizeTreasuriesWithPlayerGold(int price, SimpleMapState buildingState, Grid2D grid) {
		findTreasureWithAmountAndShrink(price, buildingState, grid);
	}
	
	/**
	 * Find Treasure-Tiles with amount and shrink amount, so the Imps can earn more gold and refill the tile.
	 * 
	 * @param price  Integer, will be removed from Tile Amount
	 * @param buildingState SimpleMapState, Object to allocate Tiles with MapType Treasure
	 * @param grid Grid2D
	 */
	private static void findTreasureWithAmountAndShrink(int price, SimpleMapState buildingState, Grid2D grid) {
		HashMap<Vector2Int, TileInfo> treasuries = buildingState.getTypes(MapType.TREASURY);

		Set<Vector2Int> treasuryPositions = treasuries.keySet();

		ArrayList<Vector2Int> treasuryPositionsList = new ArrayList<Vector2Int>();

		treasuryPositionsList.addAll(treasuryPositions);

		Collections.shuffle(treasuryPositionsList);

		Iterator<Vector2Int> it = treasuryPositionsList.iterator();
		while (it.hasNext()) {
			Vector2Int treasury = it.next();
			TreasuryInfo treasuryInfo = (TreasuryInfo) treasuries.get(treasury);
			price = shrinkAmount(treasuryInfo, price, grid);
			if (price == 0) {
				break;
			}
		}
	}

	/**
	 * Reduce Amount of Treasury Tiles, so the Imps can earn more gold and fill.
	 * the treasury again.
	 * 
	 * @param treasuryInfo TreasuryInfo, target treasure tile
	 * @param price Integer, will be removed from Tile Amount
	 * @param grid Grid2D
	 * @return restPrice, Integer
	 */
	private static int shrinkAmount(TreasuryInfo treasuryInfo, int price, Grid2D grid) {
		int amount = treasuryInfo.getAmount();
		if (amount > 0) {
			if (amount > price) {
				treasuryInfo.removeAmount(price);
				return 0;
			} else {
				price -= amount;
				treasuryInfo.setAmount(0);
				SpaceObject sob = (SpaceObject) grid.getSpaceObject(treasuryInfo.getSpaceObjectId());
				sob.setProperty(ISObjStrings.PROPERTY_STATUS, "Nothing");
			}
		}
		return price;
	}

}
