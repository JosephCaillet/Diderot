package plugin;

import gui.ImageIconProxy;

import javax.swing.*;

/**
 * Container class, containing a plugin's method name, and an icon.
 * @author joseph
 */
public class OperationNameIcon
{
	public String methodName;
	public ImageIcon operationIcon;

	/**
	 * Creates a new Operation name icon.
	 * @param methodName    the method name
	 * @param operationIcon the operation icon
	 */
	public OperationNameIcon(String methodName, ImageIcon operationIcon)
	{
		this.methodName = methodName;
		this.operationIcon = operationIcon;
	}

	/**
	 * Creates a new Operation name icon.
	 * @param methodName the method name
	 * @param iconName   the icon name, as defined in ImageIconProxy
	 */
	public OperationNameIcon(String methodName, String iconName)
	{
		this(methodName, ImageIconProxy.getIcon(iconName));
	}

	/**
	 * Creates a new Operation name icon.
	 *
	 * @param methodName the method name
	 */
	public OperationNameIcon(String methodName)
	{
		this.methodName = methodName;
		this.operationIcon = null;
	}
}