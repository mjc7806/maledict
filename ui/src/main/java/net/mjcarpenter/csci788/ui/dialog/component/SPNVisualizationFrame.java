package net.mjcarpenter.csci788.ui.dialog.component;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.mjcarpenter.csci788.crypto.spn.SPNetwork;
import net.mjcarpenter.csci788.ui.geom.KeyShape;
import net.mjcarpenter.csci788.ui.geom.PermutationWeb;
import net.mjcarpenter.csci788.ui.geom.RoundPanel;
import net.mjcarpenter.csci788.ui.geom.SBoxRow;

public class SPNVisualizationFrame extends JFrame
{
	private SPNetwork spn;
	private RoundPanel[] rounds;
	
	public SPNVisualizationFrame(SPNetwork spn)
	{
		super("SPN Visualization");
		setSPN(spn);
		setSize(400,900);
		setResizable(false);
		
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
		this.removeAll();
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
			
			add(rounds[i]);
			rounds[i].setVisible(true);
		}
		
		JScrollPane jsp = new JScrollPane(panel);
		add(jsp);
	}
}
