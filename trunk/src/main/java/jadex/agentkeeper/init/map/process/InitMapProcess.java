package jadex.agentkeeper.init.map.process;

import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.Neighborhood;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.TypeVariant;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.SUtil;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceProcess;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;


/**
 * Environment process for creating wastes.
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
public class InitMapProcess extends AInitMapProcess implements ISpaceProcess, IMap, ISObjStrings
{

	private Map<String, Object> tmpProps;
	
	private ArrayList<SimpleMapType> complexNPos = new ArrayList<SimpleMapType>();

	// Building Positions for setting the Centers
	private ArrayList<Vector2Int> center_building_pos = new ArrayList<Vector2Int>();

	private int dungeon_heart_counter = 0;
	
	private Grid2D grid;
	
	
	private void readOneElementOnMap(String key, Vector2Int aktPos)
	{
		
		MapType mapType = TILE_MAP.get(key);
		String type = imagenames.get(key);
		
		tmpProps = new HashMap<String, Object>();
		createMapElement(aktPos, mapType);

		tmpProps.put(PROPERTY_CLICKED, false);
		
		//TODO: thats shit!
		tmpProps.put("bearbeitung", 0);

		// Null Check
		type = type == null ? "unknown" : type;


		

		if(mapType == MapType.GOLD)
		{
			complexNPos.add(new SimpleMapType(aktPos, type));
		}
		if(BUILDING_SET.contains(type))
		{
			if(type == TREASURY)
			{
				complexNPos.add(new SimpleMapType(aktPos, type));
			}
			else if(type == HATCHERY)
			{
				center_building_pos.add(aktPos);
				complexNPos.add(new SimpleMapType(aktPos, type));
				playerState.addClaimedSector();
			}
			else if(type.equals(DUNGEONHEART))
			{
				dungeon_heart_counter++;
				if(dungeon_heart_counter == 13)
				{
					type = DUNGEONHEARTCENTER;
				}
			}
			else if(type == LAIR)
			{
				complexNPos.add(new SimpleMapType(aktPos, type));
				playerState.addClaimedSector();
			}

			else if(type == TRAININGROOM)
			{
				center_building_pos.add(aktPos);
				complexNPos.add(new SimpleMapType(aktPos, type));
				playerState.addClaimedSector();
			}

			else if(type == LIBRARY)
			{
				center_building_pos.add(aktPos);
				complexNPos.add(new SimpleMapType(aktPos, type));
				playerState.addClaimedSector();
			}

			else if(type == TORTURE)
			{
				center_building_pos.add(aktPos);
				complexNPos.add(new SimpleMapType(aktPos, type));
				playerState.addClaimedSector();
			}
			else if(type == PORTAL)
			{
				center_building_pos.add(aktPos);
				playerState.addClaimedSector();
			}
		}

		else if(type == ROCK)
		{
			complexNPos.add(new SimpleMapType(aktPos, type));
		}
		else if(type == REINFORCED_WALL)
		{
			complexNPos.add(new SimpleMapType(aktPos, type));
		}
		else if(type == IMPENETRABLE_ROCK)
		{
			complexNPos.add(new SimpleMapType(aktPos, type));
		}
		else if(type == WATER || type == LAVA)
		{
			complexNPos.add(new SimpleMapType(aktPos, type));
		}
		else if(type == CLAIMED_PATH)
		{
			playerState.addClaimedSector();
		}

		
		
		tmpProps.put(PROPERTY_NEIGHBORHOOD, "00000000");
		tmpProps.put(Space2D.PROPERTY_POSITION, aktPos);
		grid.createSpaceObject(type, tmpProps, null);
		
		
		
	}
	
	
	// -------- ISpaceProcess interface --------

	/**
	 * This method will be executed by the object before the process gets added
	 * to the execution queue.
	 * 
	 * @param clock The clock.
	 * @param space The space this process is running in.
	 */
	public void start(IClockService clock, IEnvironmentSpace space)
	{
		this.grid = (Grid2D)space;

		loadAndSetupMissions(grid);
		
		

		try
		{
			// ClassLoader cl =
			// space.getExternalAccess().getModel().getClassLoader();
			ClassLoader cl = getClass().getClassLoader();

			/** Mapkrams */
			String mapfile = (String)getProperty("mapfile");
			InputStream is = SUtil.getResource(mapfile, cl);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			// dis.available() returns 0 if the file does not have more lines.
			while(br.ready())
			{
				String data = br.readLine();

				if("MAP".equals(data))
				{
					String size = br.readLine();
					int del = size.indexOf("X");
					String xstr = size.substring(0, del - 1);
					String ystr = size.substring(del + 1);
					int sizex = Integer.parseInt(xstr.trim()) - 2;
					int sizey = Integer.parseInt(ystr.trim()) - 2;

					grid.setAreaSize(new Vector2Int(sizex, sizey));
					
					// Now init the field
					String line = br.readLine();
					for(int y = 0; y < sizey; y++)
					{
						line = br.readLine();
						for(int x = 0; x < sizex; x++)
						{
							Vector2Int aktPos = new Vector2Int(x, y);
							String key = line.substring(x * 2 + 2, x * 2 + 4);
							
							readOneElementOnMap(key, aktPos);
						}
					}

					
					
					// The map is complete loaded, so now we generate Neighbor
					// dependencies
					// Stuff
					for(SimpleMapType postype : complexNPos)
					{
						SpaceObject thatsme = null;

						if(InitMapProcess.FIELD_SET.contains(postype.getType()))
						{
							thatsme = getFieldTypeAtPos(postype.getPosition(), grid);
						}
						else if(InitMapProcess.BUILDING_SET.contains(postype.getType()))
						{
							thatsme = getBuildingTypeAtPos(postype.getPosition(), grid);
						}

						if(thatsme != null)
						{
							Set<SpaceObject> nearRocks = InitMapProcess.getNeighborBlocksInRange(postype.getPosition(), 1, grid,
									InitMapProcess.NEIGHBOR_RELATIONS.get(thatsme.getType()));

							String newNeighborhood = Neighborhood.reCalculateNeighborhood(postype.getPosition(), nearRocks);

							thatsme.setProperty("neighborhood", newNeighborhood);
						}

					}

					/*
					 * Calculate Buildings-centers
					 */
					ArrayList<Vector2Int> center_closed_pos = new ArrayList<Vector2Int>();
					int yline = -1;
					int xcount = 1;
					for(Vector2Int pos : center_building_pos)
					{
						if(yline == -1 || pos.getYAsInteger() != yline)
						{
							yline = pos.getYAsInteger();

						}

						if(xcount == 1)
						{
							if(!center_closed_pos.contains(pos))
							{
								for(int xv = 0; xv <= 2; xv++)
								{
									for(int yv = 0; yv <= 2; yv++)
									{
										Vector2Int delta = new Vector2Int(xv, yv);

										center_closed_pos.add((Vector2Int)pos.copy().add(delta));
									}
								}

								Vector2Int centerpos = (Vector2Int)pos.copy().add(new Vector2Int(1, 1));

								SpaceObject thatsme = getBuildingTypeAtPos(centerpos, grid);

								if(thatsme != null)
								{
									Map<String, Object> properties = thatsme.getProperties();

									grid.createSpaceObject(CENTER_TYPES.get(thatsme.getType()), properties, null);
									grid.destroySpaceObject(thatsme.getId());
								}

							}

						}

						xcount++;

						if(xcount > 3)
						{
							xcount = 1;
						}

					}

				}

				if("CREATURES".equals(data))
				{
					int cnt = Integer.parseInt(br.readLine().trim());
					// cnt = 1;
					for(int i = 0; i < cnt; i++)
					{
						StringTokenizer stok = new StringTokenizer(br.readLine());
						while(stok.hasMoreTokens())
						{
							String type = stok.nextToken().toLowerCase();
							int x = Integer.parseInt(stok.nextToken()) - 1;
							int y = Integer.parseInt(stok.nextToken()) - 1;

							String level = stok.nextToken();

							String owner = stok.nextToken();

							HashMap<String, Object> props = new HashMap<String, Object>();

							// props.put(AngreifPlan.ABKLINGZEIT, new
							// Integer(0));
							props.put(Space2D.PROPERTY_POSITION, new Vector2Double(x, y));

							props.put(ISObjStrings.PROPERTY_INTPOSITION, new Vector2Int(x, y));
							// props.put("auftragsverwalter", gegnerauftraege);

							if(type.equals("spielprozesse"))
							{
								props.put("spieler", 0);
							}
							else
							{
								props.put("spieler", new Integer(1));
								creatureState.addCreature(type);

							}

							// todo: level, owner
							grid.createSpaceObject(type, props, null);
							System.out.println("type: " + type);
							System.out.println("props: " + props);
						}
					}
				}

			}
			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}


		space.removeSpaceProcess(getProperty(ISpaceProcess.ID));

	}

	

	private void createMapElement(Vector2Int aktPos, MapType mapType)
	{
		if(mapType.getVariant()==TypeVariant.BUILDING)
		{
			buildingState.addType(aktPos, mapType);
			buildingState.getTypeAtPos(aktPos);
		}
		else if(mapType.getVariant()==TypeVariant.SOLIDMAP)
		{
//			mapTypeState.addMapType(aktPos, mapType);
		}

//		TileType tmp = TileType.HATCHERY;
		
		try
		{
			Object tile = null;
			tile = mapType.getPojo().getDeclaredConstructor(MapType.class).newInstance(mapType);
			tmpProps.put(PROPERTY_TILEINFO, tile);
//			MapType[] neighbours = ((TileInfo)tile).getNeighbors();
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch(SecurityException e)
		{
			e.printStackTrace();
		}
		catch(InstantiationException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch(InvocationTargetException e)
		{
			e.printStackTrace();
		}
		catch(NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		
//		SpaceObject obj= null;
//		if (TileInfo.getTileInfo(obj, TileInfo.class).getClass().equals(HatcheryInfo.class))
//		{
			//mach was tolles mit der hatchery
//		}
//		HatcheryInfo a = HatcheryInfo.getTileInfo(obj, HatcheryInfo.class);
		
//		props.put(ISpaceObjectStrings.PROPERTY_TILEINFO, tile);
		
	}

}
