import gui.MainWindow;

import javax.swing.*;

/**
 * Created by joseph on 14/05/16.
 */
public class Main
{
	/**
	 * Application entry point, launch the program.
	 * @param args Command line arguments
	 */
	public static void main(String[] args)
	{
		setSystemLookAndFeel();
		new MainWindow();
	}

	/**
	 * Set system's look and fell.
	 */
	static public void setSystemLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		}
		catch(UnsupportedLookAndFeelException e){e.printStackTrace();}
		catch(ClassNotFoundException e){e.printStackTrace();}
		catch(InstantiationException e){e.printStackTrace();}
		catch(IllegalAccessException e){e.printStackTrace();}
	}
}