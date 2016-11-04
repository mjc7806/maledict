package net.mjcarpenter.csci788.ui.model;

import net.mjcarpenter.csci788.crypto.spn.Permutation;
import net.mjcarpenter.csci788.crypto.spn.Round;
import net.mjcarpenter.csci788.ui.dialog.component.ComponentDefinitionDialog;
import net.mjcarpenter.csci788.ui.dialog.component.PermutationDefinitionDialog;

public class PermutationTreeNode extends ComponentLeafNode<Permutation>
{
	public PermutationTreeNode(Permutation component, ComponentTreeNode<Round> parent)
	{
		super(component, parent);
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
