package net.mjcarpenter.maledict.ui.model;

import javax.swing.tree.DefaultTreeModel;

import net.mjcarpenter.maledict.crypto.spn.SPNetwork;

@SuppressWarnings("serial")
public class SPNTreeModel extends DefaultTreeModel
{
	public SPNTreeModel(SPNetwork spn)
	{
		super(new SPNTreeNode(spn));
	}
	
	public SPNetwork getSPN()
	{
		return ((SPNTreeNode)getRoot()).getComponent();
	}
}
