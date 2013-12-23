package jadex.agentkeeper.game.userinput;

import jadex.agentkeeper.game.state.buildings.Treasury;
import jadex.agentkeeper.game.state.map.SimpleMapState;
import jadex.agentkeeper.game.state.missions.Auftragsverwalter;
import jadex.agentkeeper.game.state.player.SimplePlayerState;
import jadex.agentkeeper.game.task.CreateChickenTask;
import jadex.agentkeeper.init.map.process.InitializeHelper;
import jadex.agentkeeper.init.map.process.PreCreatedSpaceObject;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.agentkeeper.util.Neighborcase;
import jadex.agentkeeper.util.Neighborhood;
import jadex.agentkeeper.view.selection.SelectionArea;
import jadex.agentkeeper.worldmodel.enums.CenterPattern;
import jadex.agentkeeper.worldmodel.enums.CenterType;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.TileInfo;
import jadex.agentkeeper.worldmodel.structure.building.ACenterBuildingInfo;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.IObjectTask;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Verwaltet die Nutzereingaben und leitet sie weiter, z.B. an Bauobject
 * Singleton-Entwurfsmuster, da es nur einmal vorhanden sein wird
 * 
 * @author 7willuwe
 */
@Deprecated
public class UserEingabenManager
{


	private Auftragsverwalter	_auftraege;

	private IEnvironmentSpace	_space;

	private Space2D				_space2D;

	private Grid2D				_grid;

	public SimpleMapState		buildingState;

	public SimplePlayerState	playerState;

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
	public void createBuildings(SelectionArea area, MapType mapType)
	{
		buildingState = (SimpleMapState)_grid.getProperty(ISpaceStrings.BUILDING_STATE);
		playerState = (SimplePlayerState)_grid.getProperty(ISpaceStrings.PLAYER_STATE);

		int price = area.getTiles() * playerState.getMapType().getCost();

		System.out.println("area pirce " + price);
		
		Treasury.synchronizeTreasuriesWithPlayerGold(price, buildingState, _grid);

		//Can the Player afford the Building?
		if(price <= playerState.getGold())
		{

			playerState.removeGold(price);


			// Temporaly save the Buildings for calculation of Centers
			ArrayList<SpaceObject> touchedBuildings = new ArrayList<SpaceObject>();

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
							Object tileinfo = InitializeHelper.createPojoElement(tmppos, mapType);
							HashMap<String, Object> props = new HashMap<String, Object>();

							props.put(ISObjStrings.PROPERTY_CLICKED, false);
							// TODO: thats shit!
							props.put("bearbeitung", 0);
							props.put("status", "Nothing");


							// TODO: why set?
							Set<Vector2Int> nearFields = new HashSet<Vector2Int>();
							Set<Vector2Int> tmpFields = new HashSet<Vector2Int>();

							for(Neighborcase neighborcase : Neighborcase.getDefault())
							{
								Vector2Int tmpVector = (Vector2Int)tmppos.copy().subtract(neighborcase.getVector());
								MapType tmp = buildingState.getTypeAtPos(tmpVector);
								tmpFields.add(tmpVector);
								if(tmp == mapType)
								{
									nearFields.add(tmpVector);
									// System.out.println("tmpVector: " +
									// tmpVector);
								}
							}

							// Calculate the Neighborhood
							String neighborhood = Neighborhood.reCalculateNeighborhoodNewMethod(tmppos, nearFields);

							props.put(ISObjStrings.PROPERTY_NEIGHBORHOOD, neighborhood);
							props.put(Space2D.PROPERTY_POSITION, tmppos);
							props.put(ISObjStrings.PROPERTY_INTPOSITION, tmppos);
							props.put(ISObjStrings.PROPERTY_TILEINFO, tileinfo);

							buildingState.addType(tmppos, tileinfo);

							_grid.destroySpaceObject(info.getSpaceObjectId());


							// TODO: Hack
							ISpaceObject justcreated = _grid.createSpaceObject(mapType.toString(), props, null);

							((TileInfo)justcreated.getProperty(ISObjStrings.PROPERTY_TILEINFO)).setSpaceObjectId(justcreated.getId());

							touchedBuildings.add((SpaceObject)justcreated);


							for(Vector2Int nearVec : nearFields)
							{
								TileInfo tmpinfo = buildingState.getInfoAtPos(nearVec);

								Set<Vector2Int> tmpnearFields = new HashSet<Vector2Int>();
								for(Neighborcase ncase : Neighborcase.getDefault())
								{
									Vector2Int tmpVector = (Vector2Int)nearVec.copy().subtract(ncase.getVector());
									if(buildingState.getTypeAtPos(tmpVector) == mapType)
									{
										tmpnearFields.add(tmpVector);
									}

								}

								// Calculate the Neighborhood for "one" other
								String tmpneighborhood = Neighborhood.reCalculateNeighborhoodNewMethod(nearVec, tmpnearFields);
								// TODO: CONNECT SpaceObject directly to
								// Tileinfo
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
										ISpaceObject justcreated2 = _grid.createSpaceObject(mapType.toString(), tmpprops, null);
										touchedBuildings.add((SpaceObject)justcreated2);
										((TileInfo)justcreated2.getProperty(ISObjStrings.PROPERTY_TILEINFO)).setSpaceObjectId(justcreated2.getId());
									}
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}

							}

