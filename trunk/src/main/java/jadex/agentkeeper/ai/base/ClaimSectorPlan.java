package jadex.agentkeeper.ai.base;


import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.game.state.missions.Auftrag;
import jadex.agentkeeper.game.state.missions.Auftragsverwalter;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;
import jadex.bdiv3.runtime.IPlan;
import jadex.commons.future.DelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.math.Vector2Int;

@Plan
public class ClaimSectorPlan {
	
	@PlanCapability
	protected AbstractBeingBDI	capa;

	@PlanAPI
	protected IPlan				iplan;
	
	@PlanBody
	public IFuture<Void> body()
	{
		final Future<Void> retb = new Future<Void>();
		Auftragsverwalter auftraege = (Auftragsverwalter)capa.getMySpaceObject().getProperty("auftraege");
		
		Auftrag  auftrag = auftraege.gibDichtestenAuftrag(new Vector2Int(capa.getMyPosition().getXAsInteger(), capa.getMyPosition().getYAsInteger()));
		System.out.println("auftrag");
		System.out.println(auftrag);
		
		
//		ladAuftrag();
//
//		// Wurde es schon bebaut..?
//		if (isCorrectField(_zielpos, MapType.DIRT_PATH)) {
//			
//			SpaceObject field = InitMapProcess.getFieldTypeAtPos(_zielpos, grid);
//			
//			if(!((Boolean)field.getProperty("locked")))
//			{
//				waitForTick();
//			}
//			
//			if(!((Boolean)field.getProperty("locked")))
//			{
//				field.setProperty("locked", true);
//	
//				erreicheZiel(_zielpos, true);
//	
//				_avatar.setProperty("status", "Idle");
//				
//				bearbeite(_zielpos, BESETZDAUER);
//	
//				setze(_zielpos, MapType.CLAIMED_PATH, false);
//				SimplePlayerState state = (SimplePlayerState)grid.getProperty(ISpaceStrings.PLAYER_STATE);
//				
//				state.addClaimedSector();
//			}
//			
//		}
		return retb;
	}
	
}
