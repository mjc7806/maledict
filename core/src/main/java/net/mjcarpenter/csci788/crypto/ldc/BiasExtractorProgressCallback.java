package net.mjcarpenter.csci788.crypto.ldc;

public interface BiasExtractorProgressCallback
{
	public void progress(int mainProg, int mainTotal, int subProg, int subTotal);
}
