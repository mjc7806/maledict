package net.mjcarpenter.csci788.ui.geom;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class SBoxRow extends JPanel
{
	public SBoxShape[] shapes;
	
	public SBoxRow(int bitWidth, int numBoxes)
	{
		super();
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		shapes = new SBoxShape[numBoxes];
		for(int i=0; i<shapes.length; i++)
		{
			shapes[i] = new SBoxShape(bitWidth);
			this.add(shapes[i]);
		}
		
		setVisible(true);
	}
}
