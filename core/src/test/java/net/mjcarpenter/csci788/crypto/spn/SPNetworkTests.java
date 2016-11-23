package net.mjcarpenter.csci788.crypto.spn;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public final class SPNetworkTests
{
	private SPNetwork spn;
	
	@Before
	public void setUp()
	throws Exception
	{
		spn = SPNetworkTests.sampleNetwork();
	}
	
	@After
	public void tearDown()
	throws Exception
	{
		spn = null;
	}
	
	@Test
	public void testEncryptionWithByteArray()
	throws Exception
	{
		byte[] plaintext = new byte[]{(byte)0xab,(byte)0x06};
		byte[] expected = new byte[]{(byte)0x80,(byte)0x35};
		
		byte[] ciphertext = spn.encrypt(plaintext);
		
		assertArrayEquals("Ciphertext did not match expected result.",
				expected, ciphertext);
	}
	
	@Test
	public void testEncryptionWithLong()
	throws Exception
	{
		long plaintext  = Long.valueOf("ab06", 16);//new byte[]{(byte)0xab,(byte)0x06};
		long expected   = Long.valueOf("8035", 16);//new byte[]{(byte)0x80,(byte)0x35};
		
		long ciphertext = spn.encrypt(plaintext);
		
		assertEquals("Ciphertext did not match expected result.",
				expected, ciphertext);
	}
	
	@Test
	public void testDecryptionWithByteArray()
	throws Exception
	{
		byte[] ciphertext = new byte[]{(byte)0x80,(byte)0x35};
		byte[] expected = new byte[]{(byte)0xab,(byte)0x06};
		
		byte[] plaintext = spn.decrypt(ciphertext);
		
		assertArrayEquals("Plaintext did not match expected result.",
				expected, plaintext);
	}
	
	@Test
	public void testDecryptionWithLong()
	throws Exception
	{
		long ciphertext  = Long.valueOf("8035", 16);
		long expected    = Long.valueOf("ab06", 16);
		
		long plaintext   = spn.decrypt(ciphertext);
		
		assertEquals("Plaintext did not match expected result.",
				expected, plaintext);
	}
	
	public static SPNetwork sampleNetwork()
	{
		Permutation first3Rounds = new Permutation(0,4,8,12,1,5,9,13,2,6,10,14,3,7,11,15);
		Permutation last2Rounds  = Permutation.noop(16);
		SBox allSBoxes = new SBox(0xE, 0x4, 0xD, 0x1, 0x2, 0xF, 0xB, 0x8, 0x3, 0xA, 0x6, 0xC, 0x5, 0x9, 0x0, 0x7);
		SBox straightThru = SBox.noop(16);
		
		// key = 609a 4029 f06b 9021 009e
		Key key1 = new Key(new byte[]{(byte)0x60,(byte)0x9a});
		Key key2 = new Key(new byte[]{(byte)0x40,(byte)0x29});
		Key key3 = new Key(new byte[]{(byte)0xf0,(byte)0x6b});
		Key key4 = new Key(new byte[]{(byte)0x90,(byte)0x21});
		Key key5 = new Key(new byte[]{(byte)0x00,(byte)0x9e});
		
		Round round1 = new Round(16, key1, first3Rounds, allSBoxes, allSBoxes, allSBoxes, allSBoxes);
		Round round2 = new Round(16, key2, first3Rounds, allSBoxes, allSBoxes, allSBoxes, allSBoxes);
		Round round3 = new Round(16, key3, first3Rounds, allSBoxes, allSBoxes, allSBoxes, allSBoxes);
		
		// Semifinal round just key and SBox
		Round round4 = new Round(16, key4, first3Rounds,  allSBoxes, allSBoxes, allSBoxes, allSBoxes);
		
		// Last round just key.
		Round round5 = new Round(16, key5, last2Rounds,  straightThru, straightThru, straightThru, straightThru);
		
		return new SPNetwork(16, new Round[]{round1, round2, round3, round4, round5});
	}
}
