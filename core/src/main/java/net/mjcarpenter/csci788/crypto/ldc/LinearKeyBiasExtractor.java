package net.mjcarpenter.csci788.crypto.ldc;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import net.mjcarpenter.csci788.crypto.spn.Key;
import net.mjcarpenter.csci788.crypto.spn.KnownPair;
import net.mjcarpenter.csci788.crypto.spn.Round;
import net.mjcarpenter.csci788.util.BitUtils;

public final class LinearKeyBiasExtractor
{
	private Map<Key, Double> biasMap;
	private int maxBiasKey;
	private Round relevantRound;
	private Approximation appx;
	
	private boolean[] boxIndexes;
	private int   boxesToCheck;
	private int   boxLength;
	//private int sBoxIdx;
	
	public LinearKeyBiasExtractor(Round relevantRound, Approximation appx)
	{
		this.relevantRound = relevantRound;
		this.appx = appx;
		biasMap = null;
		maxBiasKey = -1;
		
		int numBoxes  = relevantRound.getSBoxes().length;
		int boxesToCheck = 0;
		
		this.boxLength = relevantRound.getSBoxes()[0].bitSize();
		this.boxIndexes = new boolean[numBoxes];
		
		for(int i=0; i<numBoxes; i++)
		{
			if(((appx.getLastRoundMask()>>>i*boxLength)&((1<<boxLength)-1)) != 0)
			{
				boxesToCheck++;
				boxIndexes[i] = true;
			}
		}
		
		this.boxesToCheck = boxesToCheck;
	}
	
	public void generateBiases(List<KnownPair> plaintexts)
	{
		biasMap = new HashMap<Key, Double>();
		
		int bits = relevantRound.bitLength();
		int bytes = relevantRound.getSubKey().length()/Byte.SIZE;
		Key k = null;
		
		double maxBias = Double.MIN_VALUE;
		int maxKey = -1;
		
		for(int i=0; i<1<<(boxLength*boxesToCheck); i++)
		{
			k = getKeyFor(i);
		}
		
		/*for(int i=0; i<1<<bits; i++)
		{
			byte[] newKey = ByteBuffer.allocate(4).putInt(i).order(ByteOrder.LITTLE_ENDIAN).array();
			k = new Key(Arrays.copyOfRange(newKey, 0, bytes));
			Round r = relevantRound.replaceKey(k);
			
			int successes = 0;
			
			for(KnownPair kp: plaintexts)
			{
				long partialDecryption = 
				if(appx.testAgainst(kp.getPlaintext(), partialDecryption))
				{
					successes++;
				}
			}
			
			double bias = Math.abs((successes - (card/2.0)) / (card*1.0));
			biasMap.put(i, bias);
			
			if(bias > maxBias)
			{
				maxBias = bias;
				maxKey = i;
			}
		}//*/
		
		maxBiasKey = maxKey;
	}
	
	private Key getKeyFor(int in)
	{
		long val = 0;
		int numUsed = 0;
		
		for(int i=0; i<relevantRound.getSBoxes().length && numUsed<boxesToCheck; i++)
		{
			if(boxIndexes[i])
			{
				val |= ((1<<(boxLength*i)-1)&in);
				numUsed++;
			}
		}
		
		return new Key(BitUtils.longToByte(val, relevantRound.bitLength()/Byte.SIZE));
	}
	
	public Map<Key, Double> getBiasMap()
	{
		return this.biasMap;
	}
	
	public int getMaxBiasKey()
	{
		return maxBiasKey;
	}
	
	public double getMaxBiasValue()
	{
		return getBiasFor(maxBiasKey);
	}
	
	public double getBiasFor(int subkeyVal)
	{
		return biasMap != null ? biasMap.get(subkeyVal) : -1;
	}
}
