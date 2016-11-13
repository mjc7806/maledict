package net.mjcarpenter.csci788.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import net.mjcarpenter.csci788.crypto.spn.Round;
import net.mjcarpenter.csci788.crypto.spn.SPNetwork;
import net.mjcarpenter.csci788.ui.dialog.component.ComponentDefinitionDialog;

public class SPNTreeNode implements ComponentTreeNode<SPNetwork>
{
	private SPNetwork component;
	private List<ComponentTreeNode<Round>> children;
	
	public SPNTreeNode(SPNetwork component)
	{
		this.component = component;
		List<ComponentTreeNode<Round>> children = new ArrayList<ComponentTreeNode<Round>>();
		for(Round each: this.component.getRounds())
		{
			children.add(new RoundTreeNode(each, this));
		}
		
		this.children = Collections.unmodifiableList(children);
	}
	
	@Override
	public SPNetwork getComponent()
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
		// Always root node.
		return null;
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
	public Enumeration<ComponentTreeNode<Round>> children()
	{
		return Collections.enumeration(children);
	}

	@Override
	public int indexOnParent()
	{
		return -1;
	}
	
	public String toString()
	{
		return "SPN";
	}

	public ComponentDefinitionDialog<SPNetwork> editWithDialog()
	{
		// Only one dialog for SPNetwork, which should already exist.
		throw new UnsupportedOperationException();
	}

	@Override
	public void refreshComponent()
	{
		List<Round> newRounds = new ArrayList<Round>();
		for(ComponentTreeNode<Round> child: children)
		{
			newRounds.add(child.getComponent());
		}
		
		component = new SPNetwork(component.getBlockSize(), newRounds.toArray(new Round[component.getRounds().length]));
	}
}
