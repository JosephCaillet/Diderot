package model;

import java.util.TreeMap;

/**
 * Created by joseph on 13/05/16.
 */
public class Route
{
	private String description = "";

	private String controller = "";
	private String view = "";
	private String viewTemplate = "";

	private TreeMap<String, Route> subRoutes;
	private TreeMap<String, HttpMethod> httpMethods;


	public Route()
	{
		subRoutes = new TreeMap<String, Route>();
		httpMethods = new TreeMap<String, HttpMethod>();
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getController()
	{
		return controller;
	}

	public void setController(String controller)
	{
		this.controller = controller;
	}

	public String getView()
	{
		return view;
	}

	public void setView(String view)
	{
		this.view = view;
	}

	public String getViewTemplate()
	{
		return viewTemplate;
	}

	public void setViewTemplate(String viewTemplate)
	{
		this.viewTemplate = viewTemplate;
	}

	public TreeMap<String, Route> getSubRoutes()
	{
		return subRoutes;
	}

	private String extractRouteFirstPart(String name)
	{
		if(name.startsWith("/"))
		{
			name = name.substring(1);
		}

		if(name.indexOf('/') != -1)
		{
			name = name.substring(0, name.indexOf('/'));
		}
		return name;
	}

	private String extractRouteLastPart(String name)
	{
		String lastPart = "";

		if(name.startsWith("/"))
		{
			name = name.substring(1);
		}

		if(name.indexOf('/') != -1)
		{
			lastPart = name.substring(name.indexOf('/') + 1);
		}
		return lastPart;
	}

	public boolean addRoute(String name)
	{
		String firstPart = extractRouteFirstPart(name);
		String lastPart = extractRouteLastPart(name);

		if(firstPart.isEmpty())
		{
			return false;
		}

		if(subRoutes.containsKey(firstPart))
		{
			if(lastPart.isEmpty())
			{
				return false;
			}
			return subRoutes.get(firstPart).addRoute(lastPart);
		}
		else
		{
			Route newRoute = new Route();
			subRoutes.put(firstPart, newRoute);

			if(lastPart.isEmpty())
			{
				return true;
			}

			return newRoute.addRoute(lastPart);
		}
	}

	public boolean deleteRoute(String name)
	{
		String firstPart = extractRouteFirstPart(name);
		String lastPart = extractRouteLastPart(name);

		if(subRoutes.containsKey(firstPart))
		{
			if(lastPart.isEmpty())
			{
				Route r = subRoutes.remove(firstPart);
				return r != null;
			}
			return subRoutes.get(firstPart).deleteRoute(lastPart);
		}
		return false;
	}

	public boolean renameRoute(String oldName, String newName)
	{
		String firstPart = extractRouteFirstPart(newName);
		String lastPart = extractRouteLastPart(newName);

		if(subRoutes.containsKey(firstPart))
		{
			if(lastPart.isEmpty())
			{
				Route r = subRoutes.remove(firstPart);
				if(r == null)
				{
					return false;
				}
				subRoutes.put(newName, r);
				return true;
			}
			return subRoutes.get(firstPart).deleteRoute(lastPart);
		}
		return false;
	}
}