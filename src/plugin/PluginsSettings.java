package plugin;

import java.util.HashMap;
import java.util.Set;

/**
 * Stores plugin's settings.
 * @author joseph
 */
public class PluginsSettings
{
	static private HashMap<String, String> settings = new HashMap<>();

	/**
	 * Sets property value if property does not exists.
	 * @param propertyName the property name, should be construct by concatenating the plugin's name and property's name
	 * @param value        the value
	 */
	static public void setPropertyValueIfNotExists(String propertyName, String value)
	{
		if(!settings.containsKey(propertyName))
		{
			settings.put(propertyName, value);
		}
	}

	/**
	 * Gets property value.
	 * @param propertyName the property name, should be construct by concatenating the plugin's name and property's name
	 * @return the property value
	 */
	static public String getPropertyValue(String propertyName)
	{
		return settings.get(propertyName);
	}

	/**
	 * Gets property value.
	 * @param propertyName the property name, should be construct by concatenating the plugin's name and property's name
	 * @param defaultValue the default value
	 * @return the property value
	 */
	static public String getPropertyValue(String propertyName, String defaultValue)
	{
		setPropertyValueIfNotExists(propertyName, defaultValue);
		return settings.get(propertyName);
	}

	/**
	 * Sets property value.
	 * @param propertyName the property name, should be construct by concatenating the plugin's name and property's name
	 * @param value        the value
	 */
	static public void setPropertyValue(String propertyName, String value)
	{
		settings.put(propertyName, value);
	}

	/**
	 * Know if property already exists.
	 * @param property the property name, should be construct by concatenating the plugin's name and property's name
	 * @return true if the property already exists
	 */
	static public boolean containsProperty(String property)
	{
		return settings.containsKey(property);
	}

	/**
	 * Gets properties names list.
	 * @return the properties names
	 */
	static public Set<String> getProperties()
	{
		return settings.keySet();
	}

	/**
	 * Clear all the properties.
	 */
	static public void clear()
	{
		settings.clear();
	}
}