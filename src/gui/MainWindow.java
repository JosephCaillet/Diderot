package gui;

import gui.dialog.InputStringDialogHelper;
import gui.dialog.settings.ProjectSettingsDialog;
import model.Project;
import model.Route;
import plugin.editor.DiderotProjectEditor;
import plugin.exporter.DefaultDiderotDocumentationExporter;
import plugin.exporter.DefaultDiderotProjectExporter;
import plugin.exporter.DiderotProjectExporter;
import plugin.importer.DefaultDiderotProjectImporter;
import plugin.importer.DiderotProjectImporter;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

import static model.Route.getAbsoluteNodePath;

/**
 * Created by joseph on 14/05/16.
 */
public class MainWindow extends JFrame implements TreeSelectionListener
{
	private final static String NO_ROUTE_SELECTED = "nrs";
	private final static String ROUTE_SELECTED = "rs";

	private Route rootRoutes;
	private RoutesTreePanel routesTreePanel;

	private AbstractAction addRouteAction, delRouteAction, moveRouteAction, focusOnRouteAction;
	private JButton addRouteBtn = new JButton(),
			delRouteBtn = new JButton(),
		moveRouteBtn = new JButton();
	private JTextField currentRouteLbl = new JTextField();
	private JMenu methodMenu;
	private JMenu importMenu;
	private JMenu exportMenu;
	private JMenu editMenu;
	private MethodsManagementPanel methodsManagementPanel = new MethodsManagementPanel();
	private JPanel routeMethodPanel;

	private TreeMap<String, DiderotProjectImporter> importPlugins = new TreeMap<>();
	private TreeMap<String, DiderotProjectExporter> exportPlugins = new TreeMap<>();
	private TreeMap<String, DiderotProjectEditor> editPlugins = new TreeMap<>();

	public MainWindow()
	{
		super("Diderot");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		rootRoutes = new Route(Project.getActiveProject().getDomain());

		//createSampleRoute();

		buildUI();

		setPreferredSize(new Dimension(900, 800));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		//getWindowListeners()[0].windowClosing(null);
	}

	private void createSampleRoute()
	{
		//rootRoutes.addRoute("index");
		//rootRoutes.addRoute("index");
		rootRoutes.addRoute("home");
		rootRoutes.addRoute("home/page1");
		/*rootRoutes.addRoute("home/page2");
		rootRoutes.addRoute("data/type/subtype1");
		rootRoutes.addRoute("data/type/subtype2");
		rootRoutes.addRoute("bidule/truc");
		rootRoutes.addRoute("data/type2");
		rootRoutes.addRoute("home/page2");
		rootRoutes.addRoute("home/page2");*/

		Project project = Project.getActiveProject();
		project.addUserRouteProperty("Controller", "myController");
		project.addUserRouteProperty("View", "myView");
		project.addUserRouteProperty("View template", "myViewTemplate");
		project.addUserRouteProperty("test delete", "test del");
		project.addUserRouteProperty("test rename", "old val");

		for(String prop : project.getUserRoutesPropertiesNames())
		{
			rootRoutes.addUserProperty(prop, project.getUserRouteProperty(prop).getDefaultValue());
		}

		project.removeUserRouteProperty("test delete");
		rootRoutes.removeUserProperty("test delete");
		rootRoutes.changeUserPropertyValue("test rename", "old val", "new val");
		rootRoutes.changeUserPropertyValue("View", "old val", "new val");
	}

