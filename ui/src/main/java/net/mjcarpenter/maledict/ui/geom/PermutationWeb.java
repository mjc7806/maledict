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
package net.mjcarpenter.maledict.ui.geom;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.geom.Ellipse2D;

import javax.swing.Box;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PermutationWeb extends JPanel
{
	private static enum EndPanelType
	{
		HEADER, FOOTER
	}
	
	private static final int USR_HEIGHT_MAIN = 3;
	private static final int USR_HEIGHT_END = 2;
	
	private final JPanel header;
	private final JPanel mainPanel;
	private final JPanel footer;
	
	private List<Integer> colors;
	
	private int[]   indexMappings;
	private Point2D.Double[] endpoints;
	private Line2D.Double[]  drawingLines;
	
	public PermutationWeb(int indices, boolean endPanelsOn)
	{
		super();
		
		setLayout(new GridLayout(0,1));
				
		endpoints    = new Point2D.Double[indices * 2];
		drawingLines = new Line2D.Double[indices];
		
		colors = Collections.emptyList();
		
		for(int i=0; i<indices; i++)
		{
			endpoints[i]         = new Point2D.Double(i*2+1, 0);
			endpoints[i+indices] = new Point2D.Double(i*2+1, 2*USR_HEIGHT_MAIN);
			drawingLines[i] = new Line2D.Double();
		}
		
		int[] noopMappings = new int[indices];
		for(int i=0; i<noopMappings.length; i++)
		{
			noopMappings[i] = i;
		}
		updateMappings(noopMappings);
		
		header    = new WebEndPanel(EndPanelType.HEADER);
		mainPanel = new WebMainPanel();
		footer    = new WebEndPanel(EndPanelType.FOOTER);
		
		add(Box.createVerticalGlue());
		add(header);
		add(mainPanel);
		add(footer);
		add(Box.createVerticalGlue());
		
		setEndPanelsOn(endPanelsOn);
	}
	
	public void color(int[] indices)
	{
		this.colors = Arrays.stream(indices).boxed().collect(Collectors.toList());
	}
	
	public Point2D getEndpoint(int index)
	{
		return endpoints[index];
	}
	
	public void updateMappings(int[] mappings)
	{
		this.indexMappings = mappings;
		
		for(int i=0; i<indexMappings.length; i++)
		{
			drawingLines[i].setLine(endpoints[i], endpoints[indexMappings.length + indexMappings[i]]);
		}
		
		repaint();
		revalidate();
	}
	
	@Override
	public void repaint()
	{
		super.repaint();
		
		if(mainPanel != null)
			mainPanel.repaint();
		if(header != null)
			header.repaint();
		if(footer != null)
			footer.repaint();
	}
	
	@Override
	public void revalidate()
	{
		super.revalidate();
		
		if(mainPanel != null)
			mainPanel.revalidate();
		if(header != null)
			header.revalidate();
		if(footer != null)
			footer.revalidate();
	}
	
	public void setHeaderOn(boolean headerOn)
	{
		header.setVisible(headerOn);
	}
	
	public void setFooterOn(boolean footerOn)
	{
		footer.setVisible(footerOn);
	}
	
	public void setEndPanelsOn(boolean endPanelsOn)
	{
		header.setVisible(endPanelsOn);
		footer.setVisible(endPanelsOn);
	}
		
	private class WebMainPanel extends WebSubPanel
	{
		@Override
		public void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D)g;
			super.paintComponent(g2);
						
			float widthScale  = (float)getWidth()/(float)(2*indexMappings.length);
			float heightScale = (float)getHeight()/(float)(2*USR_HEIGHT_MAIN);
			
			g2.scale(widthScale, heightScale);
			
			for(int i=0; i<drawingLines.length; i++)
			{
				boolean contains = colors.contains(i);
				g2.setStroke(new BasicStroke((float)((contains ? 3.0 : 2.0)/widthScale)));
				g2.setColor(contains ? Color.BLUE : Color.BLACK);
				
				g2.draw(drawingLines[i]);
			}
		}
	}
	
	private class WebEndPanel extends WebSubPanel
	{
		private static final double PIN_RADIUS = 0.5;
				
		private final EndPanelType endType;
		private Line2D.Double[]    edgeLines;
		private Ellipse2D.Double[] pinHeads;
		
		public WebEndPanel(EndPanelType endType)
		{
			super();
			this.endType = endType;
			this.edgeLines = new Line2D.Double[endpoints.length/2];
			this.pinHeads  = new Ellipse2D.Double[endpoints.length/2];
			
			for(int i=0; i<edgeLines.length; i++)
			{
				Point2D pointA;
				Point2D pointB;
				
				
				if(EndPanelType.HEADER.equals(this.endType))
				{
					pointA = new Point2D.Double(i*2+1, 4);
					pointB = new Point2D.Double(i*2+1, 3);
					
					pinHeads[i]  = new Ellipse2D.Double(
							pointB.getX()-PIN_RADIUS,
							pointB.getY()-PIN_RADIUS*2,
							PIN_RADIUS*2,
							PIN_RADIUS*2);
				}
				else // FOOTER
				{
					pointA = new Point2D.Double(i*2+1, 0);
					pointB = new Point2D.Double(i*2+1, 1);
					
					pinHeads[i]  = new Ellipse2D.Double(
							pointB.getX()-PIN_RADIUS,
							2-PIN_RADIUS*2,
							PIN_RADIUS*2,
							PIN_RADIUS*2);
				}
				
				edgeLines[i] = new Line2D.Double(pointA, pointB);
			}
		}
		
		@Override
		public void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D)g;
			super.paintComponent(g2);
			
			float widthScale  = (float)getWidth()/(float)(2*indexMappings.length);
			float heightScale = (float)getHeight()/(float)(2*USR_HEIGHT_END);
			
			g2.scale(widthScale, heightScale);
			
			for(int i=0; i<edgeLines.length; i++)
			{
				int idxToColor = (EndPanelType.HEADER.equals(this.endType)) ? i : indexMappings[i];
				if(colors.contains(idxToColor))
				{
					g2.setStroke(new BasicStroke((float)3.0/widthScale));
					g2.setColor(Color.BLUE);
				}
				else
				{
					g2.setStroke(new BasicStroke((float)2.0/widthScale));
					g2.setColor(Color.BLACK);
				}
				
				g2.draw(edgeLines[i]);
				g2.draw(pinHeads[i]);
			}
		}
	}
	
	private abstract class WebSubPanel extends JPanel
	{		
		public WebSubPanel()
		{
			super();
			setBorder(null);
			setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		}
	}
}
