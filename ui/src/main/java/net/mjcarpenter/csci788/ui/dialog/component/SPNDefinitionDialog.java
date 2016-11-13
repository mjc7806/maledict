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
import javax.swing.JOptionPane;
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
import net.mjcarpenter.csci788.ui.util.MasterPropertiesCache;
import net.mjcarpenter.csci788.util.HexByteConverter;

public class SPNDefinitionDialog extends ComponentDefinitionDialog<SPNetwork> implements ActionListener, MouseListener
{
	private JMenu       jmFile;
	private JMenuItem   jmiSave;
	private JTree       spnTree;
	private ContextMenu<?> rightClickMenu;
	
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
		
		JScrollPane jsp = new JScrollPane(spnTree,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
	
	public static XStream getReadyXStream()
	{
		XStream xs = new XStream();
		xs.processAnnotations(SPNetwork.class);
		xs.processAnnotations(Round.class);
		xs.processAnnotations(Key.class);
		xs.registerLocalConverter(Key.class, "key", (Converter)(new HexByteConverter()));
		xs.processAnnotations(Permutation.class);
		xs.processAnnotations(SBox.class);
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
					pw.flush();
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	private class ContextMenu<T extends SPNComponent> extends JPopupMenu
	{
		private ComponentLeafNode<T> selectedComponent;
		private JMenuItem jmiEdit;
		private JMenuItem jmiStore;
		private JMenuItem jmiRestore;
		
		public ContextMenu(ComponentLeafNode<T> sc)
		{
			this.selectedComponent = sc;
			jmiEdit = new JMenuItem("Edit");
			add(jmiEdit);
			
			jmiEdit.addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent ae)
						{
							ComponentDefinitionDialog<T> selectedDialog = selectedComponent.editWithDialog();
							selectedComponent.replaceComponent(selectedDialog.component);
						}
					});
			
			jmiStore = new JMenuItem("Store Copy");
			add(jmiStore);
			
			jmiStore.addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent ae)
						{
							T componentToStore = selectedComponent.getComponent();
							String response = JOptionPane.showInputDialog("Insert name to store under.");
							response = (response == null) ? "" : response.trim();
							
							boolean storeSuccessful = false;
							
							if(componentToStore instanceof Key)
							{
								Key k = MasterPropertiesCache.getInstance().getNamedKey(response);
								
								if(k == null || !response.isEmpty())
								{
									MasterPropertiesCache.getInstance().saveNamedKey(response, (Key)componentToStore);
									storeSuccessful = true;
								}
							}
							else if(componentToStore instanceof Permutation)
							{
								Permutation p = MasterPropertiesCache.getInstance().getNamedPermutation(response);
								
								if(p == null || !response.isEmpty())
								{
									MasterPropertiesCache.getInstance().saveNamedPermutation(response, (Permutation)componentToStore);
									storeSuccessful = true;
								}
							}
							else if(componentToStore instanceof SBox)
							{
								SBox s = MasterPropertiesCache.getInstance().getNamedSBox(response);
								
								if(s == null || !response.isEmpty())
								{
									MasterPropertiesCache.getInstance().saveNamedSBox(response, (SBox)componentToStore);
									storeSuccessful = true;
								}
							}
							
							if(!storeSuccessful)
							{
								JOptionPane.showMessageDialog(null,
										"Cannot store component under that name.",
										"ERROR",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					});
			
			jmiRestore = new JMenuItem("Restore Named Copy");
			add(jmiRestore);
			
			jmiRestore.addActionListener(new ActionListener()
			{
				@SuppressWarnings("unchecked")
				@Override
				public void actionPerformed(ActionEvent ae)
				{
					T componentToRestore = selectedComponent.getComponent();
					String response = JOptionPane.showInputDialog("Insert name to restore from.");
					response = (response == null) ? "" : response.trim();
					
					boolean retoreSuccessful = false;
					
					if(componentToRestore instanceof Key)
					{
						Key k = MasterPropertiesCache.getInstance().getNamedKey(response);
						
						if(k == null || !response.isEmpty())
						{
							selectedComponent.replaceComponent((T)k);
							retoreSuccessful = true;
						}
					}
					else if(componentToRestore instanceof Permutation)
					{
						Permutation p = MasterPropertiesCache.getInstance().getNamedPermutation(response);
						
						if(p == null || !response.isEmpty())
						{
							selectedComponent.replaceComponent((T)p);
							retoreSuccessful = true;
						}
					}
					else if(componentToRestore instanceof SBox)
					{
						SBox s = MasterPropertiesCache.getInstance().getNamedSBox(response);
						
						if(s == null || !response.isEmpty())
						{
							selectedComponent.replaceComponent((T)s);
							retoreSuccessful = true;
						}
					}
					
					if(!retoreSuccessful)
					{
						JOptionPane.showMessageDialog(null,
								"Cannot restore component from that name.",
								"ERROR",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}
	}
}
