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

package com.cybernostics.lib.animator.track;

import com.cybernostics.lib.gui.shapeeffects.ColorFill;
import com.cybernostics.lib.gui.shapeeffects.CompositeAdjust;
import com.cybernostics.lib.gui.shapeeffects.NOPEffect;
import com.cybernostics.lib.gui.shapeeffects.ShapeEffect;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 *
 * @author jasonw
 */
public class FadingScreen implements ShapeEffect
{

	ShapeEffect screenEffect = NOPEffect.get();

	CompositeAdjust transparency = new CompositeAdjust( 1.0 );

	FadingScreen()
	{
		this( new ColorFill( Color.BLACK ) );
	}

	FadingScreen( ShapeEffect screenEffect )
	{
	}

	@Override
	public void draw( Graphics2D g2, Shape s )
	{
		throw new UnsupportedOperationException( "Not supported yet." );
	}

	public void setTransparency( double value )
	{
		transparency.setLevel( value );
	}

}
