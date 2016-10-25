package net.mjcarpenter.csci788.crypto.spn;

import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

public final class SPNetwork implements SPNComponent
{	
	private final int blockSize;
	private final Round[] rounds;
	
	public SPNetwork(final int blockSize, final Round[] rounds)
	throws IllegalArgumentException
	{
		// Validate input
		for(Round r: rounds)
		{
			if(r.bitLength() != blockSize)
				throw new IllegalArgumentException("Each round's block size must match network's block size!");
		}
		
		this.rounds    = rounds;
		this.blockSize = blockSize;
	}
	
	public byte[] encrypt(final byte[] in)
	{
		byte[] out = Arrays.copyOf(in, in.length);
		
		for(int i=0; i<rounds.length; i++)
		{
			out = rounds[i].process(out);
		}
		
		return out;
	}
	
	public byte[] decrypt(final byte[] in)
	{
		byte[] out = Arrays.copyOf(in, in.length);
		
		for(int i=rounds.length-1; i>=0; i--)
		{
			out = rounds[i].invert(out);
		}
		
		return out;
	}
	
	public int getBlockSize()
	{
		return this.blockSize;
	}
}
