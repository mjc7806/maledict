/*
 * Maledict - An Interactive Tool for Learning Linear and Differential Cryptanalysis of SPNs
 * Copyright (C) 2016  Mike Carpenter
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
