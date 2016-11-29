package net.mjcarpenter.csci788.ui.component;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")
public final class KeyExtractionProgressDialog extends JDialog
{
	private JProgressBar keyBar;
	private JProgressBar pairBar;
	private JButton      jbOK;
	
	
	public KeyExtractionProgressDialog(Dialog parent)
	{
		super(parent, "Key Extraction Progress");
		setModal(true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setLayout(new GridLayout(3,1,10,10));
		
		keyBar = new JProgressBar();
		keyBar.setStringPainted(true);
		pairBar = new JProgressBar();
		pairBar.setStringPainted(true);
		
		jbOK = new JButton("OK");
		jbOK.setMnemonic('O');
		jbOK.setEnabled(false);
		jbOK.addActionListener(ae ->
				{
					dispose();
				});
		
		setSize(300,100);
		
		add(keyBar);
		add(pairBar);
		add(jbOK);
	}

	public void progress(int keyProg, int keyMax, int pairProg, int pairMax)
	{
		keyBar.setMaximum(keyMax);
		keyBar.setValue(keyProg);
		keyBar.setString(String.format("Key %d/%d", keyProg, keyMax));
		
		pairBar.setMaximum(pairMax);
		pairBar.setValue(pairProg);
		pairBar.setString(String.format("Pair %d/%d", pairProg, pairMax));
		
		jbOK.setEnabled(keyProg == keyMax && pairProg == pairMax);
	}
}
