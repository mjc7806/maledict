package net.mjcarpenter.csci788.ui.geom;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

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
		
		setLayout(new GridBagLayout());
		
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
		
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		PermutationWeb p = new PermutationWeb(16, false);
		//p.setHeaderOn(false);
		//p.setFooterOn(true);
		
		KeyShape k = new KeyShape(16);
		
		SBoxRow s = new SBoxRow(4,4);
		
		JFrame testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testFrame.add(new RoundPanel(p,k,s));
		testFrame.pack();
		testFrame.setVisible(true);
	}
}
