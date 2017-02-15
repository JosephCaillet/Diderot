package gui;

import model.Project;
import model.Route;

import javax.swing.*;
import java.awt.*;

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
		super(new BorderLayout());
		buildUI();
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

		add(topPanel, BorderLayout.NORTH);
		add(bottomPanel, BorderLayout.SOUTH);
	}

	public void setRoute(Route route)
	{
		this.route = route;

		name.setText(route.getName());

		typeCombo.setModel(new DefaultComboBoxModel(Project.getActiveProject().getParamsTypes()));
		typeCombo.setSelectedItem(route.getUrlParamType());

		subTypeCombo.setModel(new DefaultComboBoxModel(Project.getActiveProject().getSubParamsTypes(route.getUrlParamType())));
		typeCombo.setSelectedItem(route.getUrlParamSubType());

		description.setText(route.getUrlParamDescription());
	}
}