package plugin.exporter;

import model.Project;
import model.Route;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.HashMap;

/**
 * Created by joseph on 05/10/16.
 */
public class DefaultDiderotProjectExporter implements DiderotProjectExporter
{
	private Route rootRoute;
	private Project project;

	static private HashMap<String, String> availableOperations = new HashMap<>();

	static
	{
		availableOperations.put("export", "Save project");
	}

	@Override
	public HashMap<String, String> getAvailableExportingOperations()
	{
		return availableOperations;
	}

	@Override
	public String getPluginName()
	{
		return "Diderot default project exporter";
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
	public void setDiderotData(Route rootRoute, Project project)
	{
		this.rootRoute = rootRoute;
		this.project = project;
	}

	public void export()
	{
		try
		{
			Document xmlSaveDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

			Element diderotProject = xmlSaveDocument.createElement("diderotProject");
			diderotProject.setAttribute("name", project.getName());
			diderotProject.setAttribute("company", project.getCompany());
			diderotProject.setAttribute("authors", project.getAuthors());
			diderotProject.setAttribute("contact", project.getContact());
			xmlSaveDocument.appendChild(diderotProject);

			Element projectDescription = xmlSaveDocument.createElement("description");
			projectDescription.appendChild(xmlSaveDocument.createTextNode(project.getDescription()));
			diderotProject.appendChild(projectDescription);

			Transformer xmlTransformer = TransformerFactory.newInstance().newTransformer();
			xmlTransformer.transform(new DOMSource(xmlSaveDocument), new StreamResult(new File("testSave.xml")));
		}
		catch(TransformerConfigurationException e)
		{
			e.printStackTrace();
			return;
		}
		catch(TransformerException e)
		{
			e.printStackTrace();
			return;
		}
		catch(ParserConfigurationException e)
		{
			e.printStackTrace();
			return;
		}


	}
}