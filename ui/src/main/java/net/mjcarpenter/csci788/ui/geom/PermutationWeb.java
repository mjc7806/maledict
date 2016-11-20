package net.mjcarpenter.csci788.ui.geom;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.geom.Ellipse2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class PermutationWeb extends JPanel
{
	private static enum EndPanelType
	{
		HEADER, FOOTER
	}
	
	private static final int MAX_WIDTH_PX = 800;
	private static final int USR_HEIGHT = 4;
	
	private final JPanel header;
	private final JPanel mainPanel;
	private final JPanel footer;
	
	private List<Integer> colors;
	
	private double  widthFactor;
	private double  heightFactor;
	private int     widthMax;
	private int     heightMax;
	private int[]   indexMappings;
	private Point2D.Double[] endpoints;
	private Line2D.Double[]  drawingLines;
	
	public PermutationWeb(int indices, boolean endPanelsOn)
	{
		super();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		widthMax  = 2*indices;
		heightMax = 6;//(int)Math.round(widthMax*(3.0/17.0));
		widthFactor  = 1;
		heightFactor = 1;
		
		endpoints    = new Point2D.Double[indices * 2];
		drawingLines = new Line2D.Double[indices];
		
		colors = Collections.emptyList();
		
		for(int i=0; i<indices; i++)
		{
			endpoints[i]         = new Point2D.Double(i*2+1, 0);
			endpoints[i+indices] = new Point2D.Double(i*2+1, heightMax);
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
		//setBorder(new EmptyBorder(35,35,35,35));
		
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
	
	@Override
	public Dimension getMaximumSize()
	{
		return new Dimension(Integer.MAX_VALUE, (int)Math.round(2*widthFactor));
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(MAX_WIDTH_PX, (int)Math.round(MAX_WIDTH_PX/widthFactor));
	}
	
	/*
	@Override
	public Dimension getMaximumSize()
	{
		int height = mainPanel.getPreferredSize().height;
		if(header.isVisible())
		{
			height += (header.getPreferredSize().height * 2);
		}
		
		int width = mainPanel.getPreferredSize().width;
		
		return new Dimension(width, height);
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		return getMaximumSize();
	}*/
	
	public static void main(String[] args)
	{
		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(800, 600);
		
		int[] mappings = {0,4,8,12,1,5,9,13,2,6,10,14,3,7,11,15};
		
		PermutationWeb webTest = new PermutationWeb(16, true);
		jf.add(webTest);
		jf.setVisible(true);
		webTest.setVisible(true);
		
		webTest.updateMappings(mappings);

		System.out.printf("Header:    [%s]\nMainPanel: [%s]\nFooter:    [%s]\n",
				webTest.header.getSize().toString(),
				webTest.mainPanel.getSize().toString(),
				webTest.footer.getSize().toString());
	}
	
	
	private class WebMainPanel extends WebSubPanel
	{
		private static final double HEIGHT_RATIO = 0.25;
		
		@Override
		public void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D)g;
			super.paintComponent(g2);
			
			scaleTo(getSize());
			g2.scale(widthFactor, widthFactor);
			g2.setStroke(new BasicStroke((float)(2.0/widthFactor)));
			
			for(int i=0; i<drawingLines.length; i++)
			{
				g2.setColor(colors.contains(i) ? Color.BLUE : Color.BLACK);
				
				g2.draw(drawingLines[i]);
			}
		}
		
		@Override
		public Dimension getPreferredSize()
		{
			return new Dimension(MAX_WIDTH_PX, (int)Math.round(6*widthFactor));//(int)(MAX_WIDTH_PX*HEIGHT_RATIO));
		}
	}
	
	private class WebEndPanel extends WebSubPanel
	{
		private static final double HEIGHT_RATIO = 0.125;
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
					pointA = new Point2D.Double(i*2+2, 3);
					pointB = new Point2D.Double(i*2+2, 2);
					
					pinHeads[i]  = new Ellipse2D.Double(
							pointB.getX()-PIN_RADIUS,
							pointB.getY()-PIN_RADIUS*2,
							PIN_RADIUS*2,
							PIN_RADIUS*2);
				}
				else // FOOTER
				{
					pointA = new Point2D.Double(i*2+2, 0);
					pointB = new Point2D.Double(i*2+2, 1);
					
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
		public Dimension getPreferredSize()
		{
			return new Dimension(MAX_WIDTH_PX, (int)Math.round(USR_HEIGHT*widthFactor));//(int)(MAX_WIDTH_PX*HEIGHT_RATIO));
		}
		
		@Override
		public void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D)g;
			super.paintComponent(g2);
			
			scaleTo(getSize());
			g2.scale(widthFactor, widthFactor);
			g2.setStroke(new BasicStroke((float)(2.0/widthFactor)));
			
			for(Line2D line: edgeLines)
			{
				g2.draw(line);
			}
			
			for(Ellipse2D pin: pinHeads)
			{
				g2.draw(pin);
			}
		}
	}
	
	private abstract class WebSubPanel extends JPanel
	{
		protected static final int MAX_WIDTH_PX = 800;
		
		public WebSubPanel()
		{
			super();
			setBorder(null);
			setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		}
		
		public void scaleTo(Dimension d)
		{
			widthFactor  = d.getWidth()  / (double)widthMax;
			heightFactor = d.getHeight() / (double)heightMax;
		}
		
		@Override
		public Dimension getMaximumSize()
		{
			return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
		}		
	}
}
