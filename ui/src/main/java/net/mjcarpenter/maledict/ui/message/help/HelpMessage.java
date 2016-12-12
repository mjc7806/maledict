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
package net.mjcarpenter.maledict.ui.message.help;

import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

@SuppressWarnings("serial")
public final class HelpMessage extends JFrame
{
	private final Window  parent;
	private final boolean loadedSuccessfully;
	
	public HelpMessage(String type, Window parent, HelpMessageCallback cb)
	{
		super();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		
		JTextPane contentPane = new JTextPane();
		JScrollPane jsp = new JScrollPane(contentPane);
		
		URL urlToSet = null;
		try
		{
			urlToSet = HelpMessageConstants.getResource(type);
			contentPane.setPage(urlToSet);
		}
		catch(IOException ioe)
		{
			urlToSet = null;
		}
		
		this.loadedSuccessfully = (urlToSet != null);
		this.parent = parent;
		
		addWindowListener(new WindowAdapter()
				{
					@Override
					public void windowClosed(WindowEvent we)
					{
						cb.callback();
					}
				});
		
		add(jsp);
		setSize(800, 600);
		setMinimumSize(new Dimension(500, 400));
		setLocation(this.parent.getLocation().x + this.parent.getWidth() + 20, this.parent.getLocation().y);
	}
	
	public boolean isLoadedSuccessfully()
	{
		return loadedSuccessfully;
	}
}
