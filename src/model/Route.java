package model;

import java.util.TreeMap;

/**
 * Created by joseph on 13/05/16.
 */
public class Route
{
	private String name = "";
	private String description = "";

	private String contoller = "";
	private String view = "";
	private String viewTemplate = "";

	private TreeMap<String, Route> subRoutes;
	private TreeMap<String, HttpMethod> httpMethods;
}