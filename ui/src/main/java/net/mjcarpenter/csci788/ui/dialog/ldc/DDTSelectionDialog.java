package net.mjcarpenter.csci788.ui.dialog.ldc;

import javax.swing.JDialog;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.mjcarpenter.csci788.crypto.spn.SBox;
import net.mjcarpenter.csci788.ui.component.CoordinateToggleButton;

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
		// TODO Auto-generated method stub

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
