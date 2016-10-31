package plugin;

import gui.ImageIconProxy;

import javax.swing.*;

/**
 * Created by joseph on 31/10/16.
 */
public class OperationNameIcon
{
	public String methodName;
	public ImageIcon operationIcon;

	public OperationNameIcon(String methodName, ImageIcon operationIcon)
	{
		this.methodName = methodName;
		this.operationIcon = operationIcon;
	}

	public OperationNameIcon(String methodName, String iconName)
	{
		this(methodName, ImageIconProxy.getIcon(iconName));
	}

	public OperationNameIcon(String methodName)
	{
		this.methodName = methodName;
		this.operationIcon = null;
	}
}