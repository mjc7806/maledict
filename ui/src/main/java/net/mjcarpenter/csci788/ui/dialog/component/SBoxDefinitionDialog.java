package net.mjcarpenter.csci788.ui.dialog.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.mjcarpenter.csci788.crypto.spn.SBox;

public class SBoxDefinitionDialog extends ComponentDefinitionDialog<SBox> implements ActionListener, FocusListener
{
	private JButton jbOK;
	private JButton jbCancel;
	private JButton jbHelp;
	
	private List<JTextField> textFields;
	private List<JLabel>     textLabels;
	
	private String cachedEntry;
	private int[]  cachedMappings;
	
	
	public SBoxDefinitionDialog(int size)
	{
		this(SBox.noop(size));
	}
	
	public SBoxDefinitionDialog(SBox component)
	{
		super(component);
		
		
		jbOK = new JButton("OK");
		jbOK.setMnemonic('O');
		jbCancel = new JButton("Cancel");
		jbCancel.setMnemonic('C');
		jbHelp = new JButton("Help");
		jbHelp.setMnemonic('H');
		
		JPanel buttonPanel   = new JPanel();
		JPanel subPanelLeft  = new JPanel();
		JPanel subPanelRight = new JPanel();
		
		buttonPanel.setLayout(new BorderLayout());
		subPanelLeft.add(jbHelp);
		subPanelRight.add(jbOK);
		subPanelRight.add(jbCancel);
		
		buttonPanel.add(subPanelLeft, BorderLayout.WEST);
		buttonPanel.add(subPanelRight, BorderLayout.EAST);
		
		int numFields = this.component.bitSize()<<2;
		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout(new GridLayout(1, numFields));
		fieldPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		textFields = new ArrayList<JTextField>(numFields);
		textLabels = new ArrayList<JLabel>(numFields);
		
		for(int i=0; i<numFields; i++)
		{
			JPanel colPanel = new JPanel();
			colPanel.setLayout(new GridLayout(2, 1, 5, 5));
			
			JLabel     iLabel = new JLabel(Integer.toHexString(i).toUpperCase());
			iLabel.setHorizontalAlignment(JLabel.CENTER);
			JTextField iField = new JTextField();
			iField.setText(Integer.toHexString(this.component.sub(i)).toUpperCase());
			iField.setHorizontalAlignment(JTextField.CENTER);
						
			textLabels.add(i, iLabel);
			textFields.add(i, iField);
			
			colPanel.add(iLabel);
			colPanel.add(iField);
			
			fieldPanel.add(colPanel);
		}
		
		JPanel footPanel = new JPanel();
		footPanel.setLayout(new BorderLayout());
		footPanel.add(fieldPanel, BorderLayout.CENTER);
		footPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		add(footPanel, BorderLayout.CENTER);
		
		setSize(500, 150);
		setMinimumSize(new Dimension(500,150));
		setVisible(true);
		
		cachedEntry = textFields.get(0).getText();
		textFields.get(0).requestFocusInWindow();
	}
	
	public static void main(String[] args)
	{
		new SBoxDefinitionDialog(16);
	}

	@Override
	public boolean validateComponent()
	{
		return false;
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
