package net.mjcarpenter.csci788.crypto.spn;

import java.util.BitSet;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("round")
public class Round implements SPNComponent
{
	@XStreamAsAttribute
	private final int         bitLength;
	
	@XStreamImplicit
	private final SBox[]      roundBoxes;
	@XStreamAlias("subkey")
	private final Key         subKey;
	@XStreamAlias("perm")
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
	
	public Round replaceKey(final Key k)
	{
		return new Round(this.bitLength, k, this.perm, this.roundBoxes);
	}
	
	public Round replacePermutation(final Permutation p)
	{
		return new Round(this.bitLength, this.subKey, p, this.roundBoxes);
	}
	
	public Round replaceSBox(final SBox s, final int idx)
	{
		SBox[] boxes = new SBox[roundBoxes.length];
		for(int i=0; i<boxes.length; i++)
		{
			boxes[i] = (i == idx) ? s : roundBoxes[i];
		}
		
		return replaceSBoxes(boxes);
	}
	
	public Round replaceSBoxes(final SBox... boxes)
	{
		return new Round(this.bitLength, this.subKey, this.perm, boxes);
	}
	
	public int bitLength()
	{
		return bitLength;
	}
	
	public SBox[] getSBoxes()
	{
		return roundBoxes;
	}
	
	public Permutation getPermutation()
	{
		return perm;
	}
	
	public Key getSubKey()
	{
		return subKey;
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
