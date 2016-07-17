package gui;

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
	private static final String[] settingsSections = {"Project", "User defined properties"};

	private Route rootRoutes;
	private Frame parent;
	private boolean changeMade = false;
	private JList<String> settingsSectionList;
	private CardLayout cardLayout;
	private JPanel settingsPanel;
	private JButton exitButton = new JButton("Close project settings window");

	public ProjectSettingsDialog(Frame owner, Route rootRoutes)
	{
		super(owner, "Project settings", true);
		this.rootRoutes = rootRoutes;

		parent = owner;
		buildUI();
		addListeners();

		setMinimumSize(new Dimension(400, 150));
		pack();
		setLocationRelativeTo(parent);
	}

	private void buildUI()
	{
		//settings categories
		JPanel mainPanel = new JPanel(new BorderLayout());
		settingsSectionList = new JList<String>(settingsSections);
		settingsSectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		settingsSectionList.setMinimumSize(new Dimension(100, 10));
		settingsSectionList.setSelectedIndex(0);

		mainPanel.add(new JScrollPane(settingsSectionList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.WEST);

		//settings themselves
		cardLayout = new CardLayout();
		settingsPanel = new JPanel(cardLayout);

		//Project settings
		JPanel panel = new JPanel();
		panel.add(new JLabel(settingsSections[0]));
		settingsPanel.add(panel, settingsSections[0]);

		//user defined properties
		panel = new JPanel();
		panel.add(new JLabel(settingsSections[1]));
		settingsPanel.add(panel, settingsSections[1]);
		panel.add(new JCheckBox("test"));
		panel.add(new JCheckBox("test"));
		panel.add(new JRadioButton("test"));
		panel.add(new JRadioButton("test"));

		mainPanel.add(settingsPanel, BorderLayout.CENTER);

		panel = new JPanel();
		panel.add(exitButton);
		mainPanel.add(panel, BorderLayout.SOUTH);


		setContentPane(mainPanel);
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

		//project settings

		//user defined properties
		/*
		Todo:
		*properties:
		-list all prop
		-delete prop
		-create new prop (is memo? default val?)
		-edit existing prop (is memo? default val? upd old defaullt val?)
			*properties' values(if memo):
			-add value
			-rename value(upd old val?)
			-remove value(upd old val?)
		 */
	}

	public boolean display()
	{
		setVisible(true);
		return changeMade;
	}
}