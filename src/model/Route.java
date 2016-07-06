package model;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
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

	public TreeMap<String, HttpMethod> getHttpMethods()
	{
		return httpMethods;
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

	private static String extractRouteWithoutFirstPart(String name)
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

	private static String extractRouteLastPart(String name)
	{
		if(name.startsWith("/"))
		{
			name = name.substring(1);
		}

		if(name.lastIndexOf('/') != -1)
		{
			name = name.substring(name.lastIndexOf('/') + 1);
		}
		return name;
	}

	public boolean addRoute(String path)
	{
		String name = extractRouteLastPart(path);
		Route r;

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
		String lastPart = extractRouteWithoutFirstPart(name);

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
		String lastPart = extractRouteWithoutFirstPart(name);

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

	public boolean moveRoute(String oldPath, String newPath)
	{
		Route movedRoute = getLastRoute(oldPath);

		if(movedRoute == null)
		{
			return false;
		}

		deleteRoute(oldPath);//no error check, because we know the route exists.

		if(!addRoute(newPath, movedRoute))
		{
			addRoute(oldPath, movedRoute);//rollback
			return false;
		}

		movedRoute.setName(extractRouteLastPart(newPath));
		return true;
	}

	public Object[] getPathToRoute(String path)
	{
		if(extractRouteLastPart(path).isEmpty())
		{
			return new Route[]{this};
		}

		Vector<Route> routeVector = new Vector<Route>();
		routeVector.add(this);
		if(getPathToRoute(path, routeVector))
		{
			return routeVector.toArray();
		}
		return null;
	}

	public Route getLastRoute(String path)
	{
		Object[] routePath = this.getPathToRoute(path);
		if(routePath == null)
		{
			return null;
		}
		return (Route) routePath[routePath.length-1];
	}

	private boolean getPathToRoute(String path, Vector<Route> routeVector)
	{
		String firstPart = extractRouteFirstPart(path);
		String lastPart = extractRouteWithoutFirstPart(path);

		if(subRoutes.containsKey(firstPart))
		{
			routeVector.add(subRoutes.get(firstPart));
			if(lastPart.isEmpty())
			{
				return true;
			}
			return subRoutes.get(firstPart).getPathToRoute(lastPart, routeVector);
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
	/**
	 * Unused
	 */
	public void valueForPathChanged(TreePath treePath, Object o)
	{
	}

	@Override
	/**
	 * Unused
	 */
	public int getIndexOfChild(Object o, Object o1)
	{
		return 0;
	}

	@Override
	/**
	 * Unused
	 */
	public void addTreeModelListener(TreeModelListener treeModelListener)
	{
	}

	@Override
	/**
	 * Unused
	 */
	public void removeTreeModelListener(TreeModelListener treeModelListener)
	{
	}

	@Override
	public String toString()
	{
		return name;
	}
}