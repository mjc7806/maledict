package net.mjcarpenter.csci788.ui.model;

import net.mjcarpenter.csci788.crypto.spn.Key;
import net.mjcarpenter.csci788.crypto.spn.Round;

public class KeyTreeNode extends ComponentLeafNode<Key>
{
	public KeyTreeNode(Key component, ComponentTreeNode<Round> parent)
	{
		super(component, parent);
	}
	
	public String toString()
	{
		return String.format("Key[%d]", parent.indexOnParent());
	}
}
