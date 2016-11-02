package net.mjcarpenter.csci788.ui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NewOrLoadDialog extends JDialog implements ActionListener
{
	private JLabel  jlMessage;
	private JButton jbLoad, jbNew;
	
	public NewOrLoadDialog()
	{
		super();
		setTitle("Save or Load");
		setLayout(new BorderLayout());
		
		jlMessage = new JLabel("Create a new SPN or load a previous one?");
		
		jbLoad = new JButton("Load");
		jbNew  = new JButton("New");
		
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
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