	private void buildUI()
	{
		//button for route management
		Box btnPanel = Box.createVerticalBox();

		btnPanel.add(addRouteBtn);
		btnPanel.add(moveRouteBtn);
		btnPanel.add(delRouteBtn);
		btnPanel.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));

		addRouteBtn.setAlignmentX(CENTER_ALIGNMENT);
		delRouteBtn.setAlignmentX(CENTER_ALIGNMENT);
		moveRouteBtn.setAlignmentX(CENTER_ALIGNMENT);
		addRouteBtn.setMaximumSize(new Dimension(208,34));
		delRouteBtn.setMaximumSize(new Dimension(208,34));
		moveRouteBtn.setMaximumSize(new Dimension(208,34));

		///////////////////////
		final JFrame parent = this;
		JButton confBtn = new JButton(new AbstractAction("Project settings", ImageIconProxy.getIcon("conf"))
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				ProjectSettingsDialog projectSettingsDialog = new ProjectSettingsDialog(parent, rootRoutes);
				projectSettingsDialog.display();
			}
		});
		confBtn.setAlignmentX(CENTER_ALIGNMENT);
		confBtn.setMaximumSize(new Dimension(208,34));
		btnPanel.add(confBtn);
		//confBtn.doClick();
		/*DefaultDiderotProjectImporter defaultDiderotProjectImporter = new DefaultDiderotProjectImporter();
		defaultDiderotProjectImporter.setDiderotData(rootRoutes, Project.getActiveProject());
		defaultDiderotProjectImporter.importProject();*/
		//System.exit(0);
		///////////////////////


		//route tree
		routesTreePanel = new RoutesTreePanel(rootRoutes);


		//panel containing routes tree and associated buttons
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(new JScrollPane(routesTreePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		leftPanel.add(btnPanel, BorderLayout.SOUTH);

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
		JFrame that = this;
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				//Todo: add confirmation dialog
				/*DefaultDiderotProjectExporter diderotProjectExporter = new DefaultDiderotProjectExporter();
				diderotProjectExporter.setDiderotData(rootRoutes, Project.getActiveProject());
				diderotProjectExporter.exportProject();

				DefaultDiderotDocumentationExporter defaultDiderotDocumentationExporter = new DefaultDiderotDocumentationExporter();
				defaultDiderotDocumentationExporter.setDiderotData(rootRoutes, Project.getActiveProject());
				defaultDiderotDocumentationExporter.generateHtmlDocumentation();*/

				that.dispose();
			}
		});

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

	private void setUpImportPlugins()
	{
		importPlugins.clear();
		importMenu.removeAll();
		Vector<String> availableImporters = new Vector<>();
		availableImporters.add(DefaultDiderotProjectImporter.class.getName());

		for(String importerName : availableImporters)
		{
			Class importer = null;
			try
			{
				importer = Class.forName(importerName);

				DiderotProjectImporter importerInstance = (DiderotProjectImporter) importer.newInstance();
				importPlugins.put(importerName, importerInstance);

				JMenu actionMenu = new JMenu(importerInstance.getPluginName());
				HashMap<String, String> importingOperations = importerInstance.getAvailableImportingOperations();

				JFrame parent = this;

				for(String actionName : importingOperations.keySet())
				{
					final Class finalImporter = importer;
					JMenuItem actionMenuItem = new JMenuItem(new AbstractAction(actionName)
					{
						@Override
						public void actionPerformed(ActionEvent e)
						{
							if(Project.getActiveProject().isOpened() && JOptionPane.YES_OPTION !=
									JOptionPane.showConfirmDialog(parent, "Project \"" + Project.getActiveProject().getName() + "\" is currently opened.\nUnsaved modification may be lost.\nDo you want to continue?", "Project already opened", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE))
							{
								return;
							}

							importerInstance.setDiderotData(rootRoutes, Project.getActiveProject());
							importerInstance.setParentFrame(parent);
							try
							{
								Method method = finalImporter.getMethod(importerInstance.getAvailableImportingOperations().get(actionName));
								method.invoke(importerInstance);
								routesTreePanel.updateModel();
							}
							catch(NoSuchMethodException e1)
							{
								e1.printStackTrace();
							}
							catch(InvocationTargetException e1)
							{
								e1.printStackTrace();
							}
							catch(IllegalAccessException e1)
							{
								e1.printStackTrace();
							}
						}
					});

					if("plugin.importer.DefaultDiderotProjectImporter".equals(importerName))
					{
						actionMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
					}

					actionMenu.add(actionMenuItem);
				}

				importMenu.add(actionMenu);
			}
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
				continue;
			}
			catch(InstantiationException e)
			{
				e.printStackTrace();
				continue;
			}
			catch(IllegalAccessException e)
			{
				e.printStackTrace();
				continue;
			}
		}
	}

	private void setUpExportPlugins()
	{
		exportPlugins.clear();
		exportMenu.removeAll();
		Vector<String> availableExporters = new Vector<>();
		availableExporters.add(DefaultDiderotProjectExporter.class.getName());
		availableExporters.add(DefaultDiderotDocumentationExporter.class.getName());

		for(String exporterName : availableExporters)
		{
			Class exporter = null;
			try
			{
				exporter = Class.forName(exporterName);

				DiderotProjectExporter exporterInstance = (DiderotProjectExporter) exporter.newInstance();
				exportPlugins.put(exporterName, exporterInstance);

				JMenu actionMenu = new JMenu(exporterInstance.getPluginName());
				HashMap<String, String> exportingOperations = exporterInstance.getAvailableExportingOperations();

				JFrame parent = this;

				for(String actionName : exportingOperations.keySet())
				{
					final Class finalExporter = exporter;
					JMenuItem actionMenuItem = new JMenuItem(new AbstractAction(actionName)
					{
						@Override
						public void actionPerformed(ActionEvent e)
						{
							exporterInstance.setDiderotData(rootRoutes, Project.getActiveProject());
							exporterInstance.setParentFrame(parent);
							try
							{
								Method method = finalExporter.getMethod(exporterInstance.getAvailableExportingOperations().get(actionName));
								method.invoke(exporterInstance);
							}
							catch(NoSuchMethodException e1)
							{
								e1.printStackTrace();
							}
							catch(InvocationTargetException e1)
							{
								e1.printStackTrace();
							}
							catch(IllegalAccessException e1)
							{
								e1.printStackTrace();
							}
						}
					});

					if("plugin.exporter.DefaultDiderotProjectExporter".equals(exporterName))
					{
						actionMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
					}

					actionMenu.add(actionMenuItem);
				}

				exportMenu.add(actionMenu);
			}
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
				continue;
			}
			catch(InstantiationException e)
			{
				e.printStackTrace();
				continue;
			}
			catch(IllegalAccessException e)
			{
				e.printStackTrace();
				continue;
			}
		}
	}

	private void setUpEditPlugins()
	{
		editPlugins.clear();
		editMenu.removeAll();
		Vector<String> availableEditors = new Vector<>();
		//availableEditors.add(DefaultDiderotProjectImporter.class.getName());

		for(String editorName : availableEditors)
		{
			Class importer = null;
			try
			{
				importer = Class.forName(editorName);

				DiderotProjectEditor editorInstance = (DiderotProjectEditor) importer.newInstance();
				editPlugins.put(editorName, editorInstance);

				JMenu actionMenu = new JMenu(editorInstance.getPluginName());
				HashMap<String, String> editingOperations = editorInstance.getAvailableEditingOperations();

				for(String actionName : editingOperations.keySet())
				{
					JMenuItem actionMenuItem = new JMenuItem(new AbstractAction(actionName)
					{
						@Override
						public void actionPerformed(ActionEvent e)
						{
							System.out.println(actionName);
						}
					});

					actionMenu.add(actionMenuItem);
				}

				importMenu.add(actionMenu);
			}
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
				continue;
			}
			catch(InstantiationException e)
			{
				e.printStackTrace();
				continue;
			}
			catch(IllegalAccessException e)
			{
				e.printStackTrace();
				continue;
			}
		}
	}

	private void buildMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();

		importMenu = new JMenu("Import");
		importMenu.setMnemonic('I');
		setUpImportPlugins();
		menuBar.add(importMenu);

		exportMenu = new JMenu("Export");
		exportMenu.setMnemonic('E');
		setUpExportPlugins();
		menuBar.add(exportMenu);

		editMenu = new JMenu("Edit");
		editMenu.setMnemonic('U');
		setUpEditPlugins();
		menuBar.add(editMenu);


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
				"Delete route", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE))
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