package net.mjcarpenter.csci788.ui.dialog.ldc;

import javax.swing.JDialog;
import javax.swing.UIManager;

import net.mjcarpenter.csci788.crypto.spn.SBox;
import net.mjcarpenter.csci788.ui.component.CoordinateToggleButton;
import net.mjcarpenter.csci788.ui.message.help.HelpMessageConstants;

@SuppressWarnings("serial")
public final class DDTSelectionDialog extends TableSelectionDialog
{
	public DDTSelectionDialog(SBox sbox)
	{
		super(sbox.getDDT());
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

	public static void main(String[] args)
	throws Exception
	{
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		JDialog dlg = new DDTSelectionDialog(SBox.noop(6));
		dlg.setModal(true);
		dlg.setVisible(true);
	}
}
