package jadex.agentkeeper.ai.creatures.imp;

import jadex.agentkeeper.ai.oldai.creatures.AbstractRumbewegplan;
import jadex.agentkeeper.game.state.missions.Auftrag;
import jadex.agentkeeper.game.state.missions.Auftragsverwalter;
import jadex.agentkeeper.init.map.process.InitMapProcess;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.TileInfo;
import jadex.bdi.runtime.GoalFailureException;
import jadex.bdi.runtime.IGoal;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.Vector2Int;

/**
 * Der rumbewegplan besch�ftigt sich mit den IMPs die gerade keinen Auftrag
 * haben, welche rnd-m��ig runlaufen und nebenbei die Umgebung Scannen (dabei
 * Auftrage generien sofern es etwas zu tun gibt und st�ndig nachfragen ob es
 * neue Auftrage gibt.
 * 
 * 
 * TODO: Refractor in English and as BDIv3 Agent-Plan
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 * @author 8reichel
 * 
 */
@SuppressWarnings("serial")
@Deprecated
public class RumbewegPlan extends AbstractRumbewegplan {

	public RumbewegPlan() {
		_verbrauchsgrad = 0;
	}


	


	
	protected void testAuftraege( ) {
		
		//TODO: umschreiben
		waitForTick();
		
		testUmgebung(grid, (Vector2Int) _avatar.getProperty("intPos"));
		Vector2Int richtungen[] = best4Richtungen( InitMapProcess.convertToIntPos(_mypos) );
		for (Vector2Int vector : richtungen) {
			testUmgebung(grid, vector);
		}
		

		waitForTick();
		
		Auftrag auftrag = auftragsverwalter.gibDichtestenAuftrag( (Vector2Int) _avatar.getProperty("intPos"));
		if (auftrag != null) {
			String typ = auftrag.gibTyp();
			try {
				if (typ.equals(Auftragsverwalter.WANDABBAU)) {
					IGoal neu = createGoal(Auftragsverwalter.WANDABBAU);
					neu.getParameter("auftrag").setValue(auftrag);
					dispatchSubgoalAndWait(neu);
				}

				if (typ.equals(Auftragsverwalter.VERSTAERKEWAND)) {
					IGoal neu = createGoal(Auftragsverwalter.VERSTAERKEWAND);
					neu.getParameter("auftrag").setValue(auftrag);
					dispatchSubgoalAndWait(neu);
				}

				if (typ.equals(Auftragsverwalter.BESETZEN)) {
					IGoal neu = createGoal(Auftragsverwalter.BESETZEN);
					neu.getParameter("auftrag").setValue(auftrag);
					dispatchSubgoalAndWait(neu);
				}
			}
			catch (GoalFailureException e) {
//				System.out.println("Plan fehlgeschlagen, weiter gehts!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			}
		}
		
		





	}
	
	public void testUmgebung(Grid2D space, Vector2Int zielpos) {
		
		
		SpaceObject sobj = InitMapProcess.getFieldTypeAtPos(zielpos, space);
		if(sobj != null)
		{
			TileInfo info = TileInfo.getTileInfo(sobj, TileInfo.class);
			MapType type = info.getMapType();
			//TODO: locked from info
			if (type == MapType.DIRT_PATH &&!(Boolean)sobj.getProperty("locked"))  {
				auftragsverwalter.neuerAuftrag(Auftragsverwalter.BESETZEN, zielpos);
			}

			if (type == MapType.ROCK &&!(Boolean)sobj.getProperty("locked")) {
				auftragsverwalter.neuerAuftrag(Auftragsverwalter.VERSTAERKEWAND, zielpos);
			}
			else
			{
				
			}
		}

	}

	private Vector2Int[] best4Richtungen( Vector2Int eigpos ) {
		Vector2Int n = new Vector2Int(eigpos.getXAsInteger()     , eigpos.getYAsInteger() - 1);
		Vector2Int o = new Vector2Int(eigpos.getXAsInteger() + 1 , eigpos.getYAsInteger());
		Vector2Int s = new Vector2Int(eigpos.getXAsInteger()     , eigpos.getYAsInteger() + 1);
		Vector2Int w = new Vector2Int(eigpos.getXAsInteger() - 1 , eigpos.getYAsInteger());


		Vector2Int richtungen[] = { n, o, s, w };

		return richtungen;
	}


	@Override
	protected void gegnerNaehe(long id) {
		System.out.println("Wird angegriffen");
		
	}
}
