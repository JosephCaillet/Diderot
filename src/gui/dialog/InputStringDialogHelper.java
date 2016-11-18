package gui.dialog;


import javax.swing.*;
import java.awt.*;

/**
 * No empty string input dialog.
 * @author joseph
 */
public class InputStringDialogHelper
{
	/**
	 * Displays an input string dialog, input string will not be empty.
	 * @param parentComponent Parent component
	 * @param message  Text to display above input field
	 * @param title Title
	 * @param messageType Message type : @see JOptionPane
	 * @param defaultInput Default input text
	 * @return a non empty string, or null if cancel button hit
	 */
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

	/**
	 * Displays an input string dialog, input string will not be empty.
	 * @param parentComponent Parent component
	 * @param message  Text to display above input field
	 * @param title Title
	 * @param messageType Message type : @see JOptionPane
	 * @return a non empty string, or null if cancel button hit
	 */
	public static String showInputNoSpacesDialog(Component parentComponent, String message, String title, int messageType)
	{
		return showInputNoSpacesDialog(parentComponent, message, title, messageType, "");
	}
}