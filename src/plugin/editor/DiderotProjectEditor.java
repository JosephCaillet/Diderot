package plugin.editor;

import plugin.DiderotPlugin;
import plugin.OperationNameIcon;

import java.util.HashMap;

/**
 * The interface Diderot project editor, use to edit project.
 * @author joseph
 */
public interface DiderotProjectEditor extends DiderotPlugin
{
	/**
	 * Gets available editing operations.
	 * @return the available editing operations
	 */
	public HashMap<String, OperationNameIcon> getAvailableEditingOperations();
}