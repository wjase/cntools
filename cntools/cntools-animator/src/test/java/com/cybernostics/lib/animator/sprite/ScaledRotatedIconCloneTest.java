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

package com.cybernostics.lib.animator.sprite;

import com.cybernostics.lib.concurrent.GUIEventThread;
import com.cybernostics.lib.gui.GraphicsConfigurationSource;
import com.cybernostics.lib.gui.graphics.StateSaver;
import com.cybernostics.lib.media.icon.AttributedScalableIcon;
import com.cybernostics.lib.media.icon.NoImageIcon;
import com.cybernostics.lib.media.icon.TestIcon;
import java.awt.*;
import java.awt.geom.Point2D;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author jasonw
 */
public class ScaledRotatedIconCloneTest
{

	public ScaledRotatedIconCloneTest()
	{
	}

	public static void main( String[] args )
	{
		final JFrame jf = new JFrame( "RotateTest" );
		jf.setSize(
			500,
			500 );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		Icon test = new TestIcon( 200, 50 );

		GraphicsConfigurationSource gcs = new GraphicsConfigurationSource()
		{

			@Override
			public GraphicsConfiguration getGraphicsConfiguration()
			{
				return jf.getGraphicsConfiguration();
			}

		};
		ScaledRotatedIcon rotr = new ScaledRotatedIcon( test,
			new Dimension( 400, 100 ), gcs )
		{

			@Override
			public void paintIcon( Component c, Graphics g, int x, int y )
			{
				super.paintIcon(
					c,
					g,
					x,
					y );
			}

		};
		rotr.setAnchor( Anchor.Position.EAST );
		rotr.setAngle( 90 );
		rotr.setScale( 2.0 );

		//rotr.setFlip( FlipType.HORIZONTAL );

		System.out.println( test.getIconWidth() );
		System.out.println( test.getIconHeight() );
		final Point2D p = new java.awt.geom.Point2D.Double();

		final ScaledRotatedIconClone sic = new ScaledRotatedIconClone( rotr )
		{

			@Override
			public void paintIcon( Component c, Graphics g, int x, int y )
			{
				super.paintIcon(
					c,
					g,
					x,
					y );
			}

		};
		sic.setScale( 0.5 );

		//sic.setFlip( IconTransformer.FlipType.HORIZONTAL );

		final Point2D hotspot = sic.getHotspot();
		System.out.println( "anchor" + hotspot.getX() + "," + hotspot.getY() );
		System.out.println( "translated" + p.getX() + "," + p.getY() );

		JLabel jl = new JLabel( sic ) //ic
		{

			@Override
			protected void paintComponent( Graphics g )
			{
				StateSaver g2 = StateSaver.wrap( g );
				super.paintComponent( g2 );
				g2.setColor( Color.yellow );
				g2.drawOval(
					(int) hotspot.getX() - 5,
					(int) hotspot.getY() - 5,
					10,
					10 );
			}

		};
		jl.setBackground( Color.red );
		jl.setOpaque( true );
		jf.getContentPane()
			.setLayout(
				new FlowLayout() );
		jf.getContentPane()
			.add(
				jl );
		GUIEventThread.show( jf );

	}

}
