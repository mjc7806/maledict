package net.mjcarpenter.csci788.ui.geom;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PermutationWeb extends JPanel
{
	private static enum EndPanelType
	{
		HEADER, FOOTER
	}
	
	private final JPanel header;
	private final JPanel mainPanel;
	private final JPanel footer;
	
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
		
		widthMax  = (indices + 1)*2;
		heightMax = (int)Math.round(widthMax*(3.0/17.0));
		widthFactor  = 1;
		heightFactor = 1;
		//setBounds(0,0,widthMax, heightMax);
		
		endpoints    = new Point2D.Double[indices * 2];
		drawingLines = new Line2D.Double[indices];
		
		for(int i=0; i<indices; i++)
		{
			endpoints[i]         = new Point2D.Double(i*2+2, 0);
			endpoints[i+indices] = new Point2D.Double(i*2+2, heightMax);
			drawingLines[i] = new Line2D.Double();
		}
		
		header    = new WebEndPanel(EndPanelType.HEADER);
		mainPanel = new WebMainPanel();
		footer    = new WebEndPanel(EndPanelType.FOOTER);
		
		add(header);
		add(mainPanel);
		add(footer);
		
		setEndPanelsOn(endPanelsOn);
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
		
		//paint();
	}
	
	public void setEndPanelsOn(boolean endPanelsOn)
	{
		header.setVisible(endPanelsOn);
		footer.setVisible(endPanelsOn);
	}
	
	public static void main(String[] args)
	{
		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(800, 600);
		jf.setLayout(new BorderLayout());
		
		int[] mappings = {0,4,8,12,1,5,9,13,2,6,10,14,3,7,11,15};
		
		PermutationWeb webTest = new PermutationWeb(16, true);
		jf.add(webTest, BorderLayout.CENTER);
		jf.setVisible(true);
		webTest.setVisible(true);
		
		webTest.updateMappings(mappings);
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
			
			for(Line2D line: drawingLines)
			{
				g2.draw(line);
			}
		}
		
		@Override
		public Dimension getMaximumSize()
		{
			return new Dimension(MAX_WIDTH_PX, (int)(MAX_WIDTH_PX*HEIGHT_RATIO));
		}
		
		public double preferredHeight()
		{
			return heightMax;
		}
	}
	
	private class WebEndPanel extends WebSubPanel
	{
		private static final double HEIGHT_RATIO = 0.125;
		private static final double PIN_RADIUS = 0.5;
		
		private double heightPreferred;
		
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
				Point2D pointA;// = new Point2D.Double(i*2+2, 0);
				Point2D pointB;// = new Point2D.Double(i*2+2, 2);
				
				
				if(EndPanelType.HEADER.equals(this.endType))
				{
					pointA = new Point2D.Double(i*2+2, 4);
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
					pointB = new Point2D.Double(i*2+2, 2);
					
					pinHeads[i]  = new Ellipse2D.Double(
							pointB.getX()-PIN_RADIUS,
							3-PIN_RADIUS*2,
							PIN_RADIUS*2,
							PIN_RADIUS*2);
				}
				
				edgeLines[i] = new Line2D.Double(pointA, pointB);
			}
			
			heightPreferred = (2*heightMax)/3;
		}
		
		@Override
		public Dimension getMaximumSize()
		{
			return new Dimension(MAX_WIDTH_PX, (int)(MAX_WIDTH_PX*HEIGHT_RATIO));
		}
		
		@Override
		public void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D)g;
			super.paintComponent(g2);
			
			//scaleTo(getSize());
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
		
		public void scaleTo(Dimension d)
		{
			widthFactor  = d.getWidth()  / (double)widthMax;
			heightFactor = d.getHeight() / (double)heightMax;
		}
		
		@Override
		public Dimension getPreferredSize()
		{
			return getMaximumSize();
		}		
	}
}
