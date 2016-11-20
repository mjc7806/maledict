package net.mjcarpenter.csci788.ui.dialog.ldc;

import javax.swing.JDialog;

import net.mjcarpenter.csci788.crypto.spn.SBox;
import net.mjcarpenter.csci788.ui.component.CoordinateToggleButton;

public final class LATSelectionDialog extends TableSelectionDialog
{
	private SBox sbox;
	private int  selectedIn;
	private int  selectedOut;
	private int  selectedBias;
	
	public LATSelectionDialog(SBox sbox)
	{
		super(sbox.getLAT());
		this.sbox = sbox;
		setTitle("Linear Approximation Table: Selection Dialog");
		setModal(true);
		setVisible(true);
	}
	
	public int getSelectedIn()
	{
		return selectedIn;
	}
	
	public int getSelectedOut()
	{
		return selectedOut;
	}
	
	public int getSelectedBias()
	{
		return selectedBias;
	}
	
	@Override
	protected void handleAccept(CoordinateToggleButton selectedButton)
	{
		selectedIn   = selectedButton.row;
		selectedOut  = selectedButton.col;
		selectedBias = sbox.getLAT()[selectedIn][selectedOut];
		
		setVisible(false);
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
}
