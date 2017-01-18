package plugin.importer;

import plugin.DiderotPlugin;
import plugin.OperationNameIcon;

import java.util.HashMap;

/**
 * The interface Diderot project importer, use to import project.
 * @author joseph
 */
public interface DiderotProjectImporter extends DiderotPlugin
{
	/**
	 * Decode new line string, replacing "&#xA;" by "\n".
	 * @param str the string to decode
	 * @return the string decoded
	 */
	static public String decodeNewLine(String str)
	{
		return str.replace("&#xA;", "\n");
	}

	/**
	 * Gets available importing operations.
	 * @return the available importing operations
	 */
	public HashMap<String, OperationNameIcon> getAvailableImportingOperations();
}