package jadex.agentkeeper.init.map.process;

import jadex.agentkeeper.ai.UpdateStatusTask;
import jadex.agentkeeper.ai.creatures.UpdateChickenTask;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.Neighborhood;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.TypeVariant;
import jadex.agentkeeper.worldmodel.structure.TileInfo;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.SUtil;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.IObjectTask;
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
import java.util.StringTokenizer;


/**
 * Environment process for creating wastes.
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
public class InitMapProcess extends AInitMapProcess implements ISpaceProcess, IMap, ISObjStrings
{

	private Map<String, Object>			tmpProps;

	private Grid2D						grid;

	private PreCreatedMultiState		preCreatedState;
	
	private ArrayList<Vector2Int>		chickenlist = new ArrayList<Vector2Int>();

	private void readOneElementOnMap(String key, Vector2Int aktPos)
	{
		
		
		MapType mapType = TILE_MAP.get(key);

		
		String type = mapType.toString();

		// Null Check
		
		type = type == null ? "unknown" : type;

		tmpProps = new HashMap<String, Object>();

		Object tileinfo = createPojoElement(aktPos, mapType);

		tmpProps.put(PROPERTY_CLICKED, false);

		// TODO: thats shit!
		tmpProps.put("bearbeitung", 0);
		tmpProps.put("status", "Nothing");


		if(mapType.getVariant() == TypeVariant.BUILDING || mapType == MapType.CLAIMED_PATH)
		{
			playerState.addClaimedSector();
		}

		tmpProps.put(PROPERTY_NEIGHBORHOOD, "00000000");
		tmpProps.put(Space2D.PROPERTY_POSITION, aktPos);
		tmpProps.put(PROPERTY_INTPOSITION, aktPos);
		


		tmpProps.put(PROPERTY_TILEINFO, tileinfo);

		if(mapType.getVariant() == TypeVariant.BUILDING)
		{

			buildingState.addType(aktPos, tileinfo);
			if(tileinfo != null)
			{

				preCreatedState.addType(mapType, aktPos, tileinfo, tmpProps);
			}
		}
		else
		{
			buildingState.addType(aktPos, tileinfo);
			grid.createSpaceObject(type, tmpProps, null);
		}
		
		if(mapType == MapType.HATCHERY)
		{
			chickenlist.add(aktPos);

		}

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

		preCreatedState = new PreCreatedMultiState(MapType.values());


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

					HashMap<MapType, HashMap<Vector2Int, PreCreatedSpaceObject>> toCreateB = preCreatedState.getPreparedBuildings();
					for(HashMap<Vector2Int, PreCreatedSpaceObject> hashpre : toCreateB.values())
					{
						for(PreCreatedSpaceObject presobj : hashpre.values())
						{
							grid.createSpaceObject(presobj.getTypeName(), presobj.getProperties(), null);
						}
					}


				}


				Object[] allSObj = grid.getSpaceObjects();
				for(int i = 0; i < allSObj.length; i++)
				{
					SpaceObject sobj = (SpaceObject)allSObj[i];

					if(TileInfo.getTileInfo(sobj, TileInfo.class) != null)
					{
						Neighborhood.updateMyNeighborvalueBasedOnMyNeighborhood((Vector2Int)sobj.getProperty(PROPERTY_INTPOSITION), sobj, grid);
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
							
							HashMap<String, Object> props = new HashMap<String, Object>();

							String level = stok.nextToken();
							props.put(ISObjStrings.PROPERTY_LEVEL, level);

							String owner = stok.nextToken();
							props.put(ISObjStrings.PROPERTY_OWNER, owner);

							// props.put(AngreifPlan.ABKLINGZEIT, new
							// Integer(0));
							props.put(Space2D.PROPERTY_POSITION, new Vector2Double(x, y));

							props.put(ISObjStrings.PROPERTY_INTPOSITION, new Vector2Int(x, y));
							
							props.put(PROPERTY_AWAKE, 100.0);
							props.put(PROPERTY_FED, 100.0);
							props.put(PROPERTY_HAPPINESS, 100.0);
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
							
							ArrayList<IObjectTask> list = new ArrayList<IObjectTask>();
							list.add(new UpdateStatusTask());

							// todo: level, owner
							grid.createSpaceObject(type, props, list);
							System.out.println("type: " + type);
							System.out.println("props: " + props);
						}
					}
					
					// Create Chickens
					for(Vector2Int vec : chickenlist)
					{
						HashMap<String, Object> propschick = new HashMap<String, Object>();
						propschick.put(Space2D.PROPERTY_POSITION, new Vector2Double(vec));

						propschick.put(ISObjStrings.PROPERTY_INTPOSITION, vec);
						propschick.put("spieler", new Integer(1));
						
						ArrayList<IObjectTask> list2 = new ArrayList<IObjectTask>();
						list2.add(new UpdateChickenTask());

						// todo: level, owner
						grid.createSpaceObject("chicken", propschick, list2);
					}
					
//						Vector2Int vec = chickenlist.get(0);
//						HashMap<String, Object> propschick = new HashMap<String, Object>();
//						propschick.put(Space2D.PROPERTY_POSITION, new Vector2Double(vec));
//
//						propschick.put(ISObjStrings.PROPERTY_INTPOSITION, vec);
//						propschick.put("spieler", new Integer(1));
//						
//						ArrayList<IObjectTask> list2 = new ArrayList<IObjectTask>();
//						list2.add(new UpdateChickenTask());
//
//						// todo: level, owner
//						grid.createSpaceObject("chicken", propschick, list2);
					

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


	private Object createPojoElement(Vector2Int aktPos, MapType mapType)
	{
		Object ret = null;
		try
		{
			ret = mapType.getPojo().getDeclaredConstructor(MapType.class).newInstance(mapType);
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

		return ret;
	}

}
