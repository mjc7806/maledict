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
package net.mjcarpenter.maledict.ui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.mjcarpenter.maledict.ui.message.help.HelpMessage;
import net.mjcarpenter.maledict.ui.message.help.HelpMessageConstants;
import net.mjcarpenter.maledict.ui.util.MasterPropertiesCache;

@SuppressWarnings("serial")
public class MasterPropertiesDialog extends JDialog implements ActionListener
{
	private boolean closedSuccessful;
	
	private JLabel     jlBlockSize, jlNumRounds, jlSboxSize;
	private JTextField jtfBlockSize, jtfNumRounds, jtfSboxSize;
	private JButton    jbHelp, jbOK, jbCancel;
	private HelpMessage msg;
	
	public MasterPropertiesDialog()
	{
		super();
		setModal(true);
		msg = null;
		closedSuccessful = false;
		
		jlBlockSize = new JLabel("Block Size: ");
		jlNumRounds = new JLabel("Num. Rounds: ");
		jlSboxSize  = new JLabel("S-box size: ");
		
		jtfBlockSize = new JTextField(25);
		jtfNumRounds = new JTextField(25);
		jtfSboxSize  = new JTextField(25);
		jtfBlockSize.setText(String.valueOf(MasterPropertiesCache.getInstance().getBlockSize()));
		jtfNumRounds.setText(String.valueOf(MasterPropertiesCache.getInstance().getNumRounds()));
		jtfSboxSize.setText(String.valueOf(MasterPropertiesCache.getInstance().getSBoxSize()));
		
		JPanel jpBlockSize = new JPanel();
		jpBlockSize.add(jlBlockSize);
		jpBlockSize.add(jtfBlockSize);
		
		JPanel jpNumRounds = new JPanel();
		jpNumRounds.add(jlNumRounds);
		jpNumRounds.add(jtfNumRounds);
		
		JPanel jpSboxSize  = new JPanel();
		jpSboxSize.add(jlSboxSize);
		jpSboxSize.add(jtfSboxSize);
		
		JPanel jpCenter = new JPanel();
		jpCenter.setBorder(new EmptyBorder(15,15,15,15));
		jpCenter.setLayout(new BoxLayout(jpCenter, BoxLayout.Y_AXIS));
		jpCenter.add(jpBlockSize);
		jpCenter.add(jpNumRounds);
		jpCenter.add(jpSboxSize);
		
		
		jbOK = new JButton("OK");
		jbOK.setMnemonic('O');
		jbCancel = new JButton("Cancel");
		jbCancel.setMnemonic('C');
		jbHelp = new JButton("Help");
		jbHelp.setMnemonic('H');
		
		jbOK.addActionListener(this);
		jbCancel.addActionListener(this);
		jbHelp.addActionListener(this);
		
		JPanel jpSouth       = new JPanel();
		JPanel subPanelLeft  = new JPanel();
		JPanel subPanelRight = new JPanel();
		
		jpSouth.setLayout(new BorderLayout());
		subPanelLeft.add(jbHelp);
		subPanelRight.add(jbOK);
		subPanelRight.add(jbCancel);
		
		jpSouth.add(subPanelLeft, BorderLayout.WEST);
		jpSouth.add(subPanelRight, BorderLayout.EAST);
		
		setLayout(new BorderLayout());
		add(jpCenter, BorderLayout.CENTER);
		add(jpSouth,  BorderLayout.SOUTH);
		
		setTitle("Set SPN Properties");
		pack();
		setVisible(true);
	}
	
	public boolean isClosedSuccessful()
	{
		return closedSuccessful;
	}
	
	@Override
	public void dispose()
	{
		if(msg != null)
		{
			msg.dispose();
		}
		
		super.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource().equals(jbCancel))
		{
			dispose();
		}
		else if(ae.getSource().equals(jbOK))
		{
			int blockSize = -1;
			int sboxSize  = -1;
			int numRounds = -1;
			
			try
			{
				blockSize = Integer.parseInt(jtfBlockSize.getText());
				sboxSize  = Integer.parseInt(jtfSboxSize.getText());
				numRounds = Integer.parseInt(jtfNumRounds.getText());
			}
			catch(NumberFormatException nfe)
			{
				// Do nothing.
				// Anything that would happen due to this exception is handled during
				// subsequent validation.
			}
			
			if(blockSize <= 0 || sboxSize <= 0 || numRounds <= 0)
			{
				JOptionPane.showMessageDialog(this,
						"Block Size, S-box size, and number of rounds must all be greater than zero.",
						"ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
			else if(sboxSize > blockSize || blockSize%sboxSize != 0)
			{
				JOptionPane.showMessageDialog(this,
						"Block size must be divisible by S-box size.",
						"ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
			else if(blockSize > Long.SIZE)
			{
				// We represent rounds using longs in some places, so the bit length
				// of a long needs to be a hard size-limit on block size. Since this
				// is usually 64 bits it's not a big deal, as trying to cryptanalyze
				// a 64-bit cipher with this tool would be prohibitively cumbersome.
				
				JOptionPane.showMessageDialog(this,
						"Block sizes greater than " + Long.SIZE + " are not supported.",
						"ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				// Everything is valid.
				MasterPropertiesCache.getInstance().setBlockSize(blockSize);
				MasterPropertiesCache.getInstance().setSBoxSize(sboxSize);
				MasterPropertiesCache.getInstance().setNumRounds(numRounds);
				
				closedSuccessful = true;
				dispose();
			}
		}
		else if(ae.getSource().equals(jbHelp))
		{
			if(msg == null)
			{
				msg = new HelpMessage(HelpMessageConstants.HELP_DLG_MSTR_PROP, this,
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
	}
}
