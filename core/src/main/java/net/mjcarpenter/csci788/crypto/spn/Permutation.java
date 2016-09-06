package net.mjcarpenter.csci788.crypto.spn;

public class Permutation
{
	private static final String VALIDATION_INDICES = "All inputs must have corresponding output.";
	
	private int[] mapping;
	private int[] reverse;
	
	public Permutation(int[] mapping)
	{
		int[] reverse = new int[mapping.length];
		
		for(int i=0; i<mapping.length; i++)
		{
			boolean contains = false;
			
			for(int j=0; j<mapping.length; j++)
			{
				if(mapping[j] == i)
				{
					reverse[i] = j;
					contains = true;
					break;
				}
			}
			
			if(!contains)
				throw new IllegalArgumentException(VALIDATION_INDICES);
		}
		
		
		this.mapping = mapping;
		this.reverse = reverse;
	}
	
	public int length()
	{
		return mapping.length;
	}
	
	public int outPosition(int inPosition)
	{
		return mapping[inPosition];
	}
	
	public int inPosition(int outPosition)
	{
		return reverse[outPosition];
	}
}
