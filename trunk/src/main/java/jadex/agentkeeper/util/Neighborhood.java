package jadex.agentkeeper.util;

import jadex.agentkeeper.init.map.process.InitMapProcess;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.WalkType;
import jadex.agentkeeper.worldmodel.structure.TileInfo;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;


/**
 * Class that implements Neighborhood related calculations
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
public class Neighborhood
{
	public static boolean isReachable(IVector2 zielpos, Grid2D grid)
	{
		boolean ret = false;
		for(Neighborcase neighborcase : Neighborcase.getSimple())
		{
			IVector2 ziel = zielpos.copy().add(neighborcase.getVector());
			SpaceObject thatsme = InitMapProcess.getSolidTypeAtPos(ziel, grid);
			if(thatsme != null)
			{
				TileInfo info = TileInfo.getTileInfo(thatsme, TileInfo.class);
				if(info.getWalkType() == WalkType.PASSABLE)
				{
					return true;
				}
			}
		}
		return ret;
	}


	public static boolean isReachableForDestroy(IVector2 zielpos, Grid2D grid)
	{
		boolean ret = false;
		for(Neighborcase neighborcase : Neighborcase.getSimple())
		{
			IVector2 ziel = zielpos.copy().add(neighborcase.getVector());

			// TODO: how to handle null pointer?
			SpaceObject thatsme = InitMapProcess.getSolidTypeAtPos(ziel, grid);
			if(thatsme != null)
			{
				TileInfo info = TileInfo.getTileInfo(thatsme, TileInfo.class);
				if(info.getWalkType() == WalkType.PASSABLE)
				{
					return true;
				}
				else if(thatsme.getProperty("clicked").equals(true))
				{
					return true;
				}
			}

		}
		return ret;
	}

	public static void updateMyNeighborsSimpleField(IVector2 zielpos, Grid2D grid)
	{
		for(Neighborcase neighborcase : Neighborcase.getSimple())
		{

			IVector2 ziel = zielpos.copy().add(neighborcase.getVector());
			SpaceObject thatsme = InitMapProcess.getFieldTypeAtPos(ziel, grid);
			updateMyNeighborvalueBasedOnMyNeighborhood(ziel, thatsme, grid);
		}

	}

	public static void updateMyNeighborsComplexField(IVector2 zielpos, Grid2D grid)
	{
		for(Neighborcase neighborcase : Neighborcase.getDefault())
		{
			IVector2 ziel = zielpos.copy().add(neighborcase.getVector());
			SpaceObject thatsme = InitMapProcess.getFieldTypeAtPos(ziel, grid);
			updateMyNeighborvalueBasedOnMyNeighborhood(ziel, thatsme, grid);

		}
	}


	public static void updateMyNeighborsSimpleBuilding(IVector2 zielpos, Grid2D grid)
	{
		for(Neighborcase neighborcase : Neighborcase.getSimple())
		{

			IVector2 ziel = zielpos.copy().add(neighborcase.getVector());
			SpaceObject thatsme = InitMapProcess.getBuildingTypeAtPos(ziel, grid);
			updateMyNeighborvalueBasedOnMyNeighborhood(ziel, thatsme, grid);
		}

	}

	public static void updateNeighborHood(IVector2 zielpos, Grid2D grid)
	{
		for(Neighborcase neighborcase : Neighborcase.getDefault())
		{
			IVector2 ziel = zielpos.copy().add(neighborcase.getVector());
			SpaceObject thatsme = InitMapProcess.getSolidTypeAtPos(ziel, grid);

			updateMyNeighborvalueBasedOnMyNeighborhood(ziel, thatsme, grid);

		}
	}


	public static void updateMyNeighborsComplexBuilding(IVector2 zielpos, Grid2D grid)
	{
		for(Neighborcase neighborcase : Neighborcase.getDefault())
		{
			IVector2 ziel = zielpos.copy().add(neighborcase.getVector());
			SpaceObject thatsme = InitMapProcess.getBuildingTypeAtPos(ziel, grid);
			updateMyNeighborvalueBasedOnMyNeighborhood(ziel, thatsme, grid);

		}
	}

	public synchronized static void updateMyNeighborvalueBasedOnMyNeighborhood(IVector2 ziel, SpaceObject thatsme, Grid2D grid)
	{
		if(thatsme != null)
		{
			String tmpneighborhood = (String)thatsme.getProperty("neighborhood");
			// String relations[] =
			// InitMapProcess.NEIGHBOR_RELATIONS.get(thatsme.getType());
			
			TileInfo info = TileInfo.getTileInfo(thatsme, TileInfo.class);
			MapType[] types = info.getNeighbors();
			if(types != null)
			{
				Set<SpaceObject> nearRocks = InitMapProcess.getNeighborBlocksInRange(ziel, 1, grid, types);

				String newneighborhood = Neighborhood.reCalculateNeighborhood(ziel, nearRocks);

				if(!newneighborhood.equals(tmpneighborhood))
				{

					thatsme.setProperty("neighborhood", newneighborhood);

					SpaceObject thatsmecopy = thatsme;


					boolean destroyed = grid.destroyAndVerifySpaceObject(thatsme.getId());

					if(destroyed)
					{
						grid.createSpaceObject(thatsmecopy.getType(), thatsmecopy.getProperties(), null);
					}

				}

			}


		}

	}

	/**
	 * Recalculate the Neighborhood for one Block
	 * 
	 * @param iMyPos my Position
	 * @param nearFields the SpaceoObjects around me
	 */
	public static String reCalculateNeighborhood(IVector2 iMyPos, Set<SpaceObject> nearFields)
	{

		Vector2Int myPos = (Vector2Int)iMyPos;
		boolean alternatives = true;
		int ret = 0;
		SpaceObject thatsme = null;
		ArrayList<Neighborcase> neighborcases = new ArrayList<Neighborcase>();
		Iterator<SpaceObject> it = nearFields.iterator();

		// Calculate all the neihborcases
		while(it.hasNext())
		{
			SpaceObject sobj = it.next();

			Vector2Double sobjpos = (Vector2Double)sobj.getProperty(Space2D.PROPERTY_POSITION);
			if(sobjpos.equals(myPos))
			{
				thatsme = sobj;

				TileInfo info = TileInfo.getTileInfo(thatsme, TileInfo.class);
				MapType type = info.getMapType();
				// TODO: hardcoded ugly stuff
				if(type == MapType.WATER || type == MapType.LAVA || MapType.getOnlyBuildings().contains(type))
				{
					alternatives = false;
				}
			}
			else
			{
				Vector2Double subtract = (Vector2Double)sobjpos.copy().subtract(myPos);

				// We save all the cases in a List.
				Neighborcase wert = getNeighbor(subtract);
				if(wert != null)
				{
					neighborcases.add(wert);
				}

			}

		}

		// Remove execptions
		if(neighborcases.contains(Neighborcase.CASE3))
		{
			if(!neighborcases.contains(Neighborcase.CASE2) || !neighborcases.contains(Neighborcase.CASE4))
			{
				neighborcases.remove(Neighborcase.CASE3);

			}

		}

		if(neighborcases.contains(Neighborcase.CASE5))
		{
			if(!neighborcases.contains(Neighborcase.CASE4) || !neighborcases.contains(Neighborcase.CASE6))
			{
				neighborcases.remove(Neighborcase.CASE5);
			}

		}

		if(neighborcases.contains(Neighborcase.CASE7))
		{
			if(!neighborcases.contains(Neighborcase.CASE6) || !neighborcases.contains(Neighborcase.CASE8))
			{
				neighborcases.remove(Neighborcase.CASE7);
			}

		}

		if(neighborcases.contains(Neighborcase.CASE1))
		{
			if(!(neighborcases.contains(Neighborcase.CASE2)) || !(neighborcases.contains(Neighborcase.CASE8)))
			{
				neighborcases.remove(Neighborcase.CASE1);
			}

		}

		// System.out.println("neighborcases" + neighborcases.toString());

		// Now we adress the concrete Tiles:
		for(Neighborcase ncase : neighborcases)
		{
			ret = ret ^ ncase.getValue();
		}


		// System.out.println("ret als byte " + Integer.toBinaryString(ret));
		String stringret = Integer.toBinaryString(ret);
		return parseTilesets(stringret, alternatives);

	}

	private static String parseTilesets(String neighborhood, boolean alternatives)
	{

		// String ret = "00000000";

		// Parse to correct Length according to the files
		while(neighborhood.length() < 8)
		{
			neighborhood = "0".concat(neighborhood);
		}
		// Special Random Cases
		if(alternatives
				&& (neighborhood.equals("00111110") || neighborhood.equals("10001111") || neighborhood.equals("11100011") || neighborhood.equals("11111000")))
		{
			int rnd = (int)(4 * Math.random());
			switch(rnd)
			{
				case 0:
					neighborhood = neighborhood.concat("a");
					break;
				case 1:
					neighborhood = neighborhood.concat("b");
					break;
				case 2:
					neighborhood = neighborhood.concat("c");
					break;
				case 3:
					neighborhood = neighborhood.concat("d");
					break;

				default:
					break;
			}
		}

		return neighborhood;
	}


	private static Neighborcase getNeighbor(Vector2Double diff)
	{
		for(Neighborcase neighborcase : Neighborcase.getDefault())
		{
			if(neighborcase.getVector().equals(diff))
			{
				return neighborcase;
			}

		}
		return null;


	}


}
