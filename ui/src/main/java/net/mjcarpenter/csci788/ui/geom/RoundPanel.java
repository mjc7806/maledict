package net.mjcarpenter.csci788.ui.geom;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RoundPanel extends JPanel
{
	private PermutationWeb perm;
	private KeyShape key;
	private SBoxRow srow;
	
	public RoundPanel(PermutationWeb perm, KeyShape key, SBoxRow srow)
	{
		super();
		this.perm = perm;
		this.key  = key;
		this.srow = srow;
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		/*addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				//int height = getHeight();
				int width = getWidth();
				
				setSize(new Dimension(width, perm.getHeight()+key.getHeight()+srow.getHeight()));
				
				revalidate();
				repaint();
			}
		});*/
		
		arrange();
	}
	
	public void setPermutation(PermutationWeb perm)
	{
		this.perm = perm;
		arrange();
	}
	
	public void setKeyShape(KeyShape key)
	{
		this.key = key;
		arrange();
	}
	
	public void setSboxRow(SBoxRow srow)
	{
		this.srow = srow;
		arrange();
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(perm.getPreferredSize().width,
				perm.getPreferredSize().height + srow.getPreferredSize().height + key.getPreferredSize().height);
	}
	
	
	@Override
	public Dimension getMaximumSize()
	{
		return new Dimension(perm.getPreferredSize().width,
				perm.getPreferredSize().height + srow.getPreferredSize().height + key.getPreferredSize().height);
	}
	
	public void arrange()
	{
		this.removeAll();
		
		//Box containerBox = Box.createVerticalBox();
		add(Box.createVerticalGlue());
		add(key);
		add(srow);
		//Box b = Box.createHorizontalBox();
		//for(SBoxShape shape: srow.getShapes())
		//{
		//	b.add(shape);
		//}
		//add(b);
		add(perm);
		add(Box.createVerticalGlue());
		
		/*
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		//add(Box.createVerticalGlue());
		add(key, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(srow, gbc);
		
		gbc.gridy = 2;
		add(perm, gbc);
		//add(Box.createVerticalGlue());
		*/
		perm.setVisible(true);
		
		//add(containerBox);
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		
		
		KeyShape k = new KeyShape(16);
		
		SBoxRow s = new SBoxRow(4,4);
		
		JFrame testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//BoxLayout bl = new BoxLayout(testFrame, BoxLayout.Y_AXIS);
		
		PermutationWeb p = new PermutationWeb(16, false);
		p.setVisible(true);
		//p.setHeaderOn(false);
		//p.setFooterOn(true);
		p.updateMappings(new int[]{0,4,8,12,1,5,9,13,2,6,10,14,3,7,11,15});
		
		
		//testFrame.setLayout(new GridLayout(0,1));
		//testFrame.setLayout(bl);
		//testFrame.add(k);
		//testFrame.add(s);
		//testFrame.add(p);
		testFrame.add(new RoundPanel(p,k,s));
		testFrame.pack();
		testFrame.setSize(300, 600);
		testFrame.setVisible(true);
		testFrame.revalidate();
	}
}
