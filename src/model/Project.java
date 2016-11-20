package model;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * A Diderot project
 * @author joseph
 */
public class Project
{
	private static Project activeProject = new Project();

	private TreeMap<String, UserDefinedRouteProperty> userDefinedRouteProperties;
	private TreeSet<String> responseFormatList = new TreeSet<>();
	private String defaultResponseFormat;

	private boolean openedStatus = false;

	private String name = "";
	private String company = "";
	private String description = "";
	private String domain = "";
	private String authors = "";
	private String contact = "";

	/**
	 * Get singleton instance.
	 * Should not be use by plugins.
	 * @return current diderot project
	 */
	public static Project getActiveProject()
	{
		return activeProject;
	}

	/**
	 * Creates project
	 */
	private Project()
	{
		userDefinedRouteProperties = new TreeMap<String, UserDefinedRouteProperty>();
	}

	/**
	 * Clears the project, erasing every data.
	 * Should be called by every import plugins.
	 */
	public void clear()
	{
		userDefinedRouteProperties.clear();
		responseFormatList.clear();
		defaultResponseFormat = "";

		name = "";
		company = "";
		description = "";
		domain = "";
		authors = "";
		contact = "";

		openedStatus = false;
	}

	//project properties

	/**
	 * Get project opening status.
	 * @return project opening status
	 */
	public boolean isOpened()
	{
		return openedStatus;
	}

	/**
	 * Set project opening status.
	 * Should be call by every import plugins.
	 * @param openedStatus true if import successful, false otherwise.
	 */
	public void setOpenedStatus(boolean openedStatus)
	{
		this.openedStatus = openedStatus;
	}

	/**
	 * Get project name.
	 * @return project name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Set project name.
	 * @param name new name for the project
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Get company name.
	 * @return company name
	 */
	public String getCompany()
	{
		return company;
	}

	/**
	 * Set company name.
	 * @param company new company name
	 */
	public void setCompany(String company)
	{
		this.company = company;
	}

	/**
	 * Get project description.
	 * @return project description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Set project description.
	 * @param description new project description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * Get project domain/root name.
	 * For instance, in "foo.com/bar/something", "foo.com" is the domain.
	 * @return project domain
	 */
	public String getDomain()
	{
		return domain;
	}

	/**
	 * Set project domain/root name.
	 * @param domain
	 */
	public void setDomain(String domain)
	{
		this.domain = domain;
	}

	/**
	 * Get project authors.
	 * @return project authors
	 */
	public String getAuthors()
	{
		return authors;
	}

	/**
	 * Set project authors.
	 * @param authors project authors
	 */
	public void setAuthors(String authors)
	{
		this.authors = authors;
	}

	/**
	 * Get contact information.
	 * @return contact information
	 */
	public String getContact()
	{
		return contact;
	}

	/**
	 * Set contact information.
	 * @param contact contact information
	 */
	public void setContact(String contact)
	{
		this.contact = contact;
	}

	//user defined properties management

	/**
	 * Add a new user defined property.
	 * @param name property name
	 * @param defaultValue default value of the property
	 * @return false if property already exists, false otherwise
	 */
	public boolean addUserRouteProperty(String name, String defaultValue)
	{
		if(userDefinedRouteProperties.containsKey(name))
		{
			return false;
		}
		userDefinedRouteProperties.put(name, new UserDefinedRouteProperty(defaultValue));
		return true;
	}

	/**
	 * Get user property by name.
	 * @param name desired property name
	 * @return property, or null if does not exist.
	 */
	public UserDefinedRouteProperty getUserRouteProperty(String name)
	{
		return userDefinedRouteProperties.get(name);
	}

	/**
	 * Remove a user defined property.
	 * @param name name of the property to remove
	 * @return the deleted user property
	 */
	public UserDefinedRouteProperty removeUserRouteProperty(String name)
	{
		return userDefinedRouteProperties.remove(name);
	}

