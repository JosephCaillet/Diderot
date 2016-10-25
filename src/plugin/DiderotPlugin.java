package plugin;

import model.Project;
import model.Route;

import javax.swing.*;

/**
 * Created by joseph on 04/10/16.
 */
public interface DiderotPlugin
{
	public String getPluginName();
	public String getPluginAuthor();
	public String getPluginContactInformation();
	public String getPluginVersion();
	public String getPluginDescription();

	public default void openConfigDialog()
	{
		//nothing done by default
	}
	public default boolean isConfigurable()
	{
		return false;
	}

	public void setDiderotData(Route rootRoute, Project project);
	public default void setParentFrame(JFrame parent)
	{
		//nothing done by default
	}
}