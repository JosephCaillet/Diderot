package gui.dialog.settings;

import gui.ImageIconProxy;
import model.Project;
import model.Route;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Panel displaying user defined properties.
 * @author joseph
 */
public class UserDefinedPropertiesPanel extends JSplitPane
{
	private Frame parent;
	private Route rootRoutes;

	private JList<String> valueList = new JList<String>();
	private JButton addPropBtn = new JButton("Add property", ImageIconProxy.getIcon("add")),
			editPropBtn = new JButton("Rename property", ImageIconProxy.getIcon("edit")),
			delPropBtn = new JButton("Delete property", ImageIconProxy.getIcon("del"));
	private JComboBox<String> propList;
	private JCheckBox checkBoxDisallowNewValues = new JCheckBox("Disallow new values"),
			checkBoxMemorizeNewValue = new JCheckBox("Memorize new values");
	private JLabel defaultPropValLbl = new JLabel("Nothing to display");
	private JButton changeDefaultPropValueBtn = new JButton("Change default value", ImageIconProxy.getIcon("edit")),
			addValueBtn = new JButton("Add value", ImageIconProxy.getIcon("add")),
			renameValueBtn = new JButton("Rename value", ImageIconProxy.getIcon("edit")),
			deleteValueBtn = new JButton("Delete value", ImageIconProxy.getIcon("del"));

	/**
	 * @param parent Parent frame
	 * @param rootRoutes Root route
	 */
	public UserDefinedPropertiesPanel(Frame parent, Route rootRoutes)
	{
		super(HORIZONTAL_SPLIT, true);

		this.parent = parent;
		this.rootRoutes = rootRoutes;

		add(buildUserDefinedPropertiesPanel());
		add(buildUserDefinedPropertyValuesPanel());
		addListeners();

		if(Project.getActiveProject().getUserRoutesPropertiesNames().length != 0)
		{
			propList.setSelectedIndex(0);
		}
	}

	/**
	 * Build user defined properties panel
	 * @return Build panel
	 */
	private Box buildUserDefinedPropertiesPanel()
	{
		Box mainPanel = Box.createVerticalBox();

		JLabel headLbl = new JLabel("User defined properties:");
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
		propList.setMaximumSize(new Dimension(99999999, 34));
		vBox.add(propList);

		mainPanel.add(vBox);

		vBox = Box.createVerticalBox();
		vBox.setAlignmentX(CENTER_ALIGNMENT);
		vBox.add(checkBoxDisallowNewValues);
		vBox.add(checkBoxMemorizeNewValue);
		vBox.add(new JLabel("Default value:"));
		defaultPropValLbl.setOpaque(true);
		defaultPropValLbl.setBackground(defaultPropValLbl.getBackground().brighter());
		defaultPropValLbl.setHorizontalAlignment(SwingConstants.CENTER);
		defaultPropValLbl.setBorder(BorderFactory.createLineBorder(getBackground().darker(), 3));
		JPanel panTemp = new JPanel(new BorderLayout());
		panTemp.add(defaultPropValLbl, BorderLayout.CENTER);
		panTemp.setAlignmentX(LEFT_ALIGNMENT);
		panTemp.setMaximumSize(new Dimension(999999999, 30));

		vBox.add(panTemp);

		mainPanel.add(vBox);

		vBox = Box.createVerticalBox();
		vBox.setAlignmentX(CENTER_ALIGNMENT);
		changeDefaultPropValueBtn.setAlignmentX(CENTER_ALIGNMENT);
		changeDefaultPropValueBtn.setMaximumSize(new Dimension(208,34));

		vBox.add(changeDefaultPropValueBtn);

		mainPanel.add(vBox);

		editPropBtn.setEnabled(false);
		delPropBtn.setEnabled(false);
		checkBoxDisallowNewValues.setEnabled(false);
		checkBoxMemorizeNewValue.setEnabled(false);
		changeDefaultPropValueBtn.setEnabled(false);

		return mainPanel;
	}

	/**
	 * Build user defined property values panel
	 * @return Build panel
	 */
	private Box buildUserDefinedPropertyValuesPanel()
	{
		Box mainPanel = Box.createVerticalBox();
		JLabel headLabel = new JLabel("Available values:");
		headLabel.setAlignmentX(CENTER_ALIGNMENT);

		mainPanel.add(headLabel);
		mainPanel.add(new JScrollPane(valueList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));

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

	/**
	 * Add listeners
	 */
	private void addListeners()
	{
		//project settings

		//user defined properties
		addPropBtn.addActionListener(new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				String newProp = JOptionPane.showInputDialog(parent, "Enter new property name:", "Add new property", JOptionPane.PLAIN_MESSAGE);
				if(newProp != null)
				{
					if(newProp.isEmpty())
					{
						JOptionPane.showMessageDialog(parent, "A property cannot have an empty name.", "Cannot add property", JOptionPane.ERROR_MESSAGE);
						return;
					}

					Project project = Project.getActiveProject();
					if(project.getUserRouteProperty(newProp) != null)
					{
						JOptionPane.showMessageDialog(parent, "This property already exists.", "Cannot add property", JOptionPane.ERROR_MESSAGE);
						return;
					}

					String defaultValue = JOptionPane.showInputDialog(parent, "Enter default value for property " + newProp + " :", "Add new property", JOptionPane.PLAIN_MESSAGE);
					if(defaultValue == null)
					{
						return;
					}

					project.addUserRouteProperty(newProp, defaultValue);
					rootRoutes.addUserProperty(newProp, defaultValue);
					propList.setModel(new DefaultComboBoxModel<String>(project.getUserRoutesPropertiesNames()));
					propList.setSelectedItem(newProp);
				}
			}
		});

		editPropBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				String oldName = (String) propList.getSelectedItem();
				String newName = (String) JOptionPane.showInputDialog(parent, "Enter new name for " + oldName + " property:"
						, "Rename property", JOptionPane.PLAIN_MESSAGE, null, null, oldName);
				if(newName != null)
				{
					Project project = Project.getActiveProject();
					if(project.getUserRouteProperty(newName) != null)
					{
						JOptionPane.showMessageDialog(parent, "A property with this name already exists.", "Cannot rename property", JOptionPane.ERROR_MESSAGE);
						return;
					}

					String defaultValue = project.getUserRouteProperty(oldName).getDefaultValue();
					project.removeUserRouteProperty(oldName);
					project.addUserRouteProperty(newName, defaultValue);

					rootRoutes.renameUserProperty(oldName, newName);
					propList.setModel(new DefaultComboBoxModel<String>(project.getUserRoutesPropertiesNames()));
					propList.setSelectedItem(newName);
				}
			}
		});

		delPropBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				String propName = (String) propList.getSelectedItem();
				if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(parent,
						"Are you sure you want to delete the f  ollowing property?\n" + propName,
						"Delete property", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE))
				{
					Project project = Project.getActiveProject();
					project.removeUserRouteProperty(propName);

					int lastIndex = propList.getSelectedIndex();

					rootRoutes.removeUserProperty(propName);
					propList.setModel(new DefaultComboBoxModel<String>(project.getUserRoutesPropertiesNames()));

					if(lastIndex == 0 && project.getUserRoutesPropertiesNames().length !=0)
					{
						lastIndex++;
					}
					propList.setSelectedIndex(lastIndex - 1);
				}
			}
		});

		propList.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				if(propList.getSelectedIndex() != -1)
				{
					editPropBtn.setEnabled(true);
					delPropBtn.setEnabled(true);
					checkBoxDisallowNewValues.setEnabled(true);


					Project.UserDefinedRouteProperty userDefinedRouteProperty
							= Project.getActiveProject().getUserRouteProperty((String) propList.getSelectedItem());
					defaultPropValLbl.setText(userDefinedRouteProperty.getDefaultValue());
					changeDefaultPropValueBtn.setEnabled(true);

					checkBoxMemorizeNewValue.setSelected(userDefinedRouteProperty.isValuesMemorized());
					checkBoxDisallowNewValues.setSelected(userDefinedRouteProperty.isNewValuesDisabled());
					checkBoxMemorizeNewValue.setEnabled(!userDefinedRouteProperty.isNewValuesDisabled());

					valueList.setListData(userDefinedRouteProperty.getValues());
					valueList.setEnabled(true);
					valueList.setSelectedIndex(0);
				}
				else
				{
					editPropBtn.setEnabled(false);
					delPropBtn.setEnabled(false);
					checkBoxDisallowNewValues.setEnabled(false);
					checkBoxMemorizeNewValue.setEnabled(false);
					changeDefaultPropValueBtn.setEnabled(false);
					valueList.setEnabled(false);
					addValueBtn.setEnabled(false);
					renameValueBtn.setEnabled(false);
					deleteValueBtn.setEnabled(false);

					valueList.setListData(new String[0]);

					defaultPropValLbl.setText("Nothing to display");
				}
			}
		});

		checkBoxDisallowNewValues.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				if(checkBoxDisallowNewValues.isSelected())
				{
					if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(parent,
							"Doing this will replace every non memorized values by the default value in all routes.\nAre you sure this is what you want?",
							"Update the route", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE))
					{
						rootRoutes.removeForbiddenUserPropertyValues((String) propList.getSelectedItem());
						checkBoxMemorizeNewValue.setEnabled(false);
						Project.getActiveProject().getUserRouteProperty((String) propList.getSelectedItem()).setNewValuesDisabled(true);
					}
					else
					{
						checkBoxDisallowNewValues.setSelected(false);
					}
				}
				else
				{
					checkBoxMemorizeNewValue.setEnabled(true);
					Project.getActiveProject().getUserRouteProperty((String) propList.getSelectedItem()).setNewValuesDisabled(false);
				}
			}
		});
