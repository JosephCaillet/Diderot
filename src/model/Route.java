package model;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Represents a web route.
 * @author joseph
 * Created by joseph on 13/05/16.
 * Will maybe be moved as a member of the Project class.
 */
public class Route implements TreeModel
{
	private String name = "";
	private String description = "";

	private TreeMap<String, Route> subRoutes;
	private TreeMap<String, HttpMethod> httpMethods;

	private Route root;


	//Statics methods

	/**
	 * Transforms a tree path into a string url representation of the path.
	 * eg: {"potatoes.com","I","love","them"} -> potatoes.com/I/love/them
	 * @param treePath Tree path.
	 * @param removeRoot Should the url's root be removed or not.
	 * @return A a string url representation of the path.
	 */
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

	/**
	 * Extracts the first path of a url.
	 * eg: /i/love/potatoes -> i (the first / is not required)
	 * @param name Url.
	 * @return First path of a url.
	 */
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

	/**
	 * Removes the first path of a url.
	 * eg: /i/love/potatoes -> love/potatoes (the first / is not required)
	 * @param name Url.
	 * @return Url without first part.
	 */
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

	/**
	 * Extracts the last path of a url.
	 * eg: /i/love/potatoes -> potatoes (the first / is not required)
	 * @param name Url.
	 * @return Last path of a url.
	 */
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

	//Constructors

	/**
	 * Create a route that will be the top level root of other routes.
	 * Use it only for top level root.
	 * @param name Route's name
	 */
	public Route(String name)
	{
		subRoutes = new TreeMap<String, Route>();
		httpMethods = new TreeMap<String, HttpMethod>();
		this.name = name;
		root = this;
		//createSampleHttpMethods();
	}

	/**
	 * Create a route, child of an other route.
	 * @param name Route's name
	 * @param root Top level root (not the parent route, unless parent and top level route are the same).
	 */
	public Route(String name, Route root)
	{
		this(name);
		this.root = root;
	}

	/**
	 * Create sample http methods.
	 */
	private void createSampleHttpMethods()
	{
		httpMethods.put("GET", new HttpMethod());
		httpMethods.put("HEAD", new HttpMethod());
		httpMethods.put("POST", new HttpMethod());
	}

	/**
	 * Clears the route.
	 */
	public void clear()
	{
		name = "";
		description = "";
		subRoutes.clear();
		httpMethods.clear();
	}

	//Getter and setter

	/**
	 * Get route's name.
	 * @return route's name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Set route's name.
	 * @param name New route's name.
	 */
	private void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Rename routes' root. To rename a child route, use Route#moveRoute.
	 * @see Route#moveRoute(String, String)
	 * @param name New route's name.
	 * @throws RuntimeException if you try to rename an other route than the root route.
	 */
	public void rename(String name) throws RuntimeException
	{
		if(root != this)
		{
			throw new RuntimeException("You cannot setName directly a route different from the root route. Use moveRoute() instead.");
		}
		this.name = name;
	}

	/**
	 * Get route's description.
	 * @return Route's description.
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Set route's description.
	 * @param description New route's description.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	//Route management

	/**
	 * Get children routes' names.
	 * @return Sub routes' names.
	 */
	public String[] getRoutesNames()
	{
		return subRoutes.keySet().toArray(new String[subRoutes.size()]);
	}

	/**
	 * Get a sub route by its name.
	 * @param name Name of the wanted route.
	 * @return The requested route.
	 */
	public Route getRoute(String name)
	{
		return subRoutes.get(name);
	}

	/**
	 * Add a blank route to an other one.
	 * eg: with /i/really/love/potatoes given as path, this method will create a route named 'potatoes', and will add it as a child of /i/really/love route. Sub routes will be created if needed.
	 * @param path Path to route you want to create.
	 * @return True if the route as been successfully created, false otherwise.
	 */
	public boolean addRoute(String path)
	{
		String name = extractRouteLastPart(path);
		Route r;

		if(name.isEmpty())
		{
			r = new Route(extractRouteFirstPart(path), root);
		}
		else
		{
			r = new Route(name, root);
		}

		return addRoute(path, r);
	}

