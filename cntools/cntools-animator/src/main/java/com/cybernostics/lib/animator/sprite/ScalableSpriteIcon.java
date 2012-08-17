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

import com.cybernostics.lib.media.icon.PreferredSizeListener;
import com.cybernostics.lib.media.icon.ScalableIcon;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jasonw
 */
public class ScalableSpriteIcon implements SpriteOwner, ScalableIcon
{

	ISprite toRender = null;

	Dimension dSize = new Dimension();

	Dimension dMin = new Dimension();

	AffineTransform scaler = AffineTransform.getTranslateInstance(
		1,
		1 );

	public ScalableSpriteIcon( ISprite is )
	{
		toRender = is;
		is.setRelativeSize(
			1,
			1 );
	}

	List< OwnerSizeListener > ownerSizeListeners = new ArrayList< OwnerSizeListener >();

	@Override
	public void addUpdateListener( OwnerSizeListener listener )
	{
		ownerSizeListeners.add( listener );
	}

	@Override
	public AffineTransform getTransform()
	{
		return scaler;
	}

	@Override
	public void setSize( Dimension2D d )
	{
		this.dSize.setSize( d );
		scaler.setToScale(
			d.getWidth(),
			d.getHeight() );

		for (OwnerSizeListener eachListener : ownerSizeListeners)
		{
			eachListener.ownerSizeUpdated();
		}
	}

	@Override
	public Dimension getPreferredSize()
	{
		return this.dSize;
	}

	@Override
	public void setMinimumSize( Dimension d )
	{
		this.dMin = d;
	}

	@Override
	public ScalableIcon copy()
	{
		return new ScalableSpriteIcon( toRender );
	}

	private List< PreferredSizeListener > sizeListeners = new ArrayList< PreferredSizeListener >();

	@Override
	public void addPreferredSizeListener( PreferredSizeListener listener )
	{
		sizeListeners.add( listener );
	}

	@Override
	public BufferedImage getImage()
	{
		throw new UnsupportedOperationException( "Not supported yet." );
	}

	PropertyChangeSupport changes = new PropertyChangeSupport( this );

	@Override
	public void addPropertyChangeListener( PropertyChangeListener listener )
	{
		changes.addPropertyChangeListener( listener );
	}

	@Override
	public void paintIcon( Component c, Graphics g, int x, int y )
	{
		if (toRender.getOwner() == null)
		{
			toRender.setOwner( new ComponentSpriteOwner( (Container) c ) );
			if (dSize == null)
			{
				setSize( c.getSize() );
			}
		}
		toRender.render( (Graphics2D) g );
	}

	@Override
	public int getIconWidth()
	{
		return dSize != null ? dSize.width : 0;
	}

	@Override
	public int getIconHeight()
	{
		return dSize != null ? dSize.height : 0;
	}

	@Override
	public GraphicsConfiguration getGraphicsConfiguration()
	{
		throw new UnsupportedOperationException( "Not supported yet." );
	}

}
