package plugin.importer;

import plugin.DiderotPlugin;

import java.util.HashMap;

/**
 * Created by joseph on 04/10/16.
 */
public interface DiderotProjectImporter extends DiderotPlugin
{
	public HashMap<String, String> getAvailableImportingOperations();
	public default boolean callConfigBeforeImport()
	{
		return false;
	}
}