package net.mjcarpenter.maledict.crypto.ldc;

public interface BiasExtractorProgressCallback
{
	public void progress(int mainProg, int mainTotal, int subProg, int subTotal);
}
