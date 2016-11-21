package net.mjcarpenter.csci788.crypto.spn;

import java.util.BitSet;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
@XStreamAlias("key")
public final class Key implements SPNComponent
{
	@XStreamAsAttribute
	@XStreamAlias("value")
	private final byte[]  key;
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
	
	public byte[] xor(final byte[] in)
	{
		BitSet inSet = BitSet.valueOf(in);
		BitSet keySet = BitSet.valueOf(key);
		
		inSet.xor(keySet);
		return inSet.toByteArray();
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
}
