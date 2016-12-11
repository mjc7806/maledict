package net.mjcarpenter.maledict.reports;

import java.util.ArrayList;
import java.util.Collection;
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
	private List<SBoxReport> boxReports;
	private AbstractApproximation appx;
	private AbstractKeyBiasExtractor<?> akbe;
	
	public CryptanalysisReport(AbstractApproximation appx, AbstractKeyBiasExtractor<?> akbe, Collection<SBoxReport> boxReports)
	{
		this.appx       = appx;
		this.akbe       = akbe;
		
		ArrayList<SBoxReport> rptList = new ArrayList<SBoxReport>(boxReports);
		rptList.sort((thisBox, thatBox) ->
		{
			int cmp = Integer.compare(thisBox.getBoxRow(), thatBox.getBoxRow());
			
			if(cmp == 0)
			{
				cmp = Integer.compare(thisBox.getBoxCol(), thatBox.getBoxCol());
			}
			
			return cmp;
		});
				
		this.boxReports = rptList;
	}
	
	public String getFullReportAsString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(constructFileHeader())
		  .append(constructMaskSection())
		  .append(constructBiasSection())
		  .append(constructVisualizationDiv());
		
		
		
		for(SBoxReport each: boxReports)
		{
			sb.append(each.constructApproximationTable());
		}
		
		sb.append(constructFileFooter());
		
		return sb.toString();
	}
	
	private String constructMaskSection()
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
		
		sb.append("<div id=\"masks\">\n")
		  .append("<h2>Cipher Approximation ")
		  .append(maskHdr)
		  .append("s</h2>\n")
          .append("<table>\n")
          .append("\t<tr>\n")
          .append("\t\t<th>")
          .append(maskHdr)
          .append("</th>\n")
          .append("\t\t<th>Value</th>\n")
          .append("\t</tr>\n")
          .append("\t<tr>\n")
          .append("\t\t<td>Plaintext</td>\n")
          .append("\t\t<td>")
          .append(Long.toBinaryString(appx.getPlaintextMask()))
          .append("</td>\n")
          .append("\t</tr>\n")
          .append("\t<tr>\n")
          .append("\t\t<td>Last-Round</td>\n")
          .append("\t\t<td>")
          .append(Long.toBinaryString(appx.getLastRoundMask()))
          .append("</td>\n")
          .append("\t</tr>\n</table>\n</div>");
		
		return sb.toString();
	}
	
	private String constructBiasSection()
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
	
	private static String constructVisualizationDiv()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("\t<div id=\"ciphervis\">\n")
		  .append("\t\t<h2>Cipher Visualization</h2>\n")
		  .append("\t\t<img src=\"spnImg.png\" ")
		  .append("title=\"Cipher Visualization\" ")
		  .append("alt=\"CIpher Visualization\" />\n")
		  .append("\t</div>\n");
		
		return sb.toString();
	}
	
	private static String constructFileHeader()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("<!DOCTYPE html>\n")
		  .append("<html>\n");
		
		// Header and CSS
		sb.append("<head>\n")
		  .append("\t<title>Cryptanalysis Report</title>\n")
		  .append("\t<style>\n")
		  .append("\t\tbody\n\t\t{\n\t\t\tfont-family:monospace;\n\t\t}\n")
		  .append("\t\ttable\n\t\t{\n")
		  .append("\t\t\ttext-align:center;\n")
		  .append("\t\t\tborder:2px solid black;\n")
		  .append("\t\t\tborder-collapse:collapse;\n")
		  .append("\t\t\tmargin:1em;\n")
		  .append("\t\t}\n")
		  .append("\t\tdiv.appxtab table td\n\t\t{\t\t\ttext-align:right;\n\t\t}\n")
		  .append("\t\ttd\n\t\t{\n\t\t\tpadding:5px;\n\t\t}\n")
		  .append("\t\ttd.active\n\t\t{\n")
		  .append("\t\t\tbackground-color:blue;\n")
		  .append("\t\t\tcolor:white;\n")
		  .append("\n\n}\n")
		  .append("\t</style>\n</head>\n");
		
		// Body opening
		sb.append("<body>\n")
		  .append("\t<div id=\"title\">\n")
		  .append("\t\t<h1>Cryptanalysis Report</h1>\n")
		  .append("\t</div>\n");
		
		return sb.toString();
	}
	
	private static String constructFileFooter()
	{
		return "</body>\n</html>\n";
	}
}
