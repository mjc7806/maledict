package net.mjcarpenter.csci788.ui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;

import net.mjcarpenter.csci788.crypto.spn.Key;
import net.mjcarpenter.csci788.crypto.spn.Permutation;
import net.mjcarpenter.csci788.crypto.spn.Round;
import net.mjcarpenter.csci788.crypto.spn.SBox;
import net.mjcarpenter.csci788.crypto.spn.SPNetwork;
import net.mjcarpenter.csci788.ui.dialog.component.SPNDefinitionDialog;
import net.mjcarpenter.csci788.ui.dialog.component.SPNVisualizationFrame;
import net.mjcarpenter.csci788.ui.util.MasterPropertiesCache;
import net.mjcarpenter.csci788.util.HexByteConverter;

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
			SPNetwork spn = null;
			
			JFileChooser jfc = new JFileChooser();
			jfc.setCurrentDirectory(new File(System.getProperty("user.home")));
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jfc.setFileFilter(new FileNameExtensionFilter("XML file", "xml"));
			int res = jfc.showOpenDialog(this);
			if(res == JFileChooser.APPROVE_OPTION)
			{
				XStream xs = SPNDefinitionDialog.getReadyXStream();
				
				try(ObjectInputStream ois = xs.createObjectInputStream(new FileInputStream(jfc.getSelectedFile())))
				{
					Object in = xs.fromXML(ois);
					if(in instanceof SPNetwork)
					{
						spn = (SPNetwork)in;
					}
				}
				catch (IOException ioe)
				{
					spn = null;
				}
			}
			else if(res == JFileChooser.CANCEL_OPTION)
			{
				return;
			}
			
			if(spn != null)
			{
				final SPNetwork spnRef = spn;
				SwingUtilities.invokeLater(new Runnable()
						{
							@Override
							public void run()
							{
								MasterPropertiesCache.getInstance().setSPN(spnRef);
								
								SPNVisualizationFrame frm = new SPNVisualizationFrame(MasterPropertiesCache.getInstance().getSPN());
								MasterPropertiesCache.getInstance().setVisualizationFrame(frm);
								new SPNDefinitionDialog(MasterPropertiesCache.getInstance().getSPN());
							}
						});
			}
			else
			{
				JOptionPane.showMessageDialog(this,
						"There was an error loading an SPN from this file.",
						"ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
