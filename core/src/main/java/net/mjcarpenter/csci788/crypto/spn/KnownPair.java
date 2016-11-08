package net.mjcarpenter.csci788.crypto.spn;

import java.util.Arrays;

public final class KnownPair
{
	private final byte[] plaintext;
	private final byte[] ciphertext;
	
	public KnownPair(final byte[] plaintext, final byte[] ciphertext)
	{
		this.plaintext  = plaintext;
		this.ciphertext = ciphertext;
	}
	
	public byte[] getPlaintext()
	{
		return this.plaintext;
	}
	
	public byte[] getCiphertext()
	{
		return this.ciphertext;
	}
}
