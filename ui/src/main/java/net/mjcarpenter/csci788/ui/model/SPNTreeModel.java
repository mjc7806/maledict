package net.mjcarpenter.csci788.ui.model;

import javax.swing.tree.DefaultTreeModel;

import net.mjcarpenter.csci788.crypto.spn.SPNetwork;

@SuppressWarnings("serial")
public class SPNTreeModel extends DefaultTreeModel
{
	public SPNTreeModel(SPNetwork spn)
	{
		super(new SPNTreeNode(spn));
	}
}
