package net.mjcarpenter.maledict.util;

import javax.xml.bind.DatatypeConverter;

import com.thoughtworks.xstream.converters.extended.EncodedByteArrayConverter;

public class HexByteConverter extends EncodedByteArrayConverter
{
	@Override
	public String toString(Object obj)
	{
		return DatatypeConverter.printHexBinary((byte[])obj);
	}

	@Override
	public Object fromString(String str)
	{
		return DatatypeConverter.parseHexBinary(str);
	}
}
