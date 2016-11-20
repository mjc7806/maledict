package net.mjcarpenter.csci788.ui.component;

import javax.swing.JToggleButton;

@SuppressWarnings("serial")
public class CoordinateToggleButton extends JToggleButton
{
	public final int row;
	public final int col;
	
	public CoordinateToggleButton(String title, int row, int col)
	{
		super(title);
		
		this.row = row;
		this.col = col;
	}
}