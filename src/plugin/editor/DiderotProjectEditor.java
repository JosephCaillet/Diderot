package plugin.editor;

import plugin.DiderotPlugin;
import plugin.OperationNameIcon;

import java.util.HashMap;

/**
 * Created by joseph on 04/10/16.
 */
public interface DiderotProjectEditor extends DiderotPlugin
{
	//Todo: add icon per action support
	public HashMap<String, OperationNameIcon> getAvailableEditingOperations();
}