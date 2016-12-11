package net.mjcarpenter.maledict.crypto.ldc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.ArrayUtils;

import net.mjcarpenter.maledict.crypto.spn.Key;
import net.mjcarpenter.maledict.crypto.spn.Round;
import net.mjcarpenter.maledict.util.BitUtils;

public abstract class AbstractKeyBiasExtractor<T extends AbstractApproximation>
{
	protected TreeMap<Key, Double> biasMap;
	protected Key maxBiasKey;
	protected Round relevantRound;
	protected T appx;
	
	protected int boxesToCheck;
	protected int boxLength;
	
	protected boolean cancellation;
	
	private boolean[] boxIndexes;
	
	public AbstractKeyBiasExtractor(Round relevantRound, T appx)
	{
		this.relevantRound = relevantRound;
		this.appx = appx;
		biasMap = null;
		maxBiasKey = null;
		
		int numBoxes  = relevantRound.getSBoxes().length;
		int boxesToCheck = 0;
		
		this.boxLength = relevantRound.getSBoxes()[0].bitSize();
		this.boxIndexes = new boolean[numBoxes];
		
		for(int i=0; i<numBoxes; i++)
		{
			if(((appx.getLastRoundMask()>>>i*boxLength)&((1<<boxLength)-1)) != 0)
			{
				boxesToCheck++;
				boxIndexes[boxIndexes.length-(i+1)] = true;
			}
		}
		
		this.cancellation = false;
		
		this.boxesToCheck = boxesToCheck;
	}
	
	protected Key getKeyFor(int in)
	{
		long val = 0;
		int numUsed = 0;
				
		for(int i=0; i<relevantRound.getSBoxes().length && numUsed<boxesToCheck; i++)
		{
			if(boxIndexes[i])
			{
				int mask = ((1<<(boxLength))-1)<<((boxesToCheck-numUsed-1)*boxLength);
				int boxVal = (mask&in)>>>((boxesToCheck-numUsed-1)*boxLength);
				val |= (boxVal<<(boxLength*(boxIndexes.length-i-1)));
				
				numUsed++;
			}
		}
		
		byte[] conversion = BitUtils.longToByte(val, relevantRound.bitLength()/Byte.SIZE);
		ArrayUtils.reverse(conversion);
		return new Key(conversion);
	}
		
	public void cancel()
	{
		this.cancellation = true;
	}
	
	public boolean isCanceled()
	{
		return this.cancellation;
	}
	
	public Map<Key, Double> getBiasMap()
	{
		return this.biasMap;
	}
	
	public Key getMaxBiasKey()
	{
		return maxBiasKey;
	}
	
	public double getMaxBiasValue()
	{
		return getBiasFor(maxBiasKey);
	}
	
	public double getBiasFor(Key subkeyVal)
	{
		return biasMap != null && subkeyVal != null ? biasMap.get(subkeyVal) : -1;
	}
	
	public List<Map.Entry<Key, Double>> getTopValues(int numVals)
	{
	    @SuppressWarnings("unchecked")
		Map.Entry<Key, Double>[] topEntries = biasMap.entrySet()
	              .stream()
	              .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
	              .limit(numVals)
	              .toArray(Map.Entry[]::new);
	    
	    return Arrays.asList(topEntries);
	}
}
