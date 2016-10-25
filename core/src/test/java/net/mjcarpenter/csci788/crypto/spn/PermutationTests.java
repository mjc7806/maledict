package net.mjcarpenter.csci788.crypto.spn;

import static org.junit.Assert.assertArrayEquals;

import javax.xml.bind.DatatypeConverter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public final class PermutationTests extends TestCase
{
	private Permutation perm;
	
	@Before
	public void setUp()
	throws Exception
	{
		super.setUp();
		perm = new Permutation(0,4,8,12,1,5,9,13,2,6,10,14,3,7,11,15);
	}
	
	@After
	public void tearDown()
	throws Exception
	{
		perm = null;
		super.tearDown();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidConstructionParameters()
	throws Exception
	{
		new Permutation(0,4,8,12,1,5,9,13,2,6,10,14,3,7,7,15);
	}
	
	@Test
	public void testPermutationsAccurate()
	throws Exception
	{
		byte[] testA = {(byte)0xab,(byte)0x6f};
		byte[] testB = {(byte)0x09,(byte)0x2c};
		byte[] testC = {(byte)0xe2,(byte)0x94};
		byte[] testD = {(byte)0xcb,(byte)0x44};
		byte[] testE = {(byte)0xa8,(byte)0xd8};
		
		byte[] expectedA = {(byte)0xd3,(byte)0xf5};
		byte[] expectedB = {(byte)0x51,(byte)0x24};
		byte[] expectedC = {(byte)0xa9,(byte)0xc2};
		byte[] expectedD = {(byte)0xcb,(byte)0x44};
		byte[] expectedE = {(byte)0xf2,(byte)0x82};
		
		byte[] outA = perm.permuteFwd(testA);
		byte[] outB = perm.permuteFwd(testB);
		byte[] outC = perm.permuteFwd(testC);
		byte[] outD = perm.permuteFwd(testD);
		byte[] outE = perm.permuteFwd(testE);
		
		assertArrayEquals(
				String.format("Permutation of testA failed: Expected [%s] but got [%s]",
						DatatypeConverter.printHexBinary(expectedA),
						DatatypeConverter.printHexBinary(outA)),
				expectedA,
				outA);
		
		assertArrayEquals(
				String.format("Permutation of testB failed: Expected [%s] but got [%s]",
						DatatypeConverter.printHexBinary(expectedB),
						DatatypeConverter.printHexBinary(outB)),
				expectedB,
				outB);
		
		assertArrayEquals(
				String.format("Permutation of testA failed: Expected [%s] but got [%s]",
						DatatypeConverter.printHexBinary(expectedC),
						DatatypeConverter.printHexBinary(outC)),
				expectedC,
				outC);
		
		assertArrayEquals(
				String.format("Permutation of testA failed: Expected [%s] but got [%s]",
						DatatypeConverter.printHexBinary(expectedD),
						DatatypeConverter.printHexBinary(outD)),
				expectedD,
				outD);
		
		assertArrayEquals(
				String.format("Permutation of testA failed: Expected [%s] but got [%s]",
						DatatypeConverter.printHexBinary(expectedE),
						DatatypeConverter.printHexBinary(outE)),
				expectedE,
				outE);
	}
}
