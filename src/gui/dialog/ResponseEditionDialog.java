package gui.dialog;

import gui.ImageIconProxy;
import gui.TextLineNumber;
import model.NewLineFilter;
import model.Response;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialog for editing response description and schema in wider window.
 * @author joseph
 */
public class ResponseEditionDialog extends JDialog
{
	private Response response;
	private Boolean editMade = false;
	private JTextArea description = new JTextArea(),
			schema = new JTextArea();
	private JButton okBtn = new JButton("Save", ImageIconProxy.getIcon("check")),
			cancelBtn = new JButton("Cancel", ImageIconProxy.getIcon("cancel"));

	/**
	 * @param owner Parent frame
	 * @param responseName Name of the response to edit
	 * @param response Response to edit
	 */
	public ResponseEditionDialog(Container owner, String responseName, Response response)
	{
		super((Frame) owner, "Editing of response: " + responseName, true);
		this.response = response;
		buildUI();

		setMinimumSize(new Dimension(300, 300));
		pack();
		setLocationRelativeTo(owner);
	}

	/**
	 * Build user interface.
	 */
	private void buildUI()
	{
		setLayout(new BorderLayout());

		//description.setLineWrap(true);
		description.setTabSize(2);
		description.setText(response.getDescription());
		description.setCaretPosition(0);

		schema.setTabSize(2);
		schema.setFont(new Font(Font.MONOSPACED, Font.PLAIN, schema.getFont().getSize()- 2));
		schema.setOpaque(true);
		Color color = schema.getBackground();
		schema.setOpaque(true);
		schema.setBackground(description.getForeground().darker());
		schema.setForeground(color);
		schema.setCaretColor(color);
		schema.setText(response.getSchema());
		schema.setCaretPosition(0);

		JPanel descriptionPanel = new JPanel(new BorderLayout()),
				schemaPanel = new JPanel(new BorderLayout()),
				buttonPanel = new JPanel();

		descriptionPanel.add(new JLabel("Description:"), BorderLayout.NORTH);
		descriptionPanel.add(new JScrollPane(description), BorderLayout.CENTER);

		schemaPanel.add(new JLabel("Schema:"), BorderLayout.NORTH);
		JScrollPane scrollPane = new JScrollPane(schema);
		scrollPane.setRowHeaderView(new TextLineNumber(schema));
		AbstractDocument doc = (AbstractDocument) schema.getDocument();
		doc.setDocumentFilter(new NewLineFilter());
		schemaPanel.add(scrollPane, BorderLayout.CENTER);

		buttonPanel.add(cancelBtn);
		buttonPanel.add(okBtn);
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, descriptionPanel, schemaPanel);
		splitPane.setDividerLocation(0.5);
		add(splitPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		addListener();
	}

	/**
	 * Add listeners.
	 */
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

	/**
	 * Display the dialog
	 * @return true if changed made, false otherwise.
	 */
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