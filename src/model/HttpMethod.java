package model;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.TreeMap;
import java.util.Vector;

/**
 * An http method, like GET, POST,...
 * @author joseph
 */
public class HttpMethod extends AbstractTableModel
{
	public final static String[] columnsNames = {"Name", "Required", "Description"};
	private String description = "";

	private TreeMap<String, Parameter> parameters;
	private TreeMap<String, Response> responses;
	private TreeMap<String, String> userDefinedPropertiesValues;

	/**
	 * Creates new http method.
	 */
	public HttpMethod()
	{
		parameters = new TreeMap<String, Parameter>();
		responses = new TreeMap<String, Response>();
		userDefinedPropertiesValues = new TreeMap<String, String>();

		Project project = Project.getActiveProject();
		for(String prop : project.getUserRoutesPropertiesNames())
		{
			setUserProperty(prop, project.getUserRouteProperty(prop).getDefaultValue());
		}
		//createSampleData();
	}

	/**
	 * Creates samples data.
	 */
	private void createSampleData()
	{
		Parameter toto = new Parameter();
		toto.setDescription("Gratis silvas ducunt ad ventus.");
		parameters.put("atoto", toto);
		Parameter tata = new Parameter();
		parameters.put("atata", tata);
		Parameter titi = new Parameter();
		titi.setRequired(false);
		titi.setDescription("Sunt bursaes visum fortis, castus byssuses.");
		parameters.put("atiti", titi);

		Response r = new Response();
		r.setDescription("Assimilatios sunt nutrixs de flavum clinias.");
		r.setOutputType("JSON");
		r.setSchema("{\n\tnum : 42,\n\tstring : \"The answer\"\n}");
		responses.put("200", r);
		r = new Response();
		r.setDescription("Cursus, stella, et humani generis.");
		r.setOutputType("CSS");
		r.setSchema("p\n{\n\tcolor : red;\n}");
		//responses.put("300", r);
		r = new Response();
		r.setDescription("page not found");
		r.setOutputType("XML");
		r.setSchema("<error>\n\tNever gonna give you up\n</error>");
		responses.put("404", r);
	}

	/**
	 * Get method description.
	 * @return Method description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Set method description
	 * @param description method description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * Get a name not already used by a parameter of the current http method.
	 * @return An unused parameter name
	 */
	public String getUniqueParameterName()
	{
		String name = "param";
		int i = parameters.size();

		do
		{
			i++;
		}while(parameters.containsKey(name + i));

		return name + i;
	}

	//User properties management

	/**
	 * Get value of the given user property.
	 * @param name user property name
	 * @return user property value
	 */
	public String getUserPropertyValue(String name)
	{
		return userDefinedPropertiesValues.get(name);
	}

	/**
	 * Set value of the given user property.
	 * @param name user property name
	 * @param value user property value
	 */
	public void setUserProperty(String name, String value)
	{
		userDefinedPropertiesValues.put(name, value);
	}

	/**
	 * Remove the given user property.
	 * @param name user property name
	 */
	public void removeUserProperty(String name)
	{
		userDefinedPropertiesValues.remove(name);
	}

	/**
	 * For each user properties that does not allow new values, replace the current value (if not allowed) with the default value.
	 * @param propName user property name
	 */
	public void removeUnauthorizedValues(String propName)
	{
		Vector<String> allowedValues = new Vector<String>(Project.getActiveProject().getUserRouteProperty(propName).descendingSet());
		String val = userDefinedPropertiesValues.get(propName);
		if(!allowedValues.contains(val))
		{
			userDefinedPropertiesValues.put(propName, Project.getActiveProject().getUserRouteProperty(propName).getDefaultValue());
		}
	}

	//Response management

	/**
	 * Add new response.
	 * @param name response's name.
	 * @return false if route already exists, true if not.
	 */
	public boolean addResponse(String name)
	{
		return addResponse(name, new Response());
	}

	/**
	 * Add given response.
	 * @param name response's name.
	 * @param response response to add
	 * @return false if route already exists, true if not.
	 */
	public boolean addResponse(String name, Response response)
	{
		if(responses.containsKey(name))
		{
			return false;
		}

		responses.put(name, response);
		return true;
	}

