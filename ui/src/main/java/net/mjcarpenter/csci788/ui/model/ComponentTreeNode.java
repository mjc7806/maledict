package net.mjcarpenter.csci788.ui.model;

import javax.swing.tree.TreeNode;
import net.mjcarpenter.csci788.crypto.spn.SPNComponent;

public interface ComponentTreeNode<T extends SPNComponent> extends TreeNode
{
	public T getComponent();
	
	public int indexOnParent();
}
