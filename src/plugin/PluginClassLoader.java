package plugin;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Plugin loader.
 * @author joseph
 */
public class PluginClassLoader extends URLClassLoader
{
	private static PluginClassLoader instance = new PluginClassLoader();

	/**
	 * Gets instance.
	 * @return the instance
	 */
	public static PluginClassLoader getInstance()
	{
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	private PluginClassLoader()
	{
		super(new URL[0], ClassLoader.getSystemClassLoader());
	}

	/**
	 * Appends the specified URL to the list of URLs to search for classes and resources.
	 * If the URL specified is null or is already in the list of URLs, or if this loader is closed, then invoking this method has no effect.
	 * @param url he URL to be added to the search path of URLs
	 */
	public void addURL(URL url)
	{
		super.addURL(url);
	}
}