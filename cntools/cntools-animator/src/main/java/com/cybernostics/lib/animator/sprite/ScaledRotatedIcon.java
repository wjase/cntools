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

import com.cybernostics.lib.gui.CachedImage;
import com.cybernostics.lib.gui.GraphicsConfigurationSource;
import com.cybernostics.lib.gui.shape.IconRect;
import com.cybernostics.lib.gui.shape.ShapeUtils2D;
import com.cybernostics.lib.gui.shapeeffects.IconEffect;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.Icon;

/**
 * In the quest for performance rotation and scaling seem to be more expensive
 * than translation. In addition, when using vector icons as sprites,
 * performance suffers if they have to be drawn and redrawn rather than just
 * blitted to the screen. This class caches a scaled rotated version of an icon.
 *
 * @author jasonw
 */
public class ScaledRotatedIcon implements IconTransformer
{

	private class TransformedRenderer extends IconEffect
	{

		public TransformedRenderer( Icon ic )
		{
			super( ic );
		}

		@Override
		public void draw( Graphics2D g2, Shape s )
		{
			g2.transform( rotator );
			super.draw(
				g2,
				s );
		}

	}

	@Override
	public void setIcon( Icon toSet )
	{
		this.srcImage = toSet;
		cached.setRenderer( new TransformedRenderer( toSet ) );
		myAnchor.setItemRect( IconRect.get( toSet ) );
		calculateMaxSide();
		updateTransform();
	}

	private Point2D hotspot = new Point2D.Double();

	@Override
	public Point2D getHotspot()
	{
		return hotspot;
	}

	@Override
	public void updateScaleRotationWith( IconTransformer current )
	{
		if (current != null)
		{
			setAngle( current.getAngle() );
			setScale( current.getScale() );
			setFlip( current.getFlip() );

		}

	}

	public static SingletonInstance< AffineTransform[] > flips = new SingletonInstance< AffineTransform[] >()
	{

		@Override
		protected AffineTransform[] createInstance()
		{
			AffineTransform[] res = new AffineTransform[ FlipType.values().length ];
			res[ FlipType.HORIZONTAL.ordinal() ] = AffineTransform.getScaleInstance(
				-1,
				1 );
			res[ FlipType.VERTICAL.ordinal() ] = AffineTransform.getScaleInstance(
				1,
				-1 );
			res[ FlipType.BOTH.ordinal() ] = AffineTransform.getScaleInstance(
				-1,
				-1 );
			res[ FlipType.NONE.ordinal() ] = AffineTransform.getScaleInstance(
				1,
				1 );
			return res;
		}

	};

	private FlipType myFlip = FlipType.NONE;

	private AffineTransform flipTransform = flips.get()[ FlipType.NONE.ordinal() ];

	@Override
	public FlipType getFlip()
	{
		return myFlip;
	}

	@Override
	public void setFlip( FlipType f )
	{
		myFlip = f;
		flipTransform = flips.get()[ myFlip.ordinal() ];
		updateTransform();
	}

	public enum Framing
	{

		TIGHT, FULL
	}

	private Framing frameType = Framing.FULL;

	public Framing getFrameType()
	{
		return frameType;
	}

	/**
	 * The an image is created which allows the image to be rotated without any
	 * clipping. The framing type tells this icon object whether or not to paint
	 * the whole rectangle containing the image, or just the tightly framed
	 * image.
	 *
	 * @param frameType
	 */
	public void setFrameType( Framing frameType )
	{
		this.frameType = frameType;
	}

	/**
	 * If set to true then this items cached image should be redrawn.
	 *
	 * @param needsRender
	 */
	@Override
	public void setNeedsRender( boolean needsRender )
	{
		cached.setNeedsRender( needsRender );
	}

	private Icon srcImage = null;

	private double scale = 1.0;

	private double angle = 0.0;

	private Dimension unscaledSize = new Dimension();

	private GraphicsConfigurationSource gcSource = null;

	private int maxSide = 0;

	private AffineTransform rotator = AffineTransform.getTranslateInstance(
		0,
		0 );

	private Rectangle2D transformedBounds = new Rectangle2D.Double();

	private CachedImage cached = null;

	/**
	 * Gets the image bounds tranformed by the rotation and scaling The
	 * coordinates are relative to the image which would be shown if the Framing
	 * type is FULL.
	 *
	 * @return
	 */
	@Override
	public Rectangle2D getTransformedBounds()
	{
		return transformedBounds;
	}

	public ScaledRotatedIcon(
		Icon toRotate,
		Dimension2D unscaled,
		GraphicsConfigurationSource src )
	{
		cached = new CachedImage();
		gcSource = src;
		//        this.srcImage = toRotate;
		unscaledSize.setSize( unscaled );
		//        myAnchor.setItemRect( IconRect.get( toRotate ) );
		setIcon( toRotate );

	}

	/**
	 * this decouples the actual icon dimensions from the size on the owner
	 * component. The icon might be 32x32 but you want to to be 64x64 This is
	 * the height the sprite will be if scale is set to 1.0.
	 *
	 * @param unscaled
	 */
	@Override
	public void setUnScaledSize( Dimension unscaled )
	{
		this.unscaledSize.setSize( unscaled );
		calculateMaxSide();
	}

