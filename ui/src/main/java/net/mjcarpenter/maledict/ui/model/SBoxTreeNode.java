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

import net.mjcarpenter.maledict.crypto.spn.Round;
import net.mjcarpenter.maledict.crypto.spn.SBox;
import net.mjcarpenter.maledict.ui.dialog.component.ComponentDefinitionDialog;
import net.mjcarpenter.maledict.ui.dialog.component.SBoxDefinitionDialog;

public class SBoxTreeNode extends ComponentLeafNode<SBox>
{
	public SBoxTreeNode(SBox component, ComponentTreeNode<Round> parent)
	{
		super(component, parent, SBox.class);
	}
	
	public String toString()
	{
		// Key always comes before S-boxes in index, but we don't want to factor that into
		// what we display in this case.
		return String.format("SBox[%d,%d]",
				parent.indexOnParent(),
				this.indexOnParent()-1);
	}

	public ComponentDefinitionDialog<SBox> editWithDialog()
	{
		return new SBoxDefinitionDialog(component);
	}
}
