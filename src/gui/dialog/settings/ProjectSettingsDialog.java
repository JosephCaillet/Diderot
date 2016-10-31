package gui.dialog.settings;

import gui.ImageIconProxy;
import model.Route;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by joseph on 16/07/16.
 */
public class ProjectSettingsDialog extends JDialog
{
	private static final String[] settingsSections = {"Project", "User defined properties", "Response output format"};
	private static String lastSelectedMenu = settingsSections[0];

	private Route rootRoutes;
	private Frame parent;
	private boolean changeMade = false;
	private JList<String> settingsSectionList;
	private CardLayout cardLayout;
	private JPanel settingsPanel;
	private JButton exitButton = new JButton("Close project settings window", ImageIconProxy.getIcon("exit"));


	public ProjectSettingsDialog(Frame owner, Route rootRoutes)
	{
		super(owner, "Project settings", true);
		this.rootRoutes = rootRoutes;

		parent = owner;
		buildUI();

		pack();
		setMinimumSize(getSize());
		setLocationRelativeTo(parent);
	}

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
		JPanel panel = new JPanel();
		panel.add(new JLabel(settingsSections[0]));
		settingsPanel.add(panel, settingsSections[0]);

		//user defined properties
		settingsPanel.add(new UserDefinedPropertiesPanel(parent, rootRoutes), settingsSections[1]);

		//response output format
		settingsPanel.add(new ResponsePanel(parent, rootRoutes), settingsSections[2]);

		mainPanel.add(settingsPanel, BorderLayout.CENTER);

		panel = new JPanel();
		panel.add(exitButton);
		mainPanel.add(panel, BorderLayout.SOUTH);


		setContentPane(mainPanel);

		addListeners();

		settingsSectionList.setSelectedValue(lastSelectedMenu, true);
	}

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

	public boolean display()
	{
		setVisible(true);
		return changeMade;
	}
}