package net.mjcarpenter.csci788.ui.dialog.component;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.mjcarpenter.csci788.crypto.spn.SPNetwork;
import net.mjcarpenter.csci788.ui.geom.KeyShape;
import net.mjcarpenter.csci788.ui.geom.PermutationWeb;
import net.mjcarpenter.csci788.ui.geom.RoundPanel;
import net.mjcarpenter.csci788.ui.geom.SBoxRow;

@SuppressWarnings("serial")
public class SPNVisualizationFrame extends JFrame
{
	private SPNetwork spn;
	private RoundPanel[] rounds;
	
	public SPNVisualizationFrame(SPNetwork spn)
	{
		super("SPN Visualization");
		this.spn = spn;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		setSize(400,900);
		
		createPanel();
		
		setVisible(true);
	}
	
	public void setSPN(SPNetwork spn)
	{
		this.spn = spn;
		
		createPanel();
		revalidate();
	}
	
	public void createPanel()
	{
		this.revalidate();
		
		this.rounds = new RoundPanel[spn.getRounds().length];
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1));
		
		for(int i=0; i<rounds.length; i++)
		{
			PermutationWeb perm = new PermutationWeb(spn.getBlockSize(), false);
			perm.setHeaderOn(i == 0);
			perm.setFooterOn(i == rounds.length-1);
			perm.updateMappings(spn.getRounds()[i].getPermutation().getMapping());
			perm.setVisible(true);
			
			rounds[i] = new RoundPanel(
					perm,
					new KeyShape(spn.getBlockSize()),
					new SBoxRow(spn.getRounds()[i].getSBoxes().length, spn.getRounds()[i].getSBoxes()[0].bitSize()));
			
			panel.add(rounds[i]);
			rounds[i].setVisible(true);
		}
		
		panel.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				//int height = getHeight();
				int width = getWidth();
				
				panel.setPreferredSize(new Dimension(width, rounds[0].getHeight()*rounds.length));
				
				panel.revalidate();
				panel.repaint();
				revalidate();
				repaint();
			}
		});
		
		//add(new JButton("TEST"));
		
		//JScrollPane jsp = new JScrollPane(panel);
		add(panel);
		this.pack();
	}
	
	public static void main(String[] args)
	{
		new SPNVisualizationFrame(SPNetwork.noop(16, 4, 5));
	}
}