	/**
	 * Add the given route to an other one.
	 * eg: with /i/really/love/potatoes given as path, this method will create a route named 'potatoes', and will add it as a child of /i/really/love route. Sub routes will be created if needed.
	 * @param path Path to the route that will contain the given route.
	 * @param newRoute The route to add, relative to the current route.
	 * @return True if the route as been successfully created, false otherwise.
	 */
	public boolean addRoute(String path, Route newRoute)
	{
		String firstPart = extractRouteFirstPart(path);
		String lastPart = extractRouteWithoutFirstPart(path);

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

	/**
	 * Delete a route.
	 * @param path Path to the route to delete, relative to the current route.
	 * @return True if removal successful, false otherwise.
	 */
	public boolean deleteRoute(String path)
	{
		String firstPart = extractRouteFirstPart(path);
		String lastPart = extractRouteWithoutFirstPart(path);

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

	/**
	 * Move (or rename) a route.
	 * @param oldPath Path of the old route, relative to the current route.
	 * @param newPath Path of the new route, relative to the current route.
	 * @return true if moved successfully, false otherwise
	 */
	public boolean moveRoute(String oldPath, String newPath)
	{
		if(oldPath.equals(newPath))
		{
			return false;
		}

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

	/**
	 * Get all routes that are in the given path.
	 * @param path Path of the last route wanted.
	 * @return An array of all the routes in the path given, null if any route in the path is not found.
	 */
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

	/**
	 * Add the first Route in path to routeVector.
	 * @param path Path of the routes to add in routeVector.
	 * @param routeVector Vector where to add find routes.
	 * @return True if the first route in path exist, false otherwise.
	 */
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

	/**
	 * Get the last route of the given path.
	 * @param path Path of the required route.
	 * @return Null if the given route does not exist.
	 */
	public Route getLastRoute(String path)
	{
		Object[] routePath = this.getPathToRoute(path);
		if(routePath == null)
		{
			return null;
		}
		return (Route) routePath[routePath.length-1];
	}

	//HttpMethod management
	public String[] getHttpMethodNames()
	{
		return httpMethods.keySet().toArray(new String[httpMethods.size()]);
	}

	public HttpMethod getHttpMethod(String name)
	{
		return httpMethods.get(name);
	}

	public boolean addHttpMethod(String methodName, HttpMethod method)
	{
		if(httpMethods.containsKey(methodName))
		{
			return false;
		}
		httpMethods.put(methodName, method);
		return true;
	}

	public boolean removeHttpMethod(String methodName)
	{
		return null == httpMethods.remove(methodName);
	}

	public boolean changeHttpMethod(String oldName, String newName)
	{
		if(oldName.equals(newName))
		{
			return false;
		}

		if(!addHttpMethod(newName, httpMethods.get(oldName)))
		{
			return false;
		}

		removeHttpMethod(oldName);
		return true;
	}

	//User defined properties management
	//TODO call only on root routes?

	/**
	 * Add a user property.
	 * @param name Property's name.
	 * @param defaultValue Default value of the new Property.
	 */
	public void addUserProperty(String name, String defaultValue)
	{
		for(Map.Entry<String, HttpMethod> entry : httpMethods.entrySet())
		{
			entry.getValue().setUserProperty(name, defaultValue);
		}

		for(Map.Entry<String, Route> entry : subRoutes.entrySet())
		{
			entry.getValue().addUserProperty(name, defaultValue);
		}
	}

	/**
	 * Remove a user property.
	 * @param name
	 */
	public void removeUserProperty(String name)
	{
		for(Map.Entry<String, HttpMethod> entry : httpMethods.entrySet())
		{
			entry.getValue().removeUserProperty(name);
		}

		for(Map.Entry<String, Route> entry : subRoutes.entrySet())
		{
			entry.getValue().removeUserProperty(name);
		}
	}

	public void renameUserProperty(String oldName, String newName)
	{
		for(Map.Entry<String, HttpMethod> entry : httpMethods.entrySet())
		{
			HttpMethod value = entry.getValue();
			value.setUserProperty(newName, value.getUserPropertyValue(oldName));
			value.removeUserProperty(oldName);
		}

		for(Map.Entry<String, Route> entry : subRoutes.entrySet())
		{
			entry.getValue().renameUserProperty(oldName, newName);
		}
	}

	public void changeUserPropertyValue(String name, String oldValue, String newValue)
	{
		for(Map.Entry<String, HttpMethod> entry : httpMethods.entrySet())
		{
			HttpMethod httpMethod = entry.getValue();
			if(httpMethod.getUserPropertyValue(name).equals(oldValue))
			{
				httpMethod.setUserProperty(name, newValue);
			}
		}

		for(Map.Entry<String, Route> entry : subRoutes.entrySet())
		{
			entry.getValue().changeUserPropertyValue(name, oldValue, newValue);
		}
	}

	public void removeForbiddenUserPropertyValues(String propName)
	{
		for(Map.Entry<String, HttpMethod> entry : httpMethods.entrySet())
		{
			HttpMethod httpMethod = entry.getValue();
			httpMethod.removeUnauthorizedValues(propName);
		}

		for(Map.Entry<String, Route> entry : subRoutes.entrySet())
		{
			entry.getValue().removeForbiddenUserPropertyValues(propName);
		}
	}

	//Response format management
	public void renameResponseFormatValue(String oldName, String newName)
	{
		for(Map.Entry<String, HttpMethod> entry : httpMethods.entrySet())
		{
			HttpMethod httpMethod = entry.getValue();
			for(String responseName : httpMethod.getResponsesNames())
			{
				Response response = httpMethod.getResponse(responseName);
				if(response.getOutputType().equals(oldName))
				{
					response.setOutputType(newName);
				}
			}
		}

		for(Map.Entry<String, Route> entry : subRoutes.entrySet())
		{
			entry.getValue().renameResponseFormatValue(oldName, newName);
		}
	}

	//interface TreeModel
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
		//This not the implementation you are looking for.
	}

	@Override
	/**
	 * Unused
	 */
	public int getIndexOfChild(Object o, Object o1)
	{
		//This not the implementation you are looking for.
		return 0;
	}

	@Override
	/**
	 * Unused
	 */
	public void addTreeModelListener(TreeModelListener treeModelListener)
	{
		//This not the implementation you are looking for.
	}

	@Override
	/**
	 * Unused
	 */
	public void removeTreeModelListener(TreeModelListener treeModelListener)
	{
		//This not the implementation you are looking for.
	}

	//toString override
	@Override
	public String toString()
	{
		return name;
	}
}