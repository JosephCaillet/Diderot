package plugin;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by joseph on 17/11/16.
 */
public class PluginClassLoader extends URLClassLoader
{
	private static PluginClassLoader instance = new PluginClassLoader();

	public static PluginClassLoader getInstance()
	{
		return instance;
	}

	private PluginClassLoader()
	{
		super(new URL[0], ClassLoader.getSystemClassLoader());
	}

	public void addURL(URL url)
	{
		super.addURL(url);
	}
}