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

import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import com.cybernostics.lib.svg.SubRegionContainer;
import java.awt.geom.Rectangle2D;
import java.net.URL;
import javax.swing.Icon;

/**
 *
 * @author jasonw
 */
public class SpriteConfig
{

	ISprite inner = null;

	private SpriteConfig( ISprite toSet )
	{
		inner = toSet;
	}

	public static SpriteConfig set( ISprite toSet )
	{
		return new SpriteConfig( toSet );
	}

	public SpriteConfig id( String s )
	{
		inner.setId( s );
		return this;
	}

	public SpriteConfig bounds( double x, double y, double w, double h )
	{
		inner.setRelativeBounds(
			x,
			y,
			w,
			h );
		return this;
	}

	public SpriteConfig bounds( Rectangle2D bnds )
	{
		inner.setRelativeBounds( bnds );
		return this;
	}

	public SpriteConfig boundsFromId( SubRegionContainer cont )
	{
		inner.setRelativeBounds( cont.getItemRectangle( inner.getId() ) );
		return this;
	}

	public SpriteConfig alpha( double val )
	{
		inner.setTransparency( val );
		return this;
	}

	public SpriteConfig scale( double val )
	{
		inner.setTransparency( val );
		return this;
	}

	public SpriteConfig icon( Icon ic )
	{
		( (IconSprite) inner ).setIcon( ic );
		return this;
	}

	public SpriteConfig icon( URL imgSrc )
	{
		if (imgSrc == null)
		{
			throw new RuntimeException();
		}
		ScalableSVGIcon s = new ScalableSVGIcon( imgSrc );
		( (IconSprite) inner ).setIcon( s );
		return this;
	}

	public SpriteConfig anchor( Anchor.Position toSet )
	{
		inner.setAnchor( toSet );
		return this;
	}

	public SpriteConfig zorder( int z_order )
	{
		inner.setZ_order( z_order );
		return this;
	}

}
