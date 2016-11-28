package net.mjcarpenter.csci788.ui.dialog.ldc;

import net.mjcarpenter.csci788.crypto.spn.SBox;
import net.mjcarpenter.csci788.ui.component.CoordinateToggleButton;
import net.mjcarpenter.csci788.ui.message.help.HelpMessageConstants;

@SuppressWarnings("serial")
public final class LATSelectionDialog extends TableSelectionDialog
{	
	private SBox sbox;
	
	public LATSelectionDialog(SBox sbox, int inFilter)
	{
		super(sbox.getLAT(), inFilter);
		this.sbox = sbox;
		
		setTitle("Linear Approximation Table: Selection Dialog");
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
	protected void handleCancel()
	{
		hasSelection = false;
		setVisible(false);
	}

	@Override
	protected void handleHelp()
	{
		handleHelp(HelpMessageConstants.HELP_DLG_LDC_LAT);
	}
}
