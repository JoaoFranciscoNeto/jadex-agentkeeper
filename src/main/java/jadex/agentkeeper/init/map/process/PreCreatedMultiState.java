package jadex.agentkeeper.init.map.process;

import jadex.agentkeeper.util.Neighborcase;
import jadex.agentkeeper.worldmodel.enums.CenterPattern;
import jadex.agentkeeper.worldmodel.enums.CenterType;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.TypeVariant;
import jadex.agentkeeper.worldmodel.structure.building.ACenterBuildingInfo;
import jadex.agentkeeper.worldmodel.structure.building.HatcheryInfo;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class PreCreatedMultiState
{
	// Two lists for easier and faster access.
	private HashMap<MapType, HashMap<Vector2Int, PreCreatedSpaceObject>>	listSolid		= new HashMap<MapType, HashMap<Vector2Int, PreCreatedSpaceObject>>();

	private HashMap<MapType, HashMap<Vector2Int, PreCreatedSpaceObject>>	listBuilding	= new HashMap<MapType, HashMap<Vector2Int, PreCreatedSpaceObject>>();

	public PreCreatedMultiState(MapType[] range)
	{
		for(MapType type : range)
		{

			if(type.getVariant() == TypeVariant.BUILDING)
			{
				this.listBuilding.put(type, new HashMap<Vector2Int, PreCreatedSpaceObject>());
			}
			else if(type.getVariant() == TypeVariant.SOLIDMAP)
			{
				this.listSolid.put(type, new HashMap<Vector2Int, PreCreatedSpaceObject>());
			}

		}
	}

	/**
	 * add a specific Type
	 * 
	 * @param location
	 * @param type
	 */
	public void addType(MapType type, Vector2Int pos, Object tileinfo, Map<String, Object> props)
	{
		HashMap<Vector2Int, PreCreatedSpaceObject> myList = null;
		if(type.getVariant() == TypeVariant.BUILDING)
		{
			myList = this.listBuilding.get(type);
		}
		else if(type.getVariant() == TypeVariant.SOLIDMAP)
		{
			myList = this.listSolid.get(type);
		}

		myList.put(pos, new PreCreatedSpaceObject(type.toString(), pos, tileinfo, props));
	}

	/**
	 * Get the Buildings in the Scene. With all calculations: Neighborhood and
	 * Center.
	 */
	public synchronized HashMap<MapType, HashMap<Vector2Int, PreCreatedSpaceObject>> getPreparedAndCalculatedBuildings()
	{
		createCenters();

		return listBuilding;
	}

	/**
	 * Create the Center for each Building
	 */
	private void createCenters()
	{
		for(MapType type : listBuilding.keySet())
		{
			HashMap<Vector2Int, PreCreatedSpaceObject> tmpList = listBuilding.get(type);

			ArrayList<PreCreatedSpaceObject> values = new ArrayList<PreCreatedSpaceObject>(tmpList.values());

			//Sort the Vectors, so the algorithm work
			Collections.sort(values);


			// one building set only
			for(PreCreatedSpaceObject preObj : values)
			{
				Object tileinfo = preObj.getTileinfo();
				if(tileinfo instanceof ACenterBuildingInfo)
				{
					ACenterBuildingInfo centerInfo = (ACenterBuildingInfo)tileinfo;


					// next potential....(if he is not already Border)
					if(centerInfo.getCenterPattern() == CenterPattern.ONE_MIDDLE && centerInfo.getCenterType() != CenterType.BORDER)
					{
						int potCounter = 0;
						for(Neighborcase centercase : Neighborcase.getDefault())
						{
							PreCreatedSpaceObject tmpPre = tmpList.get(preObj.getPosition().copy().add(centercase.getVector()));
							if(tmpPre != null)
							{
								ACenterBuildingInfo tmp = (ACenterBuildingInfo)tmpPre.getTileinfo();
								CenterType tmpCenter = tmp.getCenterType();
								if(tmpCenter == CenterType.UNKNOWN || tmpCenter == CenterType.POTENTIAL)
								{
									tmp.setCenterType(CenterType.POTENTIAL);
									potCounter++;
								}
							}
							else
							{
								centerInfo.setCenterType(CenterType.POTENTIAL);
							}
						}


						// if we sucessfully counted to 8 it is Center-Block!
						if(potCounter >= 8)
						{
							centerInfo.setCenterType(CenterType.CENTER);
							// set the Border for the surrounding tiles to
							// Border (so it can not be center anymore)
							for(Neighborcase centercase : Neighborcase.getDefault())
							{
								Vector2Int borderpos = (Vector2Int)preObj.getPosition().copy().add(centercase.getVector());
								ACenterBuildingInfo tmp = (ACenterBuildingInfo)tmpList.get(borderpos).getTileinfo();
								// System.out.println("tmp : " +
								// tmp.getMapType());
								tmp.setCenterType(CenterType.BORDER);
							}

						}
					}
					else if(centerInfo.getCenterPattern() == CenterPattern.ONE_BORDER_1)
					{
						int potCounter = 0;
						for(Neighborcase centercase : Neighborcase.getDefault())
						{
							PreCreatedSpaceObject tmpPre = tmpList.get(preObj.getPosition().copy().add(centercase.getVector()));
							if(tmpPre != null)
							{
								potCounter++;
							}
						}


						// if we sucessfully counted to 8 it is Center-Block!
						if(potCounter >= 8)
						{
							centerInfo.setCenterType(CenterType.CENTER);
							// set the Border for the surrounding tiles to
							// Border (so it can not be center anymore)
							for(Neighborcase centercase : Neighborcase.getDefault())
							{
								Vector2Int borderpos = (Vector2Int)preObj.getPosition().copy().add(centercase.getVector());
								ACenterBuildingInfo tmp = (ACenterBuildingInfo)tmpList.get(borderpos).getTileinfo();
								// System.out.println("tmp : " +
								// tmp.getMapType());
								if(tmp.getCenterType() != CenterType.CENTER)
								{
									tmp.setCenterType(CenterType.BORDER);
								}

							}

						}
					}
					else if(centerInfo.getCenterPattern() == CenterPattern.ONE_BIG_MIDDLE)
					{

						int bigcounter = 0;
						for(Neighborcase centercase : Neighborcase.values())
						{
							PreCreatedSpaceObject tmpPre = tmpList.get(preObj.getPosition().copy().add(centercase.getVector()));
							if(tmpPre != null)
							{
								ACenterBuildingInfo tmp = (ACenterBuildingInfo)tmpPre.getTileinfo();
								CenterType tmpCenter = tmp.getCenterType();
								if(tmpCenter == CenterType.UNKNOWN)
								{
									bigcounter++;
								}
							}
							else
							{
								// centerInfo.setCenterType(CenterType.POTENTIAL);
								continue;
							}
						}

						if(bigcounter == 24)
						{
							centerInfo.setCenterType(CenterType.CENTER);
						}

					}
				}
			}

		}
	}

}
