package net.mjcarpenter.csci788.ui.dialog.ldc;

import javax.swing.JDialog;

import net.mjcarpenter.csci788.crypto.spn.SBox;

public final class LATSelectionDialog extends TableSelectionDialog
{
	public LATSelectionDialog(SBox sbox)
	{
		super(sbox.getLAT());
		setTitle("Linear Approximation Table: Selection Dialog");
	}
	
	@Override
	protected void handleAccept() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleHelp() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args)
	{
		JDialog dlg = new LATSelectionDialog(SBox.noop(4));
		dlg.setModal(true);
		dlg.setVisible(true);
	}
}
