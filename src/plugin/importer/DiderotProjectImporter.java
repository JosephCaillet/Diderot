package plugin.importer;

import plugin.DiderotPlugin;

import java.util.HashMap;

/**
 * Created by joseph on 04/10/16.
 */
public interface DiderotProjectImporter extends DiderotPlugin
{
	static public String decodeNewLine(String str)
	{
		return str.replace("&#xA;", "\n");
	}

	//Todo: add icon per action support
	public HashMap<String, String> getAvailableImportingOperations();
	public default boolean callConfigBeforeImport()
	{
		return false;
	}
}