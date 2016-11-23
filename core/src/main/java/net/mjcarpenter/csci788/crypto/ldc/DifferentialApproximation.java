package net.mjcarpenter.csci788.crypto.ldc;

public final class DifferentialApproximation extends AbstractApproximation {

	public DifferentialApproximation(long plaintextMask, long lastRoundMask)
	{
		super(plaintextMask, lastRoundMask);
	}
	
	public boolean testAgainst(long plaintextA, long plaintextB, long lastRoundA, long lastRoundB)
	{
		long diffA = plaintextA^plaintextB;
		if(diffA != getPlaintextMask())
		{
			throw new IllegalArgumentException("Plaintexts do not fit the input mask for this approximation.");
		}
		
		long diffB = lastRoundA^lastRoundB;
		
		return getLastRoundMask() == diffB;
	}
}
