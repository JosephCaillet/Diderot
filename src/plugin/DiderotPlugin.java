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

	public void openConfigDialog();
	public boolean isConfigurable();

	public void setDiderotData(Route rootRoute, Project project);
}