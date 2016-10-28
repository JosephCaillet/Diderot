package plugin;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by joseph on 27/10/16.
 */
public class PluginsSettings
{
	static private HashMap<String, String> settings = new HashMap<>();

	static public void setPropertyValueIfNotExists(String propertyName, String value)
	{
		if(!settings.containsKey(propertyName))
		{
			settings.put(propertyName, value);
		}
	}

	static public String getPropertyValue(String propertyName)
	{
		return settings.get(propertyName);
	}

	static public String getPropertyValue(String propertyName, String defaultValue)
	{
		setPropertyValueIfNotExists(propertyName, defaultValue);
		return settings.get(propertyName);
	}

	static public void setPropertyValue(String propertyName, String value)
	{
		settings.put(propertyName, value);
	}

	static public boolean containsProperty(String property)
	{
		return settings.containsKey(property);
	}

	static public Set<String> getProperties()
	{
		return settings.keySet();
	}

	static public void clear()
	{
		settings.clear();
	}
}