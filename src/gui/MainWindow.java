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

	private JButton addRouteBtn = new JButton("Add route", ImageIconProxy.getIcon("rsc/plus.png"));
	private JButton delRouteBtn = new JButton("Delete route", ImageIconProxy.getIcon("rsc/del.png"));
	private JButton moveRouteBtn = new JButton("Move route", ImageIconProxy.getIcon("rsc/edit.png"));
	private JLabel currentRouteLbl = new JLabel(" ");
	private final JPanel leftPanel = new JPanel(new BorderLayout());
	private final JPanel rightPanel = new JPanel(new BorderLayout());
	private JLabel defaultCenterLabel = new JLabel("Select an existing route or create one.");

	public MainWindow()
	{
		super("Diderot");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		rootRoutes = new Route(projectName);
		routesTreePanel = new RoutesTreePanel(rootRoutes);

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

		setPreferredSize(new Dimension(850, 400));
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
		btnPannel.add(moveRouteBtn);
		btnPannel.add(delRouteBtn);
		btnPannel.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));

		addRouteBtn.setAlignmentX(CENTER_ALIGNMENT);
		delRouteBtn.setAlignmentX(CENTER_ALIGNMENT);
		moveRouteBtn.setAlignmentX(CENTER_ALIGNMENT);
		addRouteBtn.setMaximumSize(new Dimension(208,34));
		delRouteBtn.setMaximumSize(new Dimension(208,34));
		moveRouteBtn.setMaximumSize(new Dimension(208,34));
		addRouteBtn.addActionListener(this);
		delRouteBtn.addActionListener(this);
		moveRouteBtn.addActionListener(this);

		leftPanel.add(new JScrollPane(routesTreePanel,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		leftPanel.add(btnPannel, BorderLayout.SOUTH);

		currentRouteLbl.setOpaque(true);
		currentRouteLbl.setBorder(BorderFactory.createLineBorder(currentRouteLbl.getBackground().darker(), 1));

		rightPanel.add(currentRouteLbl, BorderLayout.NORTH);
		defaultCenterLabel.setHorizontalAlignment(SwingConstants.CENTER);
		rightPanel.add(defaultCenterLabel, BorderLayout.CENTER);

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

		BorderLayout layout = (BorderLayout) rightPanel.getLayout();
		rightPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));

		if(routesTreePanel.getLastSelectedPathComponent() != null)
		{
			Route lastRoute = rootRoutes.getLastRoute(getAbsoluteNodePath(e.getPath(), true));
			rightPanel.add(new RouteHttpMethodsManagementPanel(lastRoute), BorderLayout.CENTER);
		}
		else
		{
			rightPanel.add(defaultCenterLabel, BorderLayout.CENTER);
		}


		rightPanel.revalidate();
		rightPanel.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == addRouteBtn)
		{
			actionAddRoute();
		}
		else if(e.getSource() == delRouteBtn)
		{
			actionRemoveRoute();
		}
		else if(e.getSource() == moveRouteBtn)
		{
			actionMoveRoute();
		}
	}

	private void actionAddRoute()
	{
		String defaultRoute = "";
		TreePath treePath = routesTreePanel.getSelectionPath();
		if(treePath != null)
		{
			defaultRoute = getAbsoluteNodePath(treePath, true);
		}

		String routeToAdd = (String) JOptionPane.showInputDialog(this,
				"Enter route path:", "Add new route", JOptionPane.PLAIN_MESSAGE, null, null, defaultRoute + "/");

		if(routeToAdd != null)
		{
			if(!rootRoutes.addRoute(routeToAdd))
			{
				JOptionPane.showMessageDialog(this, "This route already exists, or contains multiple occurrence of '/' without character between them.", "Cannot add route", JOptionPane.ERROR_MESSAGE);
				return;
			}
			routesTreePanel.updateModel();
			routesTreePanel.setSelectionPath(new TreePath(rootRoutes.getPathToRoute(routeToAdd)));
		}
	}

	private void actionRemoveRoute()
	{
		TreePath treePath = routesTreePanel.getSelectionPath();
		if(treePath == null)
		{
			JOptionPane.showMessageDialog(this, "No route selected", "Cannot delete route", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String routeToDelete = getAbsoluteNodePath(treePath, true);

		if(routeToDelete.isEmpty())
		{
			JOptionPane.showMessageDialog(this, "You cannot delete project's root.", "Cannot delete project root", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this,
				"Are you sure you want to delete the following route and its sub-routes?\n" + routeToDelete,
				"Delete route", JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE))
		{
			if(!rootRoutes.deleteRoute(routeToDelete))
			{
				JOptionPane.showMessageDialog(this, "This route does not exists.", "Cannot delete route", JOptionPane.ERROR_MESSAGE);
				return;
			}
			routesTreePanel.updateModel(treePath, null);
			currentRouteLbl.setText(" ");
		}
	}
//
//
//
//          replaceAll("\\s","")
//
//
//
//
//
	private void actionMoveRoute()
	{
		TreePath treePath = routesTreePanel.getSelectionPath();
		if(treePath == null)
		{
			JOptionPane.showMessageDialog(this, "No route selected", "Cannot rename route", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String oldRoutePath = getAbsoluteNodePath(treePath, true);

		if(oldRoutePath.isEmpty())
		{
			JOptionPane.showMessageDialog(this, "You cannot rename project's root here, check project settings to do so.", "Cannot rename project root", JOptionPane.ERROR_MESSAGE);
			return;
		}


		String newRoutePath = (String) JOptionPane.showInputDialog(this,
				"Move route:\n" + oldRoutePath + "\nto:", "Move route", JOptionPane.PLAIN_MESSAGE, null, null, oldRoutePath);

		if(newRoutePath != null)
		{
			if(!rootRoutes.moveRoute(oldRoutePath, newRoutePath))
			{
				JOptionPane.showMessageDialog(this, "The destination route already exists, or contains multiple occurrence of '/' without character between them.", "Cannot move route", JOptionPane.ERROR_MESSAGE);
				return;
			}
			TreePath tp = new TreePath(rootRoutes.getPathToRoute(newRoutePath));
			routesTreePanel.updateModel(treePath, tp);
			routesTreePanel.setSelectionPath(tp);
		}
	}
}