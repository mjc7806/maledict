package net.mjcarpenter.maledict.ui.model;

import net.mjcarpenter.maledict.crypto.spn.Permutation;
import net.mjcarpenter.maledict.crypto.spn.Round;
import net.mjcarpenter.maledict.ui.dialog.component.ComponentDefinitionDialog;
import net.mjcarpenter.maledict.ui.dialog.component.PermutationDefinitionDialog;

public class PermutationTreeNode extends ComponentLeafNode<Permutation>
{
	public PermutationTreeNode(Permutation component, ComponentTreeNode<Round> parent)
	{
		super(component, parent, Permutation.class);
	}
	
	public String toString()
	{
		return String.format("Permutation[%d]", parent.indexOnParent());
	}

	public ComponentDefinitionDialog<Permutation> editWithDialog()
	{
		return new PermutationDefinitionDialog(component);
	}
}
