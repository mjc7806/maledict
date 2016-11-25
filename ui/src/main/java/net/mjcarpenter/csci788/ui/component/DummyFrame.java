package net.mjcarpenter.csci788.ui.component;

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