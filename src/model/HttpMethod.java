package model;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.TreeMap;

/**
 * Created by joseph on 13/05/16.
 */
public class HttpMethod extends AbstractTableModel
{
	public final static String[] columnsNames = {"Name", "Required", "Description"};
	private String description;

	private TreeMap<String, Parameter> parameters;
	private TreeMap<Integer, Response> responses;
	private TreeMap<String, String> userDefinedPropertiesValues;


	public HttpMethod()
	{
		parameters = new TreeMap<String, Parameter>();
		responses = new TreeMap<Integer, Response>();
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
		int i = parameters.size() + 1;

		do
		{
			name += i;
			i++;
		}while(parameters.containsKey(name));

		return name;
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