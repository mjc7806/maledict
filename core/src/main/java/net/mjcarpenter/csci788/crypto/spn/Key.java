package net.mjcarpenter.csci788.crypto.spn;

import java.util.BitSet;

public final class Key
{
	final byte[] key;
	
	public Key(final byte[] key)
	{
		this.key = key;
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
