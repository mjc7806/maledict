package net.mjcarpenter.maledict.ui.dialog.component;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import net.mjcarpenter.maledict.crypto.spn.SPNetwork;
import net.mjcarpenter.maledict.ui.geom.SPNShape;

@SuppressWarnings("serial")
public class SPNVisualizationFrame extends JFrame
{
	private SPNetwork spn;
	private SPNShape  shape;
	
	public SPNVisualizationFrame(SPNetwork spn)
	{
		super("SPN Visualization");
		this.spn   = spn;
		this.shape = null;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		createPanel();
		setSize(500, 620);
		setVisible(true);
	}
	
	public void setSPN(SPNetwork spn)
	{
		this.spn = spn;
		
		createPanel();
		revalidate();
	}
	
	public void colorVisualization(long[] inMask, long[] outMask)
	{
		shape.applyRoundMasks(inMask, outMask);
	}
	
	public void clearVisualizationColoring()
	{
		shape.clearRoundMasks();
	}
	
	public BufferedImage visualizationImage()
	{
		return shape.captureAsImage();
	}
	
	public void createPanel()
	{
		if(shape != null)
		{
			remove(shape);
		}
		
		shape = new SPNShape(spn);
		add(shape);
	}
}
