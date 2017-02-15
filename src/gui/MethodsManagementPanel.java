package gui;

import gui.dialog.InputStringDialogHelper;
import model.HttpMethod;
import model.Route;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Panel displaying HttpMethods contained by a certain route.
 */
public class MethodsManagementPanel extends JPanel
{
	private AbstractAction addMethodAction, updMethodAction, delMethodAction, sortMethodAction;

	private JButton addMethodBtn = new JButton(),
			updMethodBtn = new JButton(),
			delMethodBtn = new JButton(),
			sortMethodBtn = new JButton();

	private JTabbedPane methodsTabbedPanel = new JTabbedPane();
	private Route route;
	private UrlParameterPanel urlParameterPanel = new UrlParameterPanel();
	private JTextArea descriptionTextArea = new JTextArea();
	private int savedMethodIndex;
	private int savedViewHorizontalPosition;
	private JSplitPane mainPanel;
	private int dividerLocationBeforeUrlParameterPanelDisplay;

	/**
	 * Create methods management panel.
	 */
	public MethodsManagementPanel()
	{
		super(new BorderLayout());
		buildUI();
	}

	/**
	 * Get add method action.
	 * @return add method action
	 */
	public AbstractAction getAddMethodAction()
	{
		return addMethodAction;
	}

	/**
	 * Get update method action.
	 * @return update method action
	 */
	public AbstractAction getUpdMethodAction()
	{
		return updMethodAction;
	}

	/**
	 * Get delete method action.
	 * @return delete method action
	 */
	public AbstractAction getDelMethodAction()
	{
		return delMethodAction;
	}

	/**
	 * Set displayed route.
	 * @param route Route to display
	 */
	public void setRoute(Route route)
	{
		this.route = route;
		descriptionTextArea.setText(route.getDescription());
		descriptionTextArea.setCaretPosition(0);

		if(route.hasUrlParameter())
		{
			dividerLocationBeforeUrlParameterPanelDisplay = mainPanel.getDividerLocation();
			urlParameterPanel.setVisible(true);
			urlParameterPanel.setRoute(route);
			mainPanel.setDividerLocation(dividerLocationBeforeUrlParameterPanelDisplay + urlParameterPanel.getSize().height);
		}
		else
		{
			urlParameterPanel.setVisible(false);
			mainPanel.setDividerLocation(dividerLocationBeforeUrlParameterPanelDisplay);
		}


		rebuildTabbedPane();

		if(methodsTabbedPanel.getTabCount() == 0)
		{
			setEnabledButton(false);
		}
		else
		{
			setEnabledButton(true);
		}
	}

	/**
	 * Rebuild tabs.
	 */
	private void rebuildTabbedPane()
	{
		methodsTabbedPanel.removeAll();

		for(String methodName : route.getHttpMethodNames())
		{
			HttpMethod method = route.getHttpMethod(methodName);
			//methodsTabbedPanel.addTab(entry.getKey(), new JLabel(entry.getKey()));
			MethodPanel methodPanel = new MethodPanel(method);
			JScrollPane scrollPane = new JScrollPane(methodPanel,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
			scrollPane.getViewport().setOpaque(false);
			methodsTabbedPanel.addTab(methodName, scrollPane);
		}
	}

	/**
	 * Build user interface.
	 */
	private void buildUI()
	{
		JPanel baseRouteInfoPanel = new JPanel(new BorderLayout());

		descriptionTextArea = new JTextArea();
		descriptionTextArea.setTabSize(2);
		JScrollPane scrollPanel = new JScrollPane(descriptionTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		baseRouteInfoPanel.add(new JLabel("Global route description:"), BorderLayout.NORTH);
		baseRouteInfoPanel.add(scrollPanel, BorderLayout.CENTER);

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(baseRouteInfoPanel, BorderLayout.CENTER);
		topPanel.add(urlParameterPanel, BorderLayout.SOUTH);


		JPanel bottomPanel = new JPanel(new BorderLayout());

		JPanel btnPanel = new JPanel();
		btnPanel.add(addMethodBtn);
		btnPanel.add(updMethodBtn);
		btnPanel.add(delMethodBtn);
		btnPanel.add(new JLabel(" "));
		btnPanel.add(sortMethodBtn);

		bottomPanel.add(btnPanel, BorderLayout.NORTH);
		bottomPanel.add(methodsTabbedPanel, BorderLayout.CENTER);
		mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, topPanel, bottomPanel);
		add(mainPanel);

		dividerLocationBeforeUrlParameterPanelDisplay = mainPanel.getDividerLocation();
		addListeners();
	}

	/**
	 * Add action listeners.
	 */
	private void addListeners()
	{
		addMethodAction = new AbstractAction("Add new http method", ImageIconProxy.getIcon("add"))
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				actionAddMethod();
			}
		};

