package gui.dialog.settings;

import gui.ImageIconProxy;
import model.Project;
import model.Route;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by joseph on 28/09/16.
 */
public class ResponsePanel extends JPanel
{
	private Route rootRoute;
	private Frame parent;

	private JButton addRespFormatBtn = new JButton("Add response format", ImageIconProxy.getIcon("add")),
			editRespFormatBtn = new JButton("Rename response format", ImageIconProxy.getIcon("edit")),
			delRespFormatBtn = new JButton("Delete response format", ImageIconProxy.getIcon("del"));
	private JList respFormatList = new JList(Project.getActiveProject().getResponsesFormat());
	private JComboBox defaultRespFormat = new JComboBox(new DefaultComboBoxModel(Project.getActiveProject().getResponsesFormat()));


	public ResponsePanel(Frame parent, Route rootRoute)
	{
		super();
		this.rootRoute = rootRoute;
		this.parent = parent;

		buildUI();
	}

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

		enableButton(false);
		addListeners();
	}

	private void enableButton(boolean enabled)
	{
		editRespFormatBtn.setEnabled(enabled);
		delRespFormatBtn.setEnabled(enabled);
		defaultRespFormat.setEnabled(enabled);

		if(!enabled)
		{
			defaultRespFormat.removeAllItems();
		}
	}

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

					respFormatList.setModel(new DefaultComboBoxModel(Project.getActiveProject().getResponsesFormat()));
					respFormatList.setSelectedValue(newRespFormat, true);
				}
			}

		});
	}
}