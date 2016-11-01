package net.mjcarpenter.csci788.crypto.spn;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public final class SBoxTests
{
	private static final int[][] SBOX_LAT =
		{
		//         0   1   2   3   4   5   6   7   8   9   A   B   C   D   E   F
		/* 0 */ {  8,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},
		/* 1 */ {  0,  0, -2, -2,  0,  0, -2,  6,  2,  2,  0,  0,  2,  2,  0,  0},
		/* 2 */ {  0,  0, -2, -2,  0,  0, -2, -2,  0,  0,  2,  2,  0,  0, -6,  2},
		/* 3 */ {  0,  0,  0,  0,  0,  0,  0,  0,  2, -6, -2, -2,  2,  2, -2, -2},
		/* 4 */ {  0,  2,  0, -2, -2, -4, -2,  0,  0, -2,  0,  2,  2, -4,  2,  0},
		/* 5 */ {  0, -2, -2,  0, -2,  0,  4,  2, -2,  0, -4,  2,  0, -2, -2,  0},
		/* 6 */ {  0,  2, -2,  4,  2,  0,  0,  2,  0, -2,  2,  4, -2,  0,  0, -2},
		/* 7 */ {  0, -2,  0,  2,  2, -4,  2,  0, -2,  0,  2,  0,  4,  2,  0,  2},
		/* 8 */ {  0,  0,  0,  0,  0,  0,  0,  0, -2,  2,  2, -2,  2, -2, -2, -6},
		/* 9 */ {  0,  0, -2, -2,  0,  0, -2, -2, -4,  0, -2,  2,  0,  4,  2, -2},
		/* A */ {  0,  4, -2,  2, -4,  0,  2, -2,  2,  2,  0,  0,  2,  2,  0,  0},
		/* B */ {  0,  4,  0, -4,  4,  0,  4,  0,  0,  0,  0,  0,  0,  0,  0,  0},
		/* C */ {  0, -2,  4, -2, -2,  0,  2,  0,  2,  0,  2,  4,  0,  2,  0, -2},
		/* D */ {  0,  2,  2,  0, -2,  4,  0,  2, -4, -2,  2,  0,  2,  0,  0,  2},
		/* E */ {  0,  2,  2,  0, -2, -4,  0,  2, -2,  0,  0, -2, -4,  2, -2,  0},
		/* F */ {  0, -2, -4, -2, -2,  0,  2,  0,  0, -2,  4, -2, -2,  0,  2,  0}
		};
	
	private static final int[][] SBOX_DDT =
		{
		//         0   1   2   3   4   5   6   7   8   9   A   B   C   D   E   F
		/* 0 */ { 16,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},
		/* 1 */ {  0,  0,  0,  2,  0,  0,  0,  2,  0,  2,  4,  0,  4,  2,  0,  0},
		/* 2 */ {  0,  0,  0,  2,  0,  6,  2,  2,  0,  2,  0,  0,  0,  0,  2,  0},
		/* 3 */ {  0,  0,  2,  0,  2,  0,  0,  0,  0,  4,  2,  0,  2,  0,  0,  4},
		/* 4 */ {  0,  0,  0,  2,  0,  0,  6,  0,  0,  2,  0,  4,  2,  0,  0,  0},
		/* 5 */ {  0,  4,  0,  0,  0,  2,  2,  0,  0,  0,  4,  0,  2,  0,  0,  2},
		/* 6 */ {  0,  0,  0,  4,  0,  4,  0,  0,  0,  0,  0,  0,  2,  2,  2,  2},
		/* 7 */ {  0,  0,  2,  2,  2,  0,  2,  0,  0,  2,  2,  0,  0,  0,  0,  4},
		/* 8 */ {  0,  0,  0,  0,  0,  0,  2,  2,  0,  0,  0,  4,  0,  4,  2,  2},
		/* 9 */ {  0,  2,  0,  0,  2,  0,  0,  4,  2,  0,  2,  2,  2,  0,  0,  0},
		/* A */ {  0,  2,  2,  0,  0,  0,  0,  0,  6,  0,  0,  2,  0,  0,  4,  0},
		/* B */ {  0,  0,  8,  0,  0,  2,  0,  2,  0,  0,  0,  0,  0,  2,  0,  2},
		/* C */ {  0,  2,  0,  0,  2,  2,  2,  0,  0,  0,  0,  2,  0,  6,  0,  0},
		/* D */ {  0,  4,  0,  0,  0,  0,  0,  4,  2,  0,  2,  0,  2,  0,  2,  0},
		/* E */ {  0,  0,  2,  4,  2,  0,  0,  0,  6,  0,  0,  0,  0,  0,  2,  0},
		/* F */ {  0,  2,  0,  0,  6,  0,  0,  0,  0,  4,  0,  2,  0,  0,  2,  0}
		};
	
	private SBox sbox;
	
	@Before
	public void setUp()
	throws Exception
	{
		sbox = new SBox(0xE, 0x4, 0xD, 0x1, 0x2, 0xF, 0xB, 0x8, 0x3, 0xA, 0x6, 0xC, 0x5, 0x9, 0x0, 0x7);
	}
	
	@After
	public void tearDown()
	throws Exception
	{
		sbox = null;
	}
	
	@Test
	public void testSboxSubtitutions()
	throws Exception
	{
		int start = 0xA;
		int expected = 0x6;
		
		int fwdSub  = sbox.sub(start);
		int backSub = sbox.invert().sub(expected);
		
		assertEquals(String.format("Forward SBox returned [%x] when expecting [%x]", fwdSub,  expected),
				fwdSub, expected);
		
		assertEquals(String.format("Reverse SBox returned [%x] when expecting [%x]", backSub, start),
				backSub, start);
		
		
		boolean caughtException;
		try
		{
			sbox.sub(100);
			caughtException = false;
		}
		catch(IllegalArgumentException e)
		{
			caughtException = true;
		}
		
		assertTrue("Did not catch exception when substituting out-of-range index.", caughtException);
		
	}
	
	@Test
	public void testSBoxMakesCorrectLAT()
	throws Exception
	{
		assertArrayEquals("Control LAT did not match constructed LAT for same SBox.",
				sbox.getLAT(),
				SBOX_LAT);
	}
	
	@Test
	public void testSBoxMakesCorrectDDT()
	throws Exception
	{
		assertArrayEquals("Control DDT did not match constructed DDT for same SBox.",
				sbox.getDDT(),
				SBOX_DDT);
	}
	
	@Test
	public void testRowsColsSumInLAT()
	throws Exception
	{
		int[][] lat = sbox.getLAT();
		
		int[] rowsums = new int[lat.length];
		int[] colsums = new int[lat.length];
		
		for(int i=0; i<lat.length; i++)
		{
			for(int j=0; j<lat.length; j++)
			{
				rowsums[i] += lat[i][j];
				colsums[j] += lat[i][j];
			}
		}
		
		for(int i=0; i<rowsums.length; i++)
			assertEquals(String.format("Row %d sum is %d, expecting 8 or -8!",i,rowsums[i]),
					8,Math.abs(rowsums[i]));
		
		for(int i=0; i<colsums.length; i++)
			assertEquals(String.format("Col %d sum is %d, expecting 8 or -8!",i,colsums[i]),
					8,Math.abs(colsums[i]));
	}
	
	@Test
	public void testRowsColsSumInDDT()
	throws Exception
	{
		int[][] lat = sbox.getDDT();
		
		int[] rowsums = new int[lat.length];
		int[] colsums = new int[lat.length];
		
		for(int i=0; i<lat.length; i++)
		{
			for(int j=0; j<lat.length; j++)
			{
				rowsums[i] += lat[i][j];
				colsums[j] += lat[i][j];
			}
		}
		
		for(int i=0; i<rowsums.length; i++)
			assertEquals(String.format("Row %d sum is %d, expecting 16!",i,rowsums[i]),
					16,rowsums[i]);
		
		for(int i=0; i<colsums.length; i++)
			assertEquals(String.format("Col %d sum is %d, expecting 16!",i,colsums[i]),
					16,colsums[i]);
	}
	
	private static String printableLAT(int[][] inLAT)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("   "); // Header offset
		for(int i=0; i<inLAT.length; i++)
			sb.append(String.format("  %x", i));
		sb.append("\n  +");
		for(int i=0; i<inLAT.length; i++)
			sb.append("---");
		sb.append("\n");
		
		for(int i=0; i<inLAT.length; i++)
		{
			sb.append(String.format("%x |", i));
			
			for(int j=0; j<inLAT[i].length; j++)
			{
				sb.append(String.format((inLAT[i][j] < 0) ? " %d" : "  %d", inLAT[i][j]));
			}
			
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
