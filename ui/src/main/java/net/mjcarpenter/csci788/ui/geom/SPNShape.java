package net.mjcarpenter.csci788.ui.geom;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.mjcarpenter.csci788.crypto.spn.SPNetwork;
import net.mjcarpenter.csci788.crypto.spn.SPNetworkTests;

@SuppressWarnings("serial")
public final class SPNShape extends JPanel
{
	private static final int HEIGHT_KEY  = 2;
	private static final int HEIGHT_PERM = 5;
	private static final int HEIGHT_SBOX = 3;
	
	private Collection<Point2D.Double>     points;
	private Collection<Line2D.Double>      lines;
	private Collection<Rectangle2D.Double> rects;
	
	private SPNetwork spn;
	private double scale;
	private int    fullUsrHeight;
	private int    fullScaledHeight;
	
	public SPNShape(SPNetwork spn)
	{
		this.spn = spn;
		scaleTo(getSize());
		build();
		setVisible(true);
	}
	
	public void build()
	{
		this.points = new ArrayList<Point2D.Double>();
		this.lines = new ArrayList<Line2D.Double>();
		this.rects = new ArrayList<Rectangle2D.Double>();
		
		int bitWidth  = spn.getBlockSize();
		int curHeight = 0;
		
		for(int i=0; i<spn.getRounds().length; i++)
		{
			// HANDLE KEY
			if(!spn.getRounds()[i].getSubKey().isNoop())
			{
				for(int j=0; j<bitWidth; j++)
				{
					double x = j*2+1;
					lines.add(new Line2D.Double(x, curHeight, x, curHeight+HEIGHT_KEY/4.0));
					lines.add(new Line2D.Double(x, curHeight+HEIGHT_KEY, x, curHeight+(HEIGHT_KEY*0.75)));
				}
				
				rects.add(new Rectangle2D.Double(0.5, curHeight+HEIGHT_KEY/4.0, bitWidth*2-1, HEIGHT_KEY/2.0));
				
				curHeight += HEIGHT_KEY;
			}
			
			// HANDLE S-BOXES
			boolean includeRow = true;
			for(int j=0; j<spn.getRounds()[i].getSBoxes().length; j++)
			{
				// If at least one S-box in the row is non-noop then include it.
				includeRow &= !spn.getRounds()[i].getSBoxes()[j].isNoop();
			}
			if(includeRow)
			{
				int sbx = 0;
				
				for(int j=0; j<spn.getRounds()[i].getSBoxes().length; j++)
				{
					int boxBitSize = spn.getRounds()[i].getSBoxes()[j].bitSize();
					for(int k=0; k<boxBitSize; k++)
					{
						double x = (sbx+k)*2+1;
						lines.add(new Line2D.Double(x, curHeight, x, curHeight+0.5));
						lines.add(new Line2D.Double(x, curHeight+HEIGHT_SBOX, x, curHeight+(HEIGHT_SBOX-0.5)));
					}
					
					rects.add(new Rectangle2D.Double((sbx*2)+0.5, curHeight+0.5, boxBitSize*2-1, HEIGHT_SBOX-1));
					sbx += boxBitSize;
				}
				
				curHeight += HEIGHT_SBOX;
			}
			
			// HANDLE PERMUTATIONS
			if(!spn.getRounds()[i].getPermutation().isNoop())
			{
				for(int j=0; j<bitWidth; j++)
				{
					double x1 = j*2+1;
					double x2 = spn.getRounds()[i].getPermutation().outPosition(j)*2+1;
					lines.add(new Line2D.Double(x1, curHeight, x2, curHeight+HEIGHT_PERM));
				}
				
				curHeight += HEIGHT_PERM;
			}
		}
		
		this.fullUsrHeight = curHeight;
		scaleTo(getSize());
		
		revalidate();
		repaint();
	}
	
	public void scaleTo(Dimension d)
	{
		scale = (double)d.getWidth()/((double)spn.getBlockSize()*2.0);
	}
	
	@Override
	public Dimension getMinimumSize()
	{
		scaleTo(getSize());
		int prefHeight = Math.max(getHeight(), (int)Math.ceil(fullUsrHeight/scale));
		
		return new Dimension(getWidth(), prefHeight);
	}
	
	public static void main(String[] args)
	{
		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.add(new SPNShape(SPNetworkTests.sampleNetwork()));
		jf.setSize(800, 1000);
		jf.setVisible(true);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		
		super.paintComponent(g2);
		
		scaleTo(getSize());

		g2.setStroke(new BasicStroke((int)Math.round(2/scale)));
		g2.scale(scale, scale);
		
		for(Line2D each: lines)
		{
			g2.draw(each);
		}
		for(Rectangle2D each: rects)
		{
			g2.draw(each);
		}
	}
}
