package net.mjcarpenter.maledict.ui.dialog.component;

import java.awt.Frame;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JDialog;

import net.mjcarpenter.maledict.crypto.spn.SPNComponent;
import net.mjcarpenter.maledict.ui.message.help.HelpMessage;

@SuppressWarnings("serial")
public abstract class ComponentDefinitionDialog<T extends SPNComponent> extends JDialog
{
	private HelpMessage helpDisplayed;
	
	protected T component;
	protected T originalComponent;
	
	public ComponentDefinitionDialog(Frame owner, String title, T component)
	{
		super(owner, title);
		setModalityType(ModalityType.APPLICATION_MODAL);
		this.component = component;
		this.originalComponent = component;
		this.helpDisplayed = null;
	}
	
	public ComponentDefinitionDialog(String title, T component)
	{
		this(null, title, component);
	}
	
	public T getComponent()
	{
		return this.component;
	}
	
	@Override
	public void dispose()
	{
		if(helpDisplayed != null)
		{
			helpDisplayed.dispose();
			helpDisplayed = null;
		}
		
		super.dispose();
	}
	
	protected void openHelpMessage(String helpMessageType)
	{
		if(helpDisplayed == null)
		{
			helpDisplayed = new HelpMessage(helpMessageType, this,
					() ->
					{
						if(helpDisplayed != null)
						{
							helpDisplayed.dispose();
						}
						
						helpDisplayed = null;
					});
			
			if(helpDisplayed.isLoadedSuccessfully())
			{
				helpDisplayed.setVisible(true);
			}
			else
			{
				helpDisplayed.dispose();
				helpDisplayed = null;
			}
		}
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
