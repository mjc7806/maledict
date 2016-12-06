package net.mjcarpenter.maledict.ui.model;

import net.mjcarpenter.maledict.crypto.spn.Round;
import net.mjcarpenter.maledict.crypto.spn.SBox;
import net.mjcarpenter.maledict.ui.dialog.component.ComponentDefinitionDialog;
import net.mjcarpenter.maledict.ui.dialog.component.SBoxDefinitionDialog;

public class SBoxTreeNode extends ComponentLeafNode<SBox>
{
	public SBoxTreeNode(SBox component, ComponentTreeNode<Round> parent)
	{
		super(component, parent, SBox.class);
	}
	
	public String toString()
	{
		// Key always comes before S-boxes in index, but we don't want to factor that into
		// what we display in this case.
		return String.format("SBox[%d,%d]",
				parent.indexOnParent(),
				this.indexOnParent()-1);
	}

	public ComponentDefinitionDialog<SBox> editWithDialog()
	{
		return new SBoxDefinitionDialog(component);
	}
}
