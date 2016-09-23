package model;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Created by joseph on 13/05/16.
 */
public class HttpMethod extends AbstractTableModel
{
	public final static String[] columnsNames = {"Name", "Required", "Description"};
	private String description;

	private TreeMap<String, Parameter> parameters;
	private TreeMap<String, Response> responses;
	private TreeMap<String, String> userDefinedPropertiesValues;


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
		createSampleData();
	}

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
		responses.put("300", r);
		r = new Response();
		r.setDescription("page not found");
		r.setOutputType("XML");
		r.setSchema("<error>\n\tNever gonna give you up\n</error>");
		responses.put("404", r);
		responses.put("500", r);
		responses.put("506", r);
		responses.put("506", r);
		responses.put("507", r);
		responses.put("508", r);
		responses.put("509", r);
		responses.put("510", r);
		responses.put("511", r);
		responses.put("512", r);
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

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
	public String getUserPropertyValue(String name)
	{
		return userDefinedPropertiesValues.get(name);
	}

	public void setUserProperty(String name, String value)
	{
		userDefinedPropertiesValues.put(name, value);
	}

	public void removeUserProperty(String name)
	{
		userDefinedPropertiesValues.remove(name);
	}

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
	public boolean addResponse(String name)
	{
		if(responses.containsKey(name))
		{
			return false;
		}

		responses.put(name, new Response());
		return true;
	}

	public void delResponse(String name)
	{
		responses.remove(name);
	}

	public Response getResponse(String name)
	{
		return responses.get(name);
	}

	public String[] getResponsesNames()
	{
		return responses.keySet().toArray(new String[responses.size()]);
	}

	//parameters management
	public boolean addParameter(String name)
	{
		if(parameters.containsKey(name))
		{
			return false;
		}
		parameters.put(name, new Parameter());

		Object[] keys = parameters.keySet().toArray();

		int i = 0;
		while(!name.equals(keys[i]))
		{
			i++;
		}

		fireTableRowsInserted(i, i);
		return true;
	}

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