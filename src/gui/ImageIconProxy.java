package gui;

import javax.swing.*;
import java.util.HashMap;

/**
 * Icon manager, loads some icons in ./rsc folder.
 * @author joseph
 */
public class ImageIconProxy
{
	private static HashMap<String, ImageIcon> imageIconHashMap = new HashMap<String, ImageIcon>();

	static
	{
		imageIconHashMap.put("add", new ImageIcon("rsc/plus.png"));
		imageIconHashMap.put("del", new ImageIcon("rsc/del.png"));
		imageIconHashMap.put("edit", new ImageIcon("rsc/edit.png"));
		imageIconHashMap.put("conf", new ImageIcon("rsc/conf.png"));
		imageIconHashMap.put("exit", new ImageIcon("rsc/exit.png"));
		imageIconHashMap.put("sort", new ImageIcon("rsc/sort.png"));
		imageIconHashMap.put("editwide", new ImageIcon("rsc/editwide.png"));

		imageIconHashMap.put("new", new ImageIcon("rsc/new.png"));
		imageIconHashMap.put("save", new ImageIcon("rsc/save.png"));
		imageIconHashMap.put("saveas", new ImageIcon("rsc/saveas.png"));
		imageIconHashMap.put("exportweb", new ImageIcon("rsc/exportweb.png"));
		imageIconHashMap.put("open", new ImageIcon("rsc/open.png"));

		imageIconHashMap.put("conf2", new ImageIcon("rsc/conf2.png"));
		imageIconHashMap.put("info", new ImageIcon("rsc/info.png"));
		imageIconHashMap.put("help", new ImageIcon("rsc/help.png"));

		imageIconHashMap.put("icon", new ImageIcon("rsc/icon.png"));
	}

	/**
	 * Get an icon.
	 * @param iconName icon name's.
	 * @return The requested icon.
	 */
	public static ImageIcon getIcon(String iconName)
	{
		if(imageIconHashMap.containsKey(iconName))
		{
			return  imageIconHashMap.get(iconName);
		}

		ImageIcon imageIcon = new ImageIcon(iconName);
		imageIconHashMap.put(iconName, imageIcon);
		return imageIcon;
	}
}
