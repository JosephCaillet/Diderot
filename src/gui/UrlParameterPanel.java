package gui;

import model.Project;
import model.Route;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Panel to edit url
 * @author joseph
 * Created by joseph on 15/02/17.
 */
public class UrlParameterPanel extends JPanel
{
	private Route route;
	private JComboBox typeCombo = new JComboBox(),
			subTypeCombo = new JComboBox();
	private JTextField description = new JTextField();
	private JLabel name = new JLabel();

	public UrlParameterPanel()
	{
		super(new BorderLayout(0, 2));
		buildUI();
		addListeners();
	}

	private void buildUI()
	{
		Box topPanel = Box.createHorizontalBox();

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("Name:   "), BorderLayout.WEST);
		panel.add(name, BorderLayout.CENTER);
		topPanel.add(panel);

		panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("  Type: "), BorderLayout.WEST);
		panel.add(typeCombo, BorderLayout.CENTER);
		topPanel.add(panel);

		panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("  Sub-type: "), BorderLayout.WEST);
		panel.add(subTypeCombo, BorderLayout.CENTER);
		topPanel.add(panel);

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(new JLabel("Description: "), BorderLayout.WEST);
		bottomPanel.add(description, BorderLayout.CENTER);

		add(new JLabel("Url parameter:"), BorderLayout.NORTH);
		add(topPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
	}

	private void addListeners()
	{
		description.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				route.setUrlParamDescription(description.getText());
			}
		});

		typeCombo.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if(e.getStateChange() == ItemEvent.SELECTED)
				{
					route.setUrlParamType((String) typeCombo.getSelectedItem());
					subTypeCombo.setModel(new DefaultComboBoxModel(Project.getActiveProject().getSubParamsTypes(route.getUrlParamType())));
				}
			}
		});

		subTypeCombo.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if(e.getStateChange() == ItemEvent.SELECTED)
				{
					route.setUrlParamSubType((String) subTypeCombo.getSelectedItem());
					//System.out.println(route.getUrlParamSubType());
				}
			}
		});
	}

	public void setRoute(Route route)
	{
		this.route = route;

		name.setText(route.getUrlParamName());

		typeCombo.setModel(new DefaultComboBoxModel(Project.getActiveProject().getParamsTypes()));
		typeCombo.setSelectedItem(route.getUrlParamType());

		subTypeCombo.setModel(new DefaultComboBoxModel(Project.getActiveProject().getSubParamsTypes(route.getUrlParamType())));
		subTypeCombo.setSelectedItem(route.getUrlParamSubType());

		description.setText(route.getUrlParamDescription());
	}
}