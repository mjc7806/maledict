/*
 * Maledict - An Interactive Tool for Learning Linear and Differential Cryptanalysis of SPNs
 * Copyright (C) 2016  Mike Carpenter
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.mjcarpenter.maledict.ui.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import net.mjcarpenter.maledict.crypto.spn.Round;
import net.mjcarpenter.maledict.crypto.spn.SPNetwork;
import net.mjcarpenter.maledict.ui.dialog.component.ComponentDefinitionDialog;
import net.mjcarpenter.maledict.ui.util.MasterPropertiesCache;

public class SPNTreeNode extends ComponentTreeNode<SPNetwork>
{
	private List<ComponentTreeNode<Round>> children;
	
	public SPNTreeNode(SPNetwork component)
	{
		super(component, SPNetwork.class);
		
		List<ComponentTreeNode<Round>> children = new ArrayList<ComponentTreeNode<Round>>();
		for(Round each: this.component.getRounds())
		{
			children.add(new RoundTreeNode(each, this));
		}
		
		this.children = Collections.unmodifiableList(children);
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
	
	public Collection<ComponentTreeNode<Round>> childrenCollection()
	{
		return children;
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
		MasterPropertiesCache.getInstance().setSPN(component);
	}
}
