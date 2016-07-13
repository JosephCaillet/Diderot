package gui;

import model.HttpMethod;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * Created by joseph on 10/07/16.
 */
public class MethodPanel extends JPanel implements Scrollable
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
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JLabel jLabel = new JLabel("Method description:");
		jLabel.setAlignmentX(LEFT_ALIGNMENT);
		add(jLabel);

		description.setAlignmentX(LEFT_ALIGNMENT);
		description.setBorder(BorderFactory.createLineBorder(getBackground().darker(), 1));
		description.setLineWrap(true);
		add(description);

		jLabel = new JLabel("Parameters:");
		jLabel.setAlignmentX(LEFT_ALIGNMENT);
		add(jLabel);

		JTable jTable = new JTable(httpMethod);
		JScrollPane comp = new JScrollPane(jTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		comp.setAlignmentX(LEFT_ALIGNMENT);
		comp.setPreferredSize(new Dimension(50,100));
		add(comp);

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


	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		return null;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle rectangle, int i, int i1)
	{
		return 10;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle rectangle, int i, int i1)
	{
		return 10;
	}

	@Override
	public boolean getScrollableTracksViewportWidth()
	{
		return true;
	}

	@Override
	public boolean getScrollableTracksViewportHeight()
	{
		return false;
	}
}