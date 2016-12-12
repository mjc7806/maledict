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
package net.mjcarpenter.maledict.ui.dialog.component;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.imageio.ImageIO;
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
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.bind.DatatypeConverter;

import com.thoughtworks.xstream.XStream;

import net.mjcarpenter.maledict.crypto.ldc.AbstractApproximation;
import net.mjcarpenter.maledict.crypto.ldc.AbstractKeyBiasExtractor;
import net.mjcarpenter.maledict.crypto.ldc.DifferentialApproximation;
import net.mjcarpenter.maledict.crypto.ldc.DifferentialKeyBiasExtractor;
import net.mjcarpenter.maledict.crypto.ldc.LinearApproximation;
import net.mjcarpenter.maledict.crypto.ldc.LinearKeyBiasExtractor;
import net.mjcarpenter.maledict.crypto.spn.ChosenPair;
import net.mjcarpenter.maledict.crypto.spn.Key;
import net.mjcarpenter.maledict.crypto.spn.KnownPair;
import net.mjcarpenter.maledict.crypto.spn.Permutation;
import net.mjcarpenter.maledict.crypto.spn.SBox;
import net.mjcarpenter.maledict.crypto.spn.SPNComponent;
import net.mjcarpenter.maledict.crypto.spn.SPNetwork;
import net.mjcarpenter.maledict.reports.CryptanalysisReport;
import net.mjcarpenter.maledict.ui.component.DummyFrame;
import net.mjcarpenter.maledict.ui.component.KeyExtractionProgressDialog;
import net.mjcarpenter.maledict.ui.dialog.ldc.ApproximationDialog;
import net.mjcarpenter.maledict.ui.dialog.ldc.DifferentialApproximationDialog;
import net.mjcarpenter.maledict.ui.dialog.ldc.LinearApproximationDialog;
import net.mjcarpenter.maledict.ui.model.ComponentLeafNode;
import net.mjcarpenter.maledict.ui.model.RoundTreeNode;
import net.mjcarpenter.maledict.ui.model.SPNTreeModel;
import net.mjcarpenter.maledict.ui.model.SPNTreeNode;
import net.mjcarpenter.maledict.ui.util.MasterPropertiesCache;

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
			processKeyBiasExtraction(LinearApproximation.class);
		}
		else if(arg0.getSource().equals(jmiDiff))
		{
			processKeyBiasExtraction(DifferentialApproximation.class);
		}
	}
	
	private void processKeyBiasExtraction(Class<? extends AbstractApproximation> clz)
	{
		AbstractApproximation appx                 = null;
		ApproximationDialog appxDlg                = null;
		AbstractKeyBiasExtractor<?> kbe            = null;
		KeyExtractionProgressDialog progDlg        = null;
		SwingWorker<Map<Key, Double>, Void> worker = null;
		
		if(LinearApproximation.class.equals(clz))
		{
			appxDlg = new LinearApproximationDialog(component);
			if(appxDlg.isSuccessful())
			{
				appx = appxDlg.getCipherApproximation();
				kbe  = new LinearKeyBiasExtractor(component.getRounds()[appxDlg.getLastRow()+1], (LinearApproximation)appx);
				progDlg = new KeyExtractionProgressDialog(this, kbe);
				
				final KeyExtractionProgressDialog finProg = progDlg;
				final LinearKeyBiasExtractor lkbe = (LinearKeyBiasExtractor)kbe;
				worker = new SwingWorker<Map<Key, Double>, Void>()
				{
					@Override
					protected Map<Key, Double> doInBackground() throws Exception
					{
						lkbe.generateBiases(
								KnownPair.generatePairs(10000, component),
								(keyProg, keyMax, pairProg, pairMax) ->
								{
									finProg.progress(keyProg, keyMax, pairProg, pairMax);
								});
						
						return lkbe.getBiasMap();
					}
				};
			}
		}
		else if(DifferentialApproximation.class.equals(clz))
		{
			appxDlg = new DifferentialApproximationDialog(component);
			if(appxDlg.isSuccessful())
			{
				appx = appxDlg.getCipherApproximation();
				kbe  = new DifferentialKeyBiasExtractor(component.getRounds()[appxDlg.getLastRow()+1], (DifferentialApproximation)appx);
				progDlg = new KeyExtractionProgressDialog(this, kbe);
				
				final KeyExtractionProgressDialog finProg = progDlg;
				final DifferentialApproximation dappx   = (DifferentialApproximation)appx;
				final DifferentialKeyBiasExtractor dkbe = (DifferentialKeyBiasExtractor)kbe;
				worker = new SwingWorker<Map<Key, Double>, Void>()
				{
					@Override
					protected Map<Key, Double> doInBackground() throws Exception
					{
						dkbe.generateBiases(ChosenPair.generatePairs(5000, dappx.getPlaintextMask(), component),
								(keyProg, keyMax, pairProg, pairMax) ->
								{
									finProg.progress(keyProg, keyMax, pairProg, pairMax);
								});
						
						return dkbe.getBiasMap();
					}
				};
			}
		}
		else
		{
			return;
		}
		
		
		if(appxDlg.isSuccessful())
		{
			worker.execute();
			progDlg.setVisible(true);
			
			if(!kbe.isCanceled())
			{
				int ans = JOptionPane.showConfirmDialog(this,
						String.format("Found target partial subkey [%s] with bias [%.6f]\n\nSave full report?",
								DatatypeConverter.printHexBinary(kbe.getMaxBiasKey().getKeyValue()),
								kbe.getMaxBiasValue()),
						"Results Available",
						JOptionPane.YES_NO_OPTION);
				
				
				if(ans == JOptionPane.YES_OPTION)
				{					
					CryptanalysisReport cryptRpt = new CryptanalysisReport(appx, kbe, appxDlg.reportActiveSBoxes());
					
					JFileChooser choose = new JFileChooser(System.getProperty("user.home"));
					choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int select = choose.showSaveDialog(this);
					
					if(select == JFileChooser.APPROVE_OPTION)
					{
						File chosen = choose.getSelectedFile();
						if(chosen.isDirectory())
						{
							try
							{
								File imgFile = new File(chosen.getPath() + File.separator + "spnImg.png");
										
								ImageIO.write(MasterPropertiesCache.getInstance().getVisualizationFrame().visualizationImage(),
										"png", imgFile);
							}
							catch(IOException ioe)
							{
								
							}
							
							try(PrintWriter pw = new PrintWriter(chosen.getPath() + File.separator + "cryptRpt.html"))
							{
								pw.print(cryptRpt.getFullReportAsString());
								pw.flush();
							}
							catch(IOException ioe)
							{
								
							}
						}
					}
				}
			}
		}
		
		MasterPropertiesCache.getInstance().clearVisualizationColoring();
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
