package net.mjcarpenter.csci788.ui.geom;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

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
		
		setLayout(new BorderLayout());
		
		header    = new WebEndPanel(EndPanelType.HEADER);
		mainPanel = new WebMainPanel();
		footer    = new WebEndPanel(EndPanelType.FOOTER);
		
		widthMax  = indices * 2 + 2;
		heightMax = indices / 2;
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
		
		add(header,    BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
		add(footer,    BorderLayout.SOUTH);
		
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
		
		PermutationWeb webTest = new PermutationWeb(16, false);
		jf.add(webTest, BorderLayout.CENTER);
		jf.setVisible(true);
		webTest.setVisible(true);
		
		webTest.updateMappings(mappings);
	}
	
	
	private class WebMainPanel extends WebSubPanel
	{
		@Override
		public void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D)g;
			super.paintComponent(g2);
			
			scaleTo(getSize());
			g2.scale(widthFactor, widthFactor);
			g2.setStroke(new BasicStroke((float)(2.0/((widthFactor+heightFactor)/2.0))));
			
			for(Line2D line: drawingLines)
			{
				g2.draw(line);
			}
		}
	}
	
	private class WebEndPanel extends WebSubPanel
	{
		private final EndPanelType endType;
		private Point2D.Double[]   edgePoints;
		
		public WebEndPanel(EndPanelType endType)
		{
			super();
			this.endType = endType;
			this.edgePoints = new Point2D.Double[endpoints.length/2];
			
			if(EndPanelType.HEADER.equals(endType))
			{
				
			}
			else // FOOTER
			{
				
			}
		}
		
		@Override
		public void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D)g;
			super.paintComponent(g2);
			
			scaleTo(getSize());
			g2.scale(widthFactor, widthFactor);
			g2.setStroke(new BasicStroke((float)(2.0/((widthFactor+heightFactor)/2.0))));
			
			for(Line2D line: drawingLines)
			{
				g2.draw(line);
			}
		}
	}
	
	private class WebSubPanel extends JPanel
	{
		public void scaleTo(Dimension d)
		{
			widthFactor  = d.getWidth()  / (double)widthMax;
			heightFactor = d.getHeight() / (double)heightMax;
		}
	}
}
