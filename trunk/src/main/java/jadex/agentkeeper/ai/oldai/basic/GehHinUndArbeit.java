package jadex.agentkeeper.ai.oldai.basic;

import jadex.agentkeeper.ai.oldai.creatures.KreaturenPlan;
import jadex.agentkeeper.game.state.missions.Auftrag;
import jadex.agentkeeper.init.map.process.InitMapProcess;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.Neighborhood;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.TileInfo;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.evaluation.SpaceObjectSource;
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector2Int;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


/**
 * Abstrakte Basisklasse f�r Standardpl�ne, die aus Hingehen, Warten und
 * Zielfeldtyp ver�ndern bestehen
 */
@SuppressWarnings("serial")
@Deprecated
public abstract class GehHinUndArbeit extends KreaturenPlan
{

	protected Auftrag		_auf;

	protected Vector2Int	_zielpos;

	/**
	 * Initialisiert die Klasse
	 */
	public GehHinUndArbeit()
	{
		super();
	}

	public void ladAuftrag()
	{
		_auf = (Auftrag)getParameter("auftrag").getValue();
		_zielpos = _auf.gibZiel();
	}

	public void ladAuftrag(boolean hatZiel)
	{
		_auf = (Auftrag)getParameter("auftrag").getValue();
		if(hatZiel)
		{
			_zielpos = _auf.gibZiel();
		}
		else
		{
			_zielpos = null;
		}
	}

	/**
	 * Bearbeitet Schrittweise ein Feld
	 * 
	 * @param zielpos
	 * @param dauer
	 */
	protected void bearbeite(IVector2 zielpos, int dauer)
	{
		// TODO: das ist auch Mist!
		boolean bearbeitet = false;
		while(!bearbeitet)
		{
			SpaceObject sobj = InitMapProcess.getFieldTypeAtPos(zielpos, grid);
			if(sobj != null)
			{

				int wert = (Integer)sobj.getProperty("bearbeitung");
				if(wert < dauer)
				{
					sobj.setProperty("bearbeitung", wert + 1);
				}
				else
				{
					bearbeitet = true;
					return;
				}
			}
			else
			{
				bearbeitet = true;
				fail();

			}
			waitForTick();
		}

	}

	protected void warte(int dauer)
	{
		_avatar.setProperty("status", "Idle");
		for(int i = 0; i < dauer; i++)
		{
			testeEinheiten();
			waitForTick();
		}
	}

	/**
	 * Setzt den Typ des Zielpositionsfeldes
	 * 
	 * @param zielpos
	 * @param oldtyp
	 * @param newTyp
	 * @param updateNeighbors
	 */
	protected synchronized void setze(IVector2 zielpos, MapType newTyp, boolean updateNeighbors)
	{
		SpaceObject sob = InitMapProcess.getFieldTypeAtPos(zielpos, grid);

		if(sob.getType().equals(newTyp))
		{

		}
		else
		{
			Map<String, Object> props = new HashMap<String, Object>();
			props.put("bearbeitung", new Integer(0));
			props.put("status", "byImpCreated");
			props.put("clicked", false);
			props.put("locked", false);
			props.put(Space2D.PROPERTY_POSITION, zielpos);

			String neighborhood = (String)sob.getProperty("neighborhood");

			props.put("neighborhood", neighborhood);
			
			Object tile;
			try
			{
				tile = newTyp.getPojo().getDeclaredConstructor(MapType.class).newInstance(newTyp);
				props.put(ISObjStrings.PROPERTY_TILEINFO, tile);
				mapstate.addType((Vector2Int)zielpos, tile);
			}
			catch(InstantiationException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch(IllegalAccessException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch(IllegalArgumentException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch(InvocationTargetException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch(NoSuchMethodException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch(SecurityException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			

			SpaceObject sobs = InitMapProcess.getFieldTypeAtPos(zielpos, grid);
			if(!newTyp.equals(sobs.getType()))
			{		
				
				grid.createSpaceObject(newTyp.toString(), props, null);
			}
			else
			{

			}


			try
			{
				
//				mapstate.remType((Vector2Int)zielpos, sob.getProperty(ISObjStrings.PROPERTY_TILEINFO));
				grid.destroySpaceObject(sob.getId());
			}

			catch(Exception e)
			{

			}

			if(updateNeighbors)
			{
				// TODO: detect Field/Building automatic
				Neighborhood.updateMyNeighborsComplexField(zielpos, grid);
			}

			testUmgebungComplex(grid, _myIntPos);
		}


	}


	protected boolean isCorrectField(IVector2 punkt, MapType typ)
	{

		// TODO:: umschreiben
		for(Object o : grid.getSpaceObjectsByGridPosition(punkt, null))
		{
			if(o instanceof ISpaceObject)
			{
				TileInfo info = TileInfo.getTileInfo((SpaceObject)o, TileInfo.class);
				MapType type = info.getMapType();

				if(type == typ)
				{
					return true;
				}
			}
		}
		return false;
	}




	public void passed()
	{

	}

	/**
	 * The failed method is called on plan failure/abort.
	 */
	public void failed()
	{

	}

}
