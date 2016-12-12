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
package net.mjcarpenter.maledict.ui.dialog.ldc;

import net.mjcarpenter.maledict.crypto.ldc.AbstractApproximation;
import net.mjcarpenter.maledict.crypto.ldc.LinearApproximation;
import net.mjcarpenter.maledict.crypto.spn.SBox;
import net.mjcarpenter.maledict.crypto.spn.SPNetwork;
import net.mjcarpenter.maledict.ui.message.help.HelpMessageConstants;

@SuppressWarnings("serial")
public final class LinearApproximationDialog extends ApproximationDialog
{
	public LinearApproximationDialog(SPNetwork spn)
	{
		super(spn, "Construct Linear Approximation");
	}
	
	@Override
	protected TableSelectionDialog createTableSelectionDialog(SBox box, long mask)
	{
		return new LATSelectionDialog(box, mask);
	}
	
	@Override
	public AbstractApproximation getCipherApproximation(long plaintextMask, long lastRoundMask)
	{
		return new LinearApproximation(plaintextMask, lastRoundMask);
	}
	
	@Override
	protected void handleHelp()
	{
		handleHelp(HelpMessageConstants.HELP_DLG_LDC_LINEAR);
	}

	@Override
	protected int[][] getTableFor(SBox sb)
	{
		return sb.getLAT();
	}
}
