package gui;

import model.HttpMethod;
import model.Project;
import model.Response;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

/**
 * Created by joseph on 10/07/16.
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

	public MethodPanel(HttpMethod httpMethod)
	{
		super();
		this.httpMethod = httpMethod;
		description.setText(httpMethod.getDescription());
		buildUI();
	}

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
		colMod.getColumn(2).setPreferredWidth(400);

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
		responseList = new JList<String>(httpMethod.getResponsesNames());
		responseList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		respPanel.add(new JScrollPane(responseList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), JSplitPane.LEFT);

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
		responseSchema.setTabSize(2);
		responseSchema.setFont(new Font(Font.MONOSPACED, Font.PLAIN, responseSchema.getFont().getSize()- 2));
		responseSchema.setOpaque(true);
		Color color = responseSchema.getBackground();
		responseSchema.setOpaque(true);
		responseSchema.setBackground(description.getForeground().darker());
		responseSchema.setForeground(color);
		responseSchema.setCaretColor(color);
		respEditPanel.add(new JScrollPane(responseSchema), c);
		//respEditPanel.add(responseSchema, c);

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
					JComboBox<String> comboBox = new JComboBox<String>(userProperty.getValues());
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
				responseType.setModel(new DefaultComboBoxModel<String>(new String[]{"Titi", "Toto"}));
				responseType.setSelectedItem(response.getOutputType());
				responseSchema.setText(response.getSchema());
				enableResponseEditingButtons(true);
			}
		});

		responseDescription.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyTyped(KeyEvent e)
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
			public void keyTyped(KeyEvent e)
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
					responseList.setModel(new DefaultComboBoxModel<String>(httpMethod.getResponsesNames()));
					responseList.setSelectedValue(responseToAdd, true);
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
					responseList.setModel(new DefaultComboBoxModel<String>(httpMethod.getResponsesNames()));
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
					responseList.setModel(new DefaultComboBoxModel<String>(responsesNames));

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

	private static class UserDefinedPropertyNewValueListener extends FocusAdapter
	{
		private Project.UserDefinedRouteProperty userProperty;

		public UserDefinedPropertyNewValueListener(Project.UserDefinedRouteProperty userProperty)
		{
			this.userProperty = userProperty;
		}

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

	private static class UserDefinedPropertyAutocompletionListener extends KeyAdapter
	{
		private final Project.UserDefinedRouteProperty userProperty;

		public UserDefinedPropertyAutocompletionListener(Project.UserDefinedRouteProperty userProperty)
		{
			this.userProperty = userProperty;
		}

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

	private class UserDefinedPropertyEditedValueListener extends FocusAdapter
	{
		private String property;

		public UserDefinedPropertyEditedValueListener(String property)
		{
			this.property = property;
		}

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
}