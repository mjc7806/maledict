package net.mjcarpenter.csci788.ui.geom;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
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
		
		add(Box.createVerticalGlue());
		add(key);
		add(srow);
		add(perm);
		add(Box.createVerticalGlue());
		
		perm.setVisible(true);
		
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		KeyShape k = new KeyShape(16);
		
		SBoxRow s = new SBoxRow(4,4);
		
		JFrame testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		PermutationWeb p = new PermutationWeb(16, false);
		p.setVisible(true);
		p.updateMappings(new int[]{0,4,8,12,1,5,9,13,2,6,10,14,3,7,11,15});
		
		testFrame.add(new RoundPanel(p,k,s));
		testFrame.pack();
		testFrame.setSize(300, 600);
		testFrame.setVisible(true);
		testFrame.revalidate();
	}
}
