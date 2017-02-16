package gui;

import gui.dialog.ResponseEditionDialog;
import model.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

/**
 * Panel displaying an http method stuff.
 * @author joseph
 */
public class MethodPanel extends JPanel implements Scrollable
{
	private HttpMethod httpMethod;
	private JTextArea description = new JTextArea();
	private AbstractAction addParamAction, delParamAction,
			addRespAction, editRespAction, delRespAction, editWiderAction;
	private JButton addParamBtn = new JButton(),
			delParamBtn = new JButton(),
			addRespBtn = new JButton(),
			editRespBtn = new JButton(),
			delRespBtn = new JButton(),
			editWiderRespBtn = new JButton();
	private JTable paramTable;
	private JComboBox<String> responseType = new JComboBox<>();
	private JTextArea responseDescription = new JTextArea(),
			responseSchema = new JTextArea();
	private JList<String> responseList;

	/**
	 * @param httpMethod The method to display.
	 */
	public MethodPanel(HttpMethod httpMethod)
	{
		super();
		this.httpMethod = httpMethod;
		description.setText(httpMethod.getDescription());
		description.setCaretPosition(0);
		buildUI();
	}

	/**
	 * Build user interface.
	 */
	private void buildUI()
	{
		Border labelBorder = BorderFactory.createEmptyBorder(5, 5, 0, 2);
		Border componentBorder = BorderFactory.createMatteBorder(0, 4, 0, 4, getBackground());
		Border textAreaBorder = BorderFactory.createLineBorder(getBackground().darker(), 1);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		//Method description
		JLabel label = new JLabel("Method description:");
		label.setBorder(labelBorder);
		label.setAlignmentX(LEFT_ALIGNMENT);
		add(label);

		description.setAlignmentX(LEFT_ALIGNMENT);
		description.setTabSize(2);
		description.setBorder(BorderFactory.createCompoundBorder(componentBorder, textAreaBorder));
		description.setLineWrap(true);
		add(description);
		add(new JLabel(" "));//spacer

		//Parameters
		JPanel labelButtonPanel = new JPanel(new BorderLayout());

		label = new JLabel("Parameters:");
		labelButtonPanel.add(label, BorderLayout.WEST);

		JPanel paramBtnPanel = new JPanel();
		paramBtnPanel.add(addParamBtn);
		paramBtnPanel.add(delParamBtn);
		labelButtonPanel.add(paramBtnPanel, BorderLayout.CENTER);

		paramTable = new JTable(httpMethod);
		paramTable.setAutoCreateRowSorter(true);
		TableColumnModel colMod = paramTable.getColumnModel();
		colMod.getColumn(0).setPreferredWidth(100);
		colMod.getColumn(0).setMaxWidth(400);
		colMod.getColumn(1).setMaxWidth(100);
		colMod.getColumn(2).setMaxWidth(100);
		colMod.getColumn(3).setMaxWidth(100);
		colMod.getColumn(3).setMinWidth(70);
		colMod.getColumn(4 ).setMaxWidth(100);
		colMod.getColumn(5).setPreferredWidth(400);

		colMod.getColumn(2).setCellEditor(new DefaultCellEditor(new JComboBox(Project.getActiveProject().getParamsTypes())));
		colMod.getColumn(3).setCellEditor(new ParamsSubTypesEditor());
		colMod.getColumn(4).setCellEditor(new ParameterLocationEditor(httpMethod));

		Box box = Box.createVerticalBox();
		box.setBorder(componentBorder);
		box.setAlignmentX(LEFT_ALIGNMENT);
		box.add(labelButtonPanel);
		box.add(paramTable.getTableHeader());
		box.add(paramTable);
		add(box);
		add(new JLabel(" "));//spacer

		labelButtonPanel = new JPanel(new BorderLayout());
		//labelButtonPanel.setBorder(labelBorder);

		//Responses
		label = new JLabel("Responses:");
		labelButtonPanel.add(label, BorderLayout.WEST);
		label.setAlignmentX(LEFT_ALIGNMENT);

		JPanel respBtnPanel = new JPanel();
		respBtnPanel.add(addRespBtn);
		respBtnPanel.add(editRespBtn);
		respBtnPanel.add(editWiderRespBtn);
		respBtnPanel.add(delRespBtn);
		labelButtonPanel.add(respBtnPanel, BorderLayout.CENTER);

		box = Box.createVerticalBox();
		box.setBorder(componentBorder);
		box.setAlignmentX(LEFT_ALIGNMENT);
		box.add(labelButtonPanel);


		JSplitPane respPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		responseList = new JList<>(httpMethod.getResponsesNames());
		responseList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		respPanel.add(responseList, JSplitPane.LEFT);
		responseList.setMinimumSize(new Dimension(50, 10));
		//respPanel.add(new JScrollPane(responseList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
		//		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), JSplitPane.LEFT);

		JPanel respEditPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0;
		c.weightx = 1;
		c.gridwidth = 5;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 0;
		respEditPanel.add(new JLabel("Response description:"), c);

		c.weighty = 0.2;
		c.gridheight = 2;
		c.gridy = 1;
		responseDescription.setRows(3);
		responseDescription.setLineWrap(true);
		responseDescription.setTabSize(2);
		respEditPanel.add(new JScrollPane(responseDescription), c);

		c.weighty = 0;
		c.gridheight = 1;
		c.gridy = 3;
		respEditPanel.add(new JLabel("Output format:"), c);

		c.gridheight = 1;
		c.gridy = 4;
		respEditPanel.add(responseType, c);

		c.gridheight = 1;
		c.gridy = 5;
		respEditPanel.add(new JLabel("Output schema:"), c);

		c.weighty = 1;
		c.gridheight = 4;
		c.gridy = 6;
		responseSchema.setRows(14);
		responseSchema.setTabSize(2);
		responseSchema.setFont(new Font(Font.MONOSPACED, Font.PLAIN, responseSchema.getFont().getSize()- 2));
		responseSchema.setOpaque(true);
		Color color = responseSchema.getBackground();
		responseSchema.setOpaque(true);
		responseSchema.setBackground(description.getForeground().darker());
		responseSchema.setForeground(color);
		responseSchema.setCaretColor(color);
		JScrollPane scrollPane = new JScrollPane(responseSchema);
		scrollPane.setRowHeaderView(new TextLineNumber(responseSchema));
		AbstractDocument doc = (AbstractDocument) responseSchema.getDocument();
		doc.setDocumentFilter(new NewLineFilter());
		respEditPanel.add(scrollPane, c);

		respPanel.add(respEditPanel, JSplitPane.RIGHT);

		JPanel p = new JPanel(new BorderLayout());
		p.add(respPanel, BorderLayout.CENTER);
		box.add(p);

		add(box);
		add(new JLabel(" "));//spacer


		//USer defined properties
		label = new JLabel("User defined properties:");
		label.setBorder(labelBorder);
		label.setAlignmentX(LEFT_ALIGNMENT);
		add(label);

		final String[] userDefinedProperties = Project.getActiveProject().getUserRoutesPropertiesNames();
		if(userDefinedProperties.length == 0)
		{
			label = new JLabel("No user defined properties created. To create one, go into project settings.");
			JPanel panel = new JPanel(new BorderLayout());
			panel.setAlignmentX(LEFT_ALIGNMENT);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			panel.add(label, BorderLayout.CENTER);
			add(panel);
		}
		else
		{
			final JPanel panel = new JPanel(new GridLayout(userDefinedProperties.length, 2, 15, 5));
			panel.setBorder(componentBorder);
			panel.setAlignmentX(LEFT_ALIGNMENT);

			for(final String property : userDefinedProperties)
			{
				JLabel propLabel = new JLabel(property + ":");
				propLabel.setHorizontalAlignment(SwingConstants.RIGHT);
				propLabel.setBackground(Color.RED);
				panel.add(propLabel);

				Project.UserDefinedRouteProperty userProperty = Project.getActiveProject().getUserRouteProperty(property);
				if(userProperty.isNewValuesDisabled())
				{
					JComboBox<String> comboBox = new JComboBox<>(userProperty.getValues());
					panel.add(comboBox);

					comboBox.setSelectedItem(httpMethod.getUserPropertyValue(property));
					comboBox.addFocusListener(new UserDefinedPropertyEditedValueListener(property));
				}
				else
				{
					JTextField valueText = new JTextField(httpMethod.getUserPropertyValue(property));
					panel.add(valueText);

					valueText.addFocusListener(new UserDefinedPropertyEditedValueListener(property));
					valueText.addKeyListener(new UserDefinedPropertyAutocompletionListener(userProperty));

					if(userProperty.isValuesMemorized())
					{
						valueText.addFocusListener(new UserDefinedPropertyNewValueListener(userProperty));
					}
				}
			}

			add(panel);
			add(new JLabel(" "));//spacer
		}

		addListener();

		if(httpMethod.getResponsesNames().length != 0)
		{
			responseList.setSelectedIndex(0);
		}
	}

