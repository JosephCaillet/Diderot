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
	 * {@inheritDoc}
	 * @param url {@inheritDoc}
	 */
	public void addURL(URL url)
	{
		super.addURL(url);
	}
}