package gui;

import model.Route;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.Map;

/**
 * Created by joseph on 14/05/16.
 */
public class MainWindow extends JFrame
{
	private Route rootRoutes;
	private JTree routesTree;

	public MainWindow()
	{
		super("Diderot");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setSystemLookAndFeel();

		rootRoutes = new Route();

		System.out.println((rootRoutes.addRoute("index")));
		System.out.println((rootRoutes.addRoute("index")));
		System.out.println((rootRoutes.addRoute("home")));
		System.out.println((rootRoutes.addRoute("home/page1")));
		System.out.println((rootRoutes.addRoute("home/page2")));
		System.out.println((rootRoutes.addRoute("data/type/subtype1")));
		System.out.println((rootRoutes.addRoute("data/type/subtype2")));
		System.out.println((rootRoutes.addRoute("data/type2")));
		System.out.println((rootRoutes.addRoute("home/page2")));
		System.out.println((rootRoutes.addRoute("home/page2")));

		DefaultMutableTreeNode root = new DefaultMutableTreeNode("/");
		fillTree(root, rootRoutes);
		routesTree = new JTree(root);
		add(routesTree);
		setMinimumSize(new Dimension(200, 200));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void fillTree(DefaultMutableTreeNode node, Route route)
	{
		for(Map.Entry<String, Route> entry : route.getSubRoutes().entrySet())
		{
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(entry.getKey());
			fillTree(newNode, entry.getValue());
			node.add(newNode);
		}
	}

	public void setSystemLookAndFeel()
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (UnsupportedLookAndFeelException e) {
		}
		catch (ClassNotFoundException e) {
		}
		catch (InstantiationException e) {
		}
		catch (IllegalAccessException e) {
		}
	}
}