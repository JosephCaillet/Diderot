package model;

/**
 * Created by joseph on 13/05/16.
 */
public class Parameter
{
	private boolean required;
	private boolean description;

	public boolean isRequired()
	{
		return required;
	}

	public void setRequired(boolean required)
	{
		this.required = required;
	}

	public boolean isDescription()
	{
		return description;
	}

	public void setDescription(boolean description)
	{
		this.description = description;
	}
}