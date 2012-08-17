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

package com.cybernostics.lib.animator.test;

import com.cybernostics.lib.animator.sprite.ISprite;
import com.cybernostics.lib.animator.sprite.WatchableSprite;
import com.cybernostics.lib.gui.declarative.events.RunLater;
import com.cybernostics.lib.gui.graphics.StateSaver;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Icon;

/**
 * Wraps a Sprite in the Icon interface so icons can be animated like sprites
 * 
 * @author jasonw
 * 
 */
public class SpriteIcon implements Icon, PropertyChangeListener
{

	private AffineTransform spriteScaler = new AffineTransform();
	private final Dimension defaultDimension = new Dimension( 40, 40 );
	private WatchableSprite toRender = null;
	private Dimension size = null;
	private Set< Component > clients = new HashSet< Component >();
	private static final Dimension defaultSize = new Dimension( 100, 100 );

	/**
	 * 
	 * @param someSprite
	 * @param preferredSize
	 */
	public SpriteIcon( WatchableSprite someSprite, Dimension preferredSize )
	{
		setToRender( someSprite );

		Dimension initialSize = ( preferredSize != null ) ? preferredSize
			: defaultSize;
		setSize( initialSize );
		someSprite.setRelativeSize(
			1.0f,
			1.0f );
	}

	/**
	 * @param sprite
	 */
	public SpriteIcon( WatchableSprite sprite )
	{
		this( sprite, null );
	}

	private void updateClients()
	{
		new RunLater()
		{

			@Override
			public void run( Object... args )
			{
				for (Component eachComponent : clients)
				{
					eachComponent.repaint();
				}
			}
		};
	}

	@Override
	public int getIconHeight()
	{
		return size.height;
	}

	@Override
	public int getIconWidth()
	{
		return size.width;
	}

	@Override
	public void paintIcon( Component c, Graphics g, int x, int y )
	{
		clients.add( c );
		StateSaver saver = StateSaver.wrap( g );
		try
		{
			saver.translate(
				x,
				y );
			saver.setClip(
				0,
				0,
				c.getWidth(),
				c.getHeight() );
			toRender.render( saver );
		}
		finally
		{
			saver.restore();
		}

	}

	/**
	 * @param toRender
	 *            the toRender to set
	 */
	public void setToRender( WatchableSprite toRender )
	{
		if (this.toRender != null)
		{
			toRender.removePropertyChangeListener( this );
		}
		this.toRender = toRender;
		toRender.setRelativeLocation(
			0f,
			0f );
		toRender.setRelativeSize(
			1.0,
			1.0 ); // take up all the icon space
		toRender.addPropertyChangeListener( this );
	}

	/**
	 * @return the toRender
	 */
	public ISprite getToRender()
	{
		return toRender;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize( Dimension size )
	{
		if (size != null)
		{
			this.size = size;
		}
		else
		{
			this.size = defaultDimension;
		}

		spriteScaler.setToScale(
			size.getWidth(),
			size.getHeight() );
		updateClients();
	}

	/**
	 * @return the size
	 */
	public Dimension getSize()
	{
		return size;
	}

	@Override
	public void propertyChange( PropertyChangeEvent evt )
	{
		updateClients();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main( String[] args )
	{
		//TODO Put this into test code and sort out resource loading
		//		JFrame jf = new JFrame( "sprite icon test" );
		//		AnimatedPanel jp = new AnimatedPanel( 50 );
		//		jp.setLayout( new GridLayout() );
		//		JButton jb = new JButton();
		//		Sprite cameraSprite = new SVGSprite( "camera", ( SVGIcon ) IconFactory.getStdIcon( IconFactory.CAMERA ) );
		//		SpriteIcon cameraIcon = new SpriteIcon( cameraSprite );
		//		jb.setIcon( cameraIcon );
		//		jp.add( jb );
		//		jf.getContentPane().add( jp );
		//		jf.setSize( 200, 200 );
		//		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		//		jf.setVisible( true );
		//		SawToothFunction stf = new SawToothFunction( 1.0f, 0.6f );
		//		SpriteFaderTrack buttonFlashEffect = new SpriteFaderTrack( "buttonFader", cameraSprite, stf, 1000 );
		//		buttonFlashEffect.setPeriodic( true );
		//		jp.getSequencer().addAndStartTrack( buttonFlashEffect );
	}
}
