package net.mjcarpenter.csci788.crypto.ldc;

import java.util.List;
import java.util.TreeMap;

import net.mjcarpenter.csci788.crypto.spn.Key;
import net.mjcarpenter.csci788.crypto.spn.KnownPair;
import net.mjcarpenter.csci788.crypto.spn.Round;
import net.mjcarpenter.csci788.util.BitUtils;

public final class LinearKeyBiasExtractor extends AbstractKeyBiasExtractor<LinearApproximation>
{
	public LinearKeyBiasExtractor(Round relevantRound, LinearApproximation appx)
	{
		super(relevantRound, appx);
	}
	
	public void generateBiases(List<KnownPair> pairs)
	{
		biasMap = new TreeMap<Key, Double>();
				
		Key k = null;
		
		double maxBias = Double.MIN_VALUE;
		Key maxKey = null;
		
		// We need to replace the key with a no-op key because the round begins with a key,
		// and we want to stop just short of that.
		Round testRound = relevantRound.replaceKey(Key.noop(relevantRound.bitLength()));
		
		for(int i=0; i<1<<(boxLength*boxesToCheck); i++)
		{
			k = getKeyFor(i);
			
			int matches = 0;
			
			for(KnownPair pair: pairs)
			{
				byte[] partialDecryption = pair.getCiphertext();
				
				// This is the "last round" decryption. The last round consists of only a key.
				partialDecryption = k.xor(partialDecryption);
				partialDecryption = testRound.invert(partialDecryption);
				
				if(appx.testAgainst(
						BitUtils.byteToLong(pair.getPlaintext()),
						BitUtils.byteToLong(partialDecryption)))
				{
					matches++;
				}
			}
			
			
			double bias = Math.abs(matches-(pairs.size()/2.0))/pairs.size();
			biasMap.put(k, bias);
			
			if(bias > maxBias)
			{
				maxBias = bias;
				maxKey = k;
			}
		}
		
		maxBiasKey = maxKey;
	}
}
