package net.mjcarpenter.maledict.ui.model;

import net.mjcarpenter.maledict.crypto.spn.Key;
import net.mjcarpenter.maledict.crypto.spn.Round;
import net.mjcarpenter.maledict.ui.dialog.component.ComponentDefinitionDialog;
import net.mjcarpenter.maledict.ui.dialog.component.KeyDefinitionDialog;

public class KeyTreeNode extends ComponentLeafNode<Key>
{
	public KeyTreeNode(Key component, ComponentTreeNode<Round> parent)
	{
		super(component, parent, Key.class);
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
