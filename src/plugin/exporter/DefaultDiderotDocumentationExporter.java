package plugin.exporter;

import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by joseph on 20/10/16.
 */
public class DefaultDiderotDocumentationExporter extends DefaultDiderotProjectExporter
{
	static
	{
		availableOperations.clear();
		availableOperations.put("generateHtmlDocumentation", "Generate HTML Documentation");
	}

	@Override
	public HashMap<String, String> getAvailableExportingOperations()
	{
		return availableOperations;
	}

	@Override
	public String getPluginName()
	{
		return "Diredot default documentation exporter";
	}

	@Override
	public String getPluginAuthor()
	{
		return "Joseph Caillet";
	}

	@Override
	public String getPluginContactInformation()
	{
		return "https://github.com/JosephCaillet/Diderot";
	}

	@Override
	public String getPluginVersion()
	{
		return "0.1";
	}

	@Override
	public String getPluginDescription()
	{
		return "Generate Html documentation of your routes";
	}

	public void generateHtmlDocumentation()
	{
		try
		{
			Document xmlSaveDocument = createDocument();

			StreamSource streamSource = new StreamSource("generateDocumentation.xsl");

			Transformer xmlTransformer = TransformerFactory.newInstance().newTransformer(streamSource);
			xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			xmlTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			File f = new File("doc/index.html");
			xmlTransformer.transform(new DOMSource(xmlSaveDocument), new StreamResult(f));

			Desktop.getDesktop().browse(f.toURI());
		}
		catch(ParserConfigurationException | TransformerException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}