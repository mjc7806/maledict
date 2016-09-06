package net.mjcarpenter.csci788.crypto.spn;

public abstract class SPNetwork
{
	private static final String VALIDATION_HEADER             = "Errors while constructing SPN:\n";
	private static final String VALIDATION_BLOCKSIZE_ROUNDS   = "  - Num rounds must divide block size.\n";
	private static final String VALIDATION_KEY_LENGTH         = "  - Key length must equal (block size)*(num rounds).\n";
	private static final String VALIDATION_SBOX_SIZE          = "  - SBox size must divide block size.\n";
	private static final String VALIDATION_PERMUTATION_LENGTH = "  - Permutation length must equal (block size)/(num rounds).\n";
	
	private int         blockSize;
	private SBox        sbox;
	private Permutation perm;
	private Key         key;
	
	public SPNetwork(int blockSize, int numRounds, SBox sbox, Permutation perm, Key key)
	throws IllegalArgumentException
	{
		// Validate input
		String errMsg = VALIDATION_HEADER;
		boolean hasErrors = false;
		
		if(blockSize % numRounds != 0)
		{
			hasErrors = true;
			errMsg += VALIDATION_BLOCKSIZE_ROUNDS;
		}
		if(blockSize * numRounds != key.length())
		{
			hasErrors = true;
			errMsg += VALIDATION_KEY_LENGTH;
		}
		if(blockSize % sbox.size() != 0)
		{
			hasErrors = true;
			errMsg += VALIDATION_SBOX_SIZE;
		}
		if(blockSize/numRounds != perm.length())
		{
			hasErrors = true;
			errMsg += VALIDATION_PERMUTATION_LENGTH;
		}
		if(hasErrors)
		{
			throw new IllegalArgumentException(errMsg);
		}
		
		this.blockSize = blockSize;
		this.sbox      = sbox;
		this.perm      = perm;
		this.key       = key;
	}
	
	public int getBlockSize()
	{
		return this.blockSize;
	}
}
