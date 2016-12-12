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

import java.util.Arrays;
import java.util.BitSet;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import net.mjcarpenter.maledict.util.BitUtils;
@SuppressWarnings("serial")
@XStreamAlias("key")
public final class Key implements SPNComponent, Comparable<Key>
{
	@XStreamAsAttribute
	@XStreamAlias("value")
	private final byte[]  key;
	
	@XStreamAsAttribute
	@XStreamAlias("noop")
	private final boolean noop;
	
	public Key(final byte[] key)
	{
		this(false, key);
	}
	
	private Key(boolean noop, final byte[] key)
	{
		this.noop = noop;
		this.key  = key;
	}
	
	public static Key noop(int length)
	{
		int mod = length % 8;
		byte[] nullKey = new byte[(mod > 0) ? (length/8)+1 : length/8];
		
		for(int i=0; i<nullKey.length; i++)
		{
			nullKey[i] = 0x0;
		}
		
		return new Key(true, nullKey);
	}
	
	public byte[] getKeyValue()
	{
		return this.key;
	}
	
	public byte[] xor(final byte[] in)
	{
		BitSet inSet = BitSet.valueOf(in);
		BitSet keySet = BitSet.valueOf(key);
		
		inSet.xor(keySet);
		return BitUtils.convertBitSetToByte(inSet, in.length);
		
		//return BitUtils.longToByte(BitUtils.byteToLong(in)^BitUtils.byteToLong(key), in.length);
	}
	
	public int length()
	{
		return key.length*8;
	}

	@Override
	public boolean isNoop()
	{
		return noop;
	}

	@Override
	public int compareTo(Key that)
	{
		int result = 0;
		
		if(Arrays.equals(this.key, that.key))
		{
			result = 0;
		}
		else
		{
			long thisKeyLong = BitUtils.byteToLong(this.key);
			long thatKeyLong = BitUtils.byteToLong(that.key);
			
			result = Long.compare(thisKeyLong, thatKeyLong);
		}
		
		return result;
	}
}
