package net.mjcarpenter.csci788.ui.dialog.ldc;

import net.mjcarpenter.csci788.crypto.spn.SBox;
import net.mjcarpenter.csci788.ui.component.CoordinateToggleButton;
import net.mjcarpenter.csci788.ui.message.help.HelpMessageConstants;

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
