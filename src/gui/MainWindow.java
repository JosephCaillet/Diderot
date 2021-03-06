package gui;

import gui.dialog.InputStringDialogHelper;
import gui.dialog.settings.ProjectSettingsDialog;
import model.Project;
import model.Route;
import plugin.OperationNameIcon;
import plugin.PluginClassLoader;
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
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;
import java.util.jar.JarFile;

import static model.Route.getAbsoluteNodePath;

/**
 * Main window of the application.
 * @author joseph
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
			moveRouteBtn = new JButton(),
			confBtn = new JButton();
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

	/**
	 * Creates the main window.
	 */
	public MainWindow()
	{
		super();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		rootRoutes = new Route(Project.getActiveProject().getDomain());

		buildUI();

		setPreferredSize(new Dimension(950, 800));
		pack();
		setLocationRelativeTo(null);

		loadPlugins();

		DiderotProjectImporter importer = importPlugins.get(DefaultDiderotProjectImporter.class.getName());
		DefaultDiderotProjectImporter defaultImporter = (DefaultDiderotProjectImporter) importer;
		defaultImporter.setDiderotData(rootRoutes, Project.getActiveProject());
		defaultImporter.createProject();
		routesTreePanel.updateModel();

		setTitle("Diderot - " + Project.getActiveProject().getName());
		setIconImage(ImageIconProxy.getIcon("icon").getImage());
		setVisible(true);
		//getWindowListeners()[0].windowClosing(null);
	}

	/**
	 * Create sample routes. Used for testing purposes.
	 */
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

	/**
	 * Build user interface.
	 */
	private void buildUI()
	{
		//button for route management
		Box btnPanel = Box.createVerticalBox();

		btnPanel.add(addRouteBtn);
		btnPanel.add(moveRouteBtn);
		btnPanel.add(delRouteBtn);
		btnPanel.add(confBtn);
		btnPanel.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));

		addRouteBtn.setAlignmentX(CENTER_ALIGNMENT);
		delRouteBtn.setAlignmentX(CENTER_ALIGNMENT);
		moveRouteBtn.setAlignmentX(CENTER_ALIGNMENT);
		confBtn.setAlignmentX(CENTER_ALIGNMENT);
		addRouteBtn.setMaximumSize(new Dimension(208,34));
		delRouteBtn.setMaximumSize(new Dimension(208,34));
		moveRouteBtn.setMaximumSize(new Dimension(208,34));
		confBtn.setMaximumSize(new Dimension(208,34));

		///////////////////////
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

	/**
	 * Add listeners.
	 */
	private void addListeners()
	{
		JFrame that = this;
		this.addWindowListener(new WindowAdapter()
		{
			/**
			 * Close the window.
			 * @param e {@inheritDoc}
			 */
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
			/**
			 * Set focus on route panel.
			 * @param actionEvent {@inheritDoc}
			 */
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				routesTreePanel.requestFocusInWindow();
			}
		};

		addRouteAction = new AbstractAction("Add new route", ImageIconProxy.getIcon("add"))
		{
			/**
			 * Calls MainWindow#actionAddRoute()
			 * @see MainWindow#actionAddRoute()
			 * @param actionEvent {@inheritDoc}
			 */
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				actionAddRoute();
			}
		};

		delRouteAction = new AbstractAction("Delete route", ImageIconProxy.getIcon("del"))
		{
			/**
			 * Calls MainWindow#actionRemoveRoute()
			 * @see MainWindow#actionRemoveRoute()
			 * @param actionEvent {@inheritDoc}
			 */
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				actionRemoveRoute();
			}
		};

		moveRouteAction = new AbstractAction("Move route", ImageIconProxy.getIcon("edit"))
		{
			/**
			 * Calls MainWindow#actionMoveRoute()
			 * @see MainWindow#actionMoveRoute()
			 * @param actionEvent {@inheritDoc}
			 */
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				actionMoveRoute();
			}
		};

		addRouteBtn.setAction(addRouteAction);
		delRouteBtn.setAction(delRouteAction);
		moveRouteBtn.setAction(moveRouteAction);

		final JFrame parent = this;
		confBtn.setAction(new AbstractAction("Project settings", ImageIconProxy.getIcon("conf"))
		{
			/**
			 * Show project's settings dialog
			 * @param actionEvent {@inheritDoc}
			 */
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				ProjectSettingsDialog projectSettingsDialog = new ProjectSettingsDialog(parent, rootRoutes, importPlugins, exportPlugins, editPlugins);
				projectSettingsDialog.display();

				TreePath selectedElement = routesTreePanel.getSelectionPath();
				methodsManagementPanel.saveDisplayStatus();

				routesTreePanel.updateModel();

				routesTreePanel.setSelectionPath(selectedElement);
				methodsManagementPanel.restoreDisplayStatus();
			}
		});

		routesTreePanel.addTreeSelectionListener(this);
		routesTreePanel.addMouseListener(new MouseAdapter()
		{
			/**
			 * Show context menu on routes panel on right click.
			 * @param e {@inheritDoc}
			 */
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
			/**
			 * Show context menu on context menu key press.
			 * @param e {@inheritDoc}
			 */
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

	/**
	 * Show route editing popup menu on given coordinates.
	 * @param x x position
	 * @param y y position
	 */
	private void showRoutePopUpMenu(int x, int y)
	{
		JPopupMenu jPopupMenu = new JPopupMenu();
		jPopupMenu.add(new JMenuItem(addRouteAction));
		jPopupMenu.add(new JMenuItem(moveRouteAction));
		jPopupMenu.add(new JMenuItem(delRouteAction));
		jPopupMenu.show(routesTreePanel, x, y);
	}

	/**
	 * Load plugins in ./plugins directory.
	 */
	private void loadPlugins()
	{
		Vector<String> availableImporters = new Vector<>();
		availableImporters.add(DefaultDiderotProjectImporter.class.getName());

		Vector<String> availableExporters = new Vector<>();
		availableExporters.add(DefaultDiderotProjectExporter.class.getName());
		availableExporters.add(DefaultDiderotDocumentationExporter.class.getName());

		Vector<String> availableEditors = new Vector<>();


		//inspired by http://vincentlaine.developpez.com/tutoriel/java/plugins/
		File[] jarFiles = new File("plugins").listFiles();

		if(jarFiles != null)
		{
			for(File file : jarFiles)
			{
				if(file.isDirectory())
				{
					continue;
				}

				try
				{
					JarFile jar = new JarFile(file);
					PluginClassLoader.getInstance().addURL(file.toURI().toURL());

					Enumeration jarEntries = jar.entries();
					while(jarEntries.hasMoreElements())
					{
						String entryName = jarEntries.nextElement().toString();

						if(entryName.endsWith(".class"))
						{
							String className = entryName.substring(0, entryName.length() - 6).replaceAll("/", ".");
							Class pluginClass = Class.forName(className, true, PluginClassLoader.getInstance());
							Class[] interfaces = pluginClass.getInterfaces();

							for(Class implementedInterface : interfaces)
							{
								if(implementedInterface.equals(DiderotProjectImporter.class))
								{
									availableImporters.add(className);
								}
								else if(implementedInterface.equals(DiderotProjectExporter.class))
								{
									availableExporters.add(className);
								}
								else if(implementedInterface.equals(DiderotProjectEditor.class))
								{
									availableEditors.add(className);
								}
							}
						}
					}
				}
				catch(IOException e)
				{
					System.out.println("Cannot load \"" + file.getName() + "\".");
					//e.printStackTrace();
				}
				catch(ClassNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		}

		setUpImportPlugins(availableImporters);
		setUpExportPlugins(availableExporters);
		setUpEditPlugins(availableEditors);
	}

	/**
	 * Instantiates importing plugins and fill related menu.
	 * @param availableImporters List of importing plugin class names
	 */
	private void setUpImportPlugins(Vector<String> availableImporters)
	{
		importPlugins.clear();
		importMenu.removeAll();

		for(String importerName : availableImporters)
		{
			Class importer = null;
			try
			{
				importer = Class.forName(importerName, true, PluginClassLoader.getInstance());

				DiderotProjectImporter importerInstance = (DiderotProjectImporter) importer.newInstance();
				importPlugins.put(importerName, importerInstance);

				JMenu actionMenu = new JMenu(importerInstance.getPluginName());
				HashMap<String, OperationNameIcon> importingOperations = importerInstance.getAvailableImportingOperations();

				JFrame parent = this;

				for(String actionName : importingOperations.keySet())
				{
					final Class finalImporter = importer;
					JMenuItem actionMenuItem = new JMenuItem(new AbstractAction(actionName, importingOperations.get(actionName).operationIcon)
					{
						/**
						 * Launch import plugin's action
						 * @param e {@inheritDoc}
						 */
						@Override
						public void actionPerformed(ActionEvent e)
						{
							if(Project.getActiveProject().isOpened() && JOptionPane.YES_OPTION !=
									JOptionPane.showConfirmDialog(parent, "Project \"" + Project.getActiveProject().getName() + "\" is currently opened.\nUnsaved modifications will be lost.\nDo you want to continue?", "Project already opened", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE))
							{
								return;
							}

							importerInstance.setDiderotData(rootRoutes, Project.getActiveProject());
							importerInstance.setParentFrame(parent);
							try
							{
								Method method = finalImporter.getMethod(importerInstance.getAvailableImportingOperations().get(actionName).methodName);
								method.invoke(importerInstance);

								setTitle("Diderot - " + Project.getActiveProject().getName());
								routesTreePanel.updateModel();
								enableButton(false);
								currentRouteLbl.setVisible(false);
							}
							catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e1)
							{
								e1.printStackTrace();
							}
						}
					});

					if("plugin.importer.DefaultDiderotProjectImporter".equals(importerName))
					{
						/*if("createProject".equals(importerInstance.getAvailableImportingOperations().get(actionName).methodName))
						{
							actionMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
						}
						else */if("importProject".equals(importerInstance.getAvailableImportingOperations().get(actionName).methodName))
						{
							actionMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
						}
					}

					actionMenu.add(actionMenuItem);
				}

				importMenu.add(actionMenu);
			}
			catch(ClassNotFoundException | InstantiationException | IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Instantiates exporting plugins and fill related menu.
	 * @param availableExporters List of exporting plugin class names
	 */
	private void setUpExportPlugins(Vector<String> availableExporters)
	{
		exportPlugins.clear();
		exportMenu.removeAll();

		for(String exporterName : availableExporters)
		{
			Class exporter = null;
			try
			{
				exporter = Class.forName(exporterName, true, PluginClassLoader.getInstance());

				DiderotProjectExporter exporterInstance = (DiderotProjectExporter) exporter.newInstance();
				exportPlugins.put(exporterName, exporterInstance);

				JMenu actionMenu = new JMenu(exporterInstance.getPluginName());
				HashMap<String, OperationNameIcon> exportingOperations = exporterInstance.getAvailableExportingOperations();

				JFrame parent = this;

				for(String actionName : exportingOperations.keySet())
				{
					final Class finalExporter = exporter;
					JMenuItem actionMenuItem = new JMenuItem(new AbstractAction(actionName, exportingOperations.get(actionName).operationIcon)
					{
						/**
						 * Launch export plugin's action.
						 * @param e {@inheritDoc}
						 */
						@Override
						public void actionPerformed(ActionEvent e)
						{
							exporterInstance.setDiderotData(rootRoutes, Project.getActiveProject());
							exporterInstance.setParentFrame(parent);
							try
							{
								Method method = finalExporter.getMethod(exporterInstance.getAvailableExportingOperations().get(actionName).methodName);
								method.invoke(exporterInstance);
							}
							catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e1)
							{
								e1.printStackTrace();
							}
						}
					});

					if("plugin.exporter.DefaultDiderotProjectExporter".equals(exporterName))
					{
						if("exportProject".equals(exporterInstance.getAvailableExportingOperations().get(actionName).methodName))
						{
							actionMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
						}
						else if("exportProjectAs".equals(exporterInstance.getAvailableExportingOperations().get(actionName).methodName))
						{
							actionMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
						}
					}
					else if("plugin.exporter.DefaultDiderotDocumentationExporter".equals(exporterName))
					{
						actionMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));
					}

					actionMenu.add(actionMenuItem);
				}

				exportMenu.add(actionMenu);
			}
			catch(ClassNotFoundException | InstantiationException | IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 *  Instantiates editing plugins and fills related menu.
	 * @param availableEditors List of editing plugin class names
	 */
	private void setUpEditPlugins(Vector<String> availableEditors)
	{
		editPlugins.clear();
		editMenu.removeAll();

		for(String editorName : availableEditors)
		{
			Class editor = null;
			try
			{
				editor = Class.forName(editorName, true, PluginClassLoader.getInstance());

				DiderotProjectEditor editorInstance = (DiderotProjectEditor) editor.newInstance();
				editPlugins.put(editorName, editorInstance);

				JMenu actionMenu = new JMenu(editorInstance.getPluginName());
				HashMap<String, OperationNameIcon> editingOperations = editorInstance.getAvailableEditingOperations();

				JFrame parent = this;

				for(String actionName : editingOperations.keySet())
				{
					final Class finalEditor = editor;
					JMenuItem actionMenuItem = new JMenuItem(new AbstractAction(actionName, editingOperations.get(actionName).operationIcon)
					{
						/**
						 * Launch edit plugin's action
						 * @param e {@inheritDoc}
						 */
						@Override
						public void actionPerformed(ActionEvent e)
						{
							editorInstance.setDiderotData(rootRoutes, Project.getActiveProject());
							editorInstance.setParentFrame(parent);
							try
							{
								Method method = finalEditor.getMethod(editorInstance.getAvailableEditingOperations().get(actionName).methodName);
								method.invoke(editorInstance);

								setTitle("Diderot - " + Project.getActiveProject().getName());

								TreePath tp = routesTreePanel.getSelectionPath();
								methodsManagementPanel.saveDisplayStatus();
								routesTreePanel.updateModel();
								methodsManagementPanel.restoreDisplayStatus();
								routesTreePanel.setSelectionPath(tp);
							}
							catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e1)
							{
								e1.printStackTrace();
							}
						}
					});

					actionMenu.add(actionMenuItem);
				}

				editMenu.add(actionMenu);
			}
			catch(ClassNotFoundException | InstantiationException | IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Build menu bar.
	 */
	private void buildMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();

		importMenu = new JMenu("Import");
		importMenu.setMnemonic('I');
		menuBar.add(importMenu);

		exportMenu = new JMenu("Export");
		exportMenu.setMnemonic('E');
		menuBar.add(exportMenu);

		editMenu = new JMenu("Edit");
		editMenu.setMnemonic('U');
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

	/**
	 * Enable action on selected route.
	 * @param enabled Should action be enabled or not
	 */
	private void enableButton(boolean enabled)
	{
		moveRouteAction.setEnabled(enabled);
		delRouteAction.setEnabled(enabled);
	}

	/**
	 * Display information about the selected route, and enable/disable actions.
	 * @param e {@inheritDoc}
	 */
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

	/**
	 * Creates a new route, child of the selected one.
	 */
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

	/**
	 * Delete selected route.
	 */
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

	/**
	 * Move the selected route. Can be used to rename a route.
	 */
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
			methodsManagementPanel.saveDisplayStatus();

			TreePath tp = new TreePath(rootRoutes.getPathToRoute(newRoutePath));
			routesTreePanel.updateModel(treePath, tp);
			routesTreePanel.setSelectionPath(tp);

			methodsManagementPanel.restoreDisplayStatus();
		}
	}
}