							updateCenters(touchedBuildings, buildingState);

						}
					}
				}
			}


		}


		// _auftraege.newBreakWalls(area);
	}


	private void updateCenters(ArrayList<SpaceObject> touchedBuildings, SimpleMapState buildingstate)
	{
		for(SpaceObject sobj : touchedBuildings)
		{
			boolean center = false;
			Object tileinfo = (TileInfo)sobj.getProperty(ISObjStrings.PROPERTY_TILEINFO);
			if(tileinfo instanceof ACenterBuildingInfo)
			{
				ACenterBuildingInfo centerinfo = (ACenterBuildingInfo)tileinfo;
				// next potential....(if he is not already Border)
				if(centerinfo.getCenterPattern() == CenterPattern.ONE_MIDDLE && centerinfo.getCenterType() != CenterType.BORDER)
				{
					int potCounter = 0;
					for(Neighborcase centercase : Neighborcase.getDefault())
					{
						TileInfo tmpinfo = buildingstate.getTileAtPos((Vector2Int)((Vector2Int)sobj.getProperty(ISObjStrings.PROPERTY_INTPOSITION)).copy().add(
								centercase.getVector()));
						// is it the same type? and not a Center?
						if(tmpinfo != null && tmpinfo.getMapType() == centerinfo.getMapType())
						{
							ACenterBuildingInfo tmp = (ACenterBuildingInfo)tmpinfo;
							CenterType tmpCenter = tmp.getCenterType();
							if(tmpCenter == CenterType.UNKNOWN)
							{
								potCounter++;
							}
						}
						else if(tmpinfo == null)
						{
							System.out.println("is null");
						}
					}

					// if we sucessfully counted to 8 it is Center-Block!
					if(potCounter >= 8)
					{
						center = true;

					}
				}
				else if(centerinfo.getCenterPattern() == CenterPattern.ONE_BORDER_1)
				{
					int potCounter = 0;
					for(Neighborcase centercase : Neighborcase.getDefault())
					{
						TileInfo tmpinfo = buildingstate.getTileAtPos((Vector2Int)((Vector2Int)sobj.getProperty(ISObjStrings.PROPERTY_INTPOSITION)).copy().add(
								centercase.getVector()));
						// is it the same type? and not a Center?
						if(tmpinfo != null && tmpinfo.getMapType() == centerinfo.getMapType())
						{
							potCounter++;
						}
					}


					// if we sucessfully counted to 8 it is Center-Block!
					if(potCounter >= 8)
					{
						center = true;

					}
				}
				if(center)
				{
					centerinfo.setCenterType(CenterType.CENTER);

					Map<String, Object> tmpprops = sobj.getProperties();

					boolean destroyed = _grid.destroyAndVerifySpaceObject(centerinfo.getSpaceObjectId());

					if(destroyed)
					{
						ArrayList<IObjectTask> tasklist = null;
						if(centerinfo.getMapType() == MapType.HATCHERY)
						{
							tasklist = new ArrayList<IObjectTask>();
							tasklist.add(new CreateChickenTask());
						}

						ISpaceObject justcreated2 = _grid.createSpaceObject(centerinfo.getMapType().toString(), tmpprops, tasklist);
						((TileInfo)justcreated2.getProperty(ISObjStrings.PROPERTY_TILEINFO)).setSpaceObjectId(justcreated2.getId());
					}
				}
			}


		}

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
