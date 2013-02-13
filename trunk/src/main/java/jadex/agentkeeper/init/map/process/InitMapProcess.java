package jadex.agentkeeper.init.map.process;

import jadex.agentkeeper.game.state.missions.Mission;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.Neighborhood;
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
		final Grid2D grid = (Grid2D)space;

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

					ArrayList<SimpleMapType> complexNPos = new ArrayList<SimpleMapType>();

					// Building Positions for setting the Centers
					ArrayList<Vector2Int> center_building_pos = new ArrayList<Vector2Int>();

					int dungeon_heart_counter = 0;

					// Now init the field
					String line = br.readLine();
					for(int y = 0; y < sizey; y++)
					{
						line = br.readLine();
						for(int x = 0; x < sizex; x++)
						{
							Vector2Int aktPos = new Vector2Int(x, y);
							String key = line.substring(x * 2 + 2, x * 2 + 4);
							String type = (String)imagenames.get(key);

							Map<String, Object> props = new HashMap<String, Object>();
							props.put("bearbeitung", new Integer(0));
							props.put("clicked", false);

							// Null Check
							type = type == null ? "unknown" : type;


							
//							TileType tmp = TileType.HATCHERY;
//							TileInfo tile = null;
							
//							tile = (TileInfo) TILE_MAP.get(type).newInstance();
							
//							SpaceObject obj= null;
//							if (TileInfo.getTileInfo(obj, TileInfo.class).getClass().equals(HatcheryInfo.class))
//							{
								//mach was tolles mit der hatchery
//							}
//							TileInfo a = TileInfo.getTileInfo(obj, TileInfo.class);
							
//							props.put(ISpaceObjectStrings.PROPERTY_TILEINFO, tile);
							if(type == GOLD)
							{
								props.put("amount", 30);
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

							
							
							props.put("neighborhood", "00000000");
							props.put(Space2D.PROPERTY_POSITION, aktPos);
							grid.createSpaceObject(type, props, null);
							
							
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
									Map properties = thatsme.getProperties();

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

				if("MISSIONEN".equals(data))
				{
					int cnt = Integer.parseInt(br.readLine().trim());
					for(int i = 0; i < cnt; i++)
					{
						StringTokenizer stok = new StringTokenizer(br.readLine());
						while(stok.hasMoreTokens())
						{
							String typ = stok.nextToken();
							int menge = Integer.parseInt(stok.nextToken());
							mv.neueMission(new Mission(typ, menge, uem));
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
		int gold = gebaeuedeverwalter.gibGebaeude(InitMapProcess.TREASURY).size() * 150;
		// int gold = schatztruhenliste.size() * 150;

		int mana = 450;

		int forschung = 14;

		grid.setProperty("gold", gold);
		grid.setProperty("mana", mana);
		grid.setProperty("forschung", forschung);

		grid.setProperty(GEBAEUDELISTE, gebaeuedeverwalter);
		// grid.setProperty("schatztruhen", schatztruhenliste);
		// grid.setProperty("lairliste", lairliste);
		// grid.setProperty("hatcheryliste", hatcheryliste);
		// grid.setProperty("trainingliste", trainingliste);
		// grid.setProperty("libraryliste", libraryliste);
		// grid.setProperty("tortureliste", tortureliste);

		space.removeSpaceProcess(getProperty(ISpaceProcess.ID));

	}

}
