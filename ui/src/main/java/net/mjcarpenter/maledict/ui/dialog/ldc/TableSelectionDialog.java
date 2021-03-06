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
package net.mjcarpenter.maledict.ui.dialog.ldc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.mjcarpenter.maledict.ui.component.CoordinateToggleButton;
import net.mjcarpenter.maledict.ui.message.help.HelpMessage;

@SuppressWarnings("serial")
public abstract class TableSelectionDialog extends JDialog implements ActionListener
{
	protected final Collection<CoordinateToggleButton> buttonReferences;
	
	protected boolean hasSelection;
	protected int     selectedIn;
	protected int     selectedOut;
	protected int     selectedBias;
	
	private HelpMessage msg;
	private CoordinateToggleButton selectedButtonReference;
	private CoordinateToggleButton[][] buttons;
	private JButton jbAccept, jbCancel, jbHelp;
	
	public TableSelectionDialog(int[][] table, long inFilter)
	{
		super();
		setModal(true);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		msg = null;
		this.hasSelection = false;
		
		jbAccept = new JButton("Accept");
		jbCancel = new JButton("Cancel");
		jbHelp   = new JButton("Help");
		jbAccept.setMnemonic('A');
		jbCancel.setMnemonic('C');
		jbHelp.setMnemonic('H');
		jbAccept.addActionListener(this);
		jbCancel.addActionListener(this);
		jbHelp.addActionListener(this);
		
		jbAccept.setEnabled(false);
		
		JPanel tablePanel = new JPanel();
		JPanel btnPanel   = new JPanel();
		tablePanel.setLayout(new GridLayout(table.length+1, table[0].length+1));
		btnPanel.setLayout(new BorderLayout());
		tablePanel.setBorder(new EmptyBorder(20,20,20,20));
		
		buttons = new CoordinateToggleButton[table.length][table[0].length];
		Collection<CoordinateToggleButton> butRefs = new ArrayList<CoordinateToggleButton>();
		
		// Empty label in top-left
		tablePanel.add(new JLabel());
		
		for(int i=0; i<table[0].length; i++)
		{
			// Output labels
			tablePanel.add(new JLabel(Integer.toHexString(i), SwingConstants.CENTER));
		}
		for(int i=0; i<table.length; i++)
		{
			// Input label
			tablePanel.add(new JLabel(Integer.toHexString(i), SwingConstants.RIGHT));
			
			for(int j=0; j<table[i].length; j++)
			{
				buttons[i][j] = new CoordinateToggleButton(String.valueOf(table[i][j]), i, j);
				buttons[i][j].setEnabled(i != 0 && j != 0 && table[i][j] != 0 && (inFilter == 0 || i == inFilter));
				buttons[i][j].setSelected(false);
				buttons[i][j].addActionListener(this);
				tablePanel.add(buttons[i][j]);
				butRefs.add(buttons[i][j]);
			}
		}
		
		buttonReferences = Collections.unmodifiableCollection(butRefs);
		selectedButtonReference = null;
		
		JPanel btnSubPanel = new JPanel();
		btnSubPanel.add(jbCancel);
		btnSubPanel.add(jbAccept);
		
		btnPanel.add(btnSubPanel, BorderLayout.EAST);
		btnPanel.add(jbHelp,   BorderLayout.WEST);
		
		add(tablePanel, BorderLayout.CENTER);
		add(btnPanel,   BorderLayout.SOUTH);
		
		pack();
	}
	
	public CoordinateToggleButton getSelectedButton()
	{
		return selectedButtonReference;
	}
	
	public boolean hasSelection()
	{
		return hasSelection;
	}
	
	public int getSelectedIn()
	{
		return selectedIn;
	}
	
	public int getSelectedOut()
	{
		return selectedOut;
	}
	
	public int getSelectedBias()
	{
		return selectedBias;
	}
	
	protected abstract void handleAccept(CoordinateToggleButton selectedButton);
	protected abstract void handleHelp();
	
	
	protected void handleHelp(String m)
	{
		if(msg == null)
		{
			msg = new HelpMessage(m, this,
					() ->
					{
						if(msg != null)
						{
							msg.dispose();
						}
						msg = null;
					});
			
			if(msg.isLoadedSuccessfully())
			{
				msg.setVisible(true);
			}
			else
			{
				msg.dispose();
				msg = null;
			}
		}
	}
	
	private void handleCancel()
	{
		hasSelection = false;
		setVisible(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource() instanceof CoordinateToggleButton)
		{
			CoordinateToggleButton relevantButton = (CoordinateToggleButton)ae.getSource();
			
			if(!relevantButton.isSelected())
			{
				relevantButton.setBackground(null);
				selectedButtonReference = null;
			}
			else
			{
				relevantButton.setSelected(true);
				relevantButton.setBackground(Color.BLUE);
				
				if(selectedButtonReference != null)
				{
					selectedButtonReference.setSelected(false);
					selectedButtonReference.setBackground(null);
				}
				
				selectedButtonReference = relevantButton;
			}
		}
		else if(ae.getSource() instanceof JButton)
		{
			if(ae.getSource().equals(jbAccept))
			{
				handleAccept(selectedButtonReference);
			}
			else if(ae.getSource().equals(jbCancel))
			{
				handleCancel();
			}
			else if(ae.getSource().equals(jbHelp))
			{
				handleHelp();
			}
		}
		
		jbAccept.setEnabled(selectedButtonReference != null);
	}
}
