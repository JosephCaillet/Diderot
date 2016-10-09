package plugin;

import model.Project;
import model.Route;

/**
 * Created by joseph on 04/10/16.
 */
public interface DiderotPlugin
{
	public String getPluginName();
	public String getPluginAuthor();
	public String getPluginContactInformation();
	public String getPluginVersion();
	//TODO add getDescriptionMethod

	public default void openConfigDialog()
	{
		//nothing done by default
	}
	public default boolean isConfigurable()
	{
		return false;
	}

	public void setDiderotData(Route rootRoute, Project project);
}