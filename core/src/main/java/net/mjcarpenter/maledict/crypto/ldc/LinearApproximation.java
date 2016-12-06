package net.mjcarpenter.maledict.crypto.ldc;

public final class LinearApproximation extends AbstractApproximation
{
	public LinearApproximation(long plaintextMask, long lastRoundMask)
	{
		super(plaintextMask, lastRoundMask);
	}

	public boolean testAgainst(long plaintext, long partialDecryption)
	{
		long plainMasked   = getPlaintextMask()&plaintext;
		long decryptMasked = getLastRoundMask()&partialDecryption;
				
		// Equivalent to comparing inner-xors 
		return Long.bitCount(plainMasked^decryptMasked) % 2 == 0;
	}
}
