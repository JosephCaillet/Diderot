package model;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by joseph on 15/07/16.
 */
public class Project
{
	private static Project activeProject = new Project();

	private TreeMap<String, UserDefinedRouteProperty> userDefinedRouteProperties;
	private TreeSet<String> responseFormatList = new TreeSet<>();
	private String defaultResponseFormat;

	private String name = "Test Project";
	private String company = "A.C.M.E";
	private String description = "test description";
	private String domain = "domain.com";
	private String authors = "jojo";
	private String contact = "contact@acme.wb";

	public static Project getActiveProject()
	{
		return activeProject;
	}


	public Project()
	{
		userDefinedRouteProperties = new TreeMap<String, UserDefinedRouteProperty>();
		responseFormatList.add("HTML");
		responseFormatList.add("JSON");
		responseFormatList.add("XML");
		responseFormatList.add("CSS");
		responseFormatList.add("CSV");
		responseFormatList.add("Plain text");
		defaultResponseFormat = "JSON";
	}

	//project properties

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCompany()
	{
		return company;
	}

	public void setCompany(String company)
	{
		this.company = company;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDomain()
	{
		return domain;
	}

	public void setDomain(String domain)
	{
		this.domain = domain;
	}

	public String getAuthors()
	{
		return authors;
	}

	public void setAuthors(String authors)
	{
		this.authors = authors;
	}

	public String getContact()
	{
		return contact;
	}

	public void setContact(String contact)
	{
		this.contact = contact;
	}

	//user defined properties management
	public boolean addUserRouteProperty(String name, String defaultValue)
	{
		if(userDefinedRouteProperties.containsKey(name))
		{
			return false;
		}
		userDefinedRouteProperties.put(name, new UserDefinedRouteProperty(defaultValue));
		return true;
	}

	public UserDefinedRouteProperty getUserRouteProperty(String name)
	{
		return userDefinedRouteProperties.get(name);
	}

	public UserDefinedRouteProperty removeUserRouteProperty(String name)
	{
		return userDefinedRouteProperties.remove(name);
	}

	public String[] getUserRoutesPropertiesNames()
	{
		Object[] valuesList =  userDefinedRouteProperties.keySet().toArray();
		return Arrays.copyOf(valuesList, valuesList.length, String[].class);
	}

	//response format management
	public boolean addResponseFormat(String responseFormat)
	{
		return responseFormatList.add(responseFormat);
	}

	public boolean removeResponseFormat(String responseFormat)
	{
		return responseFormatList.remove(responseFormat);
	}

	public boolean renameResponseFormat(String oldName, String newName)
	{
		if(addResponseFormat(newName))
		{
			if(removeResponseFormat(oldName))
			{
				return true;
			}
			else
			{
				removeResponseFormat(newName);
				return false;
			}
		}
		return false;
	}

	public String getDefaultResponseFormat()
	{
		return defaultResponseFormat;
	}

	public void setDefaultResponseFormat(String newDefaultResponseFormat)
	{
		if(!responseFormatList.contains(newDefaultResponseFormat))
		{
			responseFormatList.add(newDefaultResponseFormat);
		}

		defaultResponseFormat = newDefaultResponseFormat;
	}

	public String[] getResponsesFormat()
	{
		return responseFormatList.toArray(new String[responseFormatList.size()]);
	}


	public class UserDefinedRouteProperty extends TreeSet<String>
	{
		private boolean newValuesDisabled = false;
		private boolean valuesMemorized = true;
		private String defaultValue;

		public UserDefinedRouteProperty(String defaultValue)
		{
			super();
			this.defaultValue = defaultValue;
			add(defaultValue);
			add("titi");
			add("Toto");
			add("tutu");
			add("Zozo");
			add("zaza");
		}

		public boolean isNewValuesDisabled()
		{
			return newValuesDisabled;
		}

		public void setNewValuesDisabled(boolean newValuesDisabled)
		{
			this.newValuesDisabled = newValuesDisabled;
		}

		public boolean isValuesMemorized()
		{
			return valuesMemorized;
		}

		public void setValuesMemorized(boolean valuesMemorized)
		{
			this.valuesMemorized = valuesMemorized;
		}

		public String getDefaultValue()
		{
			return defaultValue;
		}

		public void setDefaultValue(String defaultValue)
		{
			this.defaultValue = defaultValue;
		}

		public String[] getValues()
		{
			Object[] valuesList =  toArray();
			return Arrays.copyOf(valuesList, valuesList.length, String[].class);
		}
	}
}