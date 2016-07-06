package gui.dialog;

import com.sun.xml.internal.ws.api.ha.StickyFeature;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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