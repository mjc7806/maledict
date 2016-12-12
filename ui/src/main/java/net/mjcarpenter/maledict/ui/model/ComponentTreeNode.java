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

import javax.swing.tree.TreeNode;

import net.mjcarpenter.maledict.crypto.spn.SPNComponent;
import net.mjcarpenter.maledict.ui.dialog.component.ComponentDefinitionDialog;

public abstract class ComponentTreeNode<T extends SPNComponent> implements TreeNode
{
	protected T component;
	private Class<T> clazz;
	
	public ComponentTreeNode(T component, Class<T> clazz)
	{
		this.component = component;
		this.clazz = clazz;
	}
	
	public Class<T> getTypeClass()
	{
		return clazz;
	}
	
	public T getComponent()
	{
		return component;
	}
	
	public abstract int indexOnParent();
	public abstract void refreshComponent();
	
	public abstract ComponentDefinitionDialog<T> editWithDialog();
}
