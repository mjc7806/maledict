package net.mjcarpenter.csci788.ui.dialog.ldc;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.BitSet;

import org.apache.commons.lang3.ArrayUtils;

import net.mjcarpenter.csci788.crypto.ldc.Approximation;
import net.mjcarpenter.csci788.crypto.spn.Round;
import net.mjcarpenter.csci788.crypto.spn.SBox;
import net.mjcarpenter.csci788.crypto.spn.SPNetwork;
import net.mjcarpenter.csci788.ui.component.CoordinateToggleButton;

public final class LinearApproximationDialog extends ApproximationDialog
{
	private int[][] roundInMasks;
	private int[][] roundOutMasks;
	
	public LinearApproximationDialog(SPNetwork spn)
	{
		super(spn);
		setTitle("Construct Linear Approximation");
		
		roundInMasks  = new int[spn.getRounds().length-1][spn.getRounds()[0].getSBoxes().length];
		roundOutMasks = new int[roundInMasks.length][roundInMasks[0].length];
		
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof CoordinateToggleButton)
		{
			CoordinateToggleButton btn = (CoordinateToggleButton)e.getSource();
			
			if(btn.isSelected())
			{
				SBox relevantBox = getBoxForButton(btn);
				
				LATSelectionDialog lsd = new LATSelectionDialog(relevantBox);
				
				int inMask = lsd.getSelectedIn();
				int outMask = lsd.getSelectedOut();
				int bias = lsd.getSelectedBias();
				
				System.out.printf("Selected: [%d, %d] --> %d\n", inMask, outMask, bias);
				
				if(btn.row == 0)
				{
					roundInMasks[btn.row][btn.col] = inMask;
				}
				
				roundOutMasks[btn.row][btn.col] = outMask;
			}
			else
			{
				roundOutMasks[btn.row][btn.col] = 0;
			}
			
			long x = getFullOutMaskForRound(btn.row);
			long y = spn.getRounds()[btn.row].getPermutation().permuteFwd(x);
			
			if(btn.row != roundInMasks.length-1)
				roundInMasks[btn.row+1] = convertMaskToRowArray(btn.row+1, y);
			
			System.out.printf("RoundOutMasks[%d] = %d\n", btn.row, x);
			System.out.printf("RoundInMasks[%d]  = %d\n", btn.row+1, y);
			
			for(CoordinateToggleButton each: allButtons)
			{
				each.setEnabled(each.row == 0 || roundInMasks[each.row][each.col] != 0);
			}
		}
	}
	
	private int[] convertMaskToRowArray(int roundNum, long mask)
	{
		int[] res = new int[spn.getRounds()[roundNum].getSBoxes().length];
		int bitSize = spn.getRounds()[roundNum].getSBoxes()[0].bitSize();
		int maskPerBox = (1<<bitSize)-1;
		
		for(int i=0; i<res.length; i++)
		{
			res[i] = (int)(maskPerBox&(mask>>(bitSize*i)));
		}
		
		return res;
	}
	
	public Approximation getCipherapproximation()
	{
		return new Approximation(getFullInMaskForRound(0), getFullOutMaskForRound(roundOutMasks.length-1));
	}
	
	private long getFullInMaskForRound(int index)
	{
		long res = 0;
		for(int i=0; i<roundInMasks[index].length; i++)
		{
			res |= (roundInMasks[index][i]<<(i*spn.getRounds()[index].getSBoxes()[i].bitSize()));
		}
		
		return res;
	}
	
	private long getFullOutMaskForRound(int index)
	{
		long res = 0;
		for(int i=0; i<roundOutMasks[index].length; i++)
		{
			res |= (roundOutMasks[index][i]<<(i*spn.getRounds()[index].getSBoxes()[i].bitSize()));
		}
		
		return res;
	}
	
	private class RoundApproximation
	{
		private int   roundNum;
		private int   boxSize;
		private int[] boxApprox;
		
		public RoundApproximation(int roundNum, Round r)
		{
			this.roundNum  = roundNum;
			this.boxSize   = r.getSBoxes()[0].bitSize();
			this.boxApprox = new int[r.getSBoxes().length];
		}
		
		public void setApproxForBox(int i, int approx)
		{
			boxApprox[i] = approx;
		}
		
		public int getApproxForBox(int i)
		{
			return boxApprox[i];
		}
	}
}
