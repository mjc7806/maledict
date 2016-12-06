package net.mjcarpenter.maledict.ui.dialog.ldc;

import net.mjcarpenter.maledict.crypto.spn.SBox;
import net.mjcarpenter.maledict.ui.component.CoordinateToggleButton;
import net.mjcarpenter.maledict.ui.message.help.HelpMessageConstants;

@SuppressWarnings("serial")
public final class DDTSelectionDialog extends TableSelectionDialog
{
	private SBox sbox;
	
	public DDTSelectionDialog(SBox sbox, long inFilter)
	{
		super(sbox.getDDT(), inFilter);
		setTitle("Difference Distribution Table: Selection Dialog");

		this.sbox = sbox;
		setModal(true);
		setVisible(true);
	}
	
	@Override
	protected void handleAccept(CoordinateToggleButton selectedButton)
	{
		selectedIn   = selectedButton.row;
		selectedOut  = selectedButton.col;
		selectedBias = sbox.getDDT()[selectedIn][selectedOut];
		
		hasSelection = true;
		setVisible(false);
	}

	@Override
	protected void handleHelp()
	{
		handleHelp(HelpMessageConstants.HELP_DLG_LDC_DDT);
	}
}
