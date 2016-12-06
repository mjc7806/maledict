package net.mjcarpenter.maledict.crypto.ldc;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import net.mjcarpenter.maledict.crypto.spn.Round;
import net.mjcarpenter.maledict.crypto.spn.SPNetwork;

public class CipherLinearApproximationBuilder
{
	private SPNetwork    spn;
	private List<byte[]> masks;
	
	public CipherLinearApproximationBuilder(SPNetwork spn)
	{
		this.spn   = spn;
		this.masks = new ArrayList<byte[]>();
	}
	
	public void setRoundInputMask(int roundNum, byte[] inputMask)
	{
		masks.add(roundNum, inputMask);
	}
	
	public int[] getRoundMask(int roundNum)
	{
		return masksForRound(roundNum, masks.get(roundNum));
	}
	
	private int[] masksForRound(int roundNum, byte[] inputMask)
	{
		Round relevantRound = spn.getRounds()[roundNum];
		BitSet input = BitSet.valueOf(inputMask);
		
		int[] sboxMasks = new int[relevantRound.getSBoxes().length];
		int sboxSize = relevantRound.getSBoxes()[0].bitSize();
		for(int i=0; i<sboxMasks.length; i++)
		{
			sboxMasks[i] = ((Long)input.get(i*sboxSize, i*sboxSize+sboxSize).toLongArray()[0]).intValue();
		}
		
		return sboxMasks;
	}
}
