package net.mjcarpenter.csci788.crypto.ldc;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.mjcarpenter.csci788.crypto.spn.Key;
import net.mjcarpenter.csci788.crypto.spn.KnownPair;
import net.mjcarpenter.csci788.crypto.spn.Round;

public final class LinearKeyBiasExtractor
{
	private Map<Integer, Double> biasMap;
	private int maxBiasKey;
	private Round relevantRound;
	//private int sBoxIdx;
	
	public LinearKeyBiasExtractor(Round relevantRound)//, int sBoxIdx)
	{
		this.relevantRound = relevantRound;
		
		biasMap = null;
		maxBiasKey = -1;
	}
	
	public void generateBiases(Map<KnownPair, byte[]> expecteds)
	{
		biasMap = new HashMap<Integer, Double>();
		
		int card = expecteds.keySet().size();
		int bits = relevantRound.bitLength();
		int bytes = relevantRound.getSubKey().length()/8;
		Key k = null;
		
		double maxBias = Double.MIN_VALUE;
		int maxKey = -1;
		
		for(int i=0; i<1<<bits; i++)
		{
			byte[] newKey = ByteBuffer.allocate(4).putInt(i).order(ByteOrder.LITTLE_ENDIAN).array();
			k = new Key(Arrays.copyOfRange(newKey, 0, bytes));
			Round r = relevantRound.replaceKey(k);
			
			int successes = 0;
			
			for(KnownPair kp: expecteds.keySet())
			{
				byte[] expected = expecteds.get(kp);
				
				if(Arrays.equals(expected, r.invert(kp.getCiphertext())))
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
		}
		
		maxBiasKey = maxKey;
	}
	
	public Map<Integer, Double> getBiasMap()
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