	/**
	 * Get all user properties names.
	 * @return all user properties names
	 */
	public String[] getUserRoutesPropertiesNames()
	{
		Object[] valuesList =  userDefinedRouteProperties.keySet().toArray();
		return Arrays.copyOf(valuesList, valuesList.length, String[].class);
	}

	//response format management

	/**
	 * Add a new response format.
	 * @param responseFormat new response format
	 * @return false if the given response format already exists, true otherwise.
	 */
	public boolean addResponseFormat(String responseFormat)
	{
		return responseFormatList.add(responseFormat);
	}

	/**
	 * Remove the given response format.
	 * @param responseFormat name of the response format to delete
	 * @return false if the given response format does not exist, true otherwise.
	 */
	public boolean removeResponseFormat(String responseFormat)
	{
		return responseFormatList.remove(responseFormat);
	}

	/**
	 * Remove the given response format.
	 * @param oldName name of the response format to rename
	 * @param newName new name for the given response format
	 * @return false if new name already exists, or if the old name does not exist. True otherwise.
	 */
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

	/**
	 * Get the default response format.
	 * @return default response format
	 */
	public String getDefaultResponseFormat()
	{
		return defaultResponseFormat;
	}

	/**
	 * Set the default response format.
	 * @param newDefaultResponseFormat new default response format
	 */
	public void setDefaultResponseFormat(String newDefaultResponseFormat)
	{
		if(!responseFormatList.contains(newDefaultResponseFormat))
		{
			responseFormatList.add(newDefaultResponseFormat);
		}

		defaultResponseFormat = newDefaultResponseFormat;
	}

	/**
	 * Get all responses formats.
	 * @return all responses formats
	 */
	public String[] getResponsesFormat()
	{
		return responseFormatList.toArray(new String[responseFormatList.size()]);
	}

	/**
	 * User defined property.
	 * @author joseph
	 */
	public class UserDefinedRouteProperty extends TreeSet<String>
	{
		private boolean newValuesDisabled = false;
		private boolean valuesMemorized = true;
		private String defaultValue;

		/**
		 * Create a new user property.
		 * @param defaultValue default value
		 */
		public UserDefinedRouteProperty(String defaultValue)
		{
			super();
			this.defaultValue = defaultValue;
			add(defaultValue);
			//createSampleData();
		}

		/**
		 * Creates sample data.
		 */
		private void createSampleData()
		{
			add("titi");
			add("Toto");
			add("tutu");
			add("Zozo");
			add("zaza");
		}

		/**
		 * Get if registering new values is disabled.
		 * @return true if registering new values is disabled
		 */
		public boolean isNewValuesDisabled()
		{
			return newValuesDisabled;
		}

		/**
		 * Set if registering new values is disabled.
		 * @param newValuesDisabled registering new values disabled status
		 */
		public void setNewValuesDisabled(boolean newValuesDisabled)
		{
			this.newValuesDisabled = newValuesDisabled;
		}

		/**
		 * Get if new values are memorized.
		 * @return true if new values should be memorized
		 */
		public boolean isValuesMemorized()
		{
			return valuesMemorized;
		}

		/**
		 * Set if new values should be memorized.
		 * @param valuesMemorized true if new values should be memorized
		 */
		public void setValuesMemorized(boolean valuesMemorized)
		{
			this.valuesMemorized = valuesMemorized;
		}

		/**
		 * Get the default value for this property.
		 * @return the default value for this property
		 */
		public String getDefaultValue()
		{
			return defaultValue;
		}

		/**
		 * Set the default value for this property.
		 * @param defaultValue the default value for this property
		 */
		public void setDefaultValue(String defaultValue)
		{
			this.defaultValue = defaultValue;
		}

		/**
		 * Get all memorized/allowed values for this property.
		 * @return all memorized/allowed values for this property
		 */
		public String[] getValues()
		{
			Object[] valuesList =  toArray();
			return Arrays.copyOf(valuesList, valuesList.length, String[].class);
		}
	}
}