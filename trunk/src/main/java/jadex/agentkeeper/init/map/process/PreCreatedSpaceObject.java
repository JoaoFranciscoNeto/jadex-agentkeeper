package jadex.agentkeeper.init.map.process;

import jadex.extension.envsupport.environment.IObjectTask;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.Map;

public class PreCreatedSpaceObject implements Comparable<PreCreatedSpaceObject>
{
	
	/**
	 * Simple Helper Class for Map generation
	 * and more efficient Map-Property generation
	 * 
	 * @author Philip Willuweit p.willuweit@gmx.de
	 */

	private String typeName;
	private Map<String, Object> properties;
	private Object tileinfo;
	private Vector2Int position;
	private ArrayList<IObjectTask> tasklist;
	
	/**
	 * @param typeName
	 * @param properties
	 */
	public PreCreatedSpaceObject(String typeName, Vector2Int pos, Object tileinfo, Map<String, Object> properties)
	{
		this.typeName = typeName;
		this.tileinfo = tileinfo;
		this.properties = properties;
		this.position = pos;
		this.tasklist = null;
	}
	
	/**
	 * @param typeName
	 * @param properties
	 */
	public PreCreatedSpaceObject(String typeName, Vector2Int pos, Object tileinfo, Map<String, Object> properties, ArrayList<IObjectTask> tasklist)
	{
		this.typeName = typeName;
		this.tileinfo = tileinfo;
		this.properties = properties;
		this.position = pos;
		this.tasklist = tasklist;
	}
	
	
	/**
	 * @return the properties
	 */
	public Map<String, Object> getProperties()
	{
		return properties;
	}
	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Map<String, Object> properties)
	{
		this.properties = properties;
	}


	/**
	 * @return the tileinfo
	 */
	public Object getTileinfo()
	{
		return tileinfo;
	}


	/**
	 * @param tileinfo the tileinfo to set
	 */
	public void setTileinfo(Object tileinfo)
	{
		this.tileinfo = tileinfo;
	}


	/**
	 * @return the typeName
	 */
	public String getTypeName()
	{
		return typeName;
	}


	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}


	/**
	 * @return the position
	 */
	public Vector2Int getPosition()
	{
		return position;
	}


	/**
	 * @param position the position to set
	 */
	public void setPosition(Vector2Int position)
	{
		this.position = position;
	}
	

	public int compareTo(PreCreatedSpaceObject o)
	{
		if(this.getPosition().getYAsInteger()>o.getPosition().getYAsInteger())
		{
			return 1;
		}
		else if(this.getPosition().getYAsInteger() == o.getPosition().getYAsInteger())
		{
			if(this.getPosition().getXAsInteger() > o.getPosition().getXAsInteger())
			{
				return 1;
			}
		}
		
		return -1;
	}

	/**
	 * @return the tasklist
	 */
	public ArrayList<IObjectTask> getTasklist()
	{
		return tasklist;
	}

	/**
	 * @param tasklist the tasklist to set
	 */
	public void setTasklist(ArrayList<IObjectTask> tasklist)
	{
		this.tasklist = tasklist;
	}


	
	

}
