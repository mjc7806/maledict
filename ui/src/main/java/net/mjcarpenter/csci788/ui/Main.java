package net.mjcarpenter.csci788.ui;

import javax.swing.JFrame;

import net.mjcarpenter.csci788.ui.text.XMLTextArea;

public final class Main
{
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.add(new XMLTextArea(null));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800,600);
		frame.setVisible(true);
	}
}
