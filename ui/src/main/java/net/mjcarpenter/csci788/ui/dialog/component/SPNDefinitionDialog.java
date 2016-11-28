package net.mjcarpenter.csci788.ui.dialog.component;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.TreeSelectionModel;

import com.thoughtworks.xstream.XStream;

import net.mjcarpenter.csci788.crypto.spn.Key;
import net.mjcarpenter.csci788.crypto.spn.Permutation;
import net.mjcarpenter.csci788.crypto.spn.SBox;
import net.mjcarpenter.csci788.crypto.spn.SPNComponent;
import net.mjcarpenter.csci788.crypto.spn.SPNetwork;
import net.mjcarpenter.csci788.ui.component.DummyFrame;
import net.mjcarpenter.csci788.ui.dialog.ldc.DifferentialApproximationDialog;
import net.mjcarpenter.csci788.ui.dialog.ldc.LinearApproximationDialog;
import net.mjcarpenter.csci788.ui.model.ComponentLeafNode;
import net.mjcarpenter.csci788.ui.model.RoundTreeNode;
import net.mjcarpenter.csci788.ui.model.SPNTreeModel;
import net.mjcarpenter.csci788.ui.model.SPNTreeNode;
import net.mjcarpenter.csci788.ui.util.MasterPropertiesCache;

@SuppressWarnings("serial")
public class SPNDefinitionDialog extends ComponentDefinitionDialog<SPNetwork> implements ActionListener
{
	private JMenu       jmFile, jmAnalyze;
	private JMenuItem   jmiSave, jmiLinear, jmiDiff;
	private JTree       spnTree;
	private ContextMenu<?> rightClickMenu;
	
	public SPNDefinitionDialog(SPNetwork component)
	{
		super(new DummyFrame("SPN"), "SPN", component);
		setModal(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		
		JMenuBar jmb = new JMenuBar();
		jmFile  = new JMenu("File");
		jmFile.setMnemonic('F');
		jmAnalyze = new JMenu("Analyze");
		jmAnalyze.setMnemonic('A');
		
		jmiSave = new JMenuItem("Save");
		jmiSave.setMnemonic('S');
		jmiSave.addActionListener(this);
		jmiLinear = new JMenuItem("Linear");
		jmiLinear.setMnemonic('L');
		jmiLinear.addActionListener(this);
		jmiDiff = new JMenuItem("Differential");
		jmiDiff.setMnemonic('D');
		jmiDiff.addActionListener(this);
		
		jmFile.add(jmiSave);
		jmAnalyze.add(jmiLinear);
		jmAnalyze.add(jmiDiff);
		
		jmb.add(jmFile);
		jmb.add(jmAnalyze);
		
		
		setJMenuBar(jmb);
		
		spnTree = new JTree();
		spnTree.setModel(new SPNTreeModel(component));
		for(int i=0; i<spnTree.getRowCount(); i++)
		{
			spnTree.expandRow(i);
		}
		spnTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		spnTree.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent arg0)
					{
						if(SwingUtilities.isRightMouseButton(arg0))
						{
							int row = spnTree.getClosestRowForLocation(arg0.getX(), arg0.getY());
							spnTree.setSelectionRow(row);
							Object selected = spnTree.getLastSelectedPathComponent();
							
							if(selected instanceof ComponentLeafNode)
							{
								@SuppressWarnings("unchecked")
								ComponentLeafNode<SPNComponent> cln = (ComponentLeafNode<SPNComponent>)selected;
								
								boolean editAllowed = true;
								
								// The last round must be key-only, so only allow the subkey to be altered.
								if(((RoundTreeNode)cln.getParent()).indexOnParent() == getRootNode().getChildCount()-1)
								{
									editAllowed = cln.getTypeClass().equals(Key.class);
								}
								
								if(editAllowed)
								{
									rightClickMenu = new ContextMenu<SPNComponent>(cln, cln.getTypeClass());
									rightClickMenu.show(arg0.getComponent(), arg0.getX(), arg0.getY());
								}
							}
						}
					}
				});
		
		JScrollPane jsp = new JScrollPane(spnTree,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(jsp, BorderLayout.CENTER);
		
		pack();
		setVisible(true);
	}

	@Override
	public boolean validateComponent()
	{
		return true;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if(arg0.getSource().equals(jmiSave))
		{
			JFileChooser jfc = new JFileChooser();
			jfc.setCurrentDirectory(new File(System.getProperty("user.home")));
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jfc.setFileFilter(new FileNameExtensionFilter("XML file", "xml"));
			
			int option = jfc.showSaveDialog(this);
			if(option == JFileChooser.APPROVE_OPTION)
			{
				XStream xs = MasterPropertiesCache.getReadyXStream();
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
		else if(arg0.getSource().equals(jmiLinear))
		{
			LinearApproximationDialog linDlg = new LinearApproximationDialog(getRootNode().getComponent());
		}
		else if(arg0.getSource().equals(jmiDiff))
		{
			DifferentialApproximationDialog diffDlg = new DifferentialApproximationDialog(getRootNode().getComponent());
		}
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		
		// JDialogs cannot be EXIT_ON_CLOSE, but since all ComponentDefinitionDialogs
		// are JDialogs by definition, we need a way to ensure that the application
		// closes when this particular window does.
		System.exit(0);
	}
	
	private SPNTreeNode getRootNode()
	{
		return (SPNTreeNode)(((SPNTreeModel)spnTree.getModel()).getRoot());
	}
	
	private class ContextMenu<T extends SPNComponent> extends JPopupMenu
	{
		private ComponentLeafNode<T> selectedComponent;
		private JMenuItem jmiEdit;
		private JMenuItem jmiStore;
		private JMenuItem jmiRestore;
		
		public ContextMenu(ComponentLeafNode<T> sc, Class<T> clazz)
		{
			this.selectedComponent = sc;
			jmiEdit = new JMenuItem("Edit");
			add(jmiEdit);
			
			jmiEdit.addActionListener(ae ->
					{
						ComponentDefinitionDialog<T> selectedDialog = selectedComponent.editWithDialog();
						selectedComponent.replaceComponent(selectedDialog.component);
					});
			
			jmiStore = new JMenuItem("Store Copy");
			add(jmiStore);
			
			jmiStore.addActionListener(ae ->
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
					});
			
			jmiRestore = new JMenuItem("Restore Named Copy");
			add(jmiRestore);
			
			jmiRestore.addActionListener(ae ->
			{
				String response = JOptionPane.showInputDialog("Insert name to restore from.");
				response = (response == null) ? "" : response.trim();
				
				boolean retoreSuccessful = false;
				
				if(clazz.equals(Key.class))
				{
					Key k = MasterPropertiesCache.getInstance().getNamedKey(response);
					
					if(k == null || !response.isEmpty())
					{
						selectedComponent.replaceComponent(clazz.cast(k));
						retoreSuccessful = true;
					}
				}
				else if(clazz.equals(Permutation.class))
				{
					Permutation p = MasterPropertiesCache.getInstance().getNamedPermutation(response);
					
					if(p == null || !response.isEmpty())
					{
						selectedComponent.replaceComponent(clazz.cast(p));
						retoreSuccessful = true;
					}
				}
				else if(clazz.equals(SBox.class))
				{
					SBox s = MasterPropertiesCache.getInstance().getNamedSBox(response);
					
					if(s == null || !response.isEmpty())
					{
						selectedComponent.replaceComponent(clazz.cast(s));
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
			});
		}
	}
}
