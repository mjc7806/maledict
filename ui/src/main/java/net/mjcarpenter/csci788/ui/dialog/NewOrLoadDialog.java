package net.mjcarpenter.csci788.ui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.mjcarpenter.csci788.crypto.spn.SPNetwork;
import net.mjcarpenter.csci788.ui.dialog.component.SPNDefinitionDialog;
import net.mjcarpenter.csci788.ui.util.MasterPropertiesCache;

public class NewOrLoadDialog extends JFrame implements ActionListener
{
	private JLabel  jlMessage;
	private JButton jbLoad, jbNew;
	
	public NewOrLoadDialog()
	{
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Save or Load");
		setLayout(new BorderLayout());
		
		jlMessage = new JLabel("Create a new SPN or load a previous one?");
		
		jbLoad = new JButton("Load");
		jbNew  = new JButton("New");
		
		jbLoad.addActionListener(this);
		jbNew.addActionListener(this);
		
		JPanel jpMessage = new JPanel();
		JPanel jpButtons = new JPanel();
		
		jpMessage.add(jlMessage);
		
		jpButtons.add(jbLoad);
		jpButtons.add(jbNew);
		
		add(jpMessage, BorderLayout.CENTER);
		add(jpButtons, BorderLayout.SOUTH);
		
		pack();
		setResizable(false); // No reason to resize this dialog.
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new NewOrLoadDialog();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(jbNew))
		{
			new MasterPropertiesDialog();
			
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					MasterPropertiesCache.getInstance().setSPN(
							SPNetwork.noop(
									MasterPropertiesCache.getInstance().getBlockSize(),
									MasterPropertiesCache.getInstance().getSBoxSize(),
									MasterPropertiesCache.getInstance().getNumRounds()));
					
					new SPNDefinitionDialog(MasterPropertiesCache.getInstance().getSPN());
				}
			});
			
			dispose();
		}
		else if(e.getSource().equals(jbLoad))
		{
			
		}
	}
}
