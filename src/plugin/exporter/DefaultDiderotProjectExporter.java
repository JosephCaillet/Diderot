package plugin.exporter;

import model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import plugin.PluginsSettings;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.HashMap;

import static plugin.exporter.DiderotProjectExporter.encodeNewLine;

/**
 * Created by joseph on 05/10/16.
 */
public class DefaultDiderotProjectExporter implements DiderotProjectExporter
{
	protected Route rootRoute;
	protected Project project;
	protected JFrame parent;

	static private HashMap<String, String> availableOperations = new HashMap<>();

	static
	{
		availableOperations.put("Save project", "exportProject");
		availableOperations.put("Save project as", "exportProjectAs");
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
		return "1.0";
	}

	@Override
	public String getPluginDescription()
	{
		return "Diderot project file saver.";
	}

	@Override
	public void setDiderotData(Route rootRoute, Project project)
	{
		this.rootRoute = rootRoute;
		this.project = project;
	}

	@Override
	public void setParentFrame(JFrame parent)
	{
		this.parent = parent;
	}

	public void exportProject()
	{
		String fileName = PluginsSettings.getValue(getPluginName() + "projectFileName");
		if(fileName == null)
		{
			exportProjectAs();
			return;
		}
		launchExport(fileName);
	}

	public void exportProjectAs()
	{
		//Todo: remove "." to start in home directory of user
		JFileChooser fileChooser = new JFileChooser(".");
		String projectFileName = PluginsSettings.getValue(getPluginName() + "projectFileName");
		if(projectFileName != null)
		{
			fileChooser.setSelectedFile(new File(projectFileName));
		}
		fileChooser.setFileFilter(new FileNameExtensionFilter("Diderot project file", "dip"));

		if(JFileChooser.APPROVE_OPTION != fileChooser.showSaveDialog(parent))
		{
			return;
		}

		String fileName = fileChooser.getSelectedFile().getAbsolutePath();
		if(!fileName.endsWith(".dip"))
		{
			fileName = fileName + ".dip";
		}

		launchExport(fileName);
	}

	private void launchExport(String fileName)
	{
		try
		{
			Document xmlSaveDocument = createDocument();

			Transformer xmlTransformer = TransformerFactory.newInstance().newTransformer();
			xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			xmlTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			xmlTransformer.transform(new DOMSource(xmlSaveDocument), new StreamResult(fileName));
			JOptionPane.showMessageDialog(parent, "Project successfully saved", "Project successfully saved", JOptionPane.INFORMATION_MESSAGE);
		}
		catch(ParserConfigurationException | TransformerException e)
		{
			e.printStackTrace();
			return;
		}
	}

	protected Document createDocument() throws ParserConfigurationException
	{
		Document xmlSaveDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Element diderotProject = xmlSaveDocument.createElement("diderotProject");

		diderotProject.setAttribute("name", project.getName());
		diderotProject.setAttribute("company", project.getCompany());
		diderotProject.setAttribute("authors", project.getAuthors());
		diderotProject.setAttribute("contact", project.getContact());
		xmlSaveDocument.appendChild(diderotProject);

		Element projectDescription = xmlSaveDocument.createElement("description");
		projectDescription.appendChild(xmlSaveDocument.createTextNode(encodeNewLine(project.getDescription())));
		diderotProject.appendChild(projectDescription);
		diderotProject.appendChild(buildPluginsPropertiesXml(xmlSaveDocument));
		diderotProject.appendChild(buildResponseOutputFormatXml(xmlSaveDocument));
		diderotProject.appendChild(buildUserDefinedPropertiesXml(xmlSaveDocument));

		Element routeXml = xmlSaveDocument.createElement("route");
		routeXml.setAttribute("name", project.getDomain());
		buildRouteXml(xmlSaveDocument, rootRoute, routeXml);
		diderotProject.appendChild(routeXml);

		return xmlSaveDocument;
	}

	private Element buildPluginsPropertiesXml(Document rootXml)
	{
		Element pluginsProperties = rootXml.createElement("pluginsProperties");

		for(String property : PluginsSettings.getKeys())
		{
			Element pluginProperty = rootXml.createElement("pluginProperty");
			pluginProperty.setAttribute("property", property);
			pluginProperty.appendChild(rootXml.createTextNode(PluginsSettings.getValue(property)));

			pluginsProperties.appendChild(pluginProperty);
		}

		return pluginsProperties;
	}

	private Element buildResponseOutputFormatXml(Document rootXml)
	{
		Element responseOutputFormat = rootXml.createElement("responseOutputFormat");

		for(String outputFormat : project.getResponsesFormat())
		{
			Element format = rootXml.createElement("format");
			format.appendChild(rootXml.createTextNode(outputFormat));
			responseOutputFormat.appendChild(format);
		}

		return responseOutputFormat;
	}

