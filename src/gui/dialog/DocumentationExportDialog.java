package gui.dialog;

import gui.ImageIconProxy;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Configuration dialog for documentation export.
 */
public class DocumentationExportDialog extends JDialog
{
	private JLabel exportLocation;
	private JLabel logo;
	private String logoLocation;
	private JCheckBox openInBrowserAsked;
	private JButton browseLocationBtn = new JButton("Browse", ImageIconProxy.getIcon("open")),
			browseLogoBtn = new JButton("Browse", ImageIconProxy.getIcon("open"));

	private boolean exportAsked = false;
	private JButton exportBtn = new JButton("Export", ImageIconProxy.getIcon("exportweb")),
			cancelBtn = new JButton("Cancel", ImageIconProxy.getIcon("cancel"));
	private Container owner;

	/**
	 * @param owner Parent frame
	 * @param exportLocation Path to export folder
	 * @param logoLocation Path to project logo
	 * @param openInBrowserAsked Open generated doc in browser
	 */
	public DocumentationExportDialog(Container owner, String exportLocation, String logoLocation, boolean openInBrowserAsked)
	{
		super((Frame) owner, true);

		this.owner = owner;

		this.exportLocation = new JLabel(exportLocation, SwingConstants.CENTER);
		this.logoLocation = logoLocation;
		this.logo = new JLabel(new ImageIcon(logoLocation), SwingConstants.CENTER);
		this.openInBrowserAsked = new JCheckBox("Open generated documentation in browser", openInBrowserAsked);
		setTitle("Generate web Documentation");

		buildUI();
		pack();
		setMinimumSize(new Dimension(getWidth(), getHeight()));
		setLocationRelativeTo(owner);
	}

	/**
	 * Build UI.
	 */
	private void buildUI()
	{
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		JLabel jLabel = new JLabel("Generated documentation location:");
		jLabel.setAlignmentX(LEFT_ALIGNMENT);
		mainPanel.add(jLabel);
		JPanel exportPanel = new JPanel(new BorderLayout());
		exportPanel.setAlignmentX(LEFT_ALIGNMENT);
		exportPanel.add(exportLocation, BorderLayout.CENTER);
		exportPanel.add(browseLocationBtn, BorderLayout.EAST);
		mainPanel.add(exportPanel);

		jLabel = new JLabel("Project logo:");
		jLabel.setAlignmentX(LEFT_ALIGNMENT);
		mainPanel.add(jLabel);
		JPanel logoPanel = new JPanel(new BorderLayout());
		logoPanel.setAlignmentX(LEFT_ALIGNMENT);
		logoPanel.add(logo, BorderLayout.CENTER);
		logoPanel.add(browseLogoBtn, BorderLayout.EAST);
		mainPanel.add(logoPanel);

		JPanel openInBrowserPanel = new JPanel();
		openInBrowserPanel.setAlignmentX(LEFT_ALIGNMENT);
		openInBrowserPanel.add(openInBrowserAsked);
		mainPanel.add(openInBrowserPanel);

		JPanel btnPanel = new JPanel();
		btnPanel.setAlignmentX(LEFT_ALIGNMENT);
		btnPanel.add(cancelBtn);
		btnPanel.add(exportBtn);
		mainPanel.add(btnPanel);

		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		add(mainPanel);

		addListener();
	}

	/**
	 * Add listeners
	 */
	private void addListener()
	{
		browseLocationBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fileChooser = new JFileChooser(exportLocation.getText());
				fileChooser.setSelectedFile(new File("."));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if(JFileChooser.APPROVE_OPTION != fileChooser.showOpenDialog(owner))
				{
					return;
				}

				exportLocation.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		});

		browseLogoBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fileChooser = new JFileChooser(logoLocation);
				fileChooser.setFileFilter(new FileNameExtensionFilter("PNG images", "png"));
				if(JFileChooser.APPROVE_OPTION != fileChooser.showOpenDialog(owner))
				{
					return;
				}

				logoLocation = fileChooser.getSelectedFile().getAbsolutePath();
				logo.setIcon(new ImageIcon(logoLocation));
				pack();
			}
		});

		exportBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				exportAsked = true;
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
	 * Display dialog box.
	 * @return True if user asked for documentation generation, false otherwise.
	 */
	public boolean display()
	{
		exportBtn.requestFocusInWindow();
		setVisible(true);
		return exportAsked;
	}

	/**
	 * Get export location.
	 * @return Path to export folder
	 */
	public String  getExportLocation()
	{
		return exportLocation.getText();
	}

	/**
	 * Get logo location.
	 * @return Path to logo
	 */
	public String  getLogoLocation()
	{
		return logoLocation;
	}

	/**
	 * Know if the user wants to open generated documentation.
	 * @return True if opening asked, false otherwise
	 */
	public boolean isOpenInBrowserAsked()
	{
		return openInBrowserAsked.isSelected();
	}
}