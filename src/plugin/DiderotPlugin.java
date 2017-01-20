package plugin;

import model.Project;
import model.Route;

import javax.swing.*;

/**
 * Diderot base plugin interface.
 * @author joseph
 */
public interface DiderotPlugin
{
	/**
	 * Gets plugin name.
	 * @return the plugin name
	 */
	public String getPluginName();

	/**
	 * Gets plugin author.
	 * @return the plugin author
	 */
	public String getPluginAuthor();

	/**
	 * Gets plugin contact information.
	 * @return the plugin contact information
	 */
	public String getPluginContactInformation();

	/**
	 * Gets plugin version.
	 * @return the plugin version
	 */
	public String getPluginVersion();

	/**
	 * Gets plugin description.
	 * @return the plugin description
	 */
	public String getPluginDescription();

	/**
	 * Opens config dialog.
	 * This function will be call by Diderot if the user want to configure the plugin, from setting panel.
	 * Does nothing by default.
	 */
	public default void openConfigDialog()
	{
		//nothing done by default
	}

	/**
	 * Can the plugin be configured?
	 * @return false by default
	 */
	public default boolean isConfigurable()
	{
		return false;
	}

	/**
	 * Sets diderot data to work with.
	 * @param rootRoute the root route
	 * @param project   the project
	 */
	public void setDiderotData(Route rootRoute, Project project);

	/**
	 * Sets parent frame.
	 * Useful if you want to set a parent to your dialog boxes.
	 * Does nothing by default.
	 * @param parent the parent
	 */
	public default void setParentFrame(JFrame parent)
	{
		//nothing done by default
	}
}