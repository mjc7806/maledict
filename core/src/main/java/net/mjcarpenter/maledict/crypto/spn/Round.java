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
package net.mjcarpenter.maledict.crypto.spn;

import java.util.BitSet;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import net.mjcarpenter.maledict.util.BitUtils;

@SuppressWarnings("serial")
@XStreamAlias("round")
public class Round implements SPNComponent
{
	@XStreamAsAttribute
	private final int         bitLength;
	
	@XStreamAsAttribute
	@XStreamAlias("noop")
	private final boolean     noop;
	
	@XStreamImplicit
	private final SBox[]      roundBoxes;
	@XStreamAlias("subkey")
	private final Key         subKey;
	@XStreamAlias("perm")
	private final Permutation perm;
	
	public Round(final int bitLength, final Key subKey, final Permutation perm, final SBox... roundBoxes)
	{
		this(false, bitLength, subKey, perm, roundBoxes);
	}
	
	private Round(boolean noop, final int bitLength, final Key subKey, final Permutation perm, final SBox... roundBoxes)
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
		
		
		this.noop       = noop;
		this.bitLength  = bitLength;
		this.roundBoxes = roundBoxes;
		this.subKey     = subKey;
		this.perm       = perm;
	}
	
	public static Round noop(int bitLength, int numBoxes)
	{
		SBox noopBox = SBox.noop(1<<(bitLength/numBoxes));
		SBox[] boxSet = new SBox[numBoxes];
		for(int i=0; i<numBoxes; i++)
		{
			boxSet[i] = noopBox;
		}
		
		Key k = Key.noop(bitLength);
		Permutation p = Permutation.noop(bitLength);
		return new Round(true, bitLength, k, p, boxSet);
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
		BitSet outSet = new BitSet(in.length*Byte.SIZE);
		
		for(int i=0; i<roundBoxes.length; i++)
		{
			BitSet subSet = set.get(i*roundBoxes[i].bitSize(), (i+1)*roundBoxes[i].bitSize());
			int subPart = (subSet.toByteArray().length > 0) ? (int)subSet.toByteArray()[0] : 0;
			long outPart = roundBoxes[i].sub(subPart)<<(i*roundBoxes[i].bitSize());
			subSet = BitSet.valueOf(new long[]{outPart});
			outSet.or(subSet);
		}
		
		byte[] osbytes = BitUtils.convertBitSetToByte(outSet, in.length);
	    return perm.permuteFwd(osbytes);
	}
	
	public byte[] invert(final byte[] in)
	{
		BitSet set = BitSet.valueOf(perm.permuteRev(in));
		BitSet outSet = new BitSet(in.length*Byte.SIZE);
		
		for(int i=0; i<roundBoxes.length; i++)
		{
			BitSet subSet = set.get(i*roundBoxes[i].bitSize(), (i+1)*roundBoxes[i].bitSize());
			int subPart = (subSet.toByteArray().length > 0) ? (int)subSet.toByteArray()[0] : 0;
			long outPart = roundBoxes[i].invert().sub(subPart)<<(i*roundBoxes[i].bitSize());
			subSet = BitSet.valueOf(new long[]{outPart});
			outSet.or(subSet);
		}
		
		byte[] osbytes = BitUtils.convertBitSetToByte(outSet, in.length);
		return subKey.xor(osbytes);
	}

	@Override
	public boolean isNoop()
	{
		return noop;
	}
}
