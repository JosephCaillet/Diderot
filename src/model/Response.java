package model;

/**
 * Created by joseph on 13/05/16.
 */
public class Response
{
	private String description;
	private String outputType;
	private String schema;

	public Response()
	{
		this.description = "";
		this.outputType = Project.getActiveProject().getDefaultResponseFormat();
		this.schema = "";
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getOutputType()
	{
		return outputType;
	}

	public void setOutputType(String outputType)
	{
		this.outputType = outputType;
	}

	public String getSchema()
	{
		return schema;
	}

	public void setSchema(String schema)
	{
		this.schema = schema;
	}
}