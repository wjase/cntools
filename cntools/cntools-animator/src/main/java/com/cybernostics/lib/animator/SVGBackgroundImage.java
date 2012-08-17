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

package com.cybernostics.lib.animator;

import com.cybernostics.lib.gui.GraphicsConfigurationSource;
import com.cybernostics.lib.gui.shape.ShapeUtils2D;
import com.cybernostics.lib.gui.shapeeffects.BufferedEffect;
import com.cybernostics.lib.gui.shapeeffects.IBufferedEffect;
import com.cybernostics.lib.gui.shapeeffects.IconBackgroundEffect;
import com.cybernostics.lib.gui.shapeeffects.ShapeEffect;
import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import com.cybernostics.lib.svg.SVGUtil;
import com.cybernostics.lib.svg.SubRegionContainer;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.elements.SVGElement;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

/**
 * @author jasonw
 *
 */
public class SVGBackgroundImage implements IBufferedEffect, SubRegionContainer
{

	public SVGBackgroundImage( URL res )
	{
		this( new ScalableSVGIcon( res ) );
	}

	public void clear()
	{
		buf.clear();
	}

	private BufferedEffect buf = null;

	private ScalableSVGIcon src = null;

	private SVGDiagram diagram = null;

	public void setGcSource( GraphicsConfigurationSource gcSource )
	{
		buf.setGcSource( gcSource );
	}

	public ShapeEffect getInternal()
	{
		return buf.getInternal();
	}

	public GraphicsConfigurationSource getGcSource()
	{
		return buf.getGcSource();
	}

	Rectangle2D lastBounds = null;

	@Override
	public void draw( Graphics2D g2, Shape s )
	{
		Rectangle2D bounds = ShapeUtils2D.getBounds( s );
		if (lastBounds == null || ( !lastBounds.equals( bounds ) ))
		{
			buf.clear();
			lastBounds = bounds;
		}
		//src.paintIcon( null, g2, (int)bounds.getX(), (int)bounds.getY() );
		buf.draw(
			g2,
			s );
	}

	/**
	 *
	 * @param location
	 * @param owner
	 */
	public SVGBackgroundImage( ScalableSVGIcon icon )
	{
		src = icon;
		buf = new BufferedEffect( null, new IconBackgroundEffect( icon ) );
		icon.addPropertyChangeListener( new PropertyChangeListener()
		{

			@Override
			public void propertyChange( PropertyChangeEvent evt )
			{
				buf.clear();
			}

		} );

	}

	public SVGDiagram getDiagram()
	{
		if (diagram == null)
		{
			diagram = src.getSvgUniverse()
				.getDiagram(
					src.getSvgURI() );
		}
		return diagram;
	}

	public ScalableSVGIcon getIcon()
	{
		return src;
	}

	/**
	 * Returns the Rectangle enclosing the specified object with the id= itemId.
	 * The returned rectangle is scaled to the size of this container
	 *
	 * @param itemId - svg element id of element for which to retrieve the
	 * rectangle
	 * @return a Rectangle enclosing the object or null if the object is not
	 * found
	 */
	@Override
	public Rectangle2D getItemRectangle( String itemId )
	{
		return SVGUtil.getSubItemRectangle(
			itemId,
			getDiagram() );
	}

	/**
	 *
	 * @param itemId
	 * @return
	 */
	public Shape getItemShape( String itemId )
	{
		return SVGUtil.getItemShape(
			itemId,
			getDiagram() );
	}

	public SVGElement getElement( String toGet )
	{
		return getDiagram().getElement(
			toGet );
	}

}