//TODO: disable listener when the program itself act on component to set their state (typicaly jcheckbox)
		checkBoxMemorizeNewValue.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent itemEvent)
			{
				if(checkBoxMemorizeNewValue.isSelected())
				{
					Project.getActiveProject().getUserRouteProperty((String) propList.getSelectedItem()).setValuesMemorized(true);
				}
				else
				{
					Project.getActiveProject().getUserRouteProperty((String) propList.getSelectedItem()).setValuesMemorized(false);
				}
			}
		});

		changeDefaultPropValueBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				String propName = (String) propList.getSelectedItem();
				Project.UserDefinedRouteProperty userDefinedRouteProperty =  Project.getActiveProject().getUserRouteProperty(propName);
				String oldDef = userDefinedRouteProperty.getDefaultValue();

				String newDef = (String) JOptionPane.showInputDialog(parent, "Change default value of property: " + propName + "\nfrom: " + oldDef + "\nto:",
						"Change default value", JOptionPane.PLAIN_MESSAGE, null, userDefinedRouteProperty.getValues(), oldDef);

				if(newDef != null)
				{
					if(newDef.equals(oldDef))
					{
						JOptionPane.showMessageDialog(parent, "Please choose a different value from the previous.");
						return;
					}

					userDefinedRouteProperty.setDefaultValue(newDef);
					defaultPropValLbl.setText(newDef);

					if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(parent, "Replace the old default value with the new one in all routes?", "Update the route",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE))
					{
						rootRoutes.changeUserPropertyValue(propName, oldDef, newDef);
					}
				}
			}
		});

		valueList.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent listSelectionEvent)
			{
				if(valueList.getSelectedIndex() == -1)
				{
					addValueBtn.setEnabled(false);
					renameValueBtn.setEnabled(false);
					deleteValueBtn.setEnabled(false);
				}
				else
				{
					addValueBtn.setEnabled(true);
					renameValueBtn.setEnabled(true);
					deleteValueBtn.setEnabled(true);
				}
			}
		});

		addValueBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				String newVal = JOptionPane.showInputDialog(parent, "Enter new value name:", "Add new value", JOptionPane.PLAIN_MESSAGE);
				if(newVal != null)
				{
					Project.UserDefinedRouteProperty userDefinedRouteProperty = Project.getActiveProject().getUserRouteProperty((String) propList.getSelectedItem());
					if(userDefinedRouteProperty.contains(newVal))
					{
						JOptionPane.showMessageDialog(parent, "This value already exists.", "Cannot add value", JOptionPane.ERROR_MESSAGE);
						return;
					}

					userDefinedRouteProperty.add(newVal);
					valueList.setModel(new DefaultComboBoxModel<String>(userDefinedRouteProperty.getValues()));
					valueList.setSelectedValue(newVal, true);
				}
			}
		});

		renameValueBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				Project.UserDefinedRouteProperty userDefinedRouteProperty = Project.getActiveProject().getUserRouteProperty((String) propList.getSelectedItem());
				String prop = (String) propList.getSelectedItem();
				String oldVal = valueList.getSelectedValue();
				boolean defValRenamed = false;

				if(userDefinedRouteProperty.getDefaultValue().equals(oldVal))
				{
					JOptionPane.showMessageDialog(parent, "You are about to rename the default value for the property " + prop +".\nBy doing so you will change the current default value, and all routes\nusing the default value will also be affected.",
							"Rename default property", JOptionPane.WARNING_MESSAGE);
					defValRenamed = true;

				}

				String newVal = JOptionPane.showInputDialog(parent, "Replace value " + oldVal + " of property " + prop + " with:"
						, "Rename value", JOptionPane.PLAIN_MESSAGE);
				if(newVal != null)
				{

					if(userDefinedRouteProperty.contains(newVal))
					{
						JOptionPane.showMessageDialog(parent, "This value already exists.", "Cannot add value", JOptionPane.ERROR_MESSAGE);
						return;
					}

					if(defValRenamed)
					{
						userDefinedRouteProperty.setDefaultValue(newVal);
						defaultPropValLbl.setText(newVal);
					}

					userDefinedRouteProperty.remove(oldVal);
					userDefinedRouteProperty.add(newVal);
					valueList.setModel(new DefaultComboBoxModel<String>(userDefinedRouteProperty.getValues()));
					valueList.setSelectedValue(newVal, true);
					rootRoutes.changeUserPropertyValue(prop, oldVal, newVal);
				}
			}
		});

		deleteValueBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				Project.UserDefinedRouteProperty userDefinedRouteProperty = Project.getActiveProject().getUserRouteProperty((String) propList.getSelectedItem());
				String prop = (String) propList.getSelectedItem();
				String oldVal = valueList.getSelectedValue();

				if(userDefinedRouteProperty.getDefaultValue().equals(oldVal))
				{
					JOptionPane.showMessageDialog(parent, "You cannot remove the default property.\nIf you really want to remove this value, you should\nswitch to an other default value before.",
							"Cannot remove property", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(parent, "Are you sure you want to remove " + oldVal + " from property " + prop + " ?\nAll route using this value will use default value instead.",
						"Remove value", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE))
				{
					userDefinedRouteProperty.remove(oldVal);
					valueList.setModel(new DefaultComboBoxModel<String>(userDefinedRouteProperty.getValues()));
					valueList.setSelectedIndex(0);
					rootRoutes.changeUserPropertyValue(prop, oldVal, userDefinedRouteProperty.getDefaultValue());
				}
			}
		});
	}
}