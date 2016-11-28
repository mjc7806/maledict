package net.mjcarpenter.csci788.ui.dialog.component;

import javax.swing.JFrame;

import net.mjcarpenter.csci788.crypto.spn.SPNetwork;
import net.mjcarpenter.csci788.ui.geom.SPNShape;

@SuppressWarnings("serial")
public class SPNVisualizationFrame extends JFrame
{
	private SPNetwork spn;
	
	public SPNVisualizationFrame(SPNetwork spn)
	{
		super("SPN Visualization");
		this.spn = spn;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		setSize(400,900);
		
		createPanel();
		setVisible(true);
	}
	
	public void setSPN(SPNetwork spn)
	{
		this.spn = spn;
		
		createPanel();
		revalidate();
	}
	
	public void createPanel()
	{
		this.add(new SPNShape(spn));
	}
}
