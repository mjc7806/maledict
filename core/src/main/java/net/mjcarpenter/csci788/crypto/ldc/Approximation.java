package net.mjcarpenter.csci788.crypto.ldc;

public final class Approximation
{
	private final long plaintextMask;
	private final long lastRoundMask;
	
	public Approximation(long plaintextMask, long lastRoundMask)
	{
		this.plaintextMask = plaintextMask;
		this.lastRoundMask = lastRoundMask;
	}
	
	public long getPlaintextMask()
	{
		return plaintextMask;
	}
	
	public long getLastRoundMask()
	{
		return lastRoundMask;
	}
	
	public boolean testAgainst(long plaintext, long partialDecryption)
	{
		long plainMasked   = plaintext&plaintextMask;
		long decryptMasked = partialDecryption&lastRoundMask;
		
		// Equivalent to comparing inner-xors 
		return Long.bitCount(plainMasked)%2 == Long.bitCount(decryptMasked)%2;
	}
}
