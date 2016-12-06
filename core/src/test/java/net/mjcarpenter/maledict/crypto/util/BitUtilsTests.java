package net.mjcarpenter.maledict.crypto.util;

import static org.junit.Assert.assertEquals;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;

import net.mjcarpenter.maledict.util.BitUtils;

public final class BitUtilsTests
{
	@Test
	public void testLongToByteArray()
	{
		long in = Long.valueOf("abcd", 16);
		byte[] out = BitUtils.longToByte(in, 2);
		
		String inString  = Long.toHexString(in).toLowerCase();
		String outString = DatatypeConverter.printHexBinary(out).toLowerCase();
		
		assertEquals(String.format("Expected [%s] but got [%s].", inString, outString),
				inString,
				outString);
	}
	
	@Test
	public void testByteArrayToLong()
	{
		byte[] in = new byte[]{(byte)0xab, (byte)0xcd};
		long out = BitUtils.byteToLong(in);
		
		
		String inString  = DatatypeConverter.printHexBinary(in).toLowerCase();
		String outString = Long.toHexString(out).toLowerCase();
		
		assertEquals(String.format("Expected [%s] but got [%s].", inString, outString),
				inString,
				outString);
		
		
		in = new byte[]{(byte)0x40, (byte)0x44};
		long expectedA  = Long.parseLong("0100000001000100", 2);
		
		inString  = DatatypeConverter.printHexBinary(in).toLowerCase();
		outString = Long.toHexString(expectedA).toLowerCase();
		
		assertEquals(String.format("Expected [%s] but got [%s].", inString, outString),
				inString,
				outString);
	}
}
