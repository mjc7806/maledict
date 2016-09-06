package net.mjcarpenter.csci788.crypto.spn;

public class SBox
{
	private static final String VALIDATION_INDICES = "All inputs must have corresponding output.";
	
	private int[] mapFwd;
	
	public SBox(int[] mapFwd)
	{		
		for(int i=0; i<mapFwd.length; i++)
		{
			boolean contains = false;
			
			for(int j=0; j<mapFwd.length; j++)
			{
				contains |= (mapFwd[j] == i);
				if(contains) break;
			}
			
			if(!contains)
				throw new IllegalArgumentException(VALIDATION_INDICES);
		}
		
		
		this.mapFwd = mapFwd;
	}
	
	
	public SBox invert()
	{
		int[] reverse = new int[mapFwd.length];
		
		for(int i=0; i<mapFwd.length; i++)
		{
			for(int j=0; j<mapFwd.length; j++)
			{
				if(mapFwd[j] == i)
				{
					reverse[i] = j;
					break;
				}
			}
		}
		
		return new SBox(reverse);
	}
	
	public int size() // inSize == outSize, all SPN S-Boxes are bijective
	{
		return mapFwd.length;
	}
	
	public int sub(int n)
	{
		if(n >= size() || n < 0)
			throw new IllegalArgumentException("Invalid index " + n);
		else
			return mapFwd[n];
	}
}
