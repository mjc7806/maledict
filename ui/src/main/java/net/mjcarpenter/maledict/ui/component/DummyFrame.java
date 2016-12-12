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
package net.mjcarpenter.maledict.ui.component;

import javax.swing.JFrame;

/**
 * This inner-class is used to make sure that this JDialog has an entry
 * in the taskbar (or equivalent icon-bar for another system).
 * 
 * Its code is adapted for this application from:
 * http://stackoverflow.com/a/8007200/2250867
 */
@SuppressWarnings("serial")
public class DummyFrame extends JFrame
{
	public DummyFrame(String title)
	{
		super(title);
		setUndecorated(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}