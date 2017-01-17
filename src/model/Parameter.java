package model;

/**
 * A request's parameter.
 * @author joseph
 */
public class Parameter
{
	private boolean required = true;
	private String description = "";

	/**
	 * Creates a parameter.
	 */
	public Parameter(){}

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
}