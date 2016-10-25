package plugin.editor;

import plugin.DiderotPlugin;

import java.util.HashMap;

/**
 * Created by joseph on 04/10/16.
 */
public interface DiderotProjectEditor extends DiderotPlugin
{
	//Todo: add icon per action support
	public default boolean callConfigBeforeEdit()
	{
		return false;
	}
	public HashMap<String, String> getAvailableEditingOperations();
}