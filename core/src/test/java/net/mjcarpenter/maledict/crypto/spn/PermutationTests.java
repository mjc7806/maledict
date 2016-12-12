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
package net.mjcarpenter.maledict.crypto.spn;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import javax.xml.bind.DatatypeConverter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.mjcarpenter.maledict.crypto.spn.Permutation;

public final class PermutationTests
{
	private Permutation perm;
	
	@Before
	public void setUp()
	throws Exception
	{
		perm = new Permutation(0,4,8,12,1,5,9,13,2,6,10,14,3,7,11,15);
	}
	
	@After
	public void tearDown()
	throws Exception
	{
		perm = null;
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidConstructionParameters()
	throws Exception
	{
		new Permutation(0,4,8,12,1,5,9,13,2,6,10,14,3,7,7,15);
	}
	
	@Test
	public void testLongPermutationsAccurate()
	{
		long testA  = Long.parseLong("0000101100000000", 2);
		long testB  = Long.parseLong("0000010100000101", 2);
		long testC  = Long.parseLong("0000000011010000", 2);
				
		long expectedA = Long.parseLong("0100000001000100", 2);
		long expectedB = Long.parseLong("0000010100000101", 2);
		long expectedC = Long.parseLong("0010001000000010", 2);
		
		long outA = perm.permuteFwd(testA);
		long outB = perm.permuteFwd(testB);
		long outC = perm.permuteFwd(testC);
		
		assertEquals(String.format("Permutation of testA failed: Expected [%s] but got [%s]",
				String.format("%16s", Long.toBinaryString(expectedA)).replace(' ', '0'),
				String.format("%16s", Long.toBinaryString(outA)).replace(' ', '0')),
				expectedA,
				outA);
		assertEquals(String.format("Permutation of testB failed: Expected [%s] but got [%s]",
				String.format("%16s", Long.toBinaryString(expectedB)).replace(' ', '0'),
				String.format("%16s", Long.toBinaryString(outB)).replace(' ', '0')),
				expectedB,
				outB);
		assertEquals(String.format("Permutation of testC failed: Expected [%s] but got [%s]",
				String.format("%16s", Long.toBinaryString(expectedC)).replace(' ', '0'),
				String.format("%16s", Long.toBinaryString(outC)).replace(' ', '0')),
				expectedC,
				outC);
	}
	
	@Test
	public void testBytePermutationsAccurate()
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
