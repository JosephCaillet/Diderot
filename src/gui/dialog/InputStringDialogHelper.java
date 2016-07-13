package gui.dialog;


import javax.swing.*;
import java.awt.*;

/**
 * Created by joseph on 14/05/16.
 */
public class InputStringDialogHelper
{
	public static String showInputNoSpacesDialog(Component parentComponent, String message, String title, int messageType, String defaultInput)
	{
		String str = (String) JOptionPane.showInputDialog(parentComponent, message, title, messageType, null, null, defaultInput);
		if(str != null)
		{
			if(str.endsWith("/"))
			{
				str = str.substring(0, str.length() - 1);
			}

			if(str.isEmpty())
			{
				JOptionPane.showMessageDialog(parentComponent, "Your input should not be empty.", "Empty input", JOptionPane.WARNING_MESSAGE);
				return showInputNoSpacesDialog(parentComponent, message, title, messageType, defaultInput);
			}

			return str.replaceAll("\\s","");
		}
		return null;
	}

	public static String showInputNoSpacesDialog(Component parentComponent, String message, String title, int messageType)
	{
		return showInputNoSpacesDialog(parentComponent, message, title, messageType, "");
	}

}