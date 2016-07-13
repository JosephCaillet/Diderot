package model;

/**
 * Created by joseph on 13/05/16.
 */
public class Parameter
{
	private boolean required = false;
	private String description = "";

	public Parameter()
	{
	}

	public boolean isRequired()
	{
		return required;
	}

	public void setRequired(boolean required)
	{
		this.required = required;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}