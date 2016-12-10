package net.mjcarpenter.maledict.crypto.spn;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class KnownPair
{
	private final byte[] plaintext;
	private final byte[] ciphertext;
	
	public KnownPair(final byte[] plaintext, final byte[] ciphertext)
	{
		this.plaintext  = plaintext;
		this.ciphertext = ciphertext;
	}
	
	public byte[] getPlaintext()
	{
		return this.plaintext;
	}
	
	public byte[] getCiphertext()
	{
		return this.ciphertext;
	}
	
	public static List<KnownPair> generatePairs(int numPairs, SPNetwork cipher)
	{
		List<KnownPair> pairList = new ArrayList<KnownPair>();
		
		Random r = new SecureRandom();
		
		int byteGenSize = cipher.getBlockSize()/Byte.SIZE + ((cipher.getBlockSize()%Byte.SIZE == 0) ? 0 : 1);
		
		for(int i=0; i<numPairs; i++)
		{
			byte[] plainT = new byte[byteGenSize];
			r.nextBytes(plainT);
			
			byte[] cipherT = cipher.encrypt(plainT);
			
			KnownPair pair = new KnownPair(plainT, cipherT);
			pairList.add(pair);
		}
		
		return pairList;
	}
}
