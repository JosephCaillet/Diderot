package gui.dialog;

import model.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by joseph on 24/09/16.
 */
public class ResponseEditionDialog extends JDialog
{
	private Response response;
	private Boolean editMade = false;
	private JTextArea description = new JTextArea(), schema = new JTextArea();
	private JButton okBtn = new JButton("Save"), cancelBtn = new JButton("Cancel");

	public ResponseEditionDialog(Container owner, String responseName, Response response)
	{
		super((Frame) owner, "Editing of response: " + responseName, true);
		this.response = response;
		buildUI();

		setMinimumSize(new Dimension(300, 300));
		pack();
		setLocationRelativeTo(owner);
	}

	private void buildUI()
	{
		setLayout(new BorderLayout());

		//description.setLineWrap(true);
		description.setTabSize(2);
		description.setText(response.getDescription());

		schema.setTabSize(2);
		schema.setFont(new Font(Font.MONOSPACED, Font.PLAIN, schema.getFont().getSize()- 2));
		schema.setOpaque(true);
		Color color = schema.getBackground();
		schema.setOpaque(true);
		schema.setBackground(description.getForeground().darker());
		schema.setForeground(color);
		schema.setCaretColor(color);
		schema.setText(response.getSchema());

		JPanel descriptionPanel = new JPanel(new BorderLayout()),
				schemaPanel = new JPanel(new BorderLayout()),
				buttonPanel = new JPanel();

		descriptionPanel.add(new JLabel("Description:"), BorderLayout.NORTH);
		descriptionPanel.add(new JScrollPane(description), BorderLayout.CENTER);

		schemaPanel.add(new JLabel("Schema:"), BorderLayout.NORTH);
		schemaPanel.add(new JScrollPane(schema), BorderLayout.CENTER);

		buttonPanel.add(okBtn);
		buttonPanel.add(cancelBtn);
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, descriptionPanel, schemaPanel);
		splitPane.setDividerLocation(0.5);
		add(splitPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		addListener();
	}

	private void addListener()
	{
		okBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(!response.getDescription().equals(description.getText()) || !response.getSchema().equals(schema.getText()))
				{
					editMade = true;
				}
				setVisible(false);
			}
		});

		cancelBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
		});
	}

	public boolean display()
	{
		setVisible(true);
		if(editMade)
		{
			response.setDescription(description.getText());
			response.setSchema(schema.getText());
		}

		return editMade;
	}
}