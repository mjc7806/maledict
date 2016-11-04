package net.mjcarpenter.csci788.ui.model;

import net.mjcarpenter.csci788.crypto.spn.Key;
import net.mjcarpenter.csci788.crypto.spn.Round;
import net.mjcarpenter.csci788.ui.dialog.component.ComponentDefinitionDialog;
import net.mjcarpenter.csci788.ui.dialog.component.KeyDefinitionDialog;

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
	
	public ComponentDefinitionDialog<Key> editWithDialog()
	{
		return new KeyDefinitionDialog(component);
	}
}
