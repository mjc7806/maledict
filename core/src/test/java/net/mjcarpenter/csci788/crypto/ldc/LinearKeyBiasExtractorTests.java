package net.mjcarpenter.csci788.crypto.ldc;

import static org.junit.Assert.assertNotNull;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.mjcarpenter.csci788.crypto.spn.Key;
import net.mjcarpenter.csci788.crypto.spn.KnownPair;
import net.mjcarpenter.csci788.crypto.spn.Permutation;
import net.mjcarpenter.csci788.crypto.spn.Round;
import net.mjcarpenter.csci788.crypto.spn.SBox;
import net.mjcarpenter.csci788.crypto.spn.SPNetwork;
import net.mjcarpenter.csci788.crypto.spn.SPNetworkTests;

public class LinearKeyBiasExtractorTests
{
	private SPNetwork spn;
	private Round rnd;
	private LinearKeyBiasExtractor lkbe;
	private Approximation apx;
	private List<KnownPair> pairs;
	
	@Before
	public void setUp()
	throws Exception
	{
		pairs = new ArrayList<KnownPair>();
		
		
		Permutation first3Rounds = new Permutation(0,4,8,12,1,5,9,13,2,6,10,14,3,7,11,15);
		Permutation last2Rounds  = Permutation.noop(16);
		SBox allSBoxes = new SBox(0xE, 0x4, 0xD, 0x1, 0x2, 0xF, 0xB, 0x8, 0x3, 0xA, 0x6, 0xC, 0x5, 0x9, 0x0, 0x7);
		SBox straightThru = SBox.noop(16);
		
		// key = 609a 4029 f06b 9021 024e
		Key key1 = new Key(new byte[]{(byte)0x60,(byte)0x9a});
		Key key2 = new Key(new byte[]{(byte)0x40,(byte)0x29});
		Key key3 = new Key(new byte[]{(byte)0xf0,(byte)0x6b});
		Key key4 = new Key(new byte[]{(byte)0x90,(byte)0x21});
		Key key5 = new Key(new byte[]{(byte)0x02,(byte)0x4e});
		
		Round round1 = new Round(16, key1, first3Rounds, allSBoxes, allSBoxes, allSBoxes, allSBoxes);
		Round round2 = new Round(16, key2, first3Rounds, allSBoxes, allSBoxes, allSBoxes, allSBoxes);
		Round round3 = new Round(16, key3, first3Rounds, allSBoxes, allSBoxes, allSBoxes, allSBoxes);
		
		// Semifinal round just key and SBox
		Round round4 = new Round(16, key4, first3Rounds,  allSBoxes, allSBoxes, allSBoxes, allSBoxes);
		
		// Last round just key.
		Round round5 = new Round(16, key5, last2Rounds,  straightThru, straightThru, straightThru, straightThru);
		
		spn = new SPNetwork(16, new Round[]{round1, round2, round3, round4, round5});
		rnd = spn.getRounds()[4];
		
		
		long inMask  = Long.parseLong("0000101100000000", 2);
		long outMask = Long.parseLong("0000010100000101", 2);
		
		apx = new Approximation(inMask, outMask);
		lkbe = new LinearKeyBiasExtractor(rnd, apx);
		
		Random r = new SecureRandom();
		
		for(int i=0; i<10000; i++)
		{
			byte[] plain = new byte[2]; // 16 bits
			r.nextBytes(plain);
			
			byte[] cipher = spn.encrypt(plain);
			
			KnownPair pair = new KnownPair(plain, cipher);
			pairs.add(pair);
		}
	}
	
	@After
	public void tearDown()
	throws Exception
	{
		spn  = null;
		rnd  = null;
		lkbe = null;
	}
	
	@Test
	public void testBiasTable()
	throws Exception
	{
		assertNotNull(lkbe);
	}
}
