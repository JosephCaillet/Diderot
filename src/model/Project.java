package model;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by joseph on 15/07/16.
 */
public class Project
{
	private static Project activeProject = new Project();

	private TreeMap<String, UserDefinedRouteProperty> userDefinedRouteProperties;

	public static Project getActiveProject()
	{
		return activeProject;
	}

	public Project()
	{
		userDefinedRouteProperties = new TreeMap<String, UserDefinedRouteProperty>();
	}

	//user defined properties management
	public boolean addUserRouteProperty(String name, String defaultValue)
	{
		if(userDefinedRouteProperties.containsKey(name))
		{
			return false;
		}
		userDefinedRouteProperties.put(name, new UserDefinedRouteProperty(defaultValue));
		return true;
	}

	public UserDefinedRouteProperty getUserRouteProperty(String name)
	{
		return userDefinedRouteProperties.get(name);
	}

	public boolean removeUserRouteProperty(String name)
	{
		return null == userDefinedRouteProperties.remove(name);
	}

	public String[] getUserRoutesPropertiesNames()
	{
		Object[] valuesList =  userDefinedRouteProperties.keySet().toArray();
		return Arrays.copyOf(valuesList, valuesList.length, String[].class);
	}


	public class UserDefinedRouteProperty extends TreeSet<String>
	{
		private boolean newValuesDisabled = false;
		private boolean valuesMemorized = true;
		private String defaultValue;

		public UserDefinedRouteProperty(String defaultValue)
		{
			super();
			this.defaultValue = defaultValue;
			add(defaultValue);
			add("titi");
			add("Toto");
			add("tutu");
			add("Zozo");
			add("zaza");
		}

		public boolean isNewValuesDisabled()
		{
			return newValuesDisabled;
		}

		public void setNewValuesDisabled(boolean newValuesDisabled)
		{
			this.newValuesDisabled = newValuesDisabled;
		}

		public boolean isValuesMemorized()
		{
			return valuesMemorized;
		}

		public void setValuesMemorized(boolean valuesMemorized)
		{
			this.valuesMemorized = valuesMemorized;
		}

		public String getDefaultValue()
		{
			return defaultValue;
		}

		public void setDefaultValue(String defaultValue)
		{
			this.defaultValue = defaultValue;
		}

		public String[] getValues()
		{
			Object[] valuesList =  toArray();
			return Arrays.copyOf(valuesList, valuesList.length, String[].class);
		}
	}
}