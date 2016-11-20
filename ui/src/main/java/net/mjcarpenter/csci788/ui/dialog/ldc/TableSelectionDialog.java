package net.mjcarpenter.csci788.ui.dialog.ldc;

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
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import net.mjcarpenter.csci788.ui.component.CoordinateToggleButton;

public abstract class TableSelectionDialog extends JDialog implements ActionListener
{
	protected final Collection<CoordinateToggleButton> buttonReferences;
	private CoordinateToggleButton selectedButtonReference;
	private CoordinateToggleButton[][] buttons;
	private JButton jbAccept, jbCancel, jbHelp;
	
	public TableSelectionDialog(int[][] table)
	{
		super();
		setModal(true);
		setLayout(new BorderLayout());
		
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
		tablePanel.setLayout(new GridLayout(table.length, table[0].length));
		btnPanel.setLayout(new BorderLayout());
		tablePanel.setBorder(new EmptyBorder(20,20,20,20));
		
		buttons = new CoordinateToggleButton[table.length][table[0].length];
		Collection<CoordinateToggleButton> butRefs = new ArrayList<CoordinateToggleButton>();
		
		for(int i=0; i<table.length; i++)
		{
			for(int j=0; j<table[i].length; j++)
			{
				buttons[i][j] = new CoordinateToggleButton(String.valueOf(table[i][j]), i, j);
				buttons[i][j].setEnabled(table[i][j] != 0);
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
	
	protected abstract void handleAccept(CoordinateToggleButton selectedButton);
	protected abstract void handleCancel();
	protected abstract void handleHelp();
	
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
