package jadex.agentkeeper.game.userinput;

import jadex.agentkeeper.game.state.missions.Auftragsverwalter;
import jadex.agentkeeper.init.map.process.InitMapProcess;
import jadex.agentkeeper.view.selection.SelectionArea;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.WalkType;
import jadex.agentkeeper.worldmodel.structure.TileInfo;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.Map;

/**
 * Verwaltet die Nutzereingaben und leitet sie weiter, z.B. an Bauobject
 * Singleton-Entwurfsmuster, da es nur einmal vorhanden sein wird
 * @author 8reichel
 *
 */
@Deprecated
public class UserEingabenManager {
	public static final int ABREISSEN = 0;
	public static final int BAUTRAINING = 1;
	public static final int BAULAIR = 2;
	public static final int BAUHATCHERY = 3;
	public static final int BAULIBRARY = 4;
	public static final int BAUTORTURE = 5;
	public static final int ZAUBERIMP = 6;
	

	private Auftragsverwalter _auftraege;
	private IEnvironmentSpace _space;
	private Space2D _space2D;
	private Grid2D _grid;
	private int _gold;
	private int _mana;
	private int _forschung;
	
	public UserEingabenManager(IEnvironmentSpace space) {
		_space = space;
		_space2D = (Space2D) _space;
		_grid = (Grid2D) _space2D;
		_auftraege = (Auftragsverwalter) _space.getProperty("auftraege");
	}



	/**
	 * Fuehrt alle Nutzeraktionen aus
	 * 
	 * @param x
	 *            X-Koordinate der Aktion
	 * @param y
	 *            Y-Koordinate der Aktion
	 * @param typ
	 *            Typ der Aktion
	 */
	public String userAktion(int x, int y, int typ) {
//		System.out.println("user aktion mit x/y: x" + x + "y " + y);

		switch (typ) {
		case ABREISSEN:
			abreissen(x, y);
			break;
		case ZAUBERIMP:
			return zauberImp(x, y);
		}
		return "";
	}
	
	public void destoryWalls(SelectionArea area)
	{
		_auftraege.newBreakWalls(area);
	}



	/**
	 * Zaubert einen Imp an die angegebene Koordinate, wenn genug Gold vorhanden ist
	 * @param x
	 * @param y
	 */
	private String zauberImp(int x, int y) {
		IVector2 zielpos = new Vector2Double( x, y );
		if (_forschung >= 15) {
			if (_mana >= 500) {
//				_bauer.zauberImp( zielpos, 500);
				return "Neuen IMP erschaffen!";
			}
			else {
				return "Nicht genug Mana!";
			}
		}
		else {
			return "Nicht genug Forschung!";
		}
	}

	public int gibGold() {
		return _gold;
	}

	public int gibMana() {
		return _mana;
	}
	
	
	
	/**
	 * L�d die Forschung aus dem Space und gibt den Wert zur�ck

	 */
	public int gibForschung( )
	{
		_forschung = (Integer) _space.getProperty("forschung");
		return _forschung;
	}
	
	/**
	 * Aktuallisiert Gold und Mana aus dem Space
	 */
	void aktuallisiere() {
		Object o = _space.getProperty("gold");
		_gold = ((Integer) o).intValue();

		o = _space.getProperty("mana");
		_mana = ((Integer) o).intValue();
	}

	/**
	 * Reisst ein Gebaeude ab
	 * 
	 * @param x
	 * @param y
	 */
	private void abreissen(int x, int y) {

		Vector2Int pos = new Vector2Int(x, y);
		SpaceObject activeField = InitMapProcess.getFieldTypeAtPos(pos, _grid);
		
		if(activeField!=null)
		{

			if (erreichbar(x, y) && abbreissbar(x, y)) {
				if (activeField.getProperty("clicked").equals(false)) {
					activeField.setProperty("clicked", true);
					
					String typename = activeField.getType();
					Map props = activeField.getProperties();

					_grid.createSpaceObject(typename, props, null);
					_grid.destroySpaceObject(activeField.getId());
					
					_auftraege.neuerAuftrag(Auftragsverwalter.WANDABBAU, pos);
				}
			}
			
		}
	}

	private boolean abbreissbar(int x, int y) {

		Vector2Double punkt = new Vector2Double(x, y);
		for (Object o : (_grid.getSpaceObjectsByGridPosition(punkt, null))) {
			if (o instanceof ISpaceObject) {
				SpaceObject blub = (SpaceObject) o;
				
				TileInfo info = TileInfo.getTileInfo(blub, TileInfo.class);
				
				if (info.getMapType() == MapType.GOLD
						|| info.getMapType() == MapType.ROCK
						|| info.getMapType() == MapType.REINFORCED_WALL) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean begehbar(Vector2Int punkt)
	{

		// TODO:: umschreiben
		for(Object o : _grid.getSpaceObjectsByGridPosition(punkt, null))
		{
			if(o instanceof ISpaceObject)
			{
				TileInfo info = TileInfo.getTileInfo((SpaceObject)o, TileInfo.class);

				return info.getWalkType() == WalkType.PASSABLE;

			}
		}
		return false;
	}

	private boolean erreichbar(int x, int y) {
		Vector2Int[] richtungen = best4Richtungen(x, y);

		for (Vector2Int vector : richtungen) {
			if (begehbar(vector)) {
				return true;
			}
		}

		return false;
	}

	private Vector2Int[] best4Richtungen(int vect_x, int vect_y) {
		Vector2Int n = new Vector2Int(vect_x, vect_y - 1);
		Vector2Int o = new Vector2Int(vect_x + 1, vect_y);
		Vector2Int s = new Vector2Int(vect_x, vect_y + 1);
		Vector2Int w = new Vector2Int(vect_x - 1, vect_y);

		Vector2Int richtungen[] = { n, o, s, w };

		return richtungen;
	}

	public int gibImps() {
		
		int imps = 0;
		for (@SuppressWarnings("unused")
		Object o : _space.getSpaceObjectsByType("imp")) {

			imps++;
		}
		return imps;
	}

	public int gibMonster() {
		
		
		return gibGoblins() + gibWarlocks();
	}
	
	public int gibWarlocks()
	{
		int warlocks = 0;
		for (@SuppressWarnings("unused")
		Object o : _space.getSpaceObjectsByType("warlock")) {

			warlocks++;
		}
		return warlocks;
	}
	
	public int gibGoblins()
	{
		int goblins = 0;
		for (@SuppressWarnings("unused")
		Object o : _space.getSpaceObjectsByType("goblin")) {
			goblins++;
		}
		return goblins;
	}



	public IEnvironmentSpace gibSpace() {
		return _space;
	}

	

}
