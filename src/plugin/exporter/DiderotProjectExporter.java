package plugin.exporter;

import plugin.DiderotPlugin;

import java.util.HashMap;

/**
 * Created by joseph on 04/10/16.
 */
public interface DiderotProjectExporter extends DiderotPlugin
{
	public HashMap<String, String> getAvailableExportingOperations();
}