package com.cybernostics.lib.gui.autocomplete;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JWindow;
import javax.swing.Popup;

public class JWindowPopup extends javax.swing.Popup
{

	public static Popup getPopup( Component owner,
		JComponent content,
		int x,
		int y )
	{
		JWindowPopup jwp = new JWindowPopup();
		jwp.getInternalWindow()
			.setLocation(
				x,
				y );
		jwp.getInternalWindow()
			.setSize(
				content.getPreferredSize() );
		jwp.getInternalWindow()
			.getContentPane()
			.setLayout(
				new GridLayout() );
		jwp.getInternalWindow()
			.getContentPane()
			.add(
				content );
		jwp.getInternalWindow()
			.setAlwaysOnTop(
				true );
		return jwp;
	}

	JWindow internalWindow = new JWindow();

	public JWindowPopup()
	{

	}

	public JWindow getInternalWindow()
	{
		return internalWindow;
	}

	@Override
	public void hide()
	{
		internalWindow.setVisible( false );
	}

	@Override
	public void show()
	{
		internalWindow.setVisible( true );

	}
}
