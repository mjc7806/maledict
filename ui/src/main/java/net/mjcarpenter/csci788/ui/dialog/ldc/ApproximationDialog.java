package net.mjcarpenter.csci788.ui.dialog.ldc;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.mjcarpenter.csci788.crypto.spn.SBox;
import net.mjcarpenter.csci788.crypto.spn.SPNetwork;
import net.mjcarpenter.csci788.ui.component.CoordinateToggleButton;
import net.mjcarpenter.csci788.ui.message.help.HelpMessage;

@SuppressWarnings("serial")
public abstract class ApproximationDialog extends JDialog implements ActionListener
{
	protected SPNetwork   spn;
	protected CoordinateToggleButton[][] boxButtons;
	protected List<CoordinateToggleButton> allButtons;
	protected JButton jbOK, jbCancel, jbHelp;
	
	private   HelpMessage msg;
	
	public ApproximationDialog(SPNetwork network)
	{
		super();
		setModal(true);
		
		this.msg = null;
		this.spn = network;
		this.boxButtons = new CoordinateToggleButton[spn.getRounds().length][spn.getRounds()[0].getSBoxes().length];
		
		allButtons = new ArrayList<CoordinateToggleButton>();
		
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(boxButtons.length, boxButtons[0].length, 10, 10));
		gridPanel.setBorder(new EmptyBorder(10,10,10,10));
		
		for(int i=0; i<spn.getRounds().length-1; i++)
		{
			for(int j=0; j<spn.getRounds()[i].getSBoxes().length; j++)
			{
				boxButtons[i][j] = new CoordinateToggleButton(String.format("S-box (%d, %d)", i, j), i, j);
				boxButtons[i][j].addActionListener(this);
				
				gridPanel.add(boxButtons[i][j]);
				allButtons.add(boxButtons[i][j]);
			}
		}
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		JPanel btnSubPanel = new JPanel();
		
		jbOK = new JButton("OK");
		jbCancel = new JButton("Cancel");
		jbHelp = new JButton("Help");
		
		jbHelp.addActionListener(ae -> handleHelp());
		
		btnSubPanel.add(jbCancel);
		btnSubPanel.add(jbOK);
		buttonPanel.add(jbHelp, BorderLayout.WEST);
		buttonPanel.add(btnSubPanel, BorderLayout.EAST);
		buttonPanel.setBorder(new EmptyBorder(10,10,10,10));
		
		setLayout(new BorderLayout());
		add(gridPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		pack();
	}
	
	protected abstract void handleHelp();
	
	protected void handleHelp(String m)
	{
		if(msg == null)
		{
			msg = new HelpMessage(m, this,
					() ->
					{
						if(msg != null)
						{
							msg.dispose();
						}
						msg = null;
					});
			
			if(msg.isLoadedSuccessfully())
			{
				msg.setVisible(true);
			}
			else
			{
				msg.dispose();
				msg = null;
			}
		}
	}
	
	protected SBox getBoxForButton(CoordinateToggleButton btn)
	{
		return spn.getRounds()[btn.row].getSBoxes()[btn.col];
	}
}
