package gui.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by joseph on 14/05/16.
 */
public class InputStringDialog extends JDialog implements ActionListener
{
	private JButton okBtn;
	private JButton cancelBtn;
	private JLabel helper;
	private JTextField input;
	private boolean status;

	public InputStringDialog(Frame owner, String title, String validateBtnText, String helperText, String defaultValue)
	{
		super(owner, title, true);
		okBtn = new JButton(validateBtnText);
		cancelBtn = new JButton("Cancel");
		helper = new JLabel("Help");
		helper.setToolTipText(helperText);
		input = new JTextField();
		input.setText(defaultValue);
		buildInterface();
		setMinimumSize(new Dimension(200, 30));
		pack();
		setLocationRelativeTo(null);
	}

	private void buildInterface()
	{
		JPanel btnPanel = new JPanel();
		btnPanel.add(okBtn);
		btnPanel.add(cancelBtn);

		JPanel helperPanel = new JPanel();
		helperPanel.add(helper);

		setLayout(new BorderLayout());
		add(helperPanel, BorderLayout.NORTH);
		add(input, BorderLayout.CENTER);
		add(btnPanel, BorderLayout.SOUTH);

		okBtn.addActionListener(this);
		cancelBtn.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent a)
	{
		if(a.getSource() == okBtn)
		{
			status = true;
			setVisible(false);
		}
		else if(a.getSource() == cancelBtn)
		{
			status = false;
			setVisible(false);
		}
	}

	public String getStringInputValue()
	{
		return input.getText();
	}

	public boolean showDialog()
	{
		setVisible(true);
		return status;
	}
}