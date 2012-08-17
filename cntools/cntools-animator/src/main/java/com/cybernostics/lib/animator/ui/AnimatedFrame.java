/*
 * #%L cntools-animator %% Copyright (C) 2012 Cybernostics Pty Ltd %% Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. #L%
 */

package com.cybernostics.lib.animator.ui;

import com.cybernostics.lib.animator.track.Sequencer;
import com.cybernostics.lib.concurrent.GUIEventThread;
import com.cybernostics.lib.gui.declarative.events.WhenMadeVisible;
import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 *
 * @author jasonw
 */
public class AnimatedFrame extends JFrame
{

	public static AnimatedFrame testPanel( AnimatedScene ap )
	{

		final AnimatedFrame jf = new AnimatedFrame( "Test Frame" );
		jf.getContentPane()
			.setLayout(
				new GridLayout() );
		jf.getContentPane()
			.add(
				ap );
		jf.setSize(
			800,
			600 );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		GUIEventThread.show( jf );

		new WhenMadeVisible( jf )
		{

			@Override
			public void doThis( AWTEvent e )
			{
				jf.startAnimation();
			}

		};
		return jf;

	}

	public AnimatedFrame( String title, Sequencer seq )
	{
		new WhenMadeVisible( this )
		{

			@Override
			public void doThis( AWTEvent e )
			{
				startAnimation();

			}

		};

	}

	boolean started = false;

	public void startAnimation()
	{

		Sequencer.get()
			.start();
	}

	public AnimatedFrame( String title )
	{
		this( title, Sequencer.get() );

	}

	public void makeFullScreen()
	{
		setUndecorated( true );
		Dimension size = Toolkit.getDefaultToolkit()
			.getScreenSize();
		setBounds(
			0,
			0,
			size.width,
			size.height );
		setAlwaysOnTop( true );
	}

	public static void main( String[] args )
	{
		AnimatedFrame af = new AnimatedFrame( "Anim Test" );

		//af.makeFullScreen();
		af.setSize(
			600,
			400 );
		GUIEventThread.show( af );
	}

}
