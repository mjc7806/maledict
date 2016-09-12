package net.mjcarpenter.csci788.crypto.spn;

import java.util.BitSet;

public class Key
{
	byte[] key;
	
	public Key(byte[] key)
	{
		this.key = key;
	}
	
	public byte[] xor(byte[] in)
	{
		BitSet inSet = BitSet.valueOf(in);
		BitSet keySet = BitSet.valueOf(key);
		
		inSet.xor(keySet);
		return inSet.toByteArray();
	}
	
	public int length()
	{
		return key.length;
	}
}
