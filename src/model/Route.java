package model;

import javax.swing.*;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Created by joseph on 13/05/16.
 */
public class Route implements TreeModel
{
	private String name = "";
	private String description = "";

	private String controller = "";
	private String view = "";
	private String viewTemplate = "";

	private TreeMap<String, Route> subRoutes;
	private TreeMap<String, HttpMethod> httpMethods;

	private Route root;


	public static String getAbsoluteNodePath(TreePath treePath, boolean removeRoot)
	{
		String absPath = "";

		int i = 0;
		if(removeRoot)
		{
			i++;
		}

		Object[] names = treePath.getPath();
		for(; i<names.length; i++)
		{
			absPath += "/" + names[i].toString();
		}

		if(!removeRoot)
		{
			absPath = absPath.substring(1);
		}

		return absPath;
	}

	public Route(String name)
	{
		subRoutes = new TreeMap<String, Route>();
		httpMethods = new TreeMap<String, HttpMethod>();
		this.name = name;
		root = this;
	}

	public Route(String name, Route root)
	{
		this(name);
		this.root = root;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
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

	private static String extractRouteFirstPart(String name)
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

	private static String extractRouteLastPart(String name)
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

	public boolean addRoute(String path)
	{
		Route r;
		String name = extractRouteLastPart(path);

		if(name.isEmpty())
		{
			r = new Route(extractRouteFirstPart(path));
		}
		else
		{
			r = new Route(name);
		}

		return addRoute(path, r);
	}

	public boolean addRoute(String name, Route newRoute)
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
			return subRoutes.get(firstPart).addRoute(lastPart, newRoute);
		}
		else
		{
			if(lastPart.isEmpty())
			{
				subRoutes.put(firstPart, newRoute);
				return true;
			}

			Route route = new Route(firstPart, root);
			subRoutes.put(firstPart, route);
			return route.addRoute(lastPart, newRoute);
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

	@Override
	public Object getRoot()
	{
		return root;
	}

	@Override
	public Object getChild(Object o, int i)
	{
		Route r = (Route)o;
		return r.subRoutes.get(r.subRoutes.keySet().toArray()[i]);
	}

	@Override
	public int getChildCount(Object o)
	{
		Route r = (Route)o;
		return r.subRoutes.size();
	}

	@Override
	public boolean isLeaf(Object o)
	{
		Route r = (Route)o;
		return r.subRoutes.size() == 0;
	}

	@Override
	public void valueForPathChanged(TreePath treePath, Object o)
	{
	}

	@Override
	public int getIndexOfChild(Object o, Object o1)
	{
		return 0;
	}

	@Override
	public void addTreeModelListener(TreeModelListener treeModelListener)
	{
	}

	@Override
	public void removeTreeModelListener(TreeModelListener treeModelListener)
	{
	}

	@Override
	public String toString()
	{
		return name;
	}
}