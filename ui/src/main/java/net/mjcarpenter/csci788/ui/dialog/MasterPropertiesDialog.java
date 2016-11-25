package net.mjcarpenter.csci788.ui.dialog;

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

import net.mjcarpenter.csci788.ui.util.MasterPropertiesCache;

@SuppressWarnings("serial")
public class MasterPropertiesDialog extends JDialog implements ActionListener
{
	private JLabel     jlBlockSize, jlNumRounds, jlSboxSize;
	private JTextField jtfBlockSize, jtfNumRounds, jtfSboxSize;
	private JButton    jbHelp, jbOK, jbCancel;
	
	public MasterPropertiesDialog()
	{
		super();
		setModal(true);
		
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
	
	public static void main(String[] args)
	{
		new MasterPropertiesDialog();
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
			else
			{
				// Everything is valid.
				MasterPropertiesCache.getInstance().setBlockSize(blockSize);
				MasterPropertiesCache.getInstance().setSBoxSize(sboxSize);
				MasterPropertiesCache.getInstance().setNumRounds(numRounds);
				
				dispose();
			}
		}
		
	}
}
