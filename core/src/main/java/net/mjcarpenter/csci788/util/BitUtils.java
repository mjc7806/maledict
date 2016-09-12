package net.mjcarpenter.csci788.util;
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
}
