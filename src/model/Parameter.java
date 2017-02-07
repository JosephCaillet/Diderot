package model;

import java.util.Collections;
import java.util.Vector;

/**
 * A request's parameter.
 * @author joseph
 */
public class Parameter
{
	private boolean required = true;
	private String description = "";
	private String type = " ";
	private String subType = " ";
	private String location = " ";

	public static final Vector<String> PARAMS_LOCATION = new Vector<>();

	static
	{
		Collections.addAll(PARAMS_LOCATION, new String[]{"query", "header", "formData", "body"});
	}

	/**
	 * Creates a parameter.
	 */
	public Parameter()
	{
		if(Project.getActiveProject().getSubParamsTypes("String").length != 0)
		{
			type = "String";
		}
		else
		{
			type = Project.getActiveProject().getParamsTypes()[0];
		}

		subType = Project.getActiveProject().getSubParamsTypes(type)[0];
		location = PARAMS_LOCATION.get(0);
	}

	/**
	 * Creates a parameter.
	 * @param required is the parameter required
	 * @param description parameter description
	 */
	public Parameter(boolean required, String description)
	{
		this.required = required;
		this.description = description;
	}

	/**
	 * Know if the parameter required.
	 * @return if the parameter is required
	 */
	public boolean isRequired()
	{
		return required;
	}

	/**
	 * Set parameter's required status.
	 * @param required Required status
	 */
	public void setRequired(boolean required)
	{
		this.required = required;
	}

	/**
	 * Get parameter's description.
	 * @return Parameter's description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Set parameter's description.
	 * @param description Parameter's description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getSubType()
	{
		return subType;
	}

	public void setSubType(String subType)
	{
		this.subType = subType;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}
}