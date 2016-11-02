package net.mjcarpenter.csci788.ui.model;

import net.mjcarpenter.csci788.crypto.spn.Round;
import net.mjcarpenter.csci788.crypto.spn.SBox;

public class SBoxTreeNode extends ComponentLeafNode<SBox>
{
	public SBoxTreeNode(SBox component, ComponentTreeNode<Round> parent)
	{
		super(component, parent);
	}
	
	public String toString()
	{
		// Key always comes before S-boxes in index, but we don't want to factor that into
		// what we display in this case.
		return String.format("SBox[%d,%d]",
				parent.indexOnParent(),
				this.indexOnParent()-1);
	}
}
