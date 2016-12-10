package net.mjcarpenter.maledict.crypto.spn;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.mjcarpenter.maledict.util.BitUtils;

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
	
	public static List<ChosenPair> generatePairs(final int number, final long diffMask, final SPNetwork cipher)
	{
		List<ChosenPair> pairList = new ArrayList<ChosenPair>();
		
		Random r = new SecureRandom();
		
		int byteGenSize = cipher.getBlockSize()/Byte.SIZE + ((cipher.getBlockSize()%Byte.SIZE == 0) ? 0 : 1);
		
		for(int i=0; i<number; i++)
		{
			byte[] plainA = new byte[byteGenSize];
			r.nextBytes(plainA);
			
			byte[] plainB = BitUtils.longToByte(diffMask^BitUtils.byteToLong(plainA), plainA.length);
			
			
			byte[] cipherA = cipher.encrypt(plainA);
			byte[] cipherB = cipher.encrypt(plainB);
			
			ChosenPair pair = new ChosenPair(
					new KnownPair(plainA, cipherA),
					new KnownPair(plainB, cipherB));
			
			pairList.add(pair);
		}
		
		return pairList;
	}
}
