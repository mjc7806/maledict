package net.mjcarpenter.csci788.ui.geom;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SBoxRow extends JPanel
{
	public SBoxShape[] shapes;
	
	public SBoxRow(int bitWidth, int numBoxes)
	{
		super();
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		//add(Box.createHorizontalGlue());
		
		shapes = new SBoxShape[numBoxes];
		for(int i=0; i<shapes.length; i++)
		{
			shapes[i] = new SBoxShape(bitWidth);
			this.add(shapes[i]);
		}
		
		//add(Box.createHorizontalGlue());
		
		setVisible(true);
	}
	
	public SBoxShape[] getShapes()
	{
		return this.shapes;
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(
				shapes[0].getPreferredSize().height,
				shapes[0].getPreferredSize().width*shapes.length);
	}
	
	@Override
	public Dimension getMaximumSize()
	{
		return new Dimension(
				shapes[0].getMaximumSize().height,
				shapes[0].getMaximumSize().width);
	}
	
	public static void main(String[] args)
	{
		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLayout(new BorderLayout());
		jf.add(new SBoxRow(4,4), BorderLayout.CENTER);
		jf.setVisible(true);
	}
}
