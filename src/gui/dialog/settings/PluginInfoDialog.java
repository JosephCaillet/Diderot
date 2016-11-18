package gui.dialog.settings;

import plugin.DiderotPlugin;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

/**
 * Dialog box displaying information about plugin.
 * @author joseph
 */
public class PluginInfoDialog extends JDialog
{
	private DiderotPlugin plugin;
	private JButton contactBtn = new JButton();
	private JButton closeBtn = new JButton("Close");
	private Component owner;

	/**
	 * @param owner Parent frame
	 * @param plugin Plugin containing information
	 */
	public PluginInfoDialog(Frame owner, DiderotPlugin plugin)
	{
		super(owner, true);
		this.plugin = plugin;
		this.owner = owner;

		setTitle(plugin.getPluginName() + " - v" + plugin.getPluginVersion());

		buildUI();
		pack();
		setMinimumSize(getSize());
		setLocationRelativeTo(owner);
	}

	/**
	 * Build user interface.
	 */
	private void buildUI()
	{
		Box box = Box.createVerticalBox();
		box.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));

		JLabel authorLbl = new JLabel("By " + plugin.getPluginAuthor(), SwingConstants.CENTER);
		authorLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		box.add(authorLbl);

		box.add(Box.createRigidArea(new Dimension(5, 5)));

		JLabel descLbl = new JLabel(plugin.getPluginDescription(), SwingConstants.CENTER);
		descLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		box.add(descLbl);

		box.add(Box.createRigidArea(new Dimension(5, 3)));

		contactBtn.setText(plugin.getPluginContactInformation());
		contactBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		box.add(contactBtn);

		box.add(Box.createRigidArea(new Dimension(5, 10)));

		closeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		box.add(closeBtn);

		add(box);

		addListener();
	}

	/**
	 * Add listeners.
	 */
	private void addListener()
	{
		contactBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				URI url;
				try
				{
					url = new URI(plugin.getPluginContactInformation());
					//if able to open by a program
					Desktop.getDesktop().browse(url);
				}
				catch(Exception exception)
				{
					//if not url
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(plugin.getPluginContactInformation()), null);
					JOptionPane.showMessageDialog(owner, "Contact information copied in clipboard.", "Copied contact information", JOptionPane.PLAIN_MESSAGE);
				}
			}
		});

		closeBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
		});
	}

	/**
	 * Show dialog box
	 */
	public void display()
	{
		closeBtn.requestFocusInWindow();
		super.setVisible(true);
	}
}