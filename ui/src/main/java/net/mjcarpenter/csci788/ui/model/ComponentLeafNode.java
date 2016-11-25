package net.mjcarpenter.csci788.ui.model;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import net.mjcarpenter.csci788.crypto.spn.Round;
import net.mjcarpenter.csci788.crypto.spn.SPNComponent;

public abstract class ComponentLeafNode<T extends SPNComponent> extends ComponentTreeNode<T>
{
	protected ComponentTreeNode<Round> parent;
	
	public ComponentLeafNode(T component, ComponentTreeNode<Round> parent, Class<T> clazz)
	{
		super(component, clazz);
		this.parent = parent;
	}
	
	public void replaceComponent(T component)
	{
		this.component = component;
		refreshComponent();
	}
	
	public void refreshComponent()
	{
		parent.refreshComponent();
	}
	
	public T getComponent()
	{
		return this.component;
	}
	
	@Override
	public TreeNode getChildAt(int childIndex)
	{
		return null;
	}

	@Override
	public int getChildCount()
	{
		return 0;
	}

	@Override
	public TreeNode getParent()
	{
		return parent;
	}

	@Override
	public int getIndex(TreeNode node)
	{
		return -1;
	}
	
	public int indexOnParent()
	{
		return parent.getIndex(this);
	}

	@Override
	public boolean getAllowsChildren()
	{
		return false;
	}

	@Override
	public boolean isLeaf()
	{
		// Always is leaf because can't have children.
		return true;
	}

	@Override
	public Enumeration<ComponentTreeNode<SPNComponent>> children()
	{
		return null;
	}
}
