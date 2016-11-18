package gui.dialog.settings;

import model.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Panel displaying project information.
 * @author joseph
 */
public class ProjectPanel extends JPanel
{
	private Frame parent;

	private JTextField name, company, domain, authors, contact;
	private JTextArea description;

	/**
	 * @param parent Parent frame
	 */
	public ProjectPanel(Frame parent)
	{
		this.parent = parent;

		buildUI();
	}

	/**
	 * Build user interface.
	 */
	private void buildUI()
	{
		Project project = Project.getActiveProject();

		setLayout(new BorderLayout());

		Box vbox1 = Box.createVerticalBox();

		vbox1.add(new JLabel("Name:"));
		name = new JTextField(project.getName());
		vbox1.add(name);

		vbox1.add(new JLabel("Company:"));
		company = new JTextField(project.getCompany());
		vbox1.add(company);

		vbox1.add(new JLabel("Authors:"));
		authors = new JTextField(project.getAuthors());
		vbox1.add(authors);

		vbox1.add(new JLabel("Contact:"));
		contact = new JTextField(project.getContact());
		vbox1.add(contact);

		vbox1.add(new JLabel("Domain:"));
		domain = new JTextField(project.getDomain());
		vbox1.add(domain);

		add(vbox1, BorderLayout.NORTH);

		Box vbox2 = Box.createVerticalBox();
		vbox2.setAlignmentX(LEFT_ALIGNMENT);

		JLabel lbl = new JLabel("Description:");
		lbl.setAlignmentX(CENTER_ALIGNMENT);
		vbox2.add(lbl);
		description = new JTextArea(project.getDescription());
		description.setTabSize(4);
		description.setLineWrap(true);
		vbox2.add(new JScrollPane(description));

		add(vbox2, BorderLayout.CENTER);

		addListener();
	}

	/**
	 * Add listeners.
	 */
	private void addListener()
	{
		Project project = Project.getActiveProject();

		name.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				project.setName(name.getText());
				parent.setTitle("Diderot - " + project.getName());
			}
		});

		company.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				project.setCompany(company.getText());
			}
		});

		domain.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				project.setDomain(domain.getText());
			}
		});

		authors.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				project.setAuthors(authors.getText());
			}
		});

		contact.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				project.setContact(contact.getText());
			}
		});

		description.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				project.setDescription(description.getText());
			}
		});
	}
}