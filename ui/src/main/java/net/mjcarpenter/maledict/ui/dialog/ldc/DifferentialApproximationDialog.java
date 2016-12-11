package net.mjcarpenter.maledict.ui.dialog.ldc;

import net.mjcarpenter.maledict.crypto.ldc.AbstractApproximation;
import net.mjcarpenter.maledict.crypto.ldc.DifferentialApproximation;
import net.mjcarpenter.maledict.crypto.spn.SBox;
import net.mjcarpenter.maledict.crypto.spn.SPNetwork;
import net.mjcarpenter.maledict.ui.message.help.HelpMessageConstants;

@SuppressWarnings("serial")
public final class DifferentialApproximationDialog extends ApproximationDialog
{	
	public DifferentialApproximationDialog(SPNetwork spn)
	{
		super(spn, "Construct Differential Approximation");
	}
	
	@Override
	protected TableSelectionDialog createTableSelectionDialog(SBox box, long mask)
	{
		return new DDTSelectionDialog(box, mask);
	}
	
	@Override
	public AbstractApproximation getCipherApproximation(long plaintextMask, long lastRoundMask)
	{
		return new DifferentialApproximation(plaintextMask, lastRoundMask);
	}
	
	@Override
	protected void handleHelp()
	{
		handleHelp(HelpMessageConstants.HELP_DLG_LDC_DIFF);
	}

	@Override
	protected int[][] getTableFor(SBox sb)
	{
		return sb.getDDT();
	}
}
