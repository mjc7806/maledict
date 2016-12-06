package net.mjcarpenter.csci788.ui.component;

import java.awt.Dialog;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import net.mjcarpenter.csci788.crypto.ldc.AbstractKeyBiasExtractor;

@SuppressWarnings("serial")
public final class KeyExtractionProgressDialog extends JDialog
{
	private AbstractKeyBiasExtractor<?> akbe;
	private JProgressBar keyBar, pairBar;
	private JButton      jbOK, jbCancel;
	
	
	public KeyExtractionProgressDialog(Dialog parent, AbstractKeyBiasExtractor<?> akbe)
	{
		super(parent, "Key Extraction Progress");
		setModal(true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setLayout(new GridLayout(3,1,10,10));
		
		this.akbe = akbe;
		
		keyBar = new JProgressBar();
		keyBar.setStringPainted(true);
		pairBar = new JProgressBar();
		pairBar.setStringPainted(true);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,2,5,5));
		
		jbOK = new JButton("OK");
		jbOK.setMnemonic('O');
		jbOK.setEnabled(false);
		jbOK.addActionListener(ae ->
				{
					dispose();
				});
		
		jbCancel = new JButton("Cancel");
		jbCancel.setMnemonic('C');
		jbCancel.setEnabled(true);
		jbCancel.addActionListener(ae ->
				{
					if(this.akbe != null)
					{
						this.akbe.cancel();
						dispose();
					}
				});
		
		setSize(300,100);
		
		add(keyBar);
		add(pairBar);
		buttonPanel.add(jbCancel);
		buttonPanel.add(jbOK);
		add(buttonPanel);
	}

	public void progress(int keyProg, int keyMax, int pairProg, int pairMax)
	{
		keyBar.setMaximum(keyMax);
		keyBar.setValue(keyProg);
		keyBar.setString(String.format("Key %d/%d", keyProg, keyMax));
		
		pairBar.setMaximum(pairMax);
		pairBar.setValue(pairProg);
		pairBar.setString(String.format("Pair %d/%d", pairProg, pairMax));
		
		boolean finished = keyProg == keyMax && pairProg == pairMax;
		
		jbOK.setEnabled(finished);
		jbCancel.setEnabled(!finished);
	}
}
