package net.mjcarpenter.csci788.ui.dialog.component;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.mjcarpenter.csci788.crypto.spn.Key;

public class KeyDefinitionDialog extends ComponentDefinitionDialog<Key>
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
		super(component);
		
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
	
	public static void main(String[] args)
	{
		new KeyDefinitionDialog(16);
	}

	@Override
	public boolean validateComponent()
	{
		String fieldKey = jtfKey.getText();
		
		
		return false;
	}

}
