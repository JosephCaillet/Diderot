package plugin.exporter;

import plugin.DiderotPlugin;
import plugin.OperationNameIcon;

import java.util.HashMap;

/**
 * The interface Diderot project exporter, use to export project.
 */
public interface DiderotProjectExporter extends DiderotPlugin
{
	/**
	 * Encode new line string, replacing "&#xA;" by "\n".
	 * @param str the string to encode
	 * @return the decoded string
	 */
	static public String encodeNewLine(String str)
	{
		return str.replace("\n", "&#xA;");
	}

	/**
	 * Gets available exporting operations.
	 * @return the available exporting operations
	 */
	//Todo: add icon per action support
	public HashMap<String, OperationNameIcon> getAvailableExportingOperations();
}