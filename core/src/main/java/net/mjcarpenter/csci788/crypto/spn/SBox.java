package net.mjcarpenter.csci788.crypto.spn;

import java.util.Arrays;

import net.mjcarpenter.csci788.util.BitUtils;

public class SBox
{
	private static final String VALIDATION_INDICES = "All inputs must have corresponding output.";
	
	private int[]   mapFwd;
	private int[][] lat;
	private int[][] ddt;
	
	public SBox(int... mapFwd)
	{	
		if(BitUtils.countSetBits(mapFwd.length) != 1) // Only one bit set == power of two
			throw new IllegalArgumentException("SBox size must be power of two!");
		
		for(int i=0; i<mapFwd.length; i++)
		{
			boolean contains = false;
			
			for(int j=0; j<mapFwd.length; j++)
			{
				contains |= (mapFwd[j] == i);
				if(contains) break;
			}
			
			if(!contains)
				throw new IllegalArgumentException(VALIDATION_INDICES);
		}
		
		
		this.mapFwd = mapFwd;
		this.lat = constructLAT();
		this.ddt = constructDDT();
	}
	
	public SBox invert()
	{
		int[] reverse = new int[mapFwd.length];
		
		for(int i=0; i<mapFwd.length; i++)
		{
			for(int j=0; j<mapFwd.length; j++)
			{
				if(mapFwd[j] == i)
				{
					reverse[i] = j;
					break;
				}
			}
		}
		
		return new SBox(reverse);
	}
	
	/**
	 * Size in bits for input and output.
	 * @return
	 */
	public int size() // inSize == outSize, all SPN S-Boxes are bijective
	{
		return mapFwd.length;
	}
	
	public int sub(int n)
	{
		if(n >= size() || n < 0)
			throw new IllegalArgumentException("Invalid index " + n);
		else
			return mapFwd[n];
	}
	
	public int[][] getLAT()
	{
		// Return a copy to maintain immutability.
		return Arrays.copyOf(lat, lat.length);
	}
	
	public int[][] getDDT()
	{
		// Return a copy to maintain immutability.
		return Arrays.copyOf(ddt, ddt.length);
	}
	
	private int[][] constructLAT()
	{
		int[][] lat = new int[size()][size()];
		for(int i=0; i<size(); i++)
			for(int j=0; j<size(); j++)
				lat[i][j] = -8;
		
		for(int i=0; i<size(); i++)
			for(int inMask=0; inMask<size(); inMask++)
				for(int outMask=0; outMask<size(); outMask++)
					if((BitUtils.countSetBits(i&inMask)%2) == (BitUtils.countSetBits(sub(i)&outMask)%2))
						lat[inMask][outMask]++;
		
		return lat;
	}
	
	private int[][] constructDDT()
	{
		int[][] ddt = new int[size()][size()];
		
		for(int input=0; input<size(); input++)
		{
			int output1 = sub(input);
			
			for(int inDiff=0; inDiff<size(); inDiff++)
			{
				ddt[inDiff][output1^sub(input^inDiff)]++;
			}
		}
		
		return ddt;
	}
}
