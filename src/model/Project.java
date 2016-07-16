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

	public boolean addUserRouteProperty(String name, boolean isValueMemorized, String defaultValue)
	{
		if(userDefinedRouteProperties.containsKey(name))
		{
			return false;
		}
		userDefinedRouteProperties.put(name, new UserDefinedRouteProperty(isValueMemorized, defaultValue));
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
		private boolean valuesMemorized;
		private String defaultValue;

		public UserDefinedRouteProperty(boolean valuesMemorized, String defaultValue)
		{
			super();
			this.valuesMemorized = valuesMemorized;
			this.defaultValue = defaultValue;
			if(valuesMemorized)
			{
				add("titi");
				add("Toto");
				add("tutu");
				add("Zozo");
				add("zaza");
			}
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