package net.mjcarpenter.maledict.ui.dialog.ldc;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.mjcarpenter.maledict.crypto.ldc.AbstractApproximation;
import net.mjcarpenter.maledict.crypto.spn.SBox;
import net.mjcarpenter.maledict.crypto.spn.SPNetwork;
import net.mjcarpenter.maledict.ui.component.CoordinateToggleButton;
import net.mjcarpenter.maledict.ui.message.help.HelpMessage;
import net.mjcarpenter.maledict.ui.util.MasterPropertiesCache;

@SuppressWarnings("serial")
public abstract class ApproximationDialog extends JDialog implements ActionListener
{
	protected long[] roundInMasks;
	protected long[] roundOutMasks;
	
	protected SPNetwork   spn;
	protected CoordinateToggleButton[][] boxButtons;
	protected List<CoordinateToggleButton> allButtons;
	protected JButton jbOK, jbCancel, jbHelp;
	
	protected int lastRow;
	
	private HelpMessage msg;
	private boolean successful;
	
	public ApproximationDialog(SPNetwork network, String title)
	{
		super();
		setTitle(title);
		setModal(true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		this.msg = null;
		this.spn = network;
		this.roundInMasks  = new long[spn.getRounds().length];
		this.roundOutMasks = new long[spn.getRounds().length];
		this.boxButtons = new CoordinateToggleButton[spn.getRounds().length][spn.getRounds()[0].getSBoxes().length];
		this.successful = false;
		
		allButtons = new ArrayList<CoordinateToggleButton>();
		
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(boxButtons.length, boxButtons[0].length, 10, 10));
		gridPanel.setBorder(new EmptyBorder(10,10,10,10));
		
		for(int i=0; i<spn.getRounds().length-1; i++) // Stop 1 short because last round never gets approximated.
		{
			boolean restOfCipherNoop = isBoxRowNoop(i+1);
			
			if(restOfCipherNoop)
			{
				// If the next row has no operative S-boxes, we need to check that the rest of the cipher also
				// has no operative S-boxes, just in case a later round does have S-boxes, in which case it is
				// not appropriate to end our approximation here.
				
				for(int j=i+2; j<spn.getRounds().length && restOfCipherNoop; j++)
				{
					restOfCipherNoop &= isBoxRowNoop(j);
				}
			}
			
			if(restOfCipherNoop)
			{
				lastRow = i-1;
				
				// Fill the rest of the cells with blanks.
				for(int j=i; j<spn.getRounds().length; j++)
				{
					for(int k=0; k<spn.getRounds()[j].getSBoxes().length; k++)
					{
						gridPanel.add(new JLabel());
					}
				}
				
				break;
			}
			else
			{
				for(int j=0; j<spn.getRounds()[i].getSBoxes().length; j++)
				{
					boxButtons[i][j] = new CoordinateToggleButton(String.format("S-box (%d, %d)", i, j), i, j);
					boxButtons[i][j].addActionListener(this);
					
					gridPanel.add(boxButtons[i][j]);
					allButtons.add(boxButtons[i][j]);
				}
			}
		}
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		JPanel btnSubPanel = new JPanel();
		
		jbOK = new JButton("OK");
		jbCancel = new JButton("Cancel");
		jbHelp = new JButton("Help");
		
		jbHelp.addActionListener(ae -> handleHelp());
		jbCancel.addActionListener(ae ->
			{
				MasterPropertiesCache.getInstance().clearVisualizationColoring();
				successful = false;
				setVisible(false);
			});
		jbOK.addActionListener(ae ->
			{
				successful = true;
				setVisible(false);
			});
		
		jbOK.setEnabled(false);
		
		btnSubPanel.add(jbCancel);
		btnSubPanel.add(jbOK);
		buttonPanel.add(jbHelp, BorderLayout.WEST);
		buttonPanel.add(btnSubPanel, BorderLayout.EAST);
		buttonPanel.setBorder(new EmptyBorder(10,10,10,10));
		
		setLayout(new BorderLayout());
		add(gridPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		pack();
		setVisible(true);
	}
	
	private boolean isBoxRowNoop(int boxRow)
	{
		boolean noopRow = true;
		for(int i=0; i<spn.getRounds()[boxRow].getSBoxes().length; i++)
		{
			noopRow &= spn.getRounds()[boxRow].getSBoxes()[i].isNoop();
			
			if(!noopRow) break;
		}
		
		return noopRow;
	}
	
	public int getLastRow()
	{
		return lastRow;
	}
	
	public boolean isSuccessful()
	{
		return successful;
	}
	
	public AbstractApproximation getCipherApproximation()
	{
		return getCipherApproximation(roundInMasks[0], roundInMasks[lastRow+1]);
	}
	
	public abstract AbstractApproximation getCipherApproximation(long plaintextMask, long lastRoundMask);
	protected abstract TableSelectionDialog createTableSelectionDialog(SBox box, long mask);
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
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof CoordinateToggleButton)
		{
			CoordinateToggleButton btn = (CoordinateToggleButton)e.getSource();
			SBox relevantBox = getBoxForButton(btn);
			
			int shift = relevantBox.bitSize()*(boxButtons[btn.row].length-(btn.col+1));
			long boxMask = ((1<<relevantBox.bitSize())-1)<<shift;
						
			if(btn.isSelected())
			{
				long shiftedMask = (roundInMasks[btn.row]&boxMask) >>> shift;
								
				TableSelectionDialog ad = createTableSelectionDialog(relevantBox, shiftedMask);
				
				if(ad.hasSelection())
				{
					long inMask = ad.getSelectedIn()<<shift;
					long outMask = ad.getSelectedOut()<<shift;
					int bias = ad.getSelectedBias();
										
					if(btn.row == 0)
					{
						roundInMasks[btn.row] ^= inMask;
					}
					
					roundOutMasks[btn.row] ^= outMask;
				}
				else
				{
					btn.setSelected(false);
					roundOutMasks[btn.row] &= (~boxMask);
				}
				
				ad.dispose();
			}
			else
			{
				roundOutMasks[btn.row] &= (~boxMask);
			}
			
			if(btn.row != roundInMasks.length-1)
			{
				roundInMasks[btn.row+1] = spn.getRounds()[btn.row].getPermutation().permuteFwd(roundOutMasks[btn.row]);
			}
						
			for(CoordinateToggleButton each: allButtons)
			{
				int eachBitSize = getBoxForButton(each).bitSize();
				int eachShift = eachBitSize*(boxButtons[each.row].length-(each.col+1));
				long mask = ((1<<eachBitSize)-1)<<eachShift;
				each.setEnabled(each.row == 0 || (roundInMasks[each.row]&mask) != 0);
			}
			
			MasterPropertiesCache.getInstance().colorVisualization(roundInMasks, roundOutMasks);
			
			boolean lastRowHasSelection = false;
			int numEnabled  = 0;
			int numSelected = 0;
			for(CoordinateToggleButton each: boxButtons[lastRow])
			{
				lastRowHasSelection |= each.isSelected();
				if(each.isSelected())
				{
					numSelected++;
				}
				if(each.isEnabled())
				{
					numEnabled++;
				}
			}
			
			// The approximation is done when ALL approximations in the last row of
			// boxes that need to be approximated have been established.
			jbOK.setEnabled(lastRowHasSelection && numSelected == numEnabled);
		}
	}
	
	protected SBox getBoxForButton(CoordinateToggleButton btn)
	{
		return spn.getRounds()[btn.row].getSBoxes()[btn.col];
	}
}
