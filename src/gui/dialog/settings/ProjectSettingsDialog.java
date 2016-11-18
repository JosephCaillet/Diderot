package gui.dialog.settings;

import gui.ImageIconProxy;
import model.Route;
import plugin.editor.DiderotProjectEditor;
import plugin.exporter.DiderotProjectExporter;
import plugin.importer.DiderotProjectImporter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.TreeMap;

/**
 * Dialog showing project setting.
 * @author joseph
 */
public class ProjectSettingsDialog extends JDialog
{
	private static final String[] settingsSections = {"Project", "User defined properties", "Response output format", "Plugins' configurations"};
	private static String lastSelectedMenu = settingsSections[0];

	private Route rootRoutes;
	private Frame parent;
	private JList<String> settingsSectionList;
	private CardLayout cardLayout;
	private JPanel settingsPanel;
	private JButton exitButton = new JButton("Close project settings window", ImageIconProxy.getIcon("exit"));

	private TreeMap<String, DiderotProjectImporter> importPlugins = new TreeMap<>();
	private TreeMap<String, DiderotProjectExporter> exportPlugins = new TreeMap<>();
	private TreeMap<String, DiderotProjectEditor> editPlugins = new TreeMap<>();

	/**
	 * @param owner Parent frame
	 * @param rootRoutes Root route
	 * @param importPlugins Import plugin map
	 * @param exportPlugins Export plugin map
	 * @param editPlugins Edit plugin map
	 */
	public ProjectSettingsDialog(Frame owner, Route rootRoutes,
	                             TreeMap<String, DiderotProjectImporter> importPlugins,
	                             TreeMap<String, DiderotProjectExporter> exportPlugins,
	                             TreeMap<String, DiderotProjectEditor> editPlugins)
	{
		super(owner, "Project settings", true);
		this.rootRoutes = rootRoutes;
		this.importPlugins = importPlugins;
		this.exportPlugins = exportPlugins;
		this.editPlugins = editPlugins;

		parent = owner;
		buildUI();

		pack();
		setMinimumSize(getSize());
		setLocationRelativeTo(parent);
	}

	/**
	 * Build user interface.
	 */
	private void buildUI()
	{
		//settings categories
		JPanel mainPanel = new JPanel(new BorderLayout());
		settingsSectionList = new JList<String>(settingsSections);
		settingsSectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		settingsSectionList.setMinimumSize(new Dimension(100, 10));

		mainPanel.add(new JScrollPane(settingsSectionList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.WEST);

		//settings themselves
		cardLayout = new CardLayout();
		settingsPanel = new JPanel(cardLayout);
		settingsPanel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

		//Project settings
		settingsPanel.add(new ProjectPanel(parent), settingsSections[0]);

		//user defined properties
		settingsPanel.add(new UserDefinedPropertiesPanel(parent, rootRoutes), settingsSections[1]);

		//response output format
		settingsPanel.add(new ResponsePanel(parent, rootRoutes), settingsSections[2]);

		//plugins' configuration
		settingsPanel.add(new PluginConfigPanel(parent, importPlugins, exportPlugins, editPlugins), settingsSections[3]);


		mainPanel.add(settingsPanel, BorderLayout.CENTER);
		JPanel panel = new JPanel();
		panel.add(exitButton);
		mainPanel.add(panel, BorderLayout.SOUTH);


		setContentPane(mainPanel);

		addListeners();

		settingsSectionList.setSelectedValue(lastSelectedMenu, true);
	}

	/**
	 * Add listeners.
	 */
	private void addListeners()
	{
		//settings selection
		settingsSectionList.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent listSelectionEvent)
			{
				if(settingsSectionList.getSelectedIndex() != -1)
				{
					cardLayout.show(settingsPanel, settingsSectionList.getSelectedValue());
					lastSelectedMenu = settingsSectionList.getSelectedValue();
				}
			}
		});

		//exit dialog window
		exitButton.addActionListener(new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				setVisible(false);
			}
		});
	}

	/**
	 * Display dialog box
	 */
	public void display()
	{
		setVisible(true);
	}
}