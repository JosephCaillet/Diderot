package gui;

import javax.swing.*;
import java.util.HashMap;

/**
 * Created by joseph on 06/07/16.
 */
public class ImageIconProxy
{
	private static HashMap<String, ImageIcon> imageIconHashMap = new HashMap<String, ImageIcon>();

	private ImageIconProxy()
	{
	}

	public static ImageIcon getIcon(String path)
	{
		if(imageIconHashMap.containsKey(path))
		{
			return  imageIconHashMap.get(path);
		}

		ImageIcon imageIcon = new ImageIcon(path);
		imageIconHashMap.put(path, imageIcon);
		return imageIcon;
	}
}
