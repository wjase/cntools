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

import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.util.Vector;
import java.util.regex.Pattern;

import com.kitfox.svg.elements.Group;
import com.kitfox.svg.elements.RenderableElement;
import com.kitfox.svg.elements.SVGElement;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGUniverse;
import com.kitfox.svg.animation.AnimationElement;
import java.awt.geom.Dimension2D;

/**
 * @author jasonw
 *
 */
public class SVGIconFrame extends ScalableSVGIcon
{

	private ScalableSVGIcon toRender;

	private Pattern elementPattern;

	private final String framePattern = "Frame";

	/**
	 * Creates a rendered version of the SVGIcon. When rendered all the groups
	 * with FrameN are shown - all else hidden
	 *
	 * @param toRender - the actual icon to paint
	 * @param pattern - "FrameN" where N is 0,1,2...
	 */
	public SVGIconFrame( ScalableSVGIcon toRender, String pattern )
	{
		elementPattern = Pattern.compile( "^" + pattern + "-" );
		this.toRender = toRender;

	}

	private void setElementVisibility( SVGElement e, boolean visible )
	{
		try
		{
			// System.out.printf( "set element %s to %s\n", e.getId(), visible
			// );

			e.setAttribute(
				"display",
				AnimationElement.AT_CSS,
				visible ? "inline" : "none" );
		}
		catch (SVGException e1)
		{
		}
	}

	/**
	 * Hunts for elements matching
	 *
	 * @param elem
	 */
	private void setVisibility( SVGElement elem )
	{
		if (( elem instanceof RenderableElement ) || ( elem instanceof Group ))
		{

			if (elem.getId()
				.startsWith(
					framePattern ))
			{
				setElementVisibility(
					elem,
					elementPattern.matcher(
						elem.getId() )
						.find() );
			}
			else
			{
				Vector< SVGElement > children = new Vector< SVGElement >();
				elem.getChildren( children );

				for (SVGElement eachElement : children)
				{
					setVisibility( eachElement );
				}

			}
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.kitfox.svg.app.beans.SVGIcon#paintIcon(java.awt.Component,
	 * java.awt.Graphics, int, int)
	 */
	@Override
	public void paintIcon( Component comp, Graphics gg, int x, int y )
	{
		// hide or show all the bits for this frame
		setVisibility( toRender.getSvgUniverse()
			.getDiagram(
				toRender.getSvgURI() )
			.getRoot() );
		toRender.paintIcon(
			comp,
			gg,
			x,
			y );
	}

	/**
	 *
	 * @param p
	 */
	@Override
	public void addPropertyChangeListener( PropertyChangeListener p )
	{
		toRender.addPropertyChangeListener( p );
	}

	@Override
	public int getIconHeight()
	{
		return toRender.getIconHeight();
	}

	@Override
	public int getIconWidth()
	{
		return toRender.getIconWidth();
	}

	/**
	 *
	 * @return
	 */
	@Override
	public Dimension getPreferredSize()
	{
		return toRender.getPreferredSize();
	}

	public URI getSvgURI()
	{
		return toRender.getSvgURI();
	}

	/**
	 *
	 * @param p
	 */
	@Override
	public void removePropertyChangeListener( PropertyChangeListener p )
	{
		toRender.removePropertyChangeListener( p );
	}

	/**
	 *
	 * @param preferredSize
	 */
	@Override
	public void setSize( Dimension2D preferredSize )
	{
		toRender.setSize( preferredSize );
	}

	//	@Override
	//	public void setSvgResourcePath(String resourcePath)
	//	{
	//		toRender.setSvgResourcePath( resourcePath );
	//	}
	/**
	 *
	 * @param svgUniverse
	 */
	public void setSvgUniverse( SVGUniverse svgUniverse )
	{
		toRender.setSvgUniverse( svgUniverse );
	}

	@Override
	public void setSvgURI( URI svgURI )
	{
		toRender.setSvgURI( svgURI );
	}

}
