package plugin.exporter;

import plugin.DiderotPlugin;
import plugin.OperationNameIcon;

import java.util.HashMap;

/**
 * Created by joseph on 04/10/16.
 */
public interface DiderotProjectExporter extends DiderotPlugin
{
	static public String encodeNewLine(String str)
	{
		return str.replace("\n", "&#xA;");
	}

	//Todo: add icon per action support
	public HashMap<String, OperationNameIcon> getAvailableExportingOperations();
}