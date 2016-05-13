package Model;

/**
 * Created by joseph on 13/05/16.
 */
public class Response
{
	private String description;
	private String outputType;
	private String outputFormatDescription;

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

	public String getOutputFormatDescription()
	{
		return outputFormatDescription;
	}

	public void setOutputFormatDescription(String outputFormatDescription)
	{
		this.outputFormatDescription = outputFormatDescription;
	}
}