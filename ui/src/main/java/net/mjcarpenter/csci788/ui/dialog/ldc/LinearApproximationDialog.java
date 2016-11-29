package net.mjcarpenter.csci788.ui.dialog.ldc;

import net.mjcarpenter.csci788.crypto.ldc.AbstractApproximation;
import net.mjcarpenter.csci788.crypto.ldc.LinearApproximation;
import net.mjcarpenter.csci788.crypto.spn.SBox;
import net.mjcarpenter.csci788.crypto.spn.SPNetwork;
import net.mjcarpenter.csci788.ui.message.help.HelpMessageConstants;

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
}
