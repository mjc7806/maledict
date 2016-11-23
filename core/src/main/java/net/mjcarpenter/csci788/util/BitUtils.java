package net.mjcarpenter.csci788.util;

import java.util.Arrays;
import java.util.BitSet;

import org.apache.commons.lang3.ArrayUtils;

/**
 * A class containing handy static methods for common bit operations.
 * 
 * @author Mike Carpenter
 */
public final class BitUtils
{
	/**
	 * Returns the number of set bits in a given {@code int}.<br />
	 * This is handy for performing bitwise {@code XOR} operations across
	 * all bits in an integer, as {@code countSetBits(i)%2 == 1} is equivalent.
	 * <br /><br />
	 * This method was taken from a Stack Overflow answer:<br />
	 * {@link http://stackoverflow.com/a/109025/2250867}
	 *   
	 * @param i The integer for which to count bits.
	 * @return The number of bits set to 1 in {@code i}.
	 */
	public static int countSetBits(int i)
	{
		i = i - ((i >>> 1) & 0x55555555);
		i = (i & 0x33333333) + ((i >>> 2) & 0x33333333);
		return (((i + (i >>> 4)) & 0x0F0F0F0F) * 0x01010101) >>> 24;
	}
	
	/*
	 * Modified conversion code from:
	 * http://stackoverflow.com/a/29132118/2250867
	 */
	public static byte[] longToByte(long in, int trimTo)
	{
		byte[] convertIn = new byte[Long.SIZE/Byte.SIZE];
		for(int i=convertIn.length-1; i>=0; i--)
		{
			convertIn[i] = (byte)(in&0xFF);
			in >>>= Byte.SIZE;
		}
				
		return Arrays.copyOfRange(convertIn, convertIn.length-trimTo, convertIn.length);
	}
	
	/*
	 * Modified conversion code from:
	 * http://stackoverflow.com/a/29132118/2250867
	 */
	public static long byteToLong(byte[] in)
	{
		long out = 0;
		for(int i=0; i<in.length; i++)
		{
			out <<= Byte.SIZE;
			out ^= (in[i]&0xFF);
		}
		
		return out;
	}
	
	/*
	 * Code modified to suit this application based on the following SE answer:
	 * http://stackoverflow.com/q/6197411/2250867
	 */
	public static byte[] convertBitSetToByte(BitSet inSet, int outLength)
	{
		byte[] bytes = new byte[outLength];
	    for (int i=0; i<inSet.length(); i++)
	    {
	        if (inSet.get(i))
	        {
	        	bytes[bytes.length-i/Byte.SIZE-1]
	        			|= 1<<(i%Byte.SIZE);
	        }
	    }
	    ArrayUtils.reverse(bytes);
	    
	    return bytes;
	}
}
