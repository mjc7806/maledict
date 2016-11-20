package net.mjcarpenter.csci788.ui.geom;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SBoxShape extends JPanel
{
	private static final int USR_HEIGHT = 3;
	private static final int MAX_WIDTH_PX = 800;
	
	private final double ratioHeightByWidth;
	private double widthScale;
	
	private List<Integer> colorsIn;
	private List<Integer> colorsOut;
	
	private int bitWidth;
	private Line2D.Double[]    topLines;
	private Line2D.Double[]    btmLines;
	private Rectangle2D.Double rect; 
	
	public SBoxShape(int bitWidth)
	{
		super();
		
		this.bitWidth = bitWidth;
		widthScale = bitWidth*2-1;
		ratioHeightByWidth = bitWidth*2;
		
		colorsIn  = Collections.emptyList();
		colorsOut = Collections.emptyList();

		topLines = new Line2D.Double[bitWidth];
		btmLines = new Line2D.Double[bitWidth];
		for(int i=0; i<bitWidth; i++)
		{
			double x = i*2+1;
			topLines[i] = new Line2D.Double(x, 0, x, 0.5);
			btmLines[i] = new Line2D.Double(x, 3, x, 2.5);
		}
		
		rect = new Rectangle2D.Double(0.5, 0.5, bitWidth*2-1, 2);
	}
	
	public void color(int[] colorIn, int[] colorOut)
	{
		colorsIn = Arrays.stream(colorIn).boxed().collect(Collectors.toList());
		colorsOut = Arrays.stream(colorOut).boxed().collect(Collectors.toList());
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
		return new Dimension(Integer.MAX_VALUE, (int)Math.round(USR_HEIGHT*widthScale));//getPreferredSize().height);
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
		jf.add(new SBoxShape(4), BorderLayout.CENTER);
		jf.setVisible(true);
	}
}
