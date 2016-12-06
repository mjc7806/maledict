package net.mjcarpenter.maledict.crypto.ldc;

public abstract class AbstractApproximation
{
	private final long plaintextMask;
	private final long lastRoundMask;
	
	public AbstractApproximation(long plaintextMask, long lastRoundMask)
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
}
