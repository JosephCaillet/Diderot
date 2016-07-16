package gui;

import gui.dialog.InputStringDialogHelper;
import model.Project;
import model.Route;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;

import static model.Route.getAbsoluteNodePath;

/**
 * Created by joseph on 14/05/16.
 */
public class MainWindow extends JFrame implements TreeSelectionListener
{
	private final static String NO_ROUTE_SELECTED = "nrs";
	private final static String ROUTE_SELECTED = "rs";

	private String projectName = "domain.com";

	private Route rootRoutes;
	private RoutesTreePanel routesTreePanel;

	private AbstractAction addRouteAction, delRouteAction, moveRouteAction, focusOnRouteAction;
	private JButton addRouteBtn = new JButton(),
			delRouteBtn = new JButton(),
		moveRouteBtn = new JButton();
	private JTextField currentRouteLbl = new JTextField();
	private JMenu methodMenu;
	private MethodsManagementPanel methodsManagementPanel = new MethodsManagementPanel();
	private JPanel routeMethodPanel;

	public MainWindow()
	{
		super("Diderot");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		rootRoutes = new Route(projectName);

		createSampleRoute();

		ProjectSettingsDialog projectSettingsDialog = new ProjectSettingsDialog(this, rootRoutes);

		buildUI();

		setPreferredSize(new Dimension(850, 700));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);

