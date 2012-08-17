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

import com.cybernostics.lib.animator.sprite.ISprite;
import com.cybernostics.lib.animator.sprite.component.ComponentSprite;
import com.cybernostics.lib.animator.track.BasicTimerTrack;
import com.cybernostics.lib.animator.track.Track;
import com.cybernostics.lib.gui.declarative.events.WhenResized;
import com.cybernostics.lib.svg.SubRegionContainer;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.border.Border;

/**
 * @author jasonw
 *
 */
public class SVGScenePanel2 extends AnimatedScene implements SubRegionContainer
{

	Map< String, Rectangle2D > regions = new HashMap< String, Rectangle2D >();

	/**
	 *
	 */
	private static final long serialVersionUID = 8848836686992527935L;

	public void defineRegion( String id, Rectangle2D region )
	{
		regions.put(
			id,
			region );
	}

	/**
	 * @param periodMillis
	 */
	public SVGScenePanel2()
	{
		setLayout( null );

		new WhenResized( this )
		{

			@Override
			public void doThis( ComponentEvent e )
			{
				insets2DNeedUpdate = true;
			}

		};

	}

	/**
	 *
	 * @param comp
	 * @return
	 */
	public Component addWithinBounds( JComponent comp )
	{
		String label = comp.getName();

		Rectangle2D bounds2D = (Rectangle2D) comp.getClientProperty( "bounds2D" );
		if (bounds2D != null)
		{
			return addWithinBounds(
				comp,
				bounds2D );
		}

		return addWithinBounds(
			comp,
			label );
	}

	/**
	 * @param comp
	 * @param areaLabel
	 * @return
	 */
	public Component addWithinBounds( JComponent comp, String areaLabel )
	{
		ComponentSprite cs = ComponentSprite.getFrom( comp );
		if (cs == null)
		{
			cs = new ComponentSprite( comp );
		}
		addSprite(
			cs,
			areaLabel );
		//        // get the rectangle for the named element in the SVG image
		//        Rectangle2D componentSVGRect = getItemRectangle( areaLabel );
		//        Component added = addWithinBounds( comp, componentSVGRect );
		//        comp.putClientProperty( "SVGBounds", areaLabel );
		return comp;
	}

	private boolean insets2DNeedUpdate = true;
	// Insets in Double precision

	private double top;

	private double left;

	private double right;

	private double bottom;

	@Override
	public void setBorder( Border border )
	{
		super.setBorder( border );
		insets2DNeedUpdate = true;
	}

	private void updateInsets2D()
	{
		if (insets2DNeedUpdate)
		{
			int height = getHeight();
			int width = getWidth();

			if (width > 0 && height > 0)
			{
				Insets i = getInsets();
				top = i.top / height;
				left = i.left / width;
				bottom = i.bottom / height;
				right = i.right / width;

			}
			insets2DNeedUpdate = false;
		}
	}

	/**
	 *
	 * @param label
	 * @return
	 */
	@Override
	public Rectangle2D getItemRectangle( String label )
	{
		Rectangle2D r = regions.get( label );
		if (r != null)
		{
			return r;
		}

		updateInsets2D();
		SubRegionContainer cont = getSubRegionContainer();
		if (cont != null)
		{
			r = cont.getItemRectangle( label );

		}

		if (r == null)
		{
			throw new RuntimeException( "Scene doesn't contain item with id"
				+ label );
		}

		r.setFrame(
			r.getMinX() + left,
			r.getMinY() + top,
			r.getWidth() - left - right,
			r.getHeight() - top - bottom );
		return r;
	}

	/**
	 *
	 * @param newSprite
	 * @param svgBoxName
	 */
	public void addSprite( ISprite newSprite, String svgBoxName )
	{
		super.addSprites( newSprite );
		Rectangle2D componentSVGRect = getItemRectangle( svgBoxName );
		newSprite.setRelativeBounds( componentSVGRect );
	}

	public Track fadeIn()
	{
		return new BasicTimerTrack( "fadeIn", 200 )
		{

			@Override
			public void update( float t )
			{
			}

		};
	}

}