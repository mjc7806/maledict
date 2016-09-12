package net.mjcarpenter.csci788.crypto.spn;

import java.util.BitSet;

import org.apache.commons.lang3.ArrayUtils;

public class Round
{
	private int         bitLength;
	private SBox[]      roundBoxes;
	private Key         subKey;
	private Permutation perm;
	
	public Round(int bitLength, Key subKey, Permutation perm, SBox... roundBoxes)
	{
		int boxLength = 0;
		for(int i=0; i<roundBoxes.length; i++)
		{
			boxLength += roundBoxes[i].size();
			
			if(i > 0 && roundBoxes[i].size() != roundBoxes[i-1].size())
				throw new IllegalArgumentException("All SBoxes in a round must be the same size!");
		}
		
		if(boxLength != bitLength)
			throw new IllegalArgumentException("Total SBox size must equal bit length!");
		
		if(subKey.length() != bitLength)
			throw new IllegalArgumentException("Subkey length must match bit length!");
		
		
		this.bitLength  = bitLength;
		this.roundBoxes = roundBoxes;
		this.subKey     = subKey;
		this.perm       = perm;
	}
	
	public byte[] process(byte[] in)
	{
		BitSet set = BitSet.valueOf(subKey.xor(in));
		BitSet outSet = new BitSet(in.length);
		
		for(int i=0; i<roundBoxes.length; i++)
		{
			BitSet subSet = set.get(i*roundBoxes[i].size(), (i+1)*roundBoxes[i].size());
			int subPart = (int)subSet.toByteArray()[0];
			long outPart = roundBoxes[i].sub(subPart)<<(i*roundBoxes[i].size());
			subSet = BitSet.valueOf(new long[]{outPart});
			outSet.or(subSet);
		}
		
		return perm.permuteFwd(outSet.toByteArray());
	}
}
