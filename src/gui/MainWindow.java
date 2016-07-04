package gui;

import model.Route;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static model.Route.getAbsoluteNodePath;

/**
 * Created by joseph on 14/05/16.
 */
public class MainWindow extends JFrame implements TreeSelectionListener, ActionListener
{
	private String projectName = "domain.com";

	private Route rootRoutes;
	private RoutesTreePanel routesTreePanel;

	private JButton addRouteBtn = new JButton("Add route", new ImageIcon("rsc/plus.png"));
	private JButton delRouteBtn = new JButton("Delete route", new ImageIcon("rsc/del.png"));
	private JButton renameRouteBtn = new JButton("Rename route", new ImageIcon("rsc/edit.png"));
	private JLabel currentRouteLbl = new JLabel(" ");

	public MainWindow()
	{
		super("Diderot");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		rootRoutes = new Route(projectName);
		routesTreePanel = new RoutesTreePanel(rootRoutes);

		/*System.out.println((rootRoutes.addRoute("index", new Route(rootRoutes))));
		System.out.println((rootRoutes.addRoute("index", new Route(rootRoutes))));
		System.out.println((rootRoutes.addRoute("home", new Route(rootRoutes))));
		System.out.println((rootRoutes.addRoute("home/page1", new Route(rootRoutes))));
		System.out.println((rootRoutes.addRoute("home/page2", new Route(rootRoutes))));
		System.out.println((rootRoutes.addRoute("data/type/subtype1", new Route(rootRoutes))));
		System.out.println((rootRoutes.addRoute("data/type/subtype2", new Route(rootRoutes))));
		System.out.println((rootRoutes.addRoute("data/type2", new Route(rootRoutes))));
		System.out.println((rootRoutes.addRoute("home/page2", new Route(rootRoutes))));
		System.out.println((rootRoutes.addRoute("home/page2", new Route(rootRoutes))));*/

		rootRoutes.addRoute("index");
		rootRoutes.addRoute("index");
		rootRoutes.addRoute("home");
		rootRoutes.addRoute("home/page1");
		rootRoutes.addRoute("home/page2");
		rootRoutes.addRoute("data/type/subtype1");
		rootRoutes.addRoute("data/type/subtype2");
		rootRoutes.addRoute("bidule/truc");
		rootRoutes.addRoute("data/type2");
		rootRoutes.addRoute("home/page2");
		rootRoutes.addRoute("home/page2");

		routesTreePanel.updateModel();
		buildUI();

		setMinimumSize(new Dimension(200, 200));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void buildUI()
	{
		setLayout(new BorderLayout());

		routesTreePanel.addTreeSelectionListener(this);

		Box btnPannel = Box.createVerticalBox();
		btnPannel.add(addRouteBtn);
		btnPannel.add(renameRouteBtn);
		btnPannel.add(delRouteBtn);

		addRouteBtn.setAlignmentX(CENTER_ALIGNMENT);
		delRouteBtn.setAlignmentX(CENTER_ALIGNMENT);
		renameRouteBtn.setAlignmentX(CENTER_ALIGNMENT);
		addRouteBtn.setMaximumSize(new Dimension(208,34));
		delRouteBtn.setMaximumSize(new Dimension(208,34));
		renameRouteBtn.setMaximumSize(new Dimension(208,34));
		addRouteBtn.addActionListener(this);
		delRouteBtn.addActionListener(this);
		renameRouteBtn.addActionListener(this);

		add(new JScrollPane(routesTreePanel,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		add(currentRouteLbl, BorderLayout.NORTH);
		add(btnPannel, BorderLayout.SOUTH);
	}

	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		String absPath = getAbsoluteNodePath(e.getPath(), false);
		currentRouteLbl.setText(absPath);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == addRouteBtn)
		{
			String defaultRoute = "";
			TreePath treePath = routesTreePanel.getSelectionPath();
			if(treePath != null)
			{
				defaultRoute = getAbsoluteNodePath(treePath, true);
			}

			String routeToAdd = (String) JOptionPane.showInputDialog(this,
					"Enter route path:", "Add new route", JOptionPane.QUESTION_MESSAGE, null, null, defaultRoute + "/");

			if(routeToAdd != null)
			{
				if(!rootRoutes.addRoute(routeToAdd))
				{
					JOptionPane.showMessageDialog(this, "This route already exists, or contains multiple occurrence of '/' without character between them.", "Cannot add route", JOptionPane.WARNING_MESSAGE);
					return;
				}
				routesTreePanel.updateModel();
			}
		}
		else if(e.getSource() == delRouteBtn)
		{
			TreePath treePath = routesTreePanel.getSelectionPath();
			if(treePath == null)
			{
				JOptionPane.showMessageDialog(this, "No route selected");
				return;
			}

			String routeToDelete = getAbsoluteNodePath(treePath, true);

			if(routeToDelete.isEmpty())
			{
				JOptionPane.showMessageDialog(this, "You can't delete project root.", "Cannot delete project root", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this,
				"Are you sure you want to delete the following route and its sub-routes?\n" + routeToDelete, "Delete route", JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE))
			{
				if(!rootRoutes.deleteRoute(routeToDelete))
				{
					JOptionPane.showMessageDialog(this, "This route does not exists.", "Cannot delete route", JOptionPane.ERROR_MESSAGE);
					return;
				}
				routesTreePanel.updateModel();
				currentRouteLbl.setText(" ");
			}
		}
	}
}