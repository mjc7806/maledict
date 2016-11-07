package net.mjcarpenter.csci788.ui.util;

import java.util.HashMap;
import java.util.Map;

import net.mjcarpenter.csci788.crypto.spn.Key;
import net.mjcarpenter.csci788.crypto.spn.Permutation;
import net.mjcarpenter.csci788.crypto.spn.SBox;
import net.mjcarpenter.csci788.crypto.spn.SPNetwork;

public class MasterPropertiesCache
{
	private static final MasterPropertiesCache instance;
	static
	{
		instance = new MasterPropertiesCache();
	}
	
	private SPNetwork spn;
	
	private Map<String, SBox>        namedSboxes;
	private Map<String, Permutation> namedPermutations;
	private Map<String, Key>         namedKeys;
	
	private int blockSize, sboxSize, numRounds;
	
	private MasterPropertiesCache()
	{
		namedSboxes = new HashMap<String, SBox>();
		namedPermutations = new HashMap<String, Permutation>();
		namedKeys = new HashMap<String, Key>();
		
		spn = null;
		blockSize = 0;
		sboxSize  = 0;
		numRounds = 0;
	}
	
	public static MasterPropertiesCache getInstance()
	{
		return instance;
	}
	
	/*
	 * BASIC PROPERTIES
	 */
	
	public int getBlockSize()
	{
		return blockSize;
	}
	
	public void setBlockSize(int bSize)
	{
		blockSize = bSize;
	}
	
	public int getSBoxSize()
	{
		return sboxSize;
	}
	
	public void setSBoxSize(int sSize)
	{
		sboxSize = sSize;
	}
	
	public int getNumRounds()
	{
		return numRounds;
	}
	
	public void setNumRounds(int nRounds)
	{
		numRounds = nRounds;
	}
	
	/*
	 * SPN COMPONENTS
	 */
	
	public SPNetwork getSPN()
	{
		return spn;
	}
	
	public void setSPN(SPNetwork inSpn)
	{
		spn = inSpn;
		setBlockSize(spn.getBlockSize());
		setNumRounds(spn.getRounds().length);
		setSBoxSize(spn.getRounds()[0].getSBoxes()[0].bitSize());
	}
	
	public SBox getNamedSBox(String name)
	{
		return (namedSboxes.containsKey(name)) ? namedSboxes.get(name) : null;
	}
	
	public void saveNamedSBox(String name, SBox sbox)
	{
		namedSboxes.put(name, sbox);
	}
	
	public Permutation getNamedPermutation(String name)
	{
		return (namedPermutations.containsKey(name)) ? namedPermutations.get(name) : null;
	}
	
	public void saveNamedPermutation(String name, Permutation perm)
	{
		namedPermutations.put(name, perm);
	}
	
	public Key getNamedKey(String name)
	{
		return (namedKeys.containsKey(name)) ? namedKeys.get(name) : null;
	}
	
	public void saveNamedKey(String name, Key key)
	{
		namedKeys.put(name, key);
	}
}
