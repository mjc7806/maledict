package net.mjcarpenter.csci788.ui.dialog.ldc;

import net.mjcarpenter.csci788.crypto.spn.SBox;
import net.mjcarpenter.csci788.ui.component.CoordinateToggleButton;
import net.mjcarpenter.csci788.ui.message.help.HelpMessageConstants;

@SuppressWarnings("serial")
public final class LATSelectionDialog extends TableSelectionDialog
{	
	private SBox sbox;
	private boolean hasSelection;
	private int     selectedIn;
	private int     selectedOut;
	private int     selectedBias;
	
	public LATSelectionDialog(SBox sbox)
	{
		super(sbox.getLAT());
		this.sbox = sbox;
		this.hasSelection = false;
		
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
	
	public boolean hasSelection()
	{
		return hasSelection;
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