	/**
	 * Add listeners/
	 */
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

			/**
			 * Not implemented
			 */
			@Override
			public void changedUpdate(DocumentEvent documentEvent)
			{
				//This not the implementation you are looking for.
			}
		});

		paramTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent listSelectionEvent)
			{
				if(paramTable.getSelectedRowCount() == 0)
				{
					delParamAction.setEnabled(false);
				}
				else
				{
					delParamAction.setEnabled(true);
				}
			}
		});

		responseList.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				String responseName = responseList.getSelectedValue();
				if(responseName == null)
				{
					return;
				}

				Response response = httpMethod.getResponse(responseName);
				responseDescription.setText(response.getDescription());
				responseDescription.setCaretPosition(0);
				responseType.setModel(new DefaultComboBoxModel<>(Project.getActiveProject().getResponsesFormat()));
				responseType.setSelectedItem(response.getOutputType());
				responseSchema.setText(response.getSchema());
				responseSchema.setCaretPosition(0);
				enableResponseEditingButtons(true);

				revalidate();
				repaint();
			}
		});

		responseDescription.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				httpMethod.getResponse(responseList.getSelectedValue()).setDescription(responseDescription.getText());
			}
		});

		responseType.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if(e.getStateChange() == ItemEvent.DESELECTED)
				{
					return;
				}

				httpMethod.getResponse(responseList.getSelectedValue()).setOutputType((String) responseType.getSelectedItem());
			}
		});

		responseSchema.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				httpMethod.getResponse(responseList.getSelectedValue()).setSchema(responseSchema.getText());
			}
		});

		final JPanel that = this;

		addRespAction = new AbstractAction("Add response", ImageIconProxy.getIcon("add"))
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				String responseToAdd = (String) JOptionPane.showInputDialog(that.getParent().getParent().getParent().getParent().getParent().getParent(),
						"Which response would you add?", "Add response", JOptionPane.PLAIN_MESSAGE);
				if(responseToAdd != null)
				{
					if(responseToAdd.isEmpty())
					{
						JOptionPane.showMessageDialog(that.getParent().getParent().getParent().getParent().getParent().getParent(),
								"Your input should not be empty.", "Empty input", JOptionPane.WARNING_MESSAGE);
						return;
					}

					if(!httpMethod.addResponse(responseToAdd))
					{
						JOptionPane.showMessageDialog(that.getParent().getParent().getParent().getParent().getParent().getParent(),
								"This response already exists.", "Cannot add response", JOptionPane.WARNING_MESSAGE);
						return;
					}
					responseList.setModel(new DefaultComboBoxModel<>(httpMethod.getResponsesNames()));
					responseList.setSelectedValue(responseToAdd, true);
					revalidate();
					repaint();
				}
			}
		};
		addRespBtn.setAction(addRespAction);

		editRespAction = new AbstractAction("Rename response", ImageIconProxy.getIcon("edit"))
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				String responseToRename = responseList.getSelectedValue();
				String responseRenamed = (String) JOptionPane.showInputDialog(that.getParent().getParent().getParent().getParent().getParent().getParent(),
						"Rename response:\n" + responseToRename + "\nto:", "Rename response", JOptionPane.PLAIN_MESSAGE, null, null, responseToRename);
				if(responseRenamed != null)
				{
					if(responseRenamed.isEmpty())
					{
						JOptionPane.showMessageDialog(that.getParent().getParent().getParent().getParent().getParent().getParent(),
								"Your input should not be empty.", "Empty input", JOptionPane.WARNING_MESSAGE);
						return;
					}

					if(!httpMethod.renameResponse(responseToRename, responseRenamed))
					{
						JOptionPane.showMessageDialog(that.getParent().getParent().getParent().getParent().getParent().getParent(),
								"This response already exists.", "Cannot rename response", JOptionPane.WARNING_MESSAGE);
						return;
					}
					responseList.setModel(new DefaultComboBoxModel<>(httpMethod.getResponsesNames()));
					responseList.setSelectedValue(responseRenamed, true);
				}
			}
		};
		editRespBtn.setAction(editRespAction);

		editWiderAction = new AbstractAction("Edit in wider window", ImageIconProxy.getIcon("editwide"))
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String responseName = responseList.getSelectedValue();
				ResponseEditionDialog responseEditionDialog = new ResponseEditionDialog(that.getParent().getParent().getParent().getParent().getParent().getParent().getParent().getParent().getParent().getParent().getParent().getParent().getParent(),
						responseName, httpMethod.getResponse(responseName));
				if(responseEditionDialog.display())
				{
					responseList.clearSelection();
					responseList.setSelectedValue(responseName, true);
				}
			}
		};
		editWiderRespBtn.setAction(editWiderAction);

		delRespAction = new AbstractAction("Delete response", ImageIconProxy.getIcon("del"))
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				String responseToDelete = responseList.getSelectedValue();
				if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(that.getParent().getParent().getParent().getParent().getParent().getParent(),
						"Are you sure you want to remove the following response?\n" + responseToDelete,
						"Remove response ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE))
				{
					httpMethod.delResponse(responseToDelete);
					String[] responsesNames = httpMethod.getResponsesNames();
					responseList.setModel(new DefaultComboBoxModel<>(responsesNames));

					if(responsesNames.length == 0)
					{
						enableResponseEditingButtons(false);
					}
					else
					{
						responseList.setSelectedIndex(0);
					}
				}
			}
		};
		delRespBtn.setAction(delRespAction);

		addParamAction = new AbstractAction("Add parameter", ImageIconProxy.getIcon("add"))
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				httpMethod.addParameter(httpMethod.getUniqueParameterName());
			}
		};
		addParamBtn.setAction(addParamAction);

		delParamAction = new AbstractAction("Delete selected parameter(s)", ImageIconProxy.getIcon("del"))
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				if(JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(null,
						"Are you sure you want to delete the selected parameter(s)?\n",
						"Delete parameter(s)", JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE))
				{
					return;
				}

				int[] selectedRowsIndex = paramTable.getSelectedRows();
				int[] correctIndex = new int[selectedRowsIndex.length];

				for(int i = 0; i < selectedRowsIndex.length; i++)
				{
					correctIndex[i] = paramTable.getRowSorter().convertRowIndexToModel(selectedRowsIndex[i]);
				}

				Arrays.sort(correctIndex);

				for(int i = selectedRowsIndex.length; i > 0; i--)
				{
					httpMethod.removeParameter(correctIndex[i - 1]);
				}
			}
		};
		delParamBtn.setAction(delParamAction);

		delParamAction.setEnabled(false);
		enableResponseEditingButtons(false);
	}

	/**
	 * Enable/disable response editing buttons.
	 * @param enabled Enabled status
	 */
	private void enableResponseEditingButtons(boolean enabled)
	{
		editRespAction.setEnabled(enabled);
		editWiderAction.setEnabled(enabled);
		delRespAction.setEnabled(enabled);
		responseDescription.setEnabled(enabled);
		responseType.setEnabled(enabled);
		responseSchema.setEnabled(enabled);

		if(!enabled)
		{
			responseDescription.setText("");
			responseType.removeAllItems();
			responseSchema.setText("");
		}
	}

	/**
	 * {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		return getPreferredSize();
	}

	/**
	 * {@inheritDoc}
	 * @param rectangle {@inheritDoc}
	 * @param i {@inheritDoc}
	 * @param i1 {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public int getScrollableUnitIncrement(Rectangle rectangle, int i, int i1)
	{
		return 10;
	}

	/**
	 * {@inheritDoc}
	 * @param rectangle {@inheritDoc}
	 * @param i {@inheritDoc}
	 * @param i1 {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public int getScrollableBlockIncrement(Rectangle rectangle, int i, int i1)
	{
		return 10;
	}

	/**
	 * {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean getScrollableTracksViewportWidth()
	{
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean getScrollableTracksViewportHeight()
	{
		return false;
	}

	/**
	 * Listener for adding new value to user defined property available values.
	 */
	private static class UserDefinedPropertyNewValueListener extends FocusAdapter
	{
		private Project.UserDefinedRouteProperty userProperty;

		/**
		 * @param userProperty Concerned user property
		 */
		public UserDefinedPropertyNewValueListener(Project.UserDefinedRouteProperty userProperty)
		{
			this.userProperty = userProperty;
		}

		/**
		 * Add current value on available values list
		 * @param e Event
		 */
		@Override
		public void focusLost(FocusEvent e)
		{
			JTextField textField = (JTextField) e.getSource();
			if(!textField.getText().isEmpty())
			{
				userProperty.add(textField.getText());
			}
		}
	}

	/**
	 * Listener for showing auto-completion popup menu.
	 */
	private static class UserDefinedPropertyAutocompletionListener extends KeyAdapter
	{
		private final Project.UserDefinedRouteProperty userProperty;

		/**
		 * @param userProperty Concerned user property
		 */
		public UserDefinedPropertyAutocompletionListener(Project.UserDefinedRouteProperty userProperty)
		{
			this.userProperty = userProperty;
		}

		/**
		 * Show autocompletion popup menu.
		 * @param e {@inheritDoc}
		 */
		@Override
		public void keyReleased(KeyEvent e)
		{
			final JTextField textField = (JTextField) e.getSource();

			if(e.getKeyCode() == KeyEvent.VK_CONTEXT_MENU || e.getKeyCode() == KeyEvent.VK_SPACE || (e.getModifiers() & KeyEvent.CTRL_DOWN_MASK) != 0)
			{
				JPopupMenu popupMenu = new JPopupMenu();

				boolean empty = true;
				for(final String s : userProperty.getValues())
				{
					if(s.toLowerCase().startsWith(textField.getText().toLowerCase()))
					{
						empty = false;
						popupMenu.add(new AbstractAction(s)
						{
							@Override
							public void actionPerformed(ActionEvent actionEvent)
							{
								textField.setText(s);
							}
						});
					}
				}
				if(!empty)
				{
					popupMenu.show(textField, 10, textField.getSize().height - 2);
				}
			}
		}
	}

	/**
	 * Listener for saving new value of a user defined property.
	 */
	private class UserDefinedPropertyEditedValueListener extends FocusAdapter
	{
		private String property;

		/**
		 * @param property Concerned user property name
		 */
		public UserDefinedPropertyEditedValueListener(String property)
		{
			this.property = property;
		}

		/**
		 * Save value for property.
		 * @param e Event
		 */
		@Override
		public void focusLost(FocusEvent e)
		{
			Object src = e.getSource();
			if(src instanceof JTextField)
			{
				JTextField textField = (JTextField) src;
				httpMethod.setUserProperty(property, textField.getText());
			}
			else if(src instanceof JComboBox)
			{
				JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
				httpMethod.setUserProperty(property, (String) comboBox.getSelectedItem());
			}
		}
	}

	private class ParameterLocationEditor extends AbstractCellEditor implements TableCellEditor, ItemListener
	{
		private String location;
		private JComboBox comboBox = new JComboBox(Parameter.PARAMS_LOCATION);
		private HttpMethod httpMethod;

		public ParameterLocationEditor(HttpMethod httpMethod)
		{
			this.httpMethod = httpMethod;
			comboBox.addItemListener(this);
		}

		@Override
		public void itemStateChanged(ItemEvent e)
		{
			if(e.getStateChange() == ItemEvent.SELECTED)
			{
				if(!checkDataConsistency(httpMethod, (String) e.getItem()))
				{
					fireEditingCanceled();
					return;
				}
				location = (String) e.getItem();
				fireEditingStopped();
			}
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
		{
			location = (String) value;
			comboBox.setSelectedItem(value);
			return comboBox;
		}

		@Override
		public Object getCellEditorValue()
		{
			return location;
		}

		private boolean checkDataConsistency(HttpMethod httpMethod, String loc)
		{
			if("body".equals(loc))//only 1 body, and no form with body.
			{
				int formDataFound = 0;

				for(String paramName : httpMethod.getParametersNames())
				{
					Parameter param = httpMethod.getParameter(paramName);

					if("body".equals(param.getLocation()))
					{
						comboBox.hidePopup();
						JOptionPane.showMessageDialog(null, "Only one parameter with \"body\" location can exists per request.", "Parameter location error", JOptionPane.ERROR_MESSAGE);
						return false;
					}
					else if("form data".equals(param.getLocation()))
					{
						formDataFound++;
					}
				}

				if(formDataFound > 1 || formDataFound == 1 && !"form data".equals(getCellEditorValue()))
				{
					comboBox.hidePopup();
					JOptionPane.showMessageDialog(null, "Parameter with \"body\" location cannot exists alongside parameter(s) with \"form data\" location.", "Parameter location error", JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
			else if("form data".equals(loc))
			{
				for(String paramName : httpMethod.getParametersNames())
				{
					Parameter param = httpMethod.getParameter(paramName);

					if("body".equals(param.getLocation()) && !"body".equals(getCellEditorValue()))
					{
						comboBox.hidePopup();
						JOptionPane.showMessageDialog(null, "Parameter with \"form data\" location cannot exists alongside parameter(s) with \"body\" location.", "Parameter location error", JOptionPane.ERROR_MESSAGE);
						return false;
					}
				}
			}

			return true;
		}
	}

	private class ParamsSubTypesEditor extends AbstractCellEditor implements TableCellEditor, ItemListener
	{
		private String subType = "";
		private JComboBox<String> combo = new JComboBox<>();

		public ParamsSubTypesEditor()
		{
			super();
			combo.addItemListener(this);
		}

		@Override
		public void itemStateChanged(ItemEvent e)
		{
			if(e.getStateChange() == ItemEvent.SELECTED)
			{
				subType = (String) combo.getSelectedItem();
				fireEditingStopped();
			}
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
		{
			subType = (String) value;
			String type = (String) table.getModel().getValueAt(row, 2);

			combo.setModel(new DefaultComboBoxModel<>(Project.getActiveProject().getSubParamsTypes(type)));
			combo.setSelectedItem(subType);
			return combo;
		}

		@Override
		public Object getCellEditorValue()
		{
			return subType;
		}
	}
}