	private Element buildUserDefinedPropertiesXml(Document rootXml)
	{
		Element userDefinedProperties = rootXml.createElement("userDefinedProperties");

		for(String outputFormat : project.getUserRoutesPropertiesNames())
		{
			Project.UserDefinedRouteProperty userDefinedRouteProperty = project.getUserRouteProperty(outputFormat);

			Element property = rootXml.createElement("property");

			property.setAttribute("name", outputFormat);
			property.setAttribute("disallow", String.valueOf(userDefinedRouteProperty.isNewValuesDisabled()));
			property.setAttribute("memorize", String.valueOf(userDefinedRouteProperty.isValuesMemorized()));

			for(String valueName : userDefinedRouteProperty.getValues())
			{
				Element value = rootXml.createElement("value");
				value.appendChild(rootXml.createTextNode(valueName));

				if(valueName.equals(userDefinedRouteProperty.getDefaultValue()))
				{
					value.setAttribute("default", "true");
				}
				property.appendChild(value);
			}

			userDefinedProperties.appendChild(property);
		}

		return userDefinedProperties;
	}

	private void buildRouteXml(Document rootXml, Route route, Element routeXml)
	{
		Element description = rootXml.createElement("description");
		description.appendChild(rootXml.createTextNode(encodeNewLine(route.getDescription())));
		routeXml.appendChild(description);

		//generate http methods
		String[] httpMethodsNames = route.getHttpMethodNames();
		if(httpMethodsNames.length != 0)
		{
			Element methods = rootXml.createElement("methods");
			for(String methodName : httpMethodsNames)
			{
				HttpMethod httpMethod = route.getHttpMethod(methodName);
				Element newHttpMethod = rootXml.createElement("method");

				//generate method description
				newHttpMethod.setAttribute("name", methodName);
				Element methodDescription = rootXml.createElement("description");
				methodDescription.appendChild(rootXml.createTextNode(encodeNewLine(httpMethod.getDescription())));
				newHttpMethod.appendChild(methodDescription);

				//generate parameters
				String[] paramsNames = httpMethod.getParametersNames();
				if(paramsNames.length != 0)
				{
					Element parameters = rootXml.createElement("parameters");
					for(String paramName : paramsNames)
					{
						Parameter parameter = httpMethod.getParameter(paramName);
						Element param = rootXml.createElement("parameter");
						param.setAttribute("name", paramName);
						param.setAttribute("required", String.valueOf(parameter.isRequired()));
						param.setAttribute("description", parameter.getDescription());
						parameters.appendChild(param);
					}
					newHttpMethod.appendChild(parameters);
				}

				//generate response
				String[] responsesNames = httpMethod.getResponsesNames();
				if(responsesNames.length != 0)
				{
					Element responses = rootXml.createElement("responses");
					for(String responseName : responsesNames)
					{
						Response resp = httpMethod.getResponse(responseName);
						Element response = rootXml.createElement("response");
						response.setAttribute("name", responseName);
						response.setAttribute("outputFormat", resp.getOutputType());

						Element desc = rootXml.createElement("description");
						desc.appendChild(rootXml.createTextNode(encodeNewLine(resp.getDescription())));
						response.appendChild(desc);

						Element outputSchema = rootXml.createElement("outputSchema");
						outputSchema.appendChild(rootXml.createTextNode(encodeNewLine(resp.getSchema())));
						response.appendChild(outputSchema);

						responses.appendChild(response);
					}
					newHttpMethod.appendChild(responses);
				}

				//generate userProp values
				String[] userDefPropNames = Project.getActiveProject().getUserRoutesPropertiesNames();
				if(userDefPropNames.length != 0)
				{
					Element userDefVal = rootXml.createElement("userDefinedProperties");
					for(String propName : userDefPropNames)
					{
						Element value = rootXml.createElement("value");
						value.setAttribute("property", propName);
						value.appendChild(rootXml.createTextNode(httpMethod.getUserPropertyValue(propName)));

						userDefVal.appendChild(value);
					}

					newHttpMethod.appendChild(userDefVal);
				}

				methods.appendChild(newHttpMethod);
			}
			routeXml.appendChild(methods);
		}

		//generate sub routes
		String[] routesNames = route.getRoutesNames();
		if(routesNames.length != 0)
		{
			Element routes = rootXml.createElement("routes");
			for(String routeName : routesNames)
			{
				Element newRouteXml = rootXml.createElement("route");
				newRouteXml.setAttribute("name", routeName);
				buildRouteXml(rootXml, route.getRoute(routeName), newRouteXml);
				routes.appendChild(newRouteXml);
			}
			routeXml.appendChild(routes);
		}
	}
}