package net.mjcarpenter.csci788.ui.dialog.component;

import javax.swing.JScrollPane;
import javax.swing.JTree;

import net.mjcarpenter.csci788.crypto.spn.Key;
import net.mjcarpenter.csci788.crypto.spn.Permutation;
import net.mjcarpenter.csci788.crypto.spn.Round;
import net.mjcarpenter.csci788.crypto.spn.SBox;
import net.mjcarpenter.csci788.crypto.spn.SPNetwork;
import net.mjcarpenter.csci788.ui.model.SPNTreeModel;

public class SPNDefinitionDialog extends ComponentDefinitionDialog<SPNetwork>
{
	private SPNetwork component;
	private JTree     spnTree;
	
	public SPNDefinitionDialog(SPNetwork component)
	{
		super(component);
		setTitle("SPN");
		
		spnTree = new JTree();
		spnTree.setModel(new SPNTreeModel(component));
		for(int i=0; i<spnTree.getRowCount(); i++)
		{
			spnTree.expandRow(i);
		}
		
		//JScrollPane jsp = new JScrollPane();
		//jsp.add(spnTree);
		//add(jsp);
		add(spnTree);
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
}
