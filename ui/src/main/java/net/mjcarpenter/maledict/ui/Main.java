package net.mjcarpenter.maledict.ui;

import javax.swing.SwingUtilities;

import net.mjcarpenter.maledict.ui.dialog.NewOrLoadDialog;

public final class Main
{
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(() -> new NewOrLoadDialog());
	}
}
