package net.mjcarpenter.csci788.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import net.mjcarpenter.csci788.crypto.spn.Round;
import net.mjcarpenter.csci788.crypto.spn.SBox;
import net.mjcarpenter.csci788.crypto.spn.SPNComponent;
import net.mjcarpenter.csci788.crypto.spn.SPNetwork;
import net.mjcarpenter.csci788.ui.dialog.component.ComponentDefinitionDialog;

public class RoundTreeNode implements ComponentTreeNode<Round>
{
	private Round component;
	private ComponentTreeNode<SPNetwork> parent;
	List<ComponentLeafNode<? extends SPNComponent>> children;
	
	public RoundTreeNode(Round component, ComponentTreeNode<SPNetwork> parent)
	{
		this.component = component;
		this.parent = parent;
		
		ArrayList<ComponentLeafNode<? extends SPNComponent>> children = new ArrayList<ComponentLeafNode<? extends SPNComponent>>();
		
		// Define and display children in the order that they will execute when the Round runs
		children.add(new KeyTreeNode(this.component.getSubKey(), this));
		for(SBox each: this.component.getSBoxes())
		{
			children.add(new SBoxTreeNode(each, this));
		}
		children.add(new PermutationTreeNode(this.component.getPermutation(), this));
		
		this.children = Collections.unmodifiableList(children);
	}
	
	@Override
	public Round getComponent()
	{
		return component;
	}
	
	@Override
	public TreeNode getChildAt(int childIndex)
	{
		return children.get(childIndex);
	}

	@Override
	public int getChildCount()
	{
		return children.size();
	}

	@Override
	public TreeNode getParent()
	{
		return parent;
	}
	
	public int indexOnParent()
	{
		return parent.getIndex(this);
	}

	@Override
	public int getIndex(TreeNode node)
	{
		return children.indexOf(node);
	}

	@Override
	public boolean getAllowsChildren()
	{
		return true;
	}

	@Override
	public boolean isLeaf()
	{
		return false;
	}

	@Override
	public Enumeration<ComponentLeafNode<? extends SPNComponent>> children()
	{
		return Collections.enumeration(children);
	}
	
	public String toString()
	{
		return String.format("Round[%d]", indexOnParent());
	}
	
	public ComponentDefinitionDialog<Round> editWithDialog()
	{
		// Rounds don't have their own dialog.
		throw new UnsupportedOperationException();
	}
}
