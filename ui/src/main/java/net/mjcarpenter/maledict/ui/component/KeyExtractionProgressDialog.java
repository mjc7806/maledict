/*
 * Maledict - An Interactive Tool for Learning Linear and Differential Cryptanalysis of SPNs
 * Copyright (C) 2016  Mike Carpenter
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.mjcarpenter.maledict.ui.component;

import java.awt.Dialog;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import net.mjcarpenter.maledict.crypto.ldc.AbstractKeyBiasExtractor;

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
