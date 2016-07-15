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

	private TreeMap<String, PropPair> routePropertiesList;

	public static Project getActiveProject()
	{
		return activeProject;
	}

	public Project()
	{
		routePropertiesList = new TreeMap<String, PropPair>();
		routePropertiesList.put("Controller", new PropPair(true));
		routePropertiesList.put("View", new PropPair(true));
		routePropertiesList.put("View template", new PropPair(false));
	}

	public boolean addRouteProperty(String name, boolean isValueMemorized)
	{
		if(routePropertiesList.containsKey(name))
		{
			return false;
		}
		routePropertiesList.put(name, new PropPair(isValueMemorized));
		return true;
	}

	public PropPair getRouteProperty(String name)
	{
		return routePropertiesList.get(name);
	}

	public boolean removeRouteProperty(String name)
	{
		return null == routePropertiesList.remove(name);
	}

	public String[] getPropertiesNames()
	{
		Object[] valuesList =  routePropertiesList.keySet().toArray();
		return Arrays.copyOf(valuesList, valuesList.length, String[].class);
	}


	private class PropPair extends TreeSet<String>
	{
		private boolean valuesMemorized;

		public PropPair(boolean valuesMemorized)
		{
			super();
			this.valuesMemorized = valuesMemorized;
			if(valuesMemorized)
			{
				add("titi");
				add("toto");
				add("tutu");
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

		public String[] getValues()
		{
			Object[] valuesList =  toArray();
			return Arrays.copyOf(valuesList, valuesList.length, String[].class);
		}
	}
}