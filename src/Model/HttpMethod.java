package Model;

import java.util.TreeMap;

/**
 * Created by joseph on 13/05/16.
 */
public class HttpMethod
{
	private String description;

	private TreeMap<String, Parameter> parameters;
	private TreeMap<Integer, Response> responses;

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}