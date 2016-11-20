package net.mjcarpenter.csci788.ui.component;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class CoordinateButton extends JButton
{
	public final int row;
	public final int col;
	
	public CoordinateButton(String title, int row, int col)
	{
		super(title);
		
		this.row = row;
		this.col = col;
	}
}