		updMethodAction = new AbstractAction("Modify http method", ImageIconProxy.getIcon("edit"))
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				actionUpdateMethod();
			}
		};

		delMethodAction = new AbstractAction("Delete http method", ImageIconProxy.getIcon("del"))
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				actionRemoveMethod();
			}
		};

		sortMethodAction = new AbstractAction("Sort methods", ImageIconProxy.getIcon("sort"))
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				rebuildTabbedPane();
			}
		};

		addMethodBtn.setAction(addMethodAction);
		updMethodBtn.setAction(updMethodAction);
		delMethodBtn.setAction(delMethodAction);
		sortMethodBtn.setAction(sortMethodAction);

		descriptionTextArea.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void insertUpdate(DocumentEvent documentEvent)
			{
				route.setDescription(descriptionTextArea.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent documentEvent)
			{
				insertUpdate(documentEvent);
			}

			/**
			 * Not implemented
			 */
			@Override
			public void changedUpdate(DocumentEvent documentEvent)
			{
				//This not the implementation you are looking for.
			}
		});

		methodsTabbedPanel.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent changeEvent)
			{
				int currentTab = methodsTabbedPanel.getSelectedIndex();
				if(currentTab != -1)
				{
					//TODO update jmenu
				}
			}
		});
	}

	/**
	 * Get http method menu.
	 * @return menu to act on http methods
	 */
	public JMenu getMethodMenu()
	{
		JMenu methodMenu = new JMenu("Method");

		JMenuItem addMethodMenuItem = new JMenuItem(addMethodAction);
		addMethodMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
		methodMenu.add(addMethodMenuItem);

		JMenuItem updMethodMenuItem = new JMenuItem(updMethodAction);
		updMethodMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
		methodMenu.add(updMethodMenuItem);

		JMenuItem delMethodMenuItem = new JMenuItem(delMethodAction);
		delMethodMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
		methodMenu.add(delMethodMenuItem);

		return methodMenu;
	}

	/**
	 * Enable actions on http methods.
	 * @param enabled Enables status
	 */
	private void setEnabledButton(boolean enabled)
	{
		updMethodAction.setEnabled(enabled);
		delMethodAction.setEnabled(enabled);
		sortMethodBtn.setEnabled(enabled);
	}

	/**
	 * Add new http method.
	 */
	private void actionAddMethod()
	{
		String methodToAdd = InputStringDialogHelper.showInputNoSpacesDialog(this, "Which HTTP method would you add?", "Add HTTP method", JOptionPane.PLAIN_MESSAGE);
		if(methodToAdd != null)
		{
			methodToAdd = methodToAdd.toUpperCase();
			HttpMethod httpMethod = new HttpMethod();

			if(!route.addHttpMethod(methodToAdd, httpMethod))
			{
				JOptionPane.showMessageDialog(this, "This method already exists.", "Cannot add HTTP method", JOptionPane.WARNING_MESSAGE);
				return;
			}

			MethodPanel methodPanel = new MethodPanel(httpMethod);
			JScrollPane scrollPane = new JScrollPane(methodPanel,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
			scrollPane.getViewport().setOpaque(false);
			methodsTabbedPanel.add(methodToAdd, scrollPane);
			methodsTabbedPanel.setSelectedIndex(methodsTabbedPanel.getTabCount()-1);
			setEnabledButton(true);
		}
	}

	/**
	 * Rename selected http method.
	 */
	private void actionUpdateMethod()
	{
		int currentTab = methodsTabbedPanel.getSelectedIndex();
		String methodToRename = methodsTabbedPanel.getTitleAt(currentTab);

		String methodRenamed = InputStringDialogHelper.showInputNoSpacesDialog(this, "Enter new name for http method: " + methodToRename, "Modify http method", JOptionPane.PLAIN_MESSAGE, methodToRename);
		if(methodRenamed != null)
		{
			methodRenamed = methodRenamed.toUpperCase();
			if(!route.renameHttpMethod(methodToRename, methodRenamed))
			{
				JOptionPane.showMessageDialog(this, "This method already exists.", "Cannot modify http method", JOptionPane.WARNING_MESSAGE);
				return;
			}

			methodsTabbedPanel.setTitleAt(methodsTabbedPanel.getSelectedIndex(), methodRenamed);
		}
	}

	/**
	 * Delete selected method.
	 */
	private void actionRemoveMethod()
	{
		int currentTab = methodsTabbedPanel.getSelectedIndex();
		String methodToRemove = methodsTabbedPanel.getTitleAt(currentTab);

		if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this,
				"Are you sure you want to remove the following HTTP method?\n" + methodToRemove,
				"Remove HTTP Method ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE))
		{
			route.removeHttpMethod(methodToRemove);
			methodsTabbedPanel.remove(currentTab);

			if(methodsTabbedPanel.getTabCount() == 0)
			{
				setEnabledButton(false);
			}
		}
	}

	/**
	 * Save selected tab, and scrollbar position.
	 */
	public void saveDisplayStatus()
	{
		JScrollPane scrollPane = (JScrollPane) methodsTabbedPanel.getSelectedComponent();
		if(scrollPane != null)
		{
			savedViewHorizontalPosition = scrollPane.getVerticalScrollBar().getValue();
			savedMethodIndex = methodsTabbedPanel.getSelectedIndex();
		}
	}

	/**
	 * Restore selected tab, and scrollbar position.
	 */
	public void restoreDisplayStatus()
	{
		if(methodsTabbedPanel.getTabCount() != 0)
		{
			methodsTabbedPanel.setSelectedIndex(savedMethodIndex);
			JScrollPane scrollPane = (JScrollPane) methodsTabbedPanel.getSelectedComponent();
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					scrollPane.getVerticalScrollBar().setValue(savedViewHorizontalPosition);
				}
			});
		}
	}
}