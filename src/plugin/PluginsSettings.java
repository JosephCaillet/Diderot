package plugin;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by joseph on 27/10/16.
 */
public class PluginsSettings
{
	static private HashMap<String, String> settings = new HashMap<>();

	static public String getValue(String propertyName)
	{
		return settings.get(propertyName);
	}

	static public void setValue(String propertyName, String value)
	{
		settings.put(propertyName, value);
	}

	static public Set<String> getKeys()
	{
		return settings.keySet();
	}

	static public void clear()
	{
		settings.clear();
	}
}