package net.mjcarpenter.csci788.ui.dialog.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.mjcarpenter.csci788.crypto.spn.Key;
import net.mjcarpenter.csci788.ui.message.help.HelpMessageConstants;

@SuppressWarnings("serial")
public class KeyDefinitionDialog extends ComponentDefinitionDialog<Key> implements ActionListener
{
	private JButton jbOK;
	private JButton jbCancel;
	private JButton jbHelp;
	
	private JLabel     jlKey;
	private JTextField jtfKey;
	
	public KeyDefinitionDialog(int length)
	{
		this(Key.noop(length));
	}
	
	public KeyDefinitionDialog(Key component)
	{
		super("Edit key", component);
		
		jbOK = new JButton("OK");
		jbOK.setMnemonic('O');
		jbCancel = new JButton("Cancel");
		jbCancel.setMnemonic('C');
		jbHelp = new JButton("Help");
		jbHelp.setMnemonic('H');
		
		jlKey  = new JLabel("Key: ");
		jtfKey = new JTextField(25);
		
		JPanel mainPanel     = new JPanel();
		JPanel buttonPanel   = new JPanel();
		JPanel subPanelLeft  = new JPanel();
		JPanel subPanelRight = new JPanel();
		
		buttonPanel.setLayout(new BorderLayout());
		subPanelLeft.add(jbHelp);
		subPanelRight.add(jbOK);
		subPanelRight.add(jbCancel);
		
		buttonPanel.add(subPanelLeft, BorderLayout.WEST);
		buttonPanel.add(subPanelRight, BorderLayout.EAST);
		
		mainPanel.setBorder(new EmptyBorder(15,15,15,15));
		mainPanel.add(jlKey);
		mainPanel.add(jtfKey);
		
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		setSize(500, 150);
		setMinimumSize(new Dimension(500,150));
		setVisible(true);
	}

	@Override
	public boolean validateComponent()
	{
		String fieldKey = jtfKey.getText().trim();
		int keyIntVal = Integer.MAX_VALUE;
		
		try
		{
			keyIntVal = Integer.parseInt(fieldKey, 16);
		}
		catch(NumberFormatException nfe)
		{
			return false;
		}
		
		return keyIntVal < (1<<component.length());
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(jbOK))
		{
			if(validateComponent())
			{
				int keyIntVal = Integer.parseInt(jtfKey.getText().trim());
				byte[] newKeyVal = ByteBuffer.allocate(component.length()/8).order(ByteOrder.LITTLE_ENDIAN).putInt(keyIntVal).array();
				component = new Key(newKeyVal);
				this.dispose();
			}
			else
			{
				JOptionPane.showMessageDialog(this,
						"This is not a valid S-box.",
						"Validation Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(e.getSource().equals(jbCancel))
		{
			component = originalComponent;
			this.dispose();
		}
		else if(e.getSource().equals(jbHelp))
		{
			openHelpMessage(HelpMessageConstants.HELP_DLG_KEY);
		}
	}
}
