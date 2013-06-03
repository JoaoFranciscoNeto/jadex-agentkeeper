package jadex.agentkeeper.init.map.process;

import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.extension.envsupport.math.Vector2Int;

import java.lang.reflect.InvocationTargetException;

public class InitializeHelper
{
	public static Object createPojoElement(Vector2Int aktPos, MapType mapType)
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
