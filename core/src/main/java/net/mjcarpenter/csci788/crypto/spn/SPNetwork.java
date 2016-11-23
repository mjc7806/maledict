package net.mjcarpenter.csci788.crypto.spn;

import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import net.mjcarpenter.csci788.util.BitUtils;
@XStreamAlias("spn")
public final class SPNetwork implements SPNComponent
{
	@XStreamAsAttribute
	private final int blockSize;
	
	@XStreamAsAttribute
	@XStreamAlias("noop")
	private final boolean noop;
	
	@XStreamImplicit
	private final Round[] rounds;
	
	
	public SPNetwork(final int blockSize, final Round[] rounds)
	{
		this(false, blockSize, rounds);
	}
	
	private SPNetwork(boolean noop, final int blockSize, final Round[] rounds)
	{
		// Validate input
		for(Round r: rounds)
		{
			if(r.bitLength() != blockSize)
				throw new IllegalArgumentException("Each round's block size must match network's block size!");
		}
		
		this.noop      = noop;
		this.rounds    = rounds;
		this.blockSize = blockSize;
	}
	
	public static SPNetwork noop(final int blockSize, final int sBoxBitSize, final int numRounds)
	{
		if(blockSize % sBoxBitSize != 0)
		{
			throw new IllegalArgumentException();
		}
		
		int sboxPerRound = blockSize/sBoxBitSize;
		
		Round[] rounds = new Round[numRounds];
		for(int i=0; i<numRounds; i++)
		{
			SBox[] boxes = new SBox[sboxPerRound];
			for(int j=0; j<sboxPerRound; j++)
			{
				boxes[j] = SBox.noop(1<<sBoxBitSize);
			}
			
			rounds[i] = new Round(blockSize,
					Key.noop(blockSize),
					Permutation.noop(blockSize),
					boxes);
		}
		
		return new SPNetwork(true, blockSize, rounds);
	}
	
	public byte[] encrypt(final byte[] in)
	{
		byte[] out = Arrays.copyOf(in, in.length);
		
		for(int i=0; i<rounds.length; i++)
		{
			out = rounds[i].process(out);
		}
		
		return out;
	}
	
	public byte[] decrypt(final byte[] in)
	{
		byte[] out = Arrays.copyOf(in, in.length);
		
		for(int i=rounds.length-1; i>=0; i--)
		{
			out = rounds[i].invert(out);
		}
		
		return out;
	}
	
	public long encrypt(final long in)
	{
		byte[] inByte = BitUtils.longToByte(in, blockSize/Byte.SIZE);
		byte[] res = encrypt(inByte);
		return BitUtils.byteToLong(res);
	}
	
	public long decrypt(final long in)
	{
		byte[] inByte = BitUtils.longToByte(in, blockSize/Byte.SIZE);
		byte[] res = decrypt(inByte);
		return BitUtils.byteToLong(res);
	}
	
	public int getBlockSize()
	{
		return this.blockSize;
	}
	
	public Round[] getRounds()
	{
		return rounds;
	}

	@Override
	public boolean isNoop()
	{
		return noop;
	}
}
