package net.mjcarpenter.csci788.ui.message.help;

import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

@SuppressWarnings("serial")
public final class HelpMessage extends JFrame
{
	private final boolean loadedSuccessfully;
	
	public HelpMessage(String type)
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
		
		loadedSuccessfully = (urlToSet != null);
		
		add(jsp);
		setSize(800, 600);
		setMinimumSize(new Dimension(500, 400));
	}
	
	public boolean isLoadedSuccessfully()
	{
		return loadedSuccessfully;
	}
}
