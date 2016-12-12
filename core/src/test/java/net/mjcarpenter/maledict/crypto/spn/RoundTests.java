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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.mjcarpenter.maledict.crypto.spn.Key;
import net.mjcarpenter.maledict.crypto.spn.Permutation;
import net.mjcarpenter.maledict.crypto.spn.Round;
import net.mjcarpenter.maledict.crypto.spn.SBox;

public final class RoundTests
{
	private Round round;
	
	@Before
	public void setUp()
	throws Exception
	{
		SBox[] sboxes = new SBox[4];
		
		SBox sbox = new SBox(0xE, 0x4, 0xD, 0x1, 0x2, 0xF, 0xB, 0x8, 0x3, 0xA, 0x6, 0xC, 0x5, 0x9, 0x0, 0x7);
		for(int i=0; i<sboxes.length; i++)
		{
			sboxes[i] = sbox;
		}
		
		Permutation perm = new Permutation(0,4,8,12,1,5,9,13,2,6,10,14,3,7,11,15);
		
		Key subkey = new Key(new byte[]{(byte)0x60,(byte)0x9a});
		
		round = new Round(16, subkey, perm, sboxes);
	}
	
	@After
	public void tearDown()
	throws Exception
	{
		round = null;
	}
	
	@Test
	public void testRoundByte()
	throws Exception
	{
		byte[] input = new byte[]{(byte)0xab,(byte)0x06};
		byte[] expected = new byte[]{(byte)0x6d, (byte)0x29};
		
		byte[] output = round.process(input);
		
		assertArrayEquals("Output did not match expected value.",
				output, expected);
	}
}
