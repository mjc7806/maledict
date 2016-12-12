/*
 * Maledict - An Interactive Tool for Learning Linear and Differential Cryptanalysis of SPNs
 * Copyright (C) 2016  Mike Carpenter
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.mjcarpenter.maledict.crypto.ldc;

import java.util.List;
import java.util.TreeMap;

import net.mjcarpenter.maledict.crypto.spn.ChosenPair;
import net.mjcarpenter.maledict.crypto.spn.Key;
import net.mjcarpenter.maledict.crypto.spn.Round;
import net.mjcarpenter.maledict.util.BitUtils;

public final class DifferentialKeyBiasExtractor extends AbstractKeyBiasExtractor<DifferentialApproximation>
{
	public DifferentialKeyBiasExtractor(Round relevantRound, DifferentialApproximation appx)
	{
		super(relevantRound, appx);
	}

	public void generateBiases(List<ChosenPair> pairs, BiasExtractorProgressCallback callback)
	{
		biasMap = new TreeMap<Key, Double>();
				
		Key k = null;
		
		double maxBias = Double.MIN_VALUE;
		Key maxKey = null;
		
		// We need to replace the key with a no-op key because the round begins with a key,
		// and we want to stop just short of that.
		Round testRound = relevantRound.replaceKey(Key.noop(relevantRound.bitLength()));
		
		int keysToCheck = 1<<(boxLength*boxesToCheck);
		
		for(int i=0; i<keysToCheck; i++)
		{
			k = getKeyFor(i);
			
			int matches = 0;
			int pairProg = 0;
			
			for(ChosenPair pair: pairs)
			{
				byte[] partialDecryptionA = pair.getPairA().getCiphertext();
				byte[] partialDecryptionB = pair.getPairB().getCiphertext();
				
				// This is the "last round" decryption. The last round consists of only a key.
				partialDecryptionA = k.xor(partialDecryptionA);
				partialDecryptionA = testRound.invert(partialDecryptionA);
				partialDecryptionB = k.xor(partialDecryptionB);
				partialDecryptionB = testRound.invert(partialDecryptionB);
				
				if(appx.testAgainst(
						BitUtils.byteToLong(pair.getPairA().getPlaintext()),
						BitUtils.byteToLong(pair.getPairB().getPlaintext()),
						BitUtils.byteToLong(partialDecryptionA),
						BitUtils.byteToLong(partialDecryptionB)))
				{
					matches++;
				}
				
				// Check for cancellation
				if(cancellation)
				{
					biasMap = null;
					maxBiasKey = null;
					maxBias = -1;
					return;
				}
				
				// Notify caller of progress.
				callback.progress(i+1, keysToCheck, ++pairProg, pairs.size());
			}
			
			
			double bias = (double)matches/(double)pairs.size();
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
