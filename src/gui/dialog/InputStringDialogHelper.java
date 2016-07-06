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
			return str.replaceAll("\\s","");
		}
		return null;
	}
}