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

import com.cybernostics.lib.gui.shape.ShapeUtils2D;
import com.cybernostics.lib.gui.shapeeffects.ShapeEffect;
import com.cybernostics.lib.maths.DoubleDimension;
import com.cybernostics.lib.media.icon.ScalableImageIcon;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author jasonw
 *
 */
public class BitmapBackground implements ShapeEffect
{

	ScalableImageIcon src = null;

	ArrayList< BackgroundImageListener > backgroundListeners = new ArrayList< BackgroundImageListener >();

	boolean loaded = false;

	/**
	 *
	 *
	 * @param bi
	 */
	public BitmapBackground( BufferedImage bi )
	{
		src = new ScalableImageIcon( bi );
	}

	//    /**
	//     *
	//     * @param c
	//     * @param g
	//     */
	//    @Override
	//    public void paint( Component c, Graphics g )
	//    {
	//        Rectangle r = g.getClipBounds();
	//        float widthScale = ( 1.0f * c.getWidth() / backgroundSource.getWidth() );
	//        float heightScale = ( 1.0f * c.getHeight() / backgroundSource.getHeight() );
	//        if ( r != null )
	//        {
	//            Rectangle srcRect = new Rectangle( ( int ) ( r.x * widthScale ), ( int ) ( r.y * heightScale ),
	//                                               ( int ) ( r.width * widthScale ), ( int ) ( r.height * heightScale ) );
	//            g.drawImage( backgroundSource, r.x, r.y, r.width, r.height, srcRect.x, srcRect.y, backgroundSource.getWidth(),
	//                         backgroundSource.getHeight(), null );
	//        }
	//        else
	//        {
	//            System.out.println( "no clip" );
	//            g.drawImage( backgroundSource, 0, 0, c.getWidth(), c.getHeight(), 0, 0, backgroundSource.getWidth(),
	//                         backgroundSource.getHeight(), null );
	//        }
	//
	//    }

	/**
	 * @return the loaded
	 */
	public boolean isLoaded()
	{
		return loaded;
	}

	Dimension2D dim = new DoubleDimension();

	/*
	 * (non-Javadoc) @see
	 * com.cybernostics.lib.animator.BackgroundImage#addBackgroundImageListener(com.cybernostics.lib.animator.BackgroundImageListener)
	 */
	/**
	 *
	 * @param listener
	 */
	@Override
	public void draw( Graphics2D g2, Shape s )
	{
		Rectangle2D bounds = ShapeUtils2D.getBounds( s );
		dim.setSize(
			bounds.getWidth(),
			bounds.getHeight() );
		src.paintIcon(
			null,
			g2,
			(int) bounds.getMinX(),
			(int) bounds.getMinY() );
	}

}
