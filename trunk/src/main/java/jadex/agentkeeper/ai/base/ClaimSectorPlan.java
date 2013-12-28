package jadex.agentkeeper.ai.base;


import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.game.state.missions.Auftrag;
import jadex.agentkeeper.game.state.missions.Auftragsverwalter;
import jadex.agentkeeper.util.ISpaceObject;
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
		
		Auftragsverwalter auftraege = (Auftragsverwalter)capa.getEnvironment().getProperty(ISpaceObject.Objects.TaskList);
		if(auftraege!= null){
			Auftrag  auftrag = auftraege.gibDichtestenAuftrag(new Vector2Int(capa.getMyPosition().getXAsInteger(), capa.getMyPosition().getYAsInteger()));
			System.out.println("auftrag");
			System.out.println(auftrag);
			return retb;
		}
		return null;
	}
	
}
