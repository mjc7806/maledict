package net.mjcarpenter.csci788.crypto.spn;

import java.util.BitSet;

public final class Key implements SPNComponent
{
	final byte[] key;
	
	public Key(final byte[] key)
	{
		this.key = key;
	}
	
	public static Key noop(int length)
	{
		int mod = length % 8;
		byte[] nullKey = new byte[(mod > 0) ? mod+1 : mod];
		
		for(int i=0; i<nullKey.length; i++)
		{
			nullKey[i] = 0x0;
		}
		
		return new Key(nullKey);
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
}
