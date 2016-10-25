package net.mjcarpenter.csci788.crypto.spn;

import java.util.BitSet;

public class Round implements SPNComponent
{
	private final int         bitLength;
	private final SBox[]      roundBoxes;
	private final Key         subKey;
	private final Permutation perm;
	
	public Round(final int bitLength, final Key subKey, final Permutation perm, final SBox... roundBoxes)
	{
		int boxLength = 0;
		for(int i=0; i<roundBoxes.length; i++)
		{
			boxLength += roundBoxes[i].bitSize();
			
			if(i > 0 && roundBoxes[i].bitSize() != roundBoxes[i-1].bitSize())
				throw new IllegalArgumentException("All SBoxes in a round must be the same size!");
		}
		
		if(boxLength != bitLength)
			throw new IllegalArgumentException(
					String.format("Total SBox size must equal bit length! Expected: %d, Got: %d.", bitLength, boxLength));
		
		if(subKey.length() != bitLength)
			throw new IllegalArgumentException("Subkey length must match bit length!");
		
		
		this.bitLength  = bitLength;
		this.roundBoxes = roundBoxes;
		this.subKey     = subKey;
		this.perm       = perm;
	}
	
	public int bitLength()
	{
		return bitLength;
	}
	
	public byte[] process(final byte[] in)
	{
		BitSet set = BitSet.valueOf(subKey.xor(in));
		BitSet outSet = new BitSet(in.length*8);
		
		for(int i=0; i<roundBoxes.length; i++)
		{
			BitSet subSet = set.get(i*roundBoxes[i].bitSize(), (i+1)*roundBoxes[i].bitSize());
			int subPart = (subSet.toByteArray().length > 0) ? (int)subSet.toByteArray()[0] : 0;
			long outPart = roundBoxes[i].sub(subPart)<<(i*roundBoxes[i].bitSize());
			subSet = BitSet.valueOf(new long[]{outPart});
			outSet.or(subSet);
		}
		
		return perm.permuteFwd(outSet.toByteArray());
	}
	
	public byte[] invert(final byte[] in)
	{
		BitSet set = BitSet.valueOf(perm.permuteRev(in));
		BitSet outSet = new BitSet(in.length*8);
		
		for(int i=0; i<roundBoxes.length; i++)
		{
			BitSet subSet = set.get(i*roundBoxes[i].bitSize(), (i+1)*roundBoxes[i].bitSize());
			int subPart = (subSet.toByteArray().length > 0) ? (int)subSet.toByteArray()[0] : 0;
			long outPart = roundBoxes[i].invert().sub(subPart)<<(i*roundBoxes[i].bitSize());
			subSet = BitSet.valueOf(new long[]{outPart});
			outSet.or(subSet);
		}
		
		return subKey.xor(outSet.toByteArray());
	}
}
