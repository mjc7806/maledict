package net.mjcarpenter.csci788.ui.component;

import javax.swing.JFrame;

import net.mjcarpenter.csci788.crypto.spn.SPNComponent;

public abstract class ComponentDefinitionDialog<T extends SPNComponent> extends JFrame
{
	private T component;
	
	public ComponentDefinitionDialog(T component)
	{
		this.component = component;
	}
	
	public T getComponent()
	{
		return this.component;
	}
	
	// TODO - Create UIMessage class and use that instead of boolean validations.
	public abstract boolean validateComponent();
}
