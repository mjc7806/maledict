package net.mjcarpenter.csci788.ui.dialog.component;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.TreeSelectionModel;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.SingleValueConverter;

import net.mjcarpenter.csci788.crypto.spn.Key;
import net.mjcarpenter.csci788.crypto.spn.Permutation;
import net.mjcarpenter.csci788.crypto.spn.Round;
import net.mjcarpenter.csci788.crypto.spn.SBox;
import net.mjcarpenter.csci788.crypto.spn.SPNComponent;
import net.mjcarpenter.csci788.crypto.spn.SPNetwork;
import net.mjcarpenter.csci788.ui.model.ComponentLeafNode;
import net.mjcarpenter.csci788.ui.model.SPNTreeModel;
import net.mjcarpenter.csci788.util.HexByteConverter;

public class SPNDefinitionDialog extends ComponentDefinitionDialog<SPNetwork> implements ActionListener, MouseListener
{
	private JMenu       jmFile;
	private JMenuItem   jmiSave;
	private SPNetwork   component;
	private JTree       spnTree;
	private ContextMenu rightClickMenu;
	
	public SPNDefinitionDialog(SPNetwork component)
	{
		super(component);
		setTitle("SPN");
		setLayout(new BorderLayout());
		
		JMenuBar jmb = new JMenuBar();
		jmFile  = new JMenu("File");
		jmFile.setMnemonic('F');
		jmiSave = new JMenuItem("Save");
		jmiSave.setMnemonic('S');
		jmiSave.addActionListener(this);
		jmFile.add(jmiSave);
		jmb.add(jmFile);
		setJMenuBar(jmb);
		
		spnTree = new JTree();
		spnTree.setModel(new SPNTreeModel(component));
		for(int i=0; i<spnTree.getRowCount(); i++)
		{
			spnTree.expandRow(i);
		}
		spnTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		spnTree.addMouseListener(this);
		
		JScrollPane jsp = new JScrollPane();
		jsp.add(spnTree);
		add(jsp, BorderLayout.CENTER);
		//add(spnTree);
		pack();
		setVisible(true);
	}

	@Override
	public boolean validateComponent() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static void main(String[] args)
	{
		new SPNDefinitionDialog(sampleNetwork());
	}
	
	private static SPNetwork sampleNetwork()
	{
		Permutation first3Rounds = new Permutation(0,4,8,12,1,5,9,13,2,6,10,14,3,7,11,15);
		Permutation last2Rounds  = Permutation.noop(16);
		SBox allSBoxes = new SBox(0xE, 0x4, 0xD, 0x1, 0x2, 0xF, 0xB, 0x8, 0x3, 0xA, 0x6, 0xC, 0x5, 0x9, 0x0, 0x7);
		SBox straightThru = SBox.noop(16);
		
		// key = 609a 4029 f06b 9021 009e
		Key key1 = new Key(new byte[]{(byte)0x60,(byte)0x9a});
		Key key2 = new Key(new byte[]{(byte)0x40,(byte)0x29});
		Key key3 = new Key(new byte[]{(byte)0xf0,(byte)0x6b});
		Key key4 = new Key(new byte[]{(byte)0x90,(byte)0x21});
		Key key5 = new Key(new byte[]{(byte)0x00,(byte)0x9e});
		
		Round round1 = new Round(16, key1, first3Rounds, allSBoxes, allSBoxes, allSBoxes, allSBoxes);
		Round round2 = new Round(16, key2, first3Rounds, allSBoxes, allSBoxes, allSBoxes, allSBoxes);
		Round round3 = new Round(16, key3, first3Rounds, allSBoxes, allSBoxes, allSBoxes, allSBoxes);
		
		// Semifinal round just key and SBox
		Round round4 = new Round(16, key4, first3Rounds,  allSBoxes, allSBoxes, allSBoxes, allSBoxes);
		
		// Last round just key.
		Round round5 = new Round(16, key5, last2Rounds,  straightThru, straightThru, straightThru, straightThru);
		
		return new SPNetwork(16, new Round[]{round1, round2, round3, round4, round5});
	}
	
	
	public static XStream getReadyXStream()
	{
		XStream xs = new XStream();
		xs.processAnnotations(Key.class);
		xs.registerLocalConverter(Key.class, "key", (Converter)(new HexByteConverter()));
		xs.processAnnotations(Permutation.class);
		xs.processAnnotations(SBox.class);
		xs.processAnnotations(Round.class);
		xs.processAnnotations(SPNetwork.class);
		return xs;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if(arg0.getSource().equals(jmiSave))
		{
			JFileChooser jfc = new JFileChooser();
			jfc.setCurrentDirectory(new File(System.getProperty("user.home")));
			
			int option = jfc.showSaveDialog(this);
			if(option == JFileChooser.APPROVE_OPTION)
			{
				XStream xs = getReadyXStream();
				SPNetwork spn = (component);
				
				try(PrintWriter pw = new PrintWriter(jfc.getSelectedFile()))
				{
					xs.toXML(spn, pw);
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		if(SwingUtilities.isRightMouseButton(arg0))
		{
			int row = spnTree.getClosestRowForLocation(arg0.getX(), arg0.getY());
			spnTree.setSelectionRow(row);
			Object selected = spnTree.getLastSelectedPathComponent();
			
			if(selected instanceof ComponentLeafNode<?>)
			{
				rightClickMenu = new ContextMenu((ComponentLeafNode<?>)selected);
				rightClickMenu.show(arg0.getComponent(), arg0.getX(), arg0.getY());
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings("serial")
	private class ContextMenu extends JPopupMenu
	{
		private ComponentLeafNode<?> selectedComponent;
		private JMenuItem jmiEdit;
		
		public ContextMenu(ComponentLeafNode<?> sc)
		{
			this.selectedComponent = sc;
			jmiEdit = new JMenuItem("Edit");
			add(jmiEdit);
			
			jmiEdit.addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent ae)
						{
							ComponentDefinitionDialog<?> selectedDialog = selectedComponent.editWithDialog();
						}
					});
		}
	}
}
