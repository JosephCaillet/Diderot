import gui.MainWindow;

import javax.swing.*;

/**
 * Created by joseph on 14/05/16.
 */
public class Main
{
	public static void main(String[] args)
	{
		setSystemLookAndFeel();
		new MainWindow();
	}

	static public void setSystemLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		}
		catch(UnsupportedLookAndFeelException e){}
		catch(ClassNotFoundException e){}
		catch(InstantiationException e){}
		catch(IllegalAccessException e){}
	}
}