package plugin.exporter;

import model.*;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import plugin.importer.DiderotProjectImporter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by joseph on 09/10/16.
 */
public class DefaultDiderotProjectImporter extends DefaultHandler implements DiderotProjectImporter
{
	private Route rootRoute;
	private Project project;
	private static enum Section {DIDEROT_PROJECT, ROUTES};
	private Section section = Section.DIDEROT_PROJECT;

	static private HashMap<String, String> availableOperations = new HashMap<>();

	static
	{
		availableOperations.put("importProject", "Open project");
	}

	@Override
	public HashMap<String, String> getAvailableImportingOperations()
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

	public void importProject()
	{
		try
		{
			String str = "titit\n\ttotot\n\tiopo";
			System.out.println(str.replaceAll("\\t",""));
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Element diderotProject = documentBuilder.parse("testOpen.xml").getDocumentElement();
			loadProject(diderotProject);
			loadResponsesOutputFormat(diderotProject);
			loadUserDefinedProperties(diderotProject);
			loadRoutes(diderotProject.getElementsByTagName("route").item(0));
		}
		catch(SAXException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(ParserConfigurationException e)
		{
			e.printStackTrace();
		}
	}

	private void loadProject(Element projectElement)
	{
		project.setName(projectElement.getAttribute("name"));
		project.setCompany(projectElement.getAttribute("company"));
		project.setAuthors(projectElement.getAttribute("authors"));
		project.setContact(projectElement.getAttribute("contact"));
		project.setDescription(projectElement.getElementsByTagName("description").item(0).getFirstChild().getTextContent());
	}

	private void loadResponsesOutputFormat(Element projectElement)
	{
		//TODO fix typo in: responseOutputFormat
		Node responsesOutputFormat = projectElement.getElementsByTagName("responseOutputFormat").item(0);
		NodeList nodeList = responsesOutputFormat.getChildNodes();

		for(int i = 0; i < nodeList.getLength(); i++)
		{
			if(nodeList.item(i).getNodeType() == Node.TEXT_NODE)
			{
				continue;
			}
			project.addResponseFormat(nodeList.item(i).getTextContent());
		}
	}

	private void loadUserDefinedProperties(Element projectElement)
	{
		Node userDefinedProperties = projectElement.getElementsByTagName("userDefinedProperties").item(0);
		NodeList nodeList = userDefinedProperties.getChildNodes();

		for(int i = 0; i < nodeList.getLength(); i++)
		{
			if(nodeList.item(i).getNodeType() == Node.TEXT_NODE)
			{
				continue;
			}

			NamedNodeMap propAtr = nodeList.item(i).getAttributes();
			Vector<String> values = new Vector<>();
			String defaultVal = "";

			NodeList valuesList = nodeList.item(i).getChildNodes();
			for(int j = 0; j< valuesList.getLength(); j++)
			{
				Node value = valuesList.item(j);
				if(value.getNodeType() == Node.TEXT_NODE)
				{
					continue;
				}

				String valueText = value.getTextContent();
				if(value.getAttributes().getNamedItem("default") != null)
				{
					defaultVal = valueText;
				}
				values.add(valueText);
			}

			project.addUserRouteProperty(propAtr.getNamedItem("name").getTextContent(), defaultVal);
			Project.UserDefinedRouteProperty userDefinedRouteProperty = project.getUserRouteProperty(propAtr.getNamedItem("name").getTextContent());
			userDefinedRouteProperty.setValuesMemorized(Boolean.parseBoolean(propAtr.getNamedItem("memorize").getTextContent()));
			userDefinedRouteProperty.setNewValuesDisabled((Boolean.parseBoolean(propAtr.getNamedItem("disallow").getTextContent())));
		}
	}

	private void loadRoutes(Node routeNode)
	{
		String routeName = routeNode.getAttributes().getNamedItem("name").getTextContent();
		rootRoute.setName(routeName);
		loadRoutes(routeNode, rootRoute);
	}

	private void loadRoutes(Node routeNode, Route route)
	{
		NodeList routeChildren = routeNode.getChildNodes();
		for(int i = 0; i < routeChildren.getLength(); i++)
		{
			if(routeChildren.item(i).getNodeType() == Node.TEXT_NODE)
			{
				continue;
			}

			String nodeName = routeChildren.item(i).getNodeName();
			if(nodeName.equals("description"))
			{
				route.setDescription(routeChildren.item(i).getTextContent());
			}
			else if(nodeName.equals("methods"))
			{
				NodeList methods = routeChildren.item(i).getChildNodes();
				for(int j = 0; j < methods.getLength(); j++)
				{
					if(methods.item(j).getNodeType() == Node.TEXT_NODE)
					{
						continue;
					}

					String name = methods.item(j).getAttributes().getNamedItem("name").getTextContent();
					HttpMethod newHttpMethod = new HttpMethod();
					route.addHttpMethod(name, newHttpMethod);

					NodeList methodsChildren = methods.item(j).getChildNodes();
					for(int k = 0; k < methodsChildren.getLength(); k++)
					{
						if(methodsChildren.item(k).getNodeType() == Node.TEXT_NODE)
						{
							continue;
						}

						nodeName = methodsChildren.item(k).getNodeName();
						if(nodeName.equals("parameters"))
						{
							NodeList params = methodsChildren.item(k).getChildNodes();
							for(int l = 0; l < params.getLength(); l++)
							{
								if(params.item(l).getNodeType() == Node.TEXT_NODE)
								{
									continue;
								}

								NamedNodeMap attributes = params.item(l).getAttributes();
								Boolean isRequired = Boolean.valueOf(attributes.getNamedItem("required").getTextContent());
								Parameter newParam = new Parameter(isRequired, attributes.getNamedItem("description").getTextContent());
								newHttpMethod.addParameter(attributes.getNamedItem("name").getTextContent(), newParam);
							}
						}
						else if(nodeName.equals("responses"))
						{
							NodeList responses = methodsChildren.item(k).getChildNodes();
							for(int l = 0; l < responses.getLength(); l++)
							{
								if(responses.item(l).getNodeType() == Node.TEXT_NODE)
								{
									continue;
								}

								nodeName = responses.item(k).getNodeName();
								for(int m = 0; m < responses.getLength(); m++)
								{
									if(responses.item(m).getNodeType() == Node.TEXT_NODE)
									{
										continue;
									}

									Response response = new Response();
									NamedNodeMap attributes = responses.item(m).getAttributes();
									response.setOutputType(attributes.getNamedItem("outputFormat").getTextContent());


									NodeList responseChildren = responses.item(m).getChildNodes();
									for(int n = 0; n < responseChildren.getLength(); n++)
									{
										if(responseChildren.item(n).getNodeType() == Node.TEXT_NODE)
										{
											continue;
										}

										nodeName = responseChildren.item(n).getNodeName();
										if(nodeName.equals("description"))
										{
											response.setDescription(responseChildren.item(n).getTextContent());
										}
										else if(nodeName.equals("outputSchema"))
										{
											response.setSchema(responseChildren.item(n).getTextContent());
										}
									}
									newHttpMethod.addResponse(attributes.getNamedItem("name").getTextContent(), response);
								}
							}
						}
					}
				}
			}
			else if(nodeName.equals("routes"))
			{
				NodeList childRoutes = routeChildren.item(i).getChildNodes();
				for(int j = 0; j < childRoutes.getLength(); j++)
				{
					if(childRoutes.item(j).getNodeType() == Node.TEXT_NODE)
					{
						continue;
					}

					String name = childRoutes.item(j).getAttributes().getNamedItem("name").getTextContent();
					Route newRoute = new Route(name);
					route.addRoute(name, newRoute);
					loadRoutes(childRoutes.item(j), newRoute);
				}
			}
		}
	}
}