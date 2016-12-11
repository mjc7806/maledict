package net.mjcarpenter.maledict.reports;

import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import net.mjcarpenter.maledict.crypto.ldc.AbstractApproximation;
import net.mjcarpenter.maledict.crypto.ldc.AbstractKeyBiasExtractor;
import net.mjcarpenter.maledict.crypto.ldc.DifferentialApproximation;
import net.mjcarpenter.maledict.crypto.ldc.LinearApproximation;
import net.mjcarpenter.maledict.crypto.spn.Key;

public class CryptanalysisReport
{
	private SBoxReport[][] boxListing;
	private AbstractApproximation appx;
	private AbstractKeyBiasExtractor<?> akbe;
	
	public CryptanalysisReport(AbstractApproximation appx, AbstractKeyBiasExtractor<?> akbe)
	{
		this.appx = appx;
		this.akbe = akbe;
	}
	
	public String constructMaskSection()
	{
		StringBuilder sb = new StringBuilder();
		
		String maskHdr = "";
		if(appx instanceof LinearApproximation)
		{
			maskHdr = "Mask";
		}
		else if(appx instanceof DifferentialApproximation)
		{
			maskHdr = "Difference";
		}
		else
		{
			maskHdr = "Mask";
		}
		
		return sb.toString();
	}
	
	public String constructBiasSection()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("<div id=\"keybiases\">\n")
          .append("\t<h2>Top Key Matches</h2>\n")
          .append("\t<table>\n")
          .append("\t\t<tr>\n")
          .append("\t\t\t<th colspan=\"2\">Top Biases</th>\n")
          .append("\t\t</tr>\n")
          .append("\t\t<tr>\n")
          .append("\t\t\t<th>Key</th>\n")
          .append("\t\t\t<th>Bias</th>\n")
          .append("\t\t</tr>\n");
		
		List<Map.Entry<Key, Double>> topMap = akbe.getTopValues(10);
		
		for(Map.Entry<Key, Double> each: topMap)
		{
			Key thisKey = each.getKey();
			Double thisVal = each.getValue();
			boolean isMax = thisVal.doubleValue() == akbe.getMaxBiasValue();
			
			sb.append("\t\t<tr>\n")
              .append("\t\t\t<td")
              .append(isMax ? " class=\"active\"" : "")
              .append(">")
              .append(DatatypeConverter.printHexBinary(thisKey.getKeyValue()))
              .append("</td>\n")
              .append("\t\t\t<td")
              .append(isMax ? " class=\"active\"" : "")
              .append(">")
              .append(String.format("%.6f", thisVal.doubleValue()))
              .append("</td>\n")
              .append("\t\t</tr>\n");
		}
		
		sb.append("\t</table>\n</div>");
		
		return sb.toString();
	}
}
