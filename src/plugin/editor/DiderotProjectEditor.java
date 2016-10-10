package plugin.editor;

import plugin.DiderotPlugin;

import java.util.HashMap;

/**
 * Created by joseph on 04/10/16.
 */
public interface DiderotProjectEditor extends DiderotPlugin
{
	public HashMap<String, String> getAvailableEditingOperations();
	public default boolean callConfigBeforeEdit()
	{
		return false;
	}
}