	@Override
	public Dimension getUnscaledSize()
	{
		return unscaledSize;
	}

	/**
	 * calculates the biggest area required to fit the scaled icon
	 */
	public final void calculateMaxSide()
	{
		int oldMaxSide = maxSide;

		double finalmax = Math.max(
			unscaledSize.getWidth(),
			unscaledSize.getHeight() );
		double max = Math.max(
			srcImage.getIconWidth(),
			srcImage.getIconHeight() );
		double scalemax = scale * finalmax / max;
		Rectangle2D r = new Rectangle2D.Double( 0, 0, max, max );
		AffineTransform maxBounds = AffineTransform.getScaleInstance(
			scalemax * aspectRatio,
			scalemax );
		maxBounds.rotate(
			Math.PI / 4,
			max / 2,
			max / 2 );
		r = ShapeUtils2D.getBounds( maxBounds.createTransformedShape( r ) );
		maxSide = (int) ( r.getWidth() );

		// maxSide = ( int ) Math.sqrt( ( height * height ) + ( width * width ) );

		//maxSide = ( int ) ( Math.max( unscaledSize.getHeight(), unscaledSize.getWidth() ) * ROOTTWO * scale ) + 4;

		if (oldMaxSide != maxSide)
		{
			cached.setSize(
				maxSide,
				maxSide );
			updateTransform();
		}
	}

	@Override
	public void setScale( double scale )
	{
		if (this.scale != scale)
		{
			this.scale = scale;
			calculateMaxSide();
		}

	}

	// Controls offsetting the hotspot to a position on
	private Anchor myAnchor = new Anchor();

	/**
	 * Sets the position offset of the sprite location. Default is top left
	 * (NORTHWEST)
	 *
	 * @param locationAnchor
	 */
	@Override
	public void setAnchor( Anchor.Position locationAnchor )
	{
		myAnchor.setPosition( locationAnchor );
		updateHostpot();

	}

	/**
	 * Returns the transform for this scale and rotate
	 *
	 * @return
	 */
	@Override
	public AffineTransform getTransform()
	{
		AffineTransform retVal = (AffineTransform) rotator.clone();
		return retVal;
	}

	private void updateTransform()
	{
		rotator.setToIdentity();
		rotator.translate(
			( ( maxSide ) / 2 ),
			( maxSide ) / 2 );
		rotator.concatenate( flipTransform );
		rotator.scale(
			1.0 * scale * unscaledSize.getWidth() / srcImage.getIconWidth(),
			1.0 * scale
				* ( unscaledSize.getHeight() / srcImage.getIconHeight() ) );
		rotator.translate(
			( -srcImage.getIconWidth() / 2 ),
			-srcImage.getIconHeight() / 2 );
		rotator.rotate(
			Math.toRadians( angle ),
			srcImage.getIconWidth() / 2,
			srcImage.getIconHeight() / 2 );

		transformedBounds.setFrame(
			0,
			0,
			srcImage.getIconWidth(),
			srcImage.getIconHeight() );
		Shape s = rotator.createTransformedShape( transformedBounds );
		transformedBounds.setFrame( s.getBounds2D() );

		setNeedsRender( true );
		updateHostpot();
	}

	@Override
	public Anchor getAnchor()
	{
		return myAnchor;
	}

	private void updateHostpot()
	{
		hotspot.setLocation( myAnchor.getAnchorLocation() );
		rotator.transform(
			hotspot,
			hotspot );
	}

	@Override
	public void setAngle( double angle )
	{
		if (this.angle == angle)
		{
			return;
		}
		this.angle = angle;
		updateTransform();
	}

	@Override
	public double getAngle()
	{
		return angle;
	}

	@Override
	public synchronized void paintIcon( Component c, Graphics g, int x, int y )
	{
		switch (frameType)
		{

			case FULL:
				cached.draw(
					(Graphics2D) g,
					x,
					y );

				break;
			case TIGHT:
				cached.draw(
					(Graphics2D) g,
					x - (int) transformedBounds.getMinX(),
					y
						- (int) transformedBounds.getMinY() );
				//g.drawImage( cached.getImage(), x - ( int ) transformedBounds.getMinX(), y - ( int ) transformedBounds.getMinY(), null );
				break;
		}
	}

	@Override
	public int getIconWidth()
	{
		switch (frameType)
		{
			case FULL:
				return maxSide;
			case TIGHT:
				return (int) transformedBounds.getWidth() + 4;
		}
		return 0;

	}

	@Override
	public int getIconHeight()
	{
		switch (frameType)
		{
			case FULL:
				return maxSide;
			case TIGHT:
				return (int) transformedBounds.getHeight() + 4;
		}
		return 0;
	}

	@Override
	public double getScale()
	{
		return scale;
	}

	@Override
	public Icon getIcon()
	{
		return srcImage;
	}

	/**
	 * width over height for the screen
	 */
	private double aspectRatio = 1;

	public double getAspectRatio()
	{
		return aspectRatio;
	}

	public void setAspectRatio( double aspectRatio )
	{
		this.aspectRatio = aspectRatio;
	}

}
