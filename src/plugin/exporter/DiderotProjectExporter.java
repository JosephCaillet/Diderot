package plugin.exporter;

import plugin.DiderotPlugin;

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

	public HashMap<String, String> getAvailableExportingOperations();
	public default boolean callConfigBeforeExport()
	{
		return false;
	}
}