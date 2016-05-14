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

	public boolean addRoute(String name)
	{
		String firstPart = name;
		if(name.indexOf('/') != -1)
		{
			firstPart = name.substring(0, name.indexOf('/'));
		}

		String secondPart = "";
		if(name.indexOf('/') != -1)
		{
			secondPart = name.substring(name.indexOf('/') + 1);
		}


		if(subRoutes.containsKey(firstPart))
		{
			if(secondPart.isEmpty())
			{
				return false;
			}
			return subRoutes.get(firstPart).addRoute(secondPart);
		}
		else
		{
			Route newRoute = new Route();
			subRoutes.put(firstPart, newRoute);

			if(secondPart.isEmpty())
			{
				return true;
			}

			return newRoute.addRoute(secondPart);
		}
	}
}