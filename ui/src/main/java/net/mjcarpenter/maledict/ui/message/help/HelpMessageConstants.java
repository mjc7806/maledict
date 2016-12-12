/*
 * Maledict - An Interactive Tool for Learning Linear and Differential Cryptanalysis of SPNs
 * Copyright (C) 2016  Mike Carpenter
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.mjcarpenter.maledict.ui.message.help;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public final class HelpMessageConstants
{
	private static final String HTML_DIRECTORY = "html/";
	private static final Set<String> VALID_STRINGS;
	
	public static final String HELP_DLG_PERMUTATION = HTML_DIRECTORY + "help_dlg_permutation.html";
	public static final String HELP_DLG_SBOX        = HTML_DIRECTORY + "help_dlg_sbox.html";
	public static final String HELP_DLG_KEY         = HTML_DIRECTORY + "help_dlg_key.html";
	public static final String HELP_DLG_MSTR_PROP   = HTML_DIRECTORY + "help_dlg_mstr_prop.html";
	public static final String HELP_DLG_LDC_LINEAR  = HTML_DIRECTORY + "help_dlg_ldc_linear.html";
	public static final String HELP_DLG_LDC_DIFF    = HTML_DIRECTORY + "help_dlg_ldc_diff.html";
	public static final String HELP_DLG_LDC_LAT     = HTML_DIRECTORY + "help_dlg_ldc_lat.html";
	public static final String HELP_DLG_LDC_DDT     = HTML_DIRECTORY + "help_dlg_ldc_ddt.html";
	
	static
	{
		Set<String> strSet = new HashSet<String>();
		
		strSet.add(HELP_DLG_PERMUTATION);
		strSet.add(HELP_DLG_SBOX);
		strSet.add(HELP_DLG_KEY);
		strSet.add(HELP_DLG_MSTR_PROP);
		strSet.add(HELP_DLG_LDC_LINEAR);
		strSet.add(HELP_DLG_LDC_DIFF);
		strSet.add(HELP_DLG_LDC_LAT);
		strSet.add(HELP_DLG_LDC_DDT);
		
		VALID_STRINGS = strSet;
	}
	
	private HelpMessageConstants(){}
	
	
	public static URL getResource(String constant)
	{
		URL result;
		
		if(VALID_STRINGS.contains(constant))
		{
			result = ClassLoader.getSystemClassLoader().getResource(constant);
		}
		else
		{
			result = null;
		}
		
		return result;
	}
}
