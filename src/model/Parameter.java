package model;

/**
 * Created by joseph on 13/05/16.
 */
public class Parameter
{
	private boolean required = true;
	private String description = "";

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