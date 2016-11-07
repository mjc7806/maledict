package net.mjcarpenter.csci788.ui.geom;

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
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
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
	
	public void arrange()
	{
		this.removeAll();
		
		add(Box.createVerticalGlue());
		add(key);
		add(srow);
		add(perm);
		add(Box.createVerticalGlue());
		
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		PermutationWeb p = new PermutationWeb(4, true);
		p.setHeaderOn(false);
		p.setFooterOn(true);
		
		KeyShape k = new KeyShape(4);
		
		SBoxRow s = new SBoxRow(4,1);
		
		JFrame testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		testFrame.add(new RoundPanel(p,k,s));
		testFrame.pack();
		testFrame.setVisible(true);
	}
}
