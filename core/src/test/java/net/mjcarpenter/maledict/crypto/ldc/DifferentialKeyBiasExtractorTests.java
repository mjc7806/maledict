package net.mjcarpenter.maledict.crypto.ldc;

import static org.junit.Assert.assertArrayEquals;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.mjcarpenter.maledict.crypto.ldc.DifferentialApproximation;
import net.mjcarpenter.maledict.crypto.ldc.DifferentialKeyBiasExtractor;
import net.mjcarpenter.maledict.crypto.spn.ChosenPair;
import net.mjcarpenter.maledict.crypto.spn.Key;
import net.mjcarpenter.maledict.crypto.spn.KnownPair;
import net.mjcarpenter.maledict.crypto.spn.Permutation;
import net.mjcarpenter.maledict.crypto.spn.Round;
import net.mjcarpenter.maledict.crypto.spn.SBox;
import net.mjcarpenter.maledict.crypto.spn.SPNetwork;
import net.mjcarpenter.maledict.util.BitUtils;

public class DifferentialKeyBiasExtractorTests
{
	private SPNetwork spn;
	private Round rnd;
	private DifferentialKeyBiasExtractor dkbe;
	private DifferentialApproximation apx;
	private List<ChosenPair> pairs;
	
	@Before
	public void setUp()
	throws Exception
	{
		pairs = new ArrayList<ChosenPair>();
		
		
		Permutation first3Rounds = new Permutation(0,4,8,12,1,5,9,13,2,6,10,14,3,7,11,15);
		Permutation last2Rounds  = Permutation.noop(16);
		SBox allSBoxes = new SBox(0xE, 0x4, 0xD, 0x1, 0x2, 0xF, 0xB, 0x8, 0x3, 0xA, 0x6, 0xC, 0x5, 0x9, 0x0, 0x7);
		SBox straightThru = SBox.noop(16);
		
		// key = 609a 4029 f06b 9021 024e
		Key key1 = new Key(new byte[]{(byte)0x60,(byte)0x9a});
		Key key2 = new Key(new byte[]{(byte)0x40,(byte)0x29});
		Key key3 = new Key(new byte[]{(byte)0xf0,(byte)0x6b});
		Key key4 = new Key(new byte[]{(byte)0x90,(byte)0x21});
		Key key5 = new Key(new byte[]{(byte)0x02,(byte)0xe4});
		
		Round round1 = new Round(16, key1, first3Rounds, allSBoxes, allSBoxes, allSBoxes, allSBoxes);
		Round round2 = new Round(16, key2, first3Rounds, allSBoxes, allSBoxes, allSBoxes, allSBoxes);
		Round round3 = new Round(16, key3, first3Rounds, allSBoxes, allSBoxes, allSBoxes, allSBoxes);
		
		// Semifinal round just key and SBox
		Round round4 = new Round(16, key4, last2Rounds,  allSBoxes, allSBoxes, allSBoxes, allSBoxes);
		
		// Last round just key.
		Round round5 = new Round(16, key5, last2Rounds,  straightThru, straightThru, straightThru, straightThru);
		
		spn = new SPNetwork(16, new Round[]{round1, round2, round3, round4, round5});
		rnd = spn.getRounds()[3];
		
		
		long inMask  = Long.parseLong("0000101100000000", 2);
		long outMask = Long.parseLong("0000011000000110", 2);
		
		apx  = new DifferentialApproximation(inMask, outMask);
		dkbe = new DifferentialKeyBiasExtractor(rnd, apx);
		
		Random r = new SecureRandom();
		for(int i=0; i<5000; i++)
		{
			byte[] plainA = new byte[2]; // 16 bits
			r.nextBytes(plainA);
			
			byte[] plainB = BitUtils.longToByte(inMask^BitUtils.byteToLong(plainA), plainA.length);
			
			
			byte[] cipherA = spn.encrypt(plainA);
			byte[] cipherB = spn.encrypt(plainB);
			
			ChosenPair pair = new ChosenPair(
					new KnownPair(plainA, cipherA),
					new KnownPair(plainB, cipherB));
			
			pairs.add(pair);
		}
	}
	
	@After
	public void tearDown()
	throws Exception
	{
		spn  = null;
		rnd  = null;
		dkbe = null;
	}
	
	@Test
	public void testBiasTableActualCipher()
	throws Exception
	{
		dkbe.generateBiases(pairs,
				(mainProg, mainTot, subProg, subTot) ->
				{
					System.out.printf("Key %d/%d Pair %d/%d\r",
							mainProg, mainTot, subProg, subTot);
				});
				
		byte[] expected = new byte[]{(byte)0x02, (byte)0x04};
		byte[] resultBytes = dkbe.getMaxBiasKey().getKeyValue();
		
		assertArrayEquals(String.format("Expected target partial subkey [%s] but got [%s] (bias %.06f)",
				DatatypeConverter.printHexBinary(expected),
				DatatypeConverter.printHexBinary(resultBytes),
				dkbe.getMaxBiasValue()),
				expected,
				resultBytes);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidPlaintextDifferential()
	throws Exception
	{
		pairs = new ArrayList<ChosenPair>();
		
		// These don't match input differential specified.
		long plain1 = Long.valueOf("0010110111010101", 2);
		long plain2 = Long.valueOf("0110101101110111", 2);
		
		byte[] plain1bytes = BitUtils.longToByte(plain1, 2);
		byte[] plain2bytes = BitUtils.longToByte(plain2, 2);
		
		pairs.add(new ChosenPair(
				new KnownPair(plain1bytes, spn.encrypt(plain1bytes)),
				new KnownPair(plain2bytes, spn.encrypt(plain2bytes))));
		
		dkbe.generateBiases(pairs, (mainProg, mainTot, subProg, subTot) -> {});
	}
}
