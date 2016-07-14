package model;

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


	public HttpMethod()
	{
		parameters = new TreeMap<String, Parameter>();
		responses = new TreeMap<Integer, Response>();
		createSampleData();
	}

	private void createSampleData()
	{
		Parameter toto = new Parameter();
		toto.setRequired(true);
		toto.setDescription("Gratis silvas ducunt ad ventus.");
		parameters.put("toto", toto);
		Parameter tata = new Parameter();
		parameters.put("tata", tata);
		Parameter titi = new Parameter();
		titi.setRequired(true);
		titi.setDescription("Sunt bursaes visum fortis, castus byssuses.");
		parameters.put("titi", titi);
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

		while(parameters.containsKey(name))
		{
			name += i;
			i++;
		}

		return name;
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