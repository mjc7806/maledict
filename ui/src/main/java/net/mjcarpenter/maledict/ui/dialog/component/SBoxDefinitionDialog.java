/*
 * Maledict - An Interactive Tool for Learning Linear and Differential Cryptanalysis of SPNs
 * Copyright (C) 2016  Mike Carpenter
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.mjcarpenter.maledict.ui.dialog.component;

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

import net.mjcarpenter.maledict.crypto.spn.SBox;
import net.mjcarpenter.maledict.ui.message.help.HelpMessageConstants;

@SuppressWarnings("serial")
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
		super("Edit S-box", component);
		
		
		jbOK = new JButton("OK");
		jbOK.setMnemonic('O');
		jbCancel = new JButton("Cancel");
		jbCancel.setMnemonic('C');
		jbHelp = new JButton("Help");
		jbHelp.setMnemonic('H');
		
		jbOK.addActionListener(this);
		jbCancel.addActionListener(this);
		jbHelp.addActionListener(this);
		
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
		cachedMappings = new int[numFields];
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
				
				cachedMappings[i-1] = this.component.sub(i-1);
				
				textLabels.add(i-1, iLabel);
				textFields.add(i-1, iField);
				
				colPanel.add(iLabel);
				colPanel.add(iField);
				iField.addFocusListener(this);
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
		else if(e.getSource().equals(jbHelp))
		{
			openHelpMessage(HelpMessageConstants.HELP_DLG_SBOX);
		}
	}

	@Override
	public void focusGained(FocusEvent arg0)
	{
		if(arg0.getSource() instanceof JTextField)
		{
			JTextField field = (JTextField)arg0.getSource();
			cachedEntry = field.getText();
		}
	}

	@Override
	public void focusLost(FocusEvent arg0)
	{
		if(arg0.getSource() instanceof JTextField)
		{
			JTextField field = (JTextField)arg0.getSource();
			int index = textFields.indexOf(field);
			int input;
			
			try
			{
				input = Integer.valueOf(field.getText(), 16);
			}
			catch(NumberFormatException nfe)
			{
				input = -1;
			}
			
			if(input >= 0 && input < cachedMappings.length)
			{
				cachedMappings[index] = input;
				
				if(validateComponent())
				{
					component = new SBox(cachedMappings);
				}
			}
			else
			{
				field.setText(cachedEntry);
			}
			
			cachedEntry = null;
		}
	}

}
