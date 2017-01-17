package model;

/**
 * An http response
 * @author joseph
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

	/**
	 * Get response description.
	 * @return response description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Set response description.
	 * @param description new description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * Get response output type.
	 * @return response output type.
	 */
	public String getOutputType()
	{
		return outputType;
	}

	/**
	 * Set response output type.
	 * @param outputType new response output type
	 */
	public void setOutputType(String outputType)
	{
		this.outputType = outputType;
	}

	/**
	 * Get response schema.
	 * @return response schema
	 */
	public String getSchema()
	{
		return schema;
	}

	/**
	 * Set response schema.
	 * @param schema new response schema
	 */
	public void setSchema(String schema)
	{
		this.schema = schema;
	}
}