package net.mjcarpenter.csci788.ui.model;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import net.mjcarpenter.csci788.crypto.spn.Permutation;
import net.mjcarpenter.csci788.crypto.spn.Round;

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
}
