package net.mjcarpenter.csci788.ui.dialog.ldc;

import javax.swing.JDialog;
import javax.swing.UIManager;

import net.mjcarpenter.csci788.crypto.spn.SBox;
import net.mjcarpenter.csci788.ui.component.CoordinateToggleButton;
import net.mjcarpenter.csci788.ui.message.help.HelpMessageConstants;

@SuppressWarnings("serial")
public final class DDTSelectionDialog extends TableSelectionDialog
{
	public DDTSelectionDialog(SBox sbox, int inFilter)
	{
		super(sbox.getDDT(), inFilter);
		setTitle("Difference Distribution Table: Selection Dialog");
	}
	
	@Override
	protected void handleAccept(CoordinateToggleButton selectedButton)
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleCancel()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleHelp()
	{
		handleHelp(HelpMessageConstants.HELP_DLG_LDC_DDT);
	}
}
