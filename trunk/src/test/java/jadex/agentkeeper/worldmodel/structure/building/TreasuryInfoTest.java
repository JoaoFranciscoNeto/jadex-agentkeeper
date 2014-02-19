//package jadex.agentkeeper.worldmodel.structure.building;
//
//import static org.junit.Assert.*;
//import jadex.agentkeeper.game.state.buildings.Treasury;
//
//import org.junit.Test;
//
//public class TreasuryInfoTest {
//
//	/**
//	 * Tests the amount calculation for one Treasury Tile.
//	 */
//	@Test
//	public void testAddRemoveAmount() {
//		TreasuryInfo treasuryInfo = new TreasuryInfo(null);
//		
//		assertEquals(Treasury.totalPossibleAmount, TreasuryInfo.MAX_AMOUNT);
//		
//		treasuryInfo.addAmount(300);
//		treasuryInfo.addAmount(300);
//		treasuryInfo.addAmount(300);
//		
//		assertEquals(treasuryInfo.getAmount(), 900);
//		assertEquals(Treasury.currentAmount, 900);
//		
//		treasuryInfo.removeAmount(300);
//		
//		assertEquals(treasuryInfo.getAmount(),600);
//		assertEquals(Treasury.currentAmount, 600);
//	}
//	
//	/**
//	 * Tests the maximal amount calculation of all TreasuryTiles, which is rising with every tile.
//	 */
//	@Test
//	public void testPossibleAmount() {
//		Treasury.totalPossibleAmount = 0;
//		new TreasuryInfo(null);
//		assertEquals(Treasury.totalPossibleAmount, TreasuryInfo.MAX_AMOUNT);
//		
//		new TreasuryInfo(null);
//		new TreasuryInfo(null);
//		assertEquals(Treasury.totalPossibleAmount, (TreasuryInfo.MAX_AMOUNT * 3));
//	}
//
//}
