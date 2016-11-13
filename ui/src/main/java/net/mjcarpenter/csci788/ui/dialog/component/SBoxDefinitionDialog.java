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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.mjcarpenter.csci788.crypto.spn.Permutation;
import net.mjcarpenter.csci788.crypto.spn.SBox;

public class SBoxDefinitionDialog extends ComponentDefinitionDialog<SBox> implements ActionListener
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
		
		for(int i=0; i<numFields+1; i++)
		{
			JPanel colPanel = new JPanel();
			colPanel.setLayout(new GridLayout(2, 1, 5, 5));
			
			if(i == 0)
			{
				JLabel inLabel = new JLabel("x");
				JLabel outLabel = new JLabel("S(x)  ");
				
				colPanel.add(inLabel);
				colPanel.add(outLabel);
			}
			else
			{
				JLabel iLabel = new JLabel(Integer.toHexString(i-1).toUpperCase());
				iLabel.setHorizontalAlignment(JLabel.CENTER);
				JTextField iField = new JTextField();
				iField.setText(Integer.toHexString(this.component.sub(i-1)).toUpperCase());
				iField.setHorizontalAlignment(JTextField.CENTER);
							
				textLabels.add(i-1, iLabel);
				textFields.add(i-1, iField);
				
				colPanel.add(iLabel);
				colPanel.add(iField);
			}
			
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
	
	@Override
	public boolean validateComponent()
	{
		for(int i=0; i<cachedMappings.length; i++)
		{
			boolean contains = false;
			
			for(int j=0; j<cachedMappings.length; j++)
			{
				contains |= (cachedMappings[j] == i);
				if(contains) break;
			}
			
			if(!contains)
				return false;
		}
		
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(jbOK))
		{
			if(validateComponent())
			{
				component = new SBox(cachedMappings);
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
	}

}
