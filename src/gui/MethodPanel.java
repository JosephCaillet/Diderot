package gui;

import model.HttpMethod;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * Created by joseph on 10/07/16.
 */
public class MethodPanel extends JPanel
{
	private HttpMethod httpMethod;
	private JTextArea description = new JTextArea();

	public MethodPanel(HttpMethod httpMethod)
	{
		super();
		this.httpMethod = httpMethod;
		description.setText(httpMethod.getDescription());
		buildUI();
	}

	private void buildUI()
	{
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridy = gbc.gridx = 0;
		gbc.weightx = 1;
		gbc.insets = new Insets(0,5,0,5);
		gbc.fill = GridBagConstraints.BOTH;

		add(new JLabel("Method description:"), gbc);
		gbc.gridy++;

		description.setBorder(BorderFactory.createLineBorder(getBackground().darker(), 1));
		add(description, gbc);

		addListener();
	}

	private void addListener()
	{
		description.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void insertUpdate(DocumentEvent documentEvent)
			{
				httpMethod.setDescription(description.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent documentEvent)
			{
				insertUpdate(documentEvent);
			}

			@Override
			/**
			 * Not implemented
			 */
			public void changedUpdate(DocumentEvent documentEvent)
			{
				//This not the implementation you are looking for.
			}
		});
	}
}