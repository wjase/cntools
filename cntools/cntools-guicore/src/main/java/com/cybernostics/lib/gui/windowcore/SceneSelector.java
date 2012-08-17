/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.cybernostics.lib.gui.windowcore;

import com.cybernostics.lib.gui.declarative.events.WhenClicked;
import com.cybernostics.lib.gui.shapeeffects.ColorFill;
import com.cybernostics.lib.gui.shapeeffects.ShapedPanel;
import com.cybernostics.lib.gui.windowcore.ScreenStack;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

/**
 * test screen used to launch other screens
 * @author jasonw
 */
public class SceneSelector extends ShapedPanel
{

	public static void showScreen()
	{
		ScreenStack.get()
			.register(
				"sceneSelector",
				new SceneSelector() );
		ScreenStack.get()
			.showScreen(
				"sceneSelector" );

	}

	public SceneSelector()
	{

		setBackgroundPainter( new ColorFill( Color.blue.darker()
			.darker() ) );
		JButton jbClose = new JButton( "Close" );
		setLayout( new FlowLayout() );
		add( jbClose );
		add( new JLabel( "Screen Picker" ) );
		new WhenClicked( jbClose )
		{

			@Override
			public void doThis( ActionEvent e )
			{
				ScreenStack.get()
					.popScreen();
			}

		};

		final JComboBox jcbWindows = new JComboBox();
		for (String eachName : ScreenStack.get()
			.getScreenNames())
		{
			jcbWindows.addItem( eachName );
		}
		add( jcbWindows );

		JButton jbShow = new JButton( "Show" );
		add( jbShow );
		new WhenClicked( jbShow )
		{

			@Override
			public void doThis( ActionEvent e )
			{
				ScreenStack.get()
					.pushScreen(
						jcbWindows.getSelectedItem()
							.toString() );
			}

		};

		revalidate();
		repaint();

	}

}
