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

import com.cybernostics.lib.animator.track.characteranimate.DisplayedElementsListener;
import com.cybernostics.lib.animator.track.characteranimate.SVGArticulatedIcon;
import com.cybernostics.lib.maths.DimensionFloat;
import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import com.cybernostics.lib.svg.SVGUtil;
import com.cybernostics.lib.svg.SubRegionContainer;
import com.kitfox.svg.SVGDiagram;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.Future;
import javax.swing.Icon;

/**
 * @author jasonw
 *
 */
public class SVGSprite extends IconSprite implements SubRegionContainer
{

	//private ArrayList< ScalableSVGIcon> svgFrames = new ArrayList< ScalableSVGIcon>();
	/**
	 *
	 * @param name
	 */
	public SVGSprite( String name )
	{
		super( name );
	}

	private boolean animatedRegions = false;

	/**
	 * @param name
	 * @param size
	 */
	public SVGSprite( String name, DimensionFloat size )
	{
		super( name );
		setRelativeSize( size );
	}

	/**
	 *
	 * @param name
	 * @param bounds
	 */
	public SVGSprite( String name, Rectangle2D bounds )
	{
		super( name );
		setRelativeBounds( bounds );

	}

	public SVGSprite( String id, URL toLoad )
	{
		this( id, new ScalableSVGIcon( toLoad ) );
	}

	/**
	 * @param string
	 * @param stdIcon
	 */
	public SVGSprite( String string, ScalableSVGIcon stdIcon )
	{
		this( string );

		setIcon( stdIcon );

	}

	public Future< URI > startLoad( URL toLoad )
	{
		SpriteIconLoader loader = new SpriteIconLoader( this, toLoad );
		return loader.startLoad();
	}

	@Override
	public void setIcon( Icon ic )
	{
		super.setIcon( ic );

		if (ic instanceof SVGArticulatedIcon)
		{
			SVGArticulatedIcon sa = (SVGArticulatedIcon) ic;
			sa.addDisplayedElementsListener( updateMe );
		}
		ScalableSVGIcon svi = (ScalableSVGIcon) ic;
		svi.addPropertyChangeListener( new PropertyChangeListener()
		{

			@Override
			public void propertyChange( PropertyChangeEvent evt )
			{
				setNeedsRender( true );
				update();
			}

		} );
	}

	DisplayedElementsListener updateMe = new DisplayedElementsListener()
	{

		@Override
		public void displayedElementChanged()
		{
			setNeedsRender( true );
			update();
		}

	};

	/**
	 *
	 * @return
	 */
	public Dimension getSVGPreferredSize()
	{
		return ( (ScalableSVGIcon) getIcon() ).getPreferredSize();
	}

	/**
	 * Returns the Rectangle enclosing the specified object with the id=itemId.
	 *
	 * The returned rectangle is scaled to the size of this container
	 *
	 * @param itemId - svg element id of element for which to retrieve the
	 * rectangle
	 * @return a Rectangle enclosing the object or null if the object is not
	 * found
	 */
	public Rectangle2D getSubItemRectangle( String itemId )
	{
		ScalableSVGIcon icon = (ScalableSVGIcon) getIcon();

		SVGDiagram dia = icon.getSvgUniverse()
			.getDiagram(
				icon.getSvgURI() );

		return SVGUtil.getSubItemRectangle(
			itemId,
			dia );
	}

	/**
	 *
	 * @param itemId
	 * @return
	 */
	@Override
	public Rectangle2D getItemRectangle( String itemId )
	{
		Rectangle2D.Float newRect = new Rectangle2D.Float();

		Rectangle2D itemRect = getSubItemRectangle( itemId );
		if (itemRect != null)
		{
			Rectangle2D bounds = getRelativeBounds();
			newRect.x = (float) ( bounds.getMinX() + ( itemRect.getMinX() * bounds.getWidth() ) );
			newRect.y = (float) ( bounds.getMinY() + ( itemRect.getMinY() * bounds.getHeight() ) );
			newRect.width = (float) ( itemRect.getWidth() * bounds.getWidth() );
			newRect.height = (float) ( itemRect.getHeight() * bounds.getHeight() );
		}

		return newRect;

	}

	/**
	 * @param i
	 * @return
	 */
	//    public ScalableSVGIcon getFrame( int i )
	//    {
	//        return this.svgFrames.get( i );
	//    }
	/**
	 * @param animatedRegions the animatedRegions to set
	 */
	public void setAnimatedRegions( boolean animatedRegions )
	{
		this.animatedRegions = animatedRegions;
		if (animatedRegions)
		{
			ScalableSVGIcon icon = (ScalableSVGIcon) getIcon();
			icon.setUseBuffer( false );
			icon.addPropertyChangeListener( iconUpdater );
		}
	}

	PropertyChangeListener iconUpdater = new PropertyChangeListener()
	{

		@Override
		public void propertyChange( PropertyChangeEvent evt )
		{
			update();
		}

	};

	/**
	 * @return the animatedRegions
	 */
	public boolean isAnimatedRegions()
	{
		return animatedRegions;
	}

}
