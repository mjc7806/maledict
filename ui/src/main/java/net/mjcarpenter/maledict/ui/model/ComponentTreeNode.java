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