	/**
	 * Delete given response.
	 * @param name response's name
	 */
	public void delResponse(String name)
	{
		responses.remove(name);
	}

	/**
	 * Get response by name.
	 * @param name response's name
	 * @return response, or null if does not exist.
	 */
	public Response getResponse(String name)
	{
		return responses.get(name);
	}

	/**
	 * Rename a response.
	 * @param oldName name of the response to rename
	 * @param newName new response's name
	 * @return true if success, false if old response does not exist, or if a response with the new name already exists.
	 */
	public boolean renameResponse(String oldName, String newName)
	{
		if(responses.containsKey(newName) || !responses.containsKey(oldName))
		{
			return false;
		}

		Response response = responses.get(oldName);
		responses.remove(oldName);
		responses.put(newName, response);
		return true;
	}

	/**
	 * Get all responses' names.
	 * @return all responses names
	 */
	public String[] getResponsesNames()
	{
		return responses.keySet().toArray(new String[responses.size()]);
	}

	//parameters management

	/**
	 * Get all parameters' names.
	 * @return all parameters' names
	 */
	public String[] getParametersNames()
	{
		return parameters.keySet().toArray(new String[parameters.size()]);
	}

	/**
	 * Get parameter by name.
	 * @param name parameter's name
	 * @return parameter, null if does not exists
	 */
	public Parameter getParameter(String name)
	{
		return parameters.get(name);
	}

	/**
	 * Add new parameter.
	 * @param name parameter's name
	 * @return false if parameter with the given name already exists, true otherwise
	 */
	public boolean addParameter(String name)
	{
		return addParameter(name, new Parameter());
	}

	/**
	 * Add given parameter.
	 * @param name parameter's name
	 * @param parameter parameter to add
	 * @return false if parameter with the given name already exists, true otherwise
	 */
	public boolean addParameter(String name, Parameter parameter)
	{
		if(parameters.containsKey(name))
		{
			return false;
		}
		parameters.put(name, parameter);

		Object[] keys = parameters.keySet().toArray();

		int i = 0;
		while(!name.equals(keys[i]))
		{
			i++;
		}

		fireTableRowsInserted(i, i);
		return true;
	}

	/**
	 * Remove a parameter.
	 * @param rowIndex index of the parameter's name, in a alphabetical sorted list of all parameters' names
	 */
	public void removeParameter(int rowIndex)
	{
		String key = (String) parameters.keySet().toArray()[rowIndex];
		parameters.remove(key);
		fireTableRowsDeleted(rowIndex, rowIndex);
	}

	//AbstractTreeModel
	@Override
	public int getRowCount()
	{
		return parameters.size();
	}

	@Override
	public int getColumnCount()
	{
		return columnsNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int colIndex)
	{
		if(colIndex == 0)
		{
			return parameters.keySet().toArray()[rowIndex];
		}

		Parameter parameter = parameters.get(parameters.keySet().toArray()[rowIndex]);
		switch(colIndex)
		{
			case 1:
				return parameter.isRequired();
			case 2:
				return parameter.getDescription();
			default:
				return 404;
		}
	}

	@Override
	public String getColumnName(int column)
	{
		return columnsNames[column];
	}

	@Override
	public Class getColumnClass(int columnIndex)
	{
		return getValueAt(0, columnIndex).getClass();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		String key = (String) parameters.keySet().toArray()[rowIndex];
		Parameter parameter = parameters.get(key);
		switch(columnIndex)
		{
			case 0:
				if(!parameters.containsKey(aValue))
				{
					parameters.remove(key);
					parameters.put((String) aValue, parameter);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "A parameter with this name already exists.", "Cannot rename parameter", JOptionPane.ERROR_MESSAGE);
				}
				break;
			case 1:
				parameter.setRequired((Boolean) aValue);
				break;
			case 2:
				parameter.setDescription((String) aValue);
				break;
			default:
				//do nothing
				break;
		}
	}
}