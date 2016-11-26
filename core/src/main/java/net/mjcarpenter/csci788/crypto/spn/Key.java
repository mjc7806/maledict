package net.mjcarpenter.csci788.crypto.spn;

import java.util.Arrays;
import java.util.BitSet;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import net.mjcarpenter.csci788.util.BitUtils;
@SuppressWarnings("serial")
@XStreamAlias("key")
public final class Key implements SPNComponent, Comparable<Key>
{
	@XStreamAsAttribute
	@XStreamAlias("value")
	private final byte[]  key;
	
	@XStreamAsAttribute
	@XStreamAlias("noop")
	private final boolean noop;
	
	public Key(final byte[] key)
	{
		this(false, key);
	}
	
	private Key(boolean noop, final byte[] key)
	{
		this.noop = noop;
		this.key  = key;
	}
	
	public static Key noop(int length)
	{
		int mod = length % 8;
		byte[] nullKey = new byte[(mod > 0) ? (length/8)+1 : length/8];
		
		for(int i=0; i<nullKey.length; i++)
		{
			nullKey[i] = 0x0;
		}
		
		return new Key(true, nullKey);
	}
	
	public byte[] getKeyValue()
	{
		return this.key;
	}
	
	public byte[] xor(final byte[] in)
	{
		BitSet inSet = BitSet.valueOf(in);
		BitSet keySet = BitSet.valueOf(key);
		
		inSet.xor(keySet);
		return BitUtils.convertBitSetToByte(inSet, in.length);
		
		//return BitUtils.longToByte(BitUtils.byteToLong(in)^BitUtils.byteToLong(key), in.length);
	}
	
	public int length()
	{
		return key.length*8;
	}

	@Override
	public boolean isNoop()
	{
		return noop;
	}

	@Override
	public int compareTo(Key that)
	{
		int result = 0;
		
		if(Arrays.equals(this.key, that.key))
		{
			result = 0;
		}
		else
		{
			long thisKeyLong = BitUtils.byteToLong(this.key);
			long thatKeyLong = BitUtils.byteToLong(that.key);
			
			result = Long.compare(thisKeyLong, thatKeyLong);
		}
		
		return result;
	}
}
