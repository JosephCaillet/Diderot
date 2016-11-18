package gui.dialog.settings;

import gui.ImageIconProxy;
import model.Project;
import model.Route;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Panel displaying available response type.
 * @author joseph
 */
public class ResponsePanel extends JPanel
{
	private Route rootRoute;
	private Frame parent;

	private JButton addRespFormatBtn = new JButton("Add response format", ImageIconProxy.getIcon("add")),
			editRespFormatBtn = new JButton("Rename response format", ImageIconProxy.getIcon("edit")),
			delRespFormatBtn = new JButton("Delete response format", ImageIconProxy.getIcon("del"));
	private JList<String> respFormatList = new JList<String>(Project.getActiveProject().getResponsesFormat());
	private JComboBox<String> defaultRespFormat = new JComboBox<String>(new DefaultComboBoxModel<String>(Project.getActiveProject().getResponsesFormat()));
	private Boolean enableComboListener = false;

	/**
	 * @param parent Parent Frame
	 * @param rootRoute Root route
	 */
	public ResponsePanel(Frame parent, Route rootRoute)
	{
		super();
		this.rootRoute = rootRoute;
		this.parent = parent;

		buildUI();
	}

	/**
	 * Build user interface
	 */
	private void buildUI()
	{
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setAlignmentX(CENTER_ALIGNMENT);

		JLabel headLbl = new JLabel("Response output format:");
		headLbl.setAlignmentX(CENTER_ALIGNMENT);
		add(headLbl);

		add(new JScrollPane(respFormatList));
		JLabel lbl = new JLabel("Default response format:");
		lbl.setAlignmentX(CENTER_ALIGNMENT);
		add(lbl);

		defaultRespFormat.setAlignmentX(CENTER_ALIGNMENT);
		defaultRespFormat.setMaximumSize(new Dimension(99999999, 34));
		add(defaultRespFormat);

		addRespFormatBtn.setAlignmentX(CENTER_ALIGNMENT);
		addRespFormatBtn.setMaximumSize(new Dimension(208,34));
		add(addRespFormatBtn);
		editRespFormatBtn.setAlignmentX(CENTER_ALIGNMENT);
		editRespFormatBtn.setMaximumSize(new Dimension(208,34));
		add(editRespFormatBtn);
		delRespFormatBtn.setAlignmentX(CENTER_ALIGNMENT);
		delRespFormatBtn.setMaximumSize(new Dimension(208,34));
		add(delRespFormatBtn);

		addListeners();
	}

	/**
	 * Add listeners
	 */
	private void addListeners()
	{
		addRespFormatBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String newRespFormat = JOptionPane.showInputDialog(parent, "Enter new response format:", "Add new response format", JOptionPane.PLAIN_MESSAGE);
				if(newRespFormat != null)
				{
					if(!Project.getActiveProject().addResponseFormat(newRespFormat))
					{
						JOptionPane.showMessageDialog(parent, "This response format already exists.", "Cannot add response format", JOptionPane.ERROR_MESSAGE);
						return;
					}

					String[] responsesFormat = Project.getActiveProject().getResponsesFormat();
					respFormatList.setModel(new DefaultComboBoxModel<String>(responsesFormat));
					respFormatList.setSelectedValue(newRespFormat, true);

					enableComboListener = false;
					defaultRespFormat.addItem(newRespFormat);
					defaultRespFormat.setSelectedItem(Project.getActiveProject().getDefaultResponseFormat());
					enableComboListener = true;
				}
			}

		});

		editRespFormatBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String oldRespFormat = respFormatList.getSelectedValue();
				String newRespFormat = JOptionPane.showInputDialog(parent, "Rename response format:\nfrom: " + oldRespFormat +"\nto:", "Rename response format", JOptionPane.PLAIN_MESSAGE);

				if(newRespFormat != null)
				{
					if(!Project.getActiveProject().renameResponseFormat(oldRespFormat, newRespFormat))
					{
						JOptionPane.showMessageDialog(parent, "This response format already exists.", "Cannot rename response format", JOptionPane.ERROR_MESSAGE);
						return;
					}

					rootRoute.renameResponseFormatValue(oldRespFormat, newRespFormat);

					respFormatList.setModel(new DefaultComboBoxModel<String>(Project.getActiveProject().getResponsesFormat()));
					respFormatList.setSelectedValue(newRespFormat, true);

					enableComboListener = false;
					defaultRespFormat.addItem(newRespFormat);
					defaultRespFormat.removeItem(oldRespFormat);
					defaultRespFormat.setSelectedItem(Project.getActiveProject().getDefaultResponseFormat());
					enableComboListener = true;
				}
			}

		});

		delRespFormatBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String responseFormat = respFormatList.getSelectedValue();
				Project activeProject = Project.getActiveProject();

				if(responseFormat.equals(activeProject.getDefaultResponseFormat()))
				{
					JOptionPane.showMessageDialog(parent, "You cannot delete the default value.", "Cannot delete response format", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(parent,
						"Are you sure you want to delete the following response format?\n" + responseFormat + "\nDoing so will replace the deleted response format with the default value.",
						"Delete response format", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE))
				{
					rootRoute.renameResponseFormatValue(responseFormat, activeProject.getDefaultResponseFormat());
					activeProject.removeResponseFormat(responseFormat);

					respFormatList.setModel(new DefaultComboBoxModel<String>(activeProject.getResponsesFormat()));

					respFormatList.setSelectedIndex(0);

					enableComboListener = false;
					defaultRespFormat.removeItem(responseFormat);
					defaultRespFormat.setSelectedItem(Project.getActiveProject().getDefaultResponseFormat());
					enableComboListener = true;
				}
			}
		});

		defaultRespFormat.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if(!enableComboListener || e.getStateChange() != ItemEvent.SELECTED)
				{
					return;
				}

				defaultRespFormat.setEnabled(false);

				if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(parent,
						"Replace the old default value with the new one in all routes?",
						"Rename default value", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE))
				{
					rootRoute.renameResponseFormatValue(Project.getActiveProject().getDefaultResponseFormat(), (String) e.getItem());
				}

				Project.getActiveProject().setDefaultResponseFormat((String) e.getItem());

				defaultRespFormat.setEnabled(true);
			}
		});

		enableComboListener = false;
		defaultRespFormat.setModel(new DefaultComboBoxModel<String>(Project.getActiveProject().getResponsesFormat()));
		defaultRespFormat.setSelectedItem(Project.getActiveProject().getDefaultResponseFormat());
		respFormatList.setSelectedIndex(0);
		enableComboListener = true;
	}
}