package net.mjcarpenter.maledict.ui.dialog.ldc;

import net.mjcarpenter.maledict.crypto.spn.SBox;
import net.mjcarpenter.maledict.ui.component.CoordinateToggleButton;
import net.mjcarpenter.maledict.ui.message.help.HelpMessageConstants;

@SuppressWarnings("serial")
public final class LATSelectionDialog extends TableSelectionDialog
{	
	private SBox sbox;
	
	public LATSelectionDialog(SBox sbox, long inFilter)
	{
		super(sbox.getLAT(), inFilter);
		setTitle("Linear Approximation Table: Selection Dialog");
		
		this.sbox = sbox;
		setModal(true);
		setVisible(true);
	}
	
	@Override
	protected void handleAccept(CoordinateToggleButton selectedButton)
	{
		selectedIn   = selectedButton.row;
		selectedOut  = selectedButton.col;
		selectedBias = sbox.getLAT()[selectedIn][selectedOut];
		
		hasSelection = true;
		setVisible(false);
	}

	@Override
	protected void handleHelp()
	{
		handleHelp(HelpMessageConstants.HELP_DLG_LDC_LAT);
	}
}
