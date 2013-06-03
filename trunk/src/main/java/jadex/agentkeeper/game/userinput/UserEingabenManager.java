package jadex.agentkeeper.game.userinput;

import jadex.agentkeeper.game.state.map.SimpleMapState;
import jadex.agentkeeper.game.state.missions.Auftragsverwalter;
import jadex.agentkeeper.init.map.process.InitializeHelper;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.agentkeeper.util.Neighborcase;
import jadex.agentkeeper.util.Neighborhood;
import jadex.agentkeeper.view.selection.SelectionArea;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.TileInfo;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Verwaltet die Nutzereingaben und leitet sie weiter, z.B. an Bauobject
 * Singleton-Entwurfsmuster, da es nur einmal vorhanden sein wird
 * 
 * @author 8reichel
 */
@Deprecated
public class UserEingabenManager
{


	private Auftragsverwalter	_auftraege;

	private IEnvironmentSpace	_space;

	private Space2D				_space2D;

	private Grid2D				_grid;

	public SimpleMapState		buildingState;

	public UserEingabenManager(IEnvironmentSpace space)
	{
		_space = space;
		_space2D = (Space2D)_space;
		_grid = (Grid2D)_space2D;
		_auftraege = (Auftragsverwalter)_space.getProperty("auftraege");

	}


	public void destroyWalls(SelectionArea area)
	{
		_auftraege.newBreakWalls(area);
	}

	// Hack for the moment
	public void createBuildings(SelectionArea area)
	{
		buildingState = (SimpleMapState)_grid.getProperty(ISpaceStrings.BUILDING_STATE);
		Vector2Int endvector = area.getWorldend();
		Vector2Int startvector = area.getWorldstart();

		for(int x = startvector.getXAsInteger(); x <= endvector.getXAsInteger(); x++)
		{
			for(int y = startvector.getYAsInteger(); y <= endvector.getYAsInteger(); y++)
			{

				synchronized(buildingState)
				{
					Vector2Int tmppos = new Vector2Int(x, y);

					MapType oldtype = buildingState.getTypeAtPos(tmppos);
					TileInfo info = buildingState.getTileAtPos(tmppos);
					if(oldtype == MapType.CLAIMED_PATH)
					{
						Object tileinfo = InitializeHelper.createPojoElement(tmppos, MapType.LAIR);
						HashMap<String, Object> props = new HashMap<String, Object>();

						props.put(ISObjStrings.PROPERTY_CLICKED, false);

						// TODO: thats shit!
						props.put("bearbeitung", 0);
						props.put("status", "Nothing");


						Set<Vector2Int> nearFields = new HashSet<Vector2Int>();
						Set<Vector2Int> tmpFields = new HashSet<Vector2Int>();


						for(Neighborcase neighborcase : Neighborcase.getDefault())
						{
							Vector2Int tmpVector = (Vector2Int)tmppos.copy().subtract(neighborcase.getVector());
							MapType tmp = buildingState.getTypeAtPos(tmpVector);
							tmpFields.add(tmpVector);
							if(tmp == MapType.LAIR)
							{
								nearFields.add(tmpVector);
								System.out.println("tmpVector: " + tmpVector);
							}
						}

						// Calculate the Neighborhood
						String neighborhood = Neighborhood.reCalculateNeighborhoodNewMethod(tmppos, nearFields);


						props.put(ISObjStrings.PROPERTY_NEIGHBORHOOD, neighborhood);


						props.put(Space2D.PROPERTY_POSITION, tmppos);
						props.put(ISObjStrings.PROPERTY_INTPOSITION, tmppos);

						props.put(ISObjStrings.PROPERTY_TILEINFO, tileinfo);

						buildingState.addType(tmppos, tileinfo);

						MapType testtype = buildingState.getTypeAtPos(tmppos);

						System.out.println("testtype: " + testtype);


						_grid.destroySpaceObject(info.getSpaceObjectId());


						// TODO: Hack
						ISpaceObject justcreated = _grid.createSpaceObject(MapType.LAIR.toString(), props, null);
						((TileInfo)justcreated.getProperty(ISObjStrings.PROPERTY_TILEINFO)).setSpaceObjectId(justcreated.getId());

						for(Vector2Int nearVec : nearFields)
						{
							TileInfo tmpinfo = buildingState.getInfoAtPos(nearVec);


							Set<Vector2Int> tmpnearFields = new HashSet<Vector2Int>();
							for(Neighborcase ncase : Neighborcase.getDefault())
							{
								Vector2Int tmpVector = (Vector2Int)nearVec.copy().subtract(ncase.getVector());
								if(buildingState.getTypeAtPos(tmpVector) == MapType.LAIR)
								{
									tmpnearFields.add(tmpVector);
								}

							}

							// Calculate the Neighborhood for "one" other
							String tmpneighborhood = Neighborhood.reCalculateNeighborhoodNewMethod(nearVec, tmpnearFields);
							//TODO: CONNECT SpaceObject directly to Tileinfo
							try
							{
								SpaceObject tmpSpace = (SpaceObject)_grid.getSpaceObject(tmpinfo.getSpaceObjectId());
								tmpSpace.setProperty(ISObjStrings.PROPERTY_NEIGHBORHOOD, tmpneighborhood);
								// tmpSpace.setProperty(ISObjStrings.PROPERTY_NEIGHBORHOOD,
								// "00000000");
								Map<String, Object> tmpprops = tmpSpace.getProperties();

								// buildingState.removeType(nearVec);
								// _grid.destroySpaceObject(tmpinfo.getSpaceObjectId());

									boolean destroyed = _grid.destroyAndVerifySpaceObject(tmpinfo.getSpaceObjectId());

									if(destroyed)
									{
										ISpaceObject justcreated2 = _grid.createSpaceObject(MapType.LAIR.toString(), tmpprops, null);

										((TileInfo)justcreated2.getProperty(ISObjStrings.PROPERTY_TILEINFO)).setSpaceObjectId(justcreated2.getId());
									}
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}

							




						}

						
						

					}
				}
			}
		}


		// _auftraege.newBreakWalls(area);
	}


	public void setShowBars(boolean set)
	{

		_grid.setProperty("showBars", set);
	}


	public IEnvironmentSpace gibSpace()
	{
		return _space;
	}


}
