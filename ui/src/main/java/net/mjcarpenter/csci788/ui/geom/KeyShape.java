package net.mjcarpenter.csci788.ui.geom;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class KeyShape extends JPanel
{
	private static final int USR_HEIGHT = 2;
	private static final int MAX_WIDTH_PX = 800;
	
	private final double ratioHeightByWidth;
	private double widthScale;
	
	private int maxHeight;
	private int bitWidth;
	private Line2D.Double[]    topLines;
	private Line2D.Double[]    btmLines;
	private Rectangle2D.Double rect; 
	
	public KeyShape(int bitWidth)
	{
		super();
		
		this.bitWidth = bitWidth;
		ratioHeightByWidth = bitWidth*2;
		
		topLines = new Line2D.Double[bitWidth];
		btmLines = new Line2D.Double[bitWidth];
		for(int i=0; i<bitWidth; i++)
		{
			double x = i*2+1;
			topLines[i] = new Line2D.Double(x, 0, x, 0.5);
			btmLines[i] = new Line2D.Double(x, 2, x, 1.5);
		}
		
		rect = new Rectangle2D.Double(0.5, 0.5, bitWidth*2-1, 1);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		super.paintComponent(g2);
		
		scaleTo(getSize());
		g2.setStroke(new BasicStroke((float)(2.0/widthScale)));
		g2.scale(widthScale, widthScale);
		
		for(Line2D line: topLines)
		{
			g2.draw(line);
		}
		
		g2.draw(rect);
		
		for(Line2D line: btmLines)
		{
			g2.draw(line);
		}
	}
	
	public void scaleTo(Dimension d)
	{
		widthScale = d.getWidth()/ratioHeightByWidth;
	}
	
	@Override
	public Dimension getMaximumSize()
	{
		return new Dimension(Integer.MAX_VALUE, (int)Math.round(2*widthScale));
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(MAX_WIDTH_PX, (int)Math.round(MAX_WIDTH_PX/widthScale));
	}
	
	public static void main(String[] args)
	{
		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLayout(new BorderLayout());
		jf.add(new KeyShape(16), BorderLayout.CENTER);
		jf.setVisible(true);
	}
}