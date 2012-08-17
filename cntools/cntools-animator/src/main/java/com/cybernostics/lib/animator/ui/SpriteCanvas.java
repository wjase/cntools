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

import com.cybernostics.lib.animator.sprite.*;
import com.cybernostics.lib.gui.graphics.StateSaver;
import com.cybernostics.lib.gui.shape.ShapeUtils2D;
import com.cybernostics.lib.gui.shapeeffects.ShapeEffect;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Utility Class to render sprites in the right coordinate space of owning
 * component
 *
 * @author jasonw
 */
public class SpriteCanvas
	implements
	ShapeEffect,
	SpriteCollection,
	Iterable< ISprite >,
	SpriteOwner
{

	private ShapeEffect overlay = null;

	public void setOverlay( ShapeEffect overlay )
	{
		this.overlay = overlay;
	}

	private SpriteLayerCollection sprites = new SpriteLayerCollection();

	public SpriteCanvas()
	{
	}

	/**
	 * Scales dimensions so the drawing surface is 0.0 -> 1.0
	 */
	AffineTransform componentSizeScaler = new AffineTransform();

	Rectangle2D previousBounds = new Rectangle2D.Double();

	private void setupHints( Graphics2D g2 )
	{
		g2.setRenderingHint(
			RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_OFF );
		g2.setRenderingHint(
			RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_SPEED );

	}

	boolean isDrawing = false;

	@Override
	public void draw( Graphics2D g2, Shape s )
	{
		if (isDrawing)
		{
			return;
		}
		isDrawing = true;
		Rectangle2D bounds = ShapeUtils2D.getBounds( s );

		if (bounds.getWidth() == 0 || bounds.getHeight() == 0)
		{
			isDrawing = false;
			return;
		}

		StateSaver gSave = StateSaver.wrap( g2 );

		boolean sizeUpdated = false;
		//        setupHints( gSave );
		// do we need to update our transform?
		if (( previousBounds.getWidth() == 0 )
			|| ( !previousBounds.equals( bounds ) ))
		{
			componentSizeScaler.setToScale(
				bounds.getWidth(),
				bounds.getHeight() );
			previousBounds.setFrame( bounds );
			fireSpriteCanvasSizeChanged();
			sizeUpdated = true;
		}

		AffineTransform saved = gSave.getTransform();
		try
		{

			// Draw the sprites
			for (ISprite eachSprite : sprites)
			{
				gSave.setTransform( saved );

				if (eachSprite != null)
				{
					if (sizeUpdated)
					{
						eachSprite.ownerSizeUpdated();
					}

					eachSprite.render( g2 );
				}
			}

			if (overlay != null)
			{
				gSave.setTransform( saved );
				overlay.draw(
					gSave,
					s );
			}

		}
		finally
		{
			isDrawing = false;
			gSave.restore();
		}

	}

	@Override
	public void removeSprites( ISprite... toRemove )
	{
		sprites.removeSprites( toRemove );

	}

	@Override
	public void addSprites( ISprite... toAdd )
	{
		for (ISprite eachSprite : toAdd)
		{
			eachSprite.setOwner( this );
			eachSprite.setRestoreContext( false ); //we'll do that

		}
		sprites.addSprites( toAdd );
	}

	@Override
	public Iterator< ISprite > iterator()
	{
		return sprites.iterator();
	}

	/**
	 * tell any non-sprite clients that our size has changed
	 */
	private void fireSpriteCanvasSizeChanged()
	{
		for (OwnerSizeListener eachListener : listeners)
		{
			eachListener.ownerSizeUpdated();
		}
	}

	private List< OwnerSizeListener > listeners = new ArrayList< OwnerSizeListener >();

	//It is assumed that all the ISprites are also ownerSizeListeners
	// so you don't need to add child sprites
	@Override
	public void addUpdateListener( OwnerSizeListener listener )
	{
		listeners.add( listener );
	}

	@Override
	public AffineTransform getTransform()
	{
		return componentSizeScaler;
	}

	private GraphicsConfiguration gc = null;

	public void setGraphicsConfiguration( GraphicsConfiguration gc )
	{
		this.gc = gc;
	}

	@Override
	public GraphicsConfiguration getGraphicsConfiguration()
	{
		return gc;
	}

	@Override
	public ISprite getById( String id )
	{
		return sprites.getById( id );
	}

}
