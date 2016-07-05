package gui;

import model.Route;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
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

		setMinimumSize(new Dimension(400, 400));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void buildUI()
	{
		//setLayout(new BorderLayout());

		routesTreePanel.addTreeSelectionListener(this);

		Box btnPannel = Box.createVerticalBox();
		btnPannel.add(addRouteBtn);
		btnPannel.add(renameRouteBtn);
		btnPannel.add(delRouteBtn);
		btnPannel.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));

		addRouteBtn.setAlignmentX(CENTER_ALIGNMENT);
		delRouteBtn.setAlignmentX(CENTER_ALIGNMENT);
		renameRouteBtn.setAlignmentX(CENTER_ALIGNMENT);
		addRouteBtn.setMaximumSize(new Dimension(208,34));
		delRouteBtn.setMaximumSize(new Dimension(208,34));
		renameRouteBtn.setMaximumSize(new Dimension(208,34));
		addRouteBtn.addActionListener(this);
		delRouteBtn.addActionListener(this);
		renameRouteBtn.addActionListener(this);

		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(new JScrollPane(routesTreePanel,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		leftPanel.add(btnPannel, BorderLayout.SOUTH);

		JPanel rightPanel = new JPanel(new BorderLayout());

		rightPanel.add(currentRouteLbl, BorderLayout.NORTH);

		JSplitPane mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, leftPanel, rightPanel);
		mainPanel.setBorder(BorderFactory.createLineBorder(mainPanel.getBackground(), 5));
		mainPanel.setResizeWeight(0.2);
		add(mainPanel);
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
			actionAddRoute(e);
		}
		else if(e.getSource() == delRouteBtn)
		{
			actionRemoveRoute(e);
		}
		else if(e.getSource() == renameRouteBtn)
		{
			actionRenameRoute(e);
		}
	}

	private void actionAddRoute(ActionEvent e)
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
			routesTreePanel.setSelectionPath(new TreePath(rootRoutes.getPathToRoute(routeToAdd)));
		}
	}

	private void actionRemoveRoute(ActionEvent e)
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
			TreePath tp = new TreePath(rootRoutes.getPathToRoute(routeToDelete));
			if(!rootRoutes.deleteRoute(routeToDelete))
			{
				JOptionPane.showMessageDialog(this, "This route does not exists.", "Cannot delete route", JOptionPane.ERROR_MESSAGE);
				return;
			}
			routesTreePanel.treeCollapsed(new TreeExpansionEvent(this, tp));
			routesTreePanel.updateModel();
			currentRouteLbl.setText(" ");
		}
	}

	private void actionRenameRoute(ActionEvent e)
	{

	}
}