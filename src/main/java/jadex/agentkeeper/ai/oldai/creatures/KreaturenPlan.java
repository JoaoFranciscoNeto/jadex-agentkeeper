package jadex.agentkeeper.ai.oldai.creatures;


import jadex.agentkeeper.game.state.map.SimpleMapState;
import jadex.agentkeeper.game.state.missions.Auftragsverwalter;
import jadex.agentkeeper.game.state.missions.TaskPoolManager;
import jadex.agentkeeper.init.map.process.InitMapProcess;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.agentkeeper.util.Neighborcase;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.TileInfo;
import jadex.bdi.runtime.GoalFailureException;
import jadex.bdi.runtime.IGoal;
import jadex.bdi.runtime.Plan;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;

/**
 * Abstrakter Plan fuer Monster, von dem jeder Monsterplan erben sollte.
 * Standardmaessig wird im Planbody gleichzeitig schlaf verbraucht/hunger
 * generiert Der Nutzer muss in der Methode Aktion festlegen, was er tun will
 * (die Methode wird pro Tick einmal aufgerufen)
 * 
 * @author 8reichel
 * 
 */
@SuppressWarnings("serial")
@Deprecated
public abstract class KreaturenPlan extends Plan {

	public static double STANDARDVERBRAUCH = 0.01;

	protected boolean _ausfuehr;
	protected int _verbrauchsgrad;
	protected Vector2Double _mypos;
	protected Vector2Int _myIntPos;

	protected  Grid2D grid;
	protected  ISpaceObject _avatar;
	
	protected SimpleMapState mapstate;
	
	protected Auftragsverwalter auftragsverwalter;
	protected TaskPoolManager taskPoolManager;

	/**
	 * Initialisiert die Informationen ueber Karte etc.
	 */
	public KreaturenPlan() {
		_ausfuehr = true;
		_verbrauchsgrad = 1;
		this.grid =  (Grid2D)getBeliefbase().getBelief("environment").getFact();
		this.auftragsverwalter = (Auftragsverwalter) grid.getProperty("auftraege");
		this.taskPoolManager = (TaskPoolManager) grid.getProperty(TaskPoolManager.PROPERTY_NAME);
		_avatar = grid.getAvatar(getComponentDescription());
		_mypos = (Vector2Double) _avatar.getProperty(Space2D.PROPERTY_POSITION);
		_myIntPos = (Vector2Int) _avatar.getProperty("intPos");

	}
	

	

	/**
	 * Body-Methode des Planes
	 */
	public final void body() {
		
		_mypos = (Vector2Double) _avatar.getProperty(Space2D.PROPERTY_POSITION);
		_myIntPos = (Vector2Int) _avatar.getProperty("intPos");
		
		mapstate = (SimpleMapState)grid.getProperty(ISpaceStrings.BUILDING_STATE);
		
		while (_ausfuehr) {

			aktion();
			
			verbrauch(_verbrauchsgrad);
			
			testeEinheiten();
			
			waitForTick();
		}
	}
	
	public void testUmgebungComplex(Grid2D space, Vector2Int zielpos) {
		
		
//		System.out.println("..........wir testen von " + zielpos);
		for(Neighborcase neighborcase : Neighborcase.values()) {
			testField(this.grid, (Vector2Int) zielpos.copy().add(neighborcase.getVector()));
		}
	
		}
	
	public void testUmgebung(Grid2D space, Vector2Int zielpos) {
		
		
//		System.out.println("..........wir testen von " + zielpos);
		for(Neighborcase neighborcase : Neighborcase.getSimple()) {
			testField(this.grid, (Vector2Int) zielpos.copy().add(neighborcase.getVector()));
		}
	
		}

	
	private void testField(Grid2D gridme, Vector2Int zielpos) {
		
//		System.out.println("auf.............feld das wir jetzt testen " + zielpos);
		
		SpaceObject sobj = InitMapProcess.getFieldTypeAtPos(zielpos, gridme);
		if(sobj != null)
		{
			TileInfo info = TileInfo.getTileInfo(sobj, TileInfo.class);
			MapType type = info.getMapType();
			
//			System.out.println("variant " + type.getVariant());
			//TODO: locked from info
			if (type == MapType.DIRT_PATH &&!info.isLocked())  {
				auftragsverwalter.neuerAuftrag(Auftragsverwalter.BESETZEN, zielpos);
			}

			if (type == MapType.ROCK &&!info.isLocked()) {
				auftragsverwalter.neuerAuftrag(Auftragsverwalter.VERSTAERKEWAND, zielpos);
			}
			else
			{
				
			}
		}

		
	}




