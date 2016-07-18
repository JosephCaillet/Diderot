package gui.dialog;

import gui.ImageIconProxy;
import model.Project;
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
	private JList<String> valueList = new JList<String>();

	private Route rootRoutes;
	private Frame parent;
	private boolean changeMade = false;
	private JList<String> settingsSectionList;
	private CardLayout cardLayout;
	private JPanel settingsPanel;
	private JButton exitButton = new JButton("Close project settings window");
	private JButton addPropBtn = new JButton("Add property", ImageIconProxy.getIcon("add"));
	private JButton editPropBtn = new JButton("Rename property", ImageIconProxy.getIcon("edit"));
	private JButton delPropBtn = new JButton("Delete property", ImageIconProxy.getIcon("del"));
	private JComboBox<String> propList;
	private JCheckBox checkBoxDisallowNewValues = new JCheckBox("Disallow new values");
	private JCheckBox checkBoxMemorizeNewValue = new JCheckBox("Memorize new values");
	private JLabel defaultPropValLbl = new JLabel("Cliniass observare!");
	private JButton editDefaultPropValueBtn = new JButton("Set default value", ImageIconProxy.getIcon("edit"));
	private JButton delDefaultPropValueBtn = new JButton("Delete default value", ImageIconProxy.getIcon("del"));

	public ProjectSettingsDialog(Frame owner, Route rootRoutes)
	{
		super(owner, "Project settings", true);
		this.rootRoutes = rootRoutes;

		parent = owner;
		buildUI();
		addListeners();

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

		//Project settings
		JPanel panel = new JPanel();
		panel.add(new JLabel(settingsSections[0]));
		settingsPanel.add(panel, settingsSections[0]);

		//user defined properties
		settingsPanel.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, buildUserDefinedPropertiesPanel(), buildUserDefinedPropertyValuesPanel()), settingsSections[1]);


		mainPanel.add(settingsPanel, BorderLayout.CENTER);

		panel = new JPanel();
		panel.add(exitButton);
		mainPanel.add(panel, BorderLayout.SOUTH);


		settingsSectionList.setSelectedIndex(1);
		cardLayout.show(settingsPanel, settingsSections[1]);
		setContentPane(mainPanel);
	}

	private Box buildUserDefinedPropertiesPanel()
	{
		Box mainPanel = Box.createVerticalBox();

		JLabel headLbl = new JLabel(settingsSections[1] + ":");
		headLbl.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(headLbl);

		Box vBox = Box.createVerticalBox();
		vBox.setAlignmentX(CENTER_ALIGNMENT);
		addPropBtn.setMaximumSize(new Dimension(208,34));
		addPropBtn.setAlignmentX(CENTER_ALIGNMENT);
		editPropBtn.setMaximumSize(new Dimension(208,34));
		editPropBtn.setAlignmentX(CENTER_ALIGNMENT);
		delPropBtn.setMaximumSize(new Dimension(208,34));
		delPropBtn.setAlignmentX(CENTER_ALIGNMENT);

		vBox.add(addPropBtn);
		vBox.add(editPropBtn);
		vBox.add(delPropBtn);
		propList = new JComboBox<String>(Project.getActiveProject().getUserRoutesPropertiesNames());
		propList.setMaximumSize(new Dimension(99999999,34));
		vBox.add(propList);

		mainPanel.add(vBox);

		vBox = Box.createVerticalBox();
		vBox.setAlignmentX(CENTER_ALIGNMENT);
		vBox.add(checkBoxDisallowNewValues);
		vBox.add(checkBoxMemorizeNewValue);
		vBox.add(new JLabel("Default value:"));
		vBox.add(defaultPropValLbl);

		mainPanel.add(vBox);

		vBox = Box.createVerticalBox();
		vBox.setAlignmentX(CENTER_ALIGNMENT);
		editDefaultPropValueBtn.setAlignmentX(CENTER_ALIGNMENT);
		editDefaultPropValueBtn.setMaximumSize(new Dimension(208,34));
		delDefaultPropValueBtn.setAlignmentX(CENTER_ALIGNMENT);
		delDefaultPropValueBtn.setMaximumSize(new Dimension(208,34));

		vBox.add(editDefaultPropValueBtn);
		vBox.add(delDefaultPropValueBtn);

		mainPanel.add(vBox);

		editPropBtn.setEnabled(false);
		delPropBtn.setEnabled(false);
		checkBoxDisallowNewValues.setEnabled(false);
		checkBoxMemorizeNewValue.setEnabled(false);
		editDefaultPropValueBtn.setEnabled(false);
		delDefaultPropValueBtn.setEnabled(false);

		return mainPanel;
	}

	private Box buildUserDefinedPropertyValuesPanel()
	{
		Box mainPanel = Box.createVerticalBox();
		JLabel headLabel = new JLabel("Available values:");
		headLabel.setAlignmentX(CENTER_ALIGNMENT);

		mainPanel.add(headLabel);
		mainPanel.add(new JScrollPane(valueList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));

		JButton addValueBtn = new JButton("Add value", ImageIconProxy.getIcon("add"));
		JButton renameValueBtn = new JButton("Rename value", ImageIconProxy.getIcon("edit"));
		JButton deleteValueBtn = new JButton("Delete", ImageIconProxy.getIcon("del"));

		addValueBtn.setAlignmentX(CENTER_ALIGNMENT);
		addValueBtn.setMaximumSize(new Dimension(208,34));
		renameValueBtn.setAlignmentX(CENTER_ALIGNMENT);
		renameValueBtn.setMaximumSize(new Dimension(208,34));
		deleteValueBtn.setAlignmentX(CENTER_ALIGNMENT);
		deleteValueBtn.setMaximumSize(new Dimension(208,34));

		mainPanel.add(addValueBtn);
		mainPanel.add(renameValueBtn);
		mainPanel.add(deleteValueBtn);

		valueList.setEnabled(false);
		addValueBtn.setEnabled(false);
		renameValueBtn.setEnabled(false);
		deleteValueBtn.setEnabled(false);

		return mainPanel;
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

		addPropBtn.addActionListener(new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				String newProp = JOptionPane.showInputDialog(null, "Enter new property name:", "Add new property", JOptionPane.PLAIN_MESSAGE);
				if(newProp != null)
				{
					Project project = Project.getActiveProject();
					if(project.getUserRouteProperty(newProp) != null)
					{
						JOptionPane.showMessageDialog(null, "This property already exists.", "Cannot add property", JOptionPane.ERROR_MESSAGE);
						return;
					}

					String defaultValue = JOptionPane.showInputDialog(null, "Enter default value for property " + newProp + " :", "Add new property", JOptionPane.PLAIN_MESSAGE);
					if(defaultValue == null)
					{
						return;
					}
					System.out.println(newProp + " - "  +defaultValue);
				}
			}
		});
	}

	public boolean display()
	{
		setVisible(true);
		return changeMade;
	}
}