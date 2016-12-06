package net.mjcarpenter.maledict.ui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.thoughtworks.xstream.XStream;

import net.mjcarpenter.maledict.crypto.spn.SPNetwork;
import net.mjcarpenter.maledict.ui.dialog.component.SPNDefinitionDialog;
import net.mjcarpenter.maledict.ui.dialog.component.SPNVisualizationFrame;
import net.mjcarpenter.maledict.ui.util.MasterPropertiesCache;

@SuppressWarnings("serial")
public class NewOrLoadDialog extends JFrame implements ActionListener
{
	private JLabel  jlMessage;
	private JLabel  jlLogo;
	private JButton jbLoad, jbNew;
	
	public NewOrLoadDialog()
	{
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Save or Load");
		setLayout(new BorderLayout());
		
		jlLogo = new JLabel(new ImageIcon(ClassLoader.getSystemClassLoader().getResource("resource/img/logo.png")));
		
		jlMessage = new JLabel("Create a new SPN or load a previous one?");
		
		jbLoad = new JButton("Load");
		jbNew  = new JButton("New");
		
		jbLoad.addActionListener(this);
		jbNew.addActionListener(this);
		
		JPanel jpMessage = new JPanel(new BorderLayout());
		JPanel jpButtons = new JPanel();
		
		jpMessage.add(jlLogo,    BorderLayout.CENTER);
		jpMessage.add(jlMessage, BorderLayout.SOUTH);
		
		jpButtons.add(jbLoad);
		jpButtons.add(jbNew);
		
		add(jpMessage, BorderLayout.CENTER);
		add(jpButtons, BorderLayout.SOUTH);
		
		pack();
		setResizable(false); // No reason to resize this dialog.
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(jbNew))
		{
			MasterPropertiesDialog mpd = new MasterPropertiesDialog();
			
			if(mpd.isClosedSuccessful())
			{
				SwingUtilities.invokeLater(
						() ->
						{
							MasterPropertiesCache.getInstance().setSPN(
									SPNetwork.noop(
											MasterPropertiesCache.getInstance().getBlockSize(),
											MasterPropertiesCache.getInstance().getSBoxSize(),
											MasterPropertiesCache.getInstance().getNumRounds()));
							
							new SPNDefinitionDialog(MasterPropertiesCache.getInstance().getSPN());
						});
				
				dispose();
			}
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
				XStream xs = MasterPropertiesCache.getReadyXStream();
				
				try(InputStream fs = new FileInputStream(jfc.getSelectedFile()))
				{
					Object in = xs.fromXML(fs);
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
				SwingUtilities.invokeLater(
						() ->
						{
							MasterPropertiesCache.getInstance().setSPN(spnRef);
							new SPNDefinitionDialog(MasterPropertiesCache.getInstance().getSPN());
						});
				this.dispose();
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