	/**
	 * Geht zum Gegner, indem es ein erreicheEinheit-goal dispatcht- und wartet
	 * 
	 * @param gegnerID
	 */
	protected void erreicheEinheit(ISpaceObject einheit) {

		if (!istAnPos((IVector2) einheit.getProperty("position"), false)) {
			IGoal goal = createGoal(Auftragsverwalter.ERREICHEEINHEIT);
			goal.getParameter("ziel").setValue(einheit);
			try 
			{
				dispatchSubgoalAndWait(goal);
			}
			catch ( GoalFailureException g )
			{
//				System.out.println("Ziel fehlgeschlagen: Kann Ziel nicht erreichen" + g.getCause());
				fail( g );
				
			}
		}

	}
	/**
	 * Geht zum Ziel, indem es ein erreichziel-goal dispatcht- und wartet
	 * 
	 * @param zielpos
	 */
	protected void erreicheZiel(IVector2 zielpos, boolean direkt) {
		if (!istAnPos(zielpos, direkt)) {
			IGoal goal = createGoal(Auftragsverwalter.ERREICHEZIEL);
			goal.getParameter("ziel").setValue(zielpos);
			goal.getParameter("direkt").setValue(direkt);
			try 
			{
				dispatchSubgoalAndWait(goal);
			}
			catch ( GoalFailureException g )
			{
//				System.out.println("Ziel fehlgeschlagen: Kann Ziel nicht erreichen" + g.getCause());
				fail( g );
				
			}
			
//			if (!istAnPos(zielpos, direkt)) {
//				// fail();
//				System.out.println("fail im kreaturen plan?! da aber doch nicht?");
//			}

		}
		else {
//			System.out.println("Schon da!Schon da!");
		}
	}
	
	//TODO: einbauen das Imps direkt VOR dem Feld stehen müssen
	protected boolean istAnPos(IVector2 zielpos, boolean direkt) {
		int entfernung = direkt ? 0 : 1;
		if (Math.abs( _mypos.copy().getXAsInteger() - zielpos.copy().getXAsInteger()) <= entfernung && 
			 Math.abs(_mypos.copy().getYAsInteger() - zielpos.copy().getYAsInteger()) <= entfernung) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Vom Nutzer zu ueberschreibende Methode; diese wird einmal pro Tick
	 * aufgerufen
	 */
	protected abstract void aktion();

	/**
	 * Verbraucht grad schlaf / generiert grad Hunger
	 * 
	 * @param grad
	 *            Grad der Schlafgenerierung/Hungererzeugung
	 */
	protected void verbrauch(int grad) {
		
		Integer leben = (Integer)_avatar.getProperty("leben");
		if ( leben <= 0 )
		{
			killAgent();
		}
		
		if (grad > 0) {

			boolean zufrieden = (Boolean) getBeliefbase().getBelief("zufrieden").getFact();
			double loyalitaet = (Double) getBeliefbase().getBelief("loyalitaet").getFact();
			double hunger = (Double) getBeliefbase().getBelief("hunger").getFact();
			double schlaf = (Double) getBeliefbase().getBelief("schlaf").getFact();
			double stimmung = (Double) getBeliefbase().getBelief("stimmung").getFact();
			double stimmungneu;
			double loyalitaetneu;

			// Wenn er zufrieden ist steigt Loyalitaet, wenn nicht sinkt diese
			if (loyalitaet < 10.0 && zufrieden) {
				loyalitaetneu = loyalitaet + STANDARDVERBRAUCH * grad / 2;
				_avatar.setProperty("loyalitaet", loyalitaetneu);
			}
			else if (!zufrieden) {
				loyalitaetneu = loyalitaet - (STANDARDVERBRAUCH * grad);
				_avatar.setProperty("loyalitaet", loyalitaetneu);
			}
			else {
				loyalitaetneu = loyalitaet;
			}

			double hungerneu = hunger + STANDARDVERBRAUCH * grad;

			if (stimmung < 10.0 && zufrieden) {
				stimmungneu = stimmung + STANDARDVERBRAUCH * grad;
			}
			else {
				stimmungneu = stimmung;
			}
			double schlafneu = schlaf - STANDARDVERBRAUCH * grad;
			// System.out.println("Hungerneu: " + hungerneu + "Schlafneu: " +
			// schlafneu + "Stimmungneu " + stimmungneu);

			getBeliefbase().getBelief("hunger").setFact(hungerneu);
			getBeliefbase().getBelief("schlaf").setFact(schlafneu);
			getBeliefbase().getBelief("stimmung").setFact(stimmungneu);
			getBeliefbase().getBelief("loyalitaet").setFact(loyalitaetneu);
		}
	}
	
	protected abstract void gegnerNaehe( long id );
	
	protected void testeEinheiten()
	{
//		for ( Object o : _space.getNearObjects( _mypos, new Vector1Int(2)) )
//		{
//			if ( o instanceof ISpaceObject )
//			{
//				ISpaceObject einheit = (ISpaceObject) o;
//				if ( !einheit.getType().equals( "field" ) )
//				{
//
//					Integer spieler = (Integer) einheit.getProperty( "spieler");
//					
//					Integer eigspieler = (Integer)_avatar.getProperty("spieler");
//					
//					if ( ! spieler.equals( eigspieler ) )
//					{
//						gegnerNaehe( (Long)einheit.getId() );
//					}
//				}
//			}
//		}
	}

}
