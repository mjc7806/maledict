package net.mjcarpenter.csci788.ui.dialog.component;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JDialog;

import net.mjcarpenter.csci788.crypto.spn.SPNComponent;
import net.mjcarpenter.csci788.ui.message.help.HelpMessage;

@SuppressWarnings("serial")
public abstract class ComponentDefinitionDialog<T extends SPNComponent> extends JDialog
{
	private HelpMessage helpDisplayed;
	
	protected T component;
	protected T originalComponent;
	
	public ComponentDefinitionDialog(T component)
	{
		setModalityType(ModalityType.APPLICATION_MODAL);
		this.component = component;
		this.originalComponent = component;
		this.helpDisplayed = null;
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
	
	protected void openHelpMessage(HelpMessage hm)
	{
		if(helpDisplayed == null && hm.isLoadedSuccessfully())
		{
			helpDisplayed = hm;
			helpDisplayed.addWindowListener(new WindowAdapter()
					{
						@Override
						public void windowClosed(WindowEvent we)
						{
							if(helpDisplayed != null)
							{
								helpDisplayed.dispose();
							}
							
							helpDisplayed = null;
						}
					});
			
			helpDisplayed.setLocation(this.getLocation().x + this.getWidth() + 20, this.getLocation().y);
			helpDisplayed.setVisible(true);
		}
		else
		{
			hm.dispose();
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
