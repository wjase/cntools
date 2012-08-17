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

import com.cybernostics.lib.gui.declarative.events.WhenResized;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jasonw
 */
public class ComponentSpriteOwner implements SpriteOwner
{

	protected AffineTransform screenSizeScaler = new AffineTransform();

	private List< OwnerSizeListener > listeners = new ArrayList< OwnerSizeListener >();

	private void updateScaler( Container c )
	{
		Dimension d = c.getSize();
		screenSizeScaler.setToScale(
			d.getWidth(),
			d.getHeight() );

	}

	private Container container = null;

	public ComponentSpriteOwner( Container c )
	{
		container = c;

		new WhenResized( c )
		{

			@Override
			public void doThis( ComponentEvent e )
			{
				updateScaler( (Container) e.getComponent() );
				for (OwnerSizeListener eachListener : listeners)
				{
					eachListener.ownerSizeUpdated();
				}
			}

		};
	}

	//    @Override
	//    public void scale( Dimension2D toScale )
	//    {
	//        Point2D pt = new Point2D.Double( toScale.getWidth(), toScale.getHeight() );
	//        screenSizeScaler.transform( pt, pt );
	//        toScale.setSize( pt.getX(), pt.getY() );
	//    }
	@Override
	public void addUpdateListener( OwnerSizeListener listener )
	{
		listeners.add( listener );
	}

	//    @Override
	//    public void scale( Point2D toScale )
	//    {
	//        screenSizeScaler.transform( toScale, toScale );
	//    }
	@Override
	public AffineTransform getTransform()
	{
		return screenSizeScaler;
	}

	@Override
	public GraphicsConfiguration getGraphicsConfiguration()
	{
		return container.getGraphicsConfiguration();
	}

}