		//projectSettingsDialog.display();
	}

	private void createSampleRoute()
	{
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

		Project project = Project.getActiveProject();
		project.addUserRouteProperty("Controller", true, "myController");
		project.addUserRouteProperty("View", false, "myView");
		project.addUserRouteProperty("View template", true, "myViewTemplate");
		project.addUserRouteProperty("test delete", false, "test del");
		project.addUserRouteProperty("test rename", true, "old val");

		for(String prop : project.getUserRoutesPropertiesNames())
		{
			rootRoutes.addUserProperty(prop, project.getUserRouteProperty(prop).getDefaultValue());
		}

		project.removeUserRouteProperty("test delete");
		rootRoutes.removeUserProperty("test delete");
		rootRoutes.removeUserPropertyValue("test rename", "old val", "new val");
		rootRoutes.removeUserPropertyValue("View", "old val", "new val");
	}

	private void buildUI()
	{
		//button for route management
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

		//route tree
		routesTreePanel = new RoutesTreePanel(rootRoutes);

		//panel containing routes tree and associated buttons
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(new JScrollPane(routesTreePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		leftPanel.add(btnPannel, BorderLayout.SOUTH);

		//full route path display
		currentRouteLbl.setOpaque(true);
		currentRouteLbl.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(currentRouteLbl.getBackground().darker(), 3),
				BorderFactory.createEmptyBorder(2,2,2,2)));
		currentRouteLbl.setBackground(currentRouteLbl.getBackground());
		currentRouteLbl.setVisible(false);
		currentRouteLbl.setEditable(false);
		currentRouteLbl.setBackground(null);
		currentRouteLbl.setBackground(null);

		//display of route characteristics
		CardLayout cardLayout = new CardLayout();
		routeMethodPanel = new JPanel(cardLayout);
		JLabel noRouteSelectedLabel = new JLabel("Select an existing route or create one.");
		noRouteSelectedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		routeMethodPanel.add(noRouteSelectedLabel, NO_ROUTE_SELECTED);

		methodsManagementPanel.setBorder(BorderFactory.createEmptyBorder(7, 2, 0, 0));
		routeMethodPanel.add(methodsManagementPanel, ROUTE_SELECTED);

		cardLayout.show(routeMethodPanel, NO_ROUTE_SELECTED);

		//display of full route path and route characteristics
		JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.add(currentRouteLbl, BorderLayout.NORTH);
		rightPanel.add(routeMethodPanel, BorderLayout.CENTER);

		leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));

		JSplitPane mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, leftPanel, rightPanel);
		mainPanel.setBorder(BorderFactory.createLineBorder(mainPanel.getBackground(), 5));
		mainPanel.setResizeWeight(0.2);
		add(mainPanel);

		addListeners();
		buildMenuBar();
		enableButton(false);
	}

	private void addListeners()
	{
		focusOnRouteAction = new AbstractAction("Set focus on route panel")
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				routesTreePanel.requestFocusInWindow();
			}
		};

		addRouteAction = new AbstractAction("Add new route", ImageIconProxy.getIcon("add"))
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				actionAddRoute();
			}
		};

		delRouteAction = new AbstractAction("Delete route", ImageIconProxy.getIcon("del"))
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				actionRemoveRoute();
			}
		};

		moveRouteAction = new AbstractAction("Move route", ImageIconProxy.getIcon("edit"))
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				actionMoveRoute();
			}
		};

		addRouteBtn.setAction(addRouteAction);
		delRouteBtn.setAction(delRouteAction);
		moveRouteBtn.setAction(moveRouteAction);

		routesTreePanel.addTreeSelectionListener(this);
		routesTreePanel.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e )
			{
				if(e.isPopupTrigger())
				{
					TreePath treePath = routesTreePanel.getPathForLocation(e.getX(), e.getY());
					if(treePath != null)
					{
						routesTreePanel.setSelectionPath(treePath);
						showRoutePopUpMenu(e.getX(), e.getY());
					}
				}
			}
		});
		routesTreePanel.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				if(e.getKeyCode() == KeyEvent.VK_CONTEXT_MENU)
				{
					TreePath treePath = routesTreePanel.getSelectionPath();
					if(treePath != null)
					{
						Rectangle bounds = routesTreePanel.getPathBounds(treePath);
						showRoutePopUpMenu(bounds.x + 30, bounds.y + bounds.height);
					}
				}
			}
		});
	}

	private void showRoutePopUpMenu(int x, int y)
	{
		JPopupMenu jPopupMenu = new JPopupMenu();
		jPopupMenu.add(new JMenuItem(addRouteAction));
		jPopupMenu.add(new JMenuItem(moveRouteAction));
		jPopupMenu.add(new JMenuItem(delRouteAction));
		jPopupMenu.show(routesTreePanel, x, y);
	}

	private void buildMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();

		JMenu routeMenu = new JMenu("Route");
		routeMenu.setMnemonic('R');

		JMenuItem addRouteMenuItem = new JMenuItem(addRouteAction);
		addRouteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		routeMenu.add(addRouteMenuItem);

		JMenuItem moveRouteMenuItem = new JMenuItem(moveRouteAction);
		moveRouteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK));
		routeMenu.add(moveRouteMenuItem);

		JMenuItem delRouteMenuItem = new JMenuItem(delRouteAction);
		delRouteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));
		routeMenu.add(delRouteMenuItem);

		JMenuItem focusRouteMenuItem = new JMenuItem(focusOnRouteAction);
		focusRouteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
		routeMenu.add(focusRouteMenuItem);

		menuBar.add(routeMenu);

		methodMenu = methodsManagementPanel.getMethodMenu();
		methodMenu.setMnemonic('M');
		methodMenu.setEnabled(false);

		menuBar.add(methodMenu);

		setJMenuBar(menuBar);
	}

	private void enableButton(boolean enabled)
	{
		moveRouteAction.setEnabled(enabled);
		delRouteAction.setEnabled(enabled);
	}

	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		String absPath = getAbsoluteNodePath(e.getPath(), false);
		currentRouteLbl.setText(absPath);
		currentRouteLbl.setVisible(true);

		if(routesTreePanel.getLastSelectedPathComponent() != null)
		{
			Route lastRoute = rootRoutes.getLastRoute(getAbsoluteNodePath(e.getPath(), true));
			methodsManagementPanel.setRoute(lastRoute);
			CardLayout cl = (CardLayout) routeMethodPanel.getLayout();
			cl.show(routeMethodPanel, ROUTE_SELECTED);

			methodMenu.setEnabled(true);
			enableButton(true);
		}
		else
		{
			CardLayout cl = (CardLayout) routeMethodPanel.getLayout();
			cl.show(routeMethodPanel, NO_ROUTE_SELECTED);
			methodMenu.setEnabled(false);
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

		String routeToAdd = InputStringDialogHelper.showInputNoSpacesDialog(this,
				"Enter route path:", "Add new route", JOptionPane.PLAIN_MESSAGE, defaultRoute + "/");

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
			currentRouteLbl.setVisible(false);
			enableButton(false);
		}
	}

	private void actionMoveRoute()
	{
		TreePath treePath = routesTreePanel.getSelectionPath();
		String oldRoutePath = getAbsoluteNodePath(treePath, true);

		if(oldRoutePath.isEmpty())
		{
			JOptionPane.showMessageDialog(this, "You cannot rename project's root here, check project settings to do so.", "Cannot rename project root", JOptionPane.ERROR_MESSAGE);
			return;
		}


		String newRoutePath = InputStringDialogHelper.showInputNoSpacesDialog(this,
				"Move route:\n" + oldRoutePath + "\nto:", "Move route", JOptionPane.PLAIN_MESSAGE, oldRoutePath);

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