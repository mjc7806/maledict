package net.mjcarpenter.csci788.crypto.ldc;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.mjcarpenter.csci788.crypto.spn.KnownPair;
import net.mjcarpenter.csci788.crypto.spn.Round;
import net.mjcarpenter.csci788.crypto.spn.SPNetwork;
import net.mjcarpenter.csci788.crypto.spn.SPNetworkTests;

public class LinearKeyBiasExtractorTests
{
	private SPNetwork spn;
	private Round rnd;
	private LinearKeyBiasExtractor lkbe;
	private List<KnownPair> pairs;
	
	@Before
	public void setUp()
	throws Exception
	{
		pairs = new ArrayList<KnownPair>();
		spn = SPNetworkTests.sampleNetwork();
		rnd = spn.getRounds()[4];
		lkbe = new LinearKeyBiasExtractor(rnd);
		
		Random r = new SecureRandom();
		
		for(int i=0; i<10000; i++)
		{
			byte[] plain = new byte[2]; // 16 bits
			r.nextBytes(plain);
			
			KnownPair pair = new KnownPair(plain, spn.encrypt(plain));
			pairs.add(pair);
		}
	}
	
	@After
	public void tearDown()
	throws Exception
	{
		spn  = null;
		rnd  = null;
		lkbe = null;
	}
	
	@Test
	public void testBiasTable()
	throws Exception
	{
		
	}
}
