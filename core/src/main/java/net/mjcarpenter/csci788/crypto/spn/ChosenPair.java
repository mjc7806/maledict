package net.mjcarpenter.csci788.crypto.spn;

public class ChosenPair
{
	private final KnownPair pairA, pairB;
	
	public ChosenPair(KnownPair pairA, KnownPair pairB)
	{
		this.pairA = pairA;
		this.pairB = pairB;
	}
	
	public KnownPair getPairA()
	{
		return this.pairA;
	}
	
	public KnownPair getPairB()
	{
		return this.pairB;
	}
}
