package plugin;

import java.util.HashMap;

/**
 * Created by joseph on 27/10/16.
 */
public class PluginsSettings
{
	static private HashMap<String, String> settings = new HashMap<>();

	static public String getValue(String pluginName, String propertyName)
	{
		return settings.get(pluginName + propertyName);
	}

	static public void setValue(String pluginName, String propertyName, String value)
	{
		settings.put(pluginName + propertyName, value);
	}
}