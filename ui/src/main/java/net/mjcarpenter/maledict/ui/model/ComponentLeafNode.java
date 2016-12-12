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

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import net.mjcarpenter.maledict.crypto.spn.Round;
import net.mjcarpenter.maledict.crypto.spn.SPNComponent;

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
