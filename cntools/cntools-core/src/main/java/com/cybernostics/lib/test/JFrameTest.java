/*
 * #%L cntools-core %% Copyright (C) 2012 Cybernostics Pty Ltd %% Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. #L%
 */

package com.cybernostics.lib.test;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author jasonw
 */
public class JFrameTest extends JFrame
{

	public static JFrameTest create( String name )
	{
		return new JFrameTest( name );

	}

	public JFrameTest( String name )
	{
		super( name );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setSize(
			800,
			600 );

	}

	public void go()
	{

		SwingUtilities.invokeLater( new Runnable()
		{

			@Override
			public void run()
			{
				setVisible( true );
			}

		} );
	}

}
