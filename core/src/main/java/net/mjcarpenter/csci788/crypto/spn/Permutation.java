package net.mjcarpenter.csci788.crypto.spn;

import java.util.Arrays;
import java.util.BitSet;

import org.apache.commons.lang3.ArrayUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("permutation")
public final class Permutation implements SPNComponent
{
	private static final String VALIDATION_INDICES = "All inputs must have corresponding output.";
	
	@XStreamAlias("mapping")
	private final     int[] mapping;
	
	// Derived transient fields.
	private transient int[] reverse;
	
	public Permutation(final int... mapping)
	{
		this.mapping = mapping;
		this.reverse = constructReverse();
	}
	
	public static Permutation noop(final int length)
	{
		int[] map = new int[length];
		for(int i=0; i<map.length; i++)
			map[i] = i;
		return new Permutation(map);
	}
	
	public int length()
	{
		return mapping.length;
	}
	
	public int[] getMapping()
	{
		return this.mapping;
	}
	
	public long permuteFwd(long in)
	{
		return permute(in, mapping);
	}
	
	public long permuteRev(long in)
	{
		return permute(in, reverse);
	}
	
	/*
	 * Modified conversion code from:
	 * http://stackoverflow.com/a/29132118/2250867
	 */
	private long permute(long in, final int[] map)
	{
		byte[] convertIn = new byte[Long.SIZE/Byte.SIZE];
		for(int i=7; i>=0; i--)
		{
			convertIn[i] = (byte)(in&0xFF);
			in >>= Byte.SIZE;
		}
		ArrayUtils.reverse(convertIn);
		
		byte[] convertOut = permute(Arrays.copyOfRange(convertIn, 0, mapping.length/Byte.SIZE), map);
		ArrayUtils.reverse(convertOut);
		long out = 0;
		for(int i=0; i<convertOut.length; i++)
		{
			out <<= Byte.SIZE;
			out |= (convertOut[i]&0xFF);
		}
		
		return out;
	}
	
	public byte[] permuteFwd(final byte[] in)
	{
		return permute(in, mapping);
	}
	
	public byte[] permuteRev(final byte[] in)
	{
		return permute(in, reverse);
	}
	
	private byte[] permute(final byte[] in, final int[] map)
	{
		if(in.length*8 != map.length)
			throw new IllegalArgumentException("Input length must match permutation size!");
		
		ArrayUtils.reverse(in);
		BitSet set = BitSet.valueOf(in);
		BitSet out = new BitSet(map.length);
		
		for(int i=0; i<map.length; i++)
		{
			if(set.get(i))
				out.set(map[i]);
		}
		
		byte[] outArray = out.toByteArray();
		ArrayUtils.reverse(outArray);
		return outArray;
	}
	
	public int outPosition(final int inPosition)
	{
		return mapping[inPosition];
	}
	
	public int inPosition(final int outPosition)
	{
		return reverse[outPosition];
	}
	
	private int[] constructReverse()
	{
		int[] revMap = new int[mapping.length];
		
		for(int i=0; i<mapping.length; i++)
		{
			boolean contains = false;
			
			for(int j=0; j<mapping.length; j++)
			{
				if(mapping[j] == i)
				{
					revMap[i] = j;
					contains = true;
					break;
				}
			}
			
			if(!contains)
				throw new IllegalArgumentException(VALIDATION_INDICES);
		}
		
		return revMap;
	}
	
	private Object readResolve()
	{
		// Reconstruct derivable transient fields during deserialization.
		this.reverse = constructReverse();
		return this;
	}
}
