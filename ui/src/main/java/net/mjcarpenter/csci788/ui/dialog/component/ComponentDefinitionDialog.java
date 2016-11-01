package net.mjcarpenter.csci788.ui.dialog.component;

import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JDialog;

import net.mjcarpenter.csci788.crypto.spn.SPNComponent;

@SuppressWarnings("serial")
public abstract class ComponentDefinitionDialog<T extends SPNComponent> extends JDialog
{
	protected T component;
	
	public ComponentDefinitionDialog(T component)
	{
		this.component = component;
	}
	
	public T getComponent()
	{
		return this.component;
	}
	
	private void writeObject(ObjectOutputStream stream)
	throws NotSerializableException
	{
		throw new NotSerializableException();
	}
	
	private void readObject(ObjectInputStream stream)
	throws NotSerializableException
	{
		throw new NotSerializableException();
	}
	
	// TODO - Create UIMessage class and use that instead of boolean validations.
	public abstract boolean validateComponent();
}
