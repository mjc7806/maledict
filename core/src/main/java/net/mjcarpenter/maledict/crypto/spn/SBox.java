package net.mjcarpenter.maledict.crypto.spn;

import java.util.Arrays;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import net.mjcarpenter.maledict.util.BitUtils;
@SuppressWarnings("serial")
@XStreamAlias("sbox")
public final class SBox implements SPNComponent
{
	private static final String VALIDATION_INDICES = "All inputs must have corresponding output.";
	
	@XStreamAlias("map")
	private final int[]   mapFwd;
	
	@XStreamAsAttribute
	@XStreamAlias("noop")
	private final boolean noop;
	
	// Derived transient fields.
	private transient int[][] lat;
	private transient int[][] ddt;
	
	public SBox(final int... mapFwd)
	{
		this(false, mapFwd);
	}
	
	private SBox(boolean noop, final int... mapFwd)
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
		this.noop = noop;
		this.lat = constructLAT();
		this.ddt = constructDDT();
	}
	
	public static SBox noop(final int length)
	{
		int[] map = new int[length];
		for(int i=0; i<map.length; i++)
			map[i] = i;
		
		return new SBox(true, map);
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
	public int bitSize() // inSize == outSize, all SPN S-Boxes are bijective
	{
		return BitUtils.countSetBits(mapFwd.length-1);
	}
	
	public int sub(final int n)
	{
		if(n >= mapFwd.length || n < 0)
			throw new IllegalArgumentException("Invalid index " + n);
		else
			return mapFwd[n];
	}
	
	public int[][] getLAT()
	{
		if(lat == null)
		{
			lat = constructLAT();
		}
		
		return Arrays.copyOf(lat, lat.length);
	}
	
	public int[][] getDDT()
	{
		if(ddt == null)
		{
			ddt = constructDDT();
		}
		
		return Arrays.copyOf(ddt, ddt.length);
	}
	
	private int[][] constructLAT()
	{
		int[][] lat = new int[mapFwd.length][mapFwd.length];
		for(int i=0; i<mapFwd.length; i++)
			for(int j=0; j<mapFwd.length; j++)
				lat[i][j] = -1*(mapFwd.length>>1);
		
		for(int i=0; i<mapFwd.length; i++)
			for(int inMask=0; inMask<mapFwd.length; inMask++)
				for(int outMask=0; outMask<mapFwd.length; outMask++)
					if((BitUtils.countSetBits(i&inMask)%2) == (BitUtils.countSetBits(sub(i)&outMask)%2))
						lat[inMask][outMask]++;
		
		return lat;
	}
	
	private int[][] constructDDT()
	{
		int[][] ddt = new int[mapFwd.length][mapFwd.length];
		
		for(int input=0; input<mapFwd.length; input++)
		{
			int output1 = sub(input);
			
			for(int inDiff=0; inDiff<mapFwd.length; inDiff++)
			{
				ddt[inDiff][output1^sub(input^inDiff)]++;
			}
		}
		
		return ddt;
	}
	
	private Object readResolve()
	{
		// Reconstruct derivable transient fields during deserialization.
		this.lat = constructLAT();
		this.ddt = constructDDT();
		
		return this;
	}

	@Override
	public boolean isNoop()
	{
		return noop;
	}
}
