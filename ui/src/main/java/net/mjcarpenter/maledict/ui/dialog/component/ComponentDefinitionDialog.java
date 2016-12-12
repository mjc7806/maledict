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
