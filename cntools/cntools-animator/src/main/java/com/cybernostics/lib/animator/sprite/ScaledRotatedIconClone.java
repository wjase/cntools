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

import com.cybernostics.lib.animator.sprite.Anchor.Position;
import com.cybernostics.lib.gui.graphics.StateSaver;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.Icon;

/**
 *
 * @author jasonw
 */
public class ScaledRotatedIconClone implements IconTransformer
{

	@Override
	public Dimension getUnscaledSize()
	{
		return toClone.getUnscaledSize();
	}

	private AffineTransform scaler = AffineTransform.getScaleInstance(
		1,
		1 );

	private IconTransformer toClone;

	public ScaledRotatedIconClone( IconTransformer toClone )
	{
		this.toClone = toClone;
	}

	@Override
	public void paintIcon( Component c, Graphics g, int x, int y )
	{
		StateSaver gSaved = StateSaver.wrap( g );
		try
		{
			gSaved.translate(
				x,
				y );

			//            int width = getIconWidth();
			//            int height = getIconHeight();
			//
			//            AffineTransform at = AffineTransform.getTranslateInstance( width / 2, height / 2 );
			//            at.concatenate( scaler );
			//            at.concatenate( flipTransform );
			////            at.concatenate( AffineTransform.getTranslateInstance(  ));
			//            at.translate( -( at.getTranslateX() / scaler.getScaleX() ), -( at.getTranslateY() / scaler.getScaleY() ) );
			gSaved.transform( getMyTransform() );
			toClone.paintIcon(
				c,
				gSaved,
				0,
				0 );
		}
		finally
		{
			gSaved.restore();
		}
	}

	int lastWidth = 0;

	int lastHeight = 0;

	FlipType lastFlipType = null;

	double lastScale = 0;

	private boolean isTransformDirty( int width, int height )
	{
		return ( myTransform == null ) || ( width != lastWidth )
			|| ( height != lastHeight )
			|| ( scaler.getScaleX() != lastScale ) || ( myFlip != lastFlipType );
	}

	private void setTransformUpdated( int width, int height )
	{
		lastWidth = width;
		lastHeight = height;
		lastScale = scaler.getScaleX();
		lastFlipType = myFlip;

	}

	AffineTransform myTransform = null;

	private AffineTransform getMyTransform()
	{
		int width = getIconWidth();
		int height = getIconHeight();
		if (isTransformDirty(
			width,
			height ))
		{
			myTransform = AffineTransform.getTranslateInstance(
				width / 2,
				height / 2 );
			myTransform.concatenate( scaler );
			myTransform.concatenate( flipTransform );
			//            at.concatenate( AffineTransform.getTranslateInstance(  ));
			myTransform.translate(
				-( myTransform.getTranslateX() / scaler.getScaleX() ),
				-( myTransform.getTranslateY() / scaler.getScaleY() ) );
			setTransformUpdated(
				width,
				height );
		}

		return (AffineTransform) myTransform.clone();
	}

	@Override
	public int getIconWidth()
	{
		return (int) ( toClone.getIconWidth() * scaler.getScaleX() );
	}

	@Override
	public int getIconHeight()
	{
		return (int) ( toClone.getIconHeight() * scaler.getScaleY() );
	}

	Rectangle2D myxFormedBounds = new Rectangle2D.Double();

	@Override
	public Rectangle2D getTransformedBounds()
	{
		myxFormedBounds.setFrame( toClone.getTransformedBounds() );
		myxFormedBounds = scaler.createTransformedShape(
			myxFormedBounds )
			.getBounds2D();
		return myxFormedBounds;
	}

	@Override
	public double getScale()
	{
		return toClone.getScale();
	}

	@Override
	public Icon getIcon()
	{
		return toClone.getIcon();
	}

	@Override
	public void setIcon( Icon toSet )
	{
		toClone.setIcon( toSet );
	}

	@Override
	public void setAngle( double angle )
	{
		throw new UnsupportedOperationException( "Not supported for clone object." );
	}

	@Override
	public double getAngle()
	{
		return toClone.getAngle();
	}

	AffineTransform myXFTransform = AffineTransform.getTranslateInstance(
		0,
		0 );

	@Override
	public AffineTransform getTransform()
	{
		myXFTransform.setTransform( toClone.getTransform() );
		myXFTransform.concatenate( getMyTransform() );

		return myXFTransform;
	}

	@Override
	public void setNeedsRender( boolean needsRender )
	{
		// do nothing - we rely on the source being updated directly
	}

	@Override
	public void setUnScaledSize( Dimension unscaled )
	{
		// do nothing we are just a scaled version of another icon
	}

	@Override
	public void setScale( double scale )
	{
		scaler.setToScale(
			scale,
			scale );
	}

	@Override
	public void setAnchor( Position locationAnchor )
	{
		throw new UnsupportedOperationException( "Not supported for clone sprites." );
	}

	private Point2D hotspot = new Point2D.Double();

	@Override
	public Point2D getHotspot()
	{
		hotspot.setLocation( getAnchor().getAnchorLocation() );
		toClone.getTransform()
			.transform(
				hotspot,
				hotspot );
		getMyTransform().transform(
			hotspot,
			hotspot );

		return hotspot;
	}

	@Override
	public void updateScaleRotationWith( IconTransformer current )
	{
		throw new UnsupportedOperationException( "Not supported for clone sprites." );
	}

	private FlipType myFlip = FlipType.NONE;

	private AffineTransform flipTransform = ScaledRotatedIcon.flips.get()[ FlipType.NONE.ordinal() ];

	@Override
	public FlipType getFlip()
	{
		return myFlip;
	}

	@Override
	public void setFlip( FlipType f )
	{
		myFlip = f;
		flipTransform = ScaledRotatedIcon.flips.get()[ myFlip.ordinal() ];
	}

	@Override
	public Anchor getAnchor()
	{
		return toClone.getAnchor();
	}

	@Override
	public void setAspectRatio( double d )
	{

	}

}
