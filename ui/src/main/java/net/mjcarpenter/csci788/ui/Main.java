package net.mjcarpenter.csci788.ui;

import javax.swing.SwingUtilities;

import net.mjcarpenter.csci788.ui.dialog.NewOrLoadDialog;

public final class Main
{
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(() -> new NewOrLoadDialog());
	}
}
