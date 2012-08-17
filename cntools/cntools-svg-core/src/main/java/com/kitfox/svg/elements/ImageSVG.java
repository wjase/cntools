/*
 * Font.java
 * 
 * 
 * The Salamander Project - 2D and 3D graphics libraries in Java Copyright (C) 2004 Mark McKay
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 * 
 * Mark McKay can be contacted at mark@kitfox.com. Salamander and other projects can be found at
 * http://www.kitfox.com
 * 
 * Created on February 20, 2004, 10:00 PM
 */
package com.kitfox.svg.elements;

import com.kitfox.svg.SVGException;
import com.kitfox.svg.xml.CSSUnits;
import com.kitfox.svg.xml.attributes.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Implements an embedded font.
 *
 * SVG specification: http://www.w3.org/TR/SVG/fonts.html
 *
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
public class ImageSVG extends RenderableElement implements HasBounds
{

	private static BufferedImage notLinkedImage = null;

	private static BufferedImage getUnlinkedImage()
	{
		if (notLinkedImage == null)
		{
			BufferedImage img = new BufferedImage( 100,
				100,
				BufferedImage.TYPE_INT_ARGB );
			Graphics2D g = img.createGraphics();
			g.setColor( Color.white );
			g.fill( new Rectangle2D.Double( 0, 0, 100, 100 ) );
			g.setColor( Color.red );
			g.setStroke( new BasicStroke( 5 ) );
			g.drawLine(
				0,
				0,
				100,
				100 );
			g.drawLine(
				0,
				100,
				100,
				0 );
			g.dispose();
			notLinkedImage = img;

		}
		return notLinkedImage;
	}

	private CSSQuantityAttribute x = new CSSQuantityAttribute( "x" );

	private CSSQuantityAttribute y = new CSSQuantityAttribute( "y" );

	private CSSQuantityAttribute width = new CSSQuantityAttribute( "width" );

	private CSSQuantityAttribute height = new CSSQuantityAttribute( "height" );

	public CSSQuantityAttribute getWidth()
	{
		return width;
	}

	public CSSQuantityAttribute getHeight()
	{
		return height;
	}

	public CSSQuantityAttribute getX()
	{
		return x;
	}

	public CSSQuantityAttribute getY()
	{
		return y;
	}

	private ImageRefAttribute xlinkhref = new ImageRefAttribute( "xlink:href",
		this );

	public ImageRefAttribute getXlinkHref()
	{
		return xlinkhref;
	}

	private CSSQuantityAttribute opacity = new CSSQuantityAttribute( "opacity" )
	{

		@Override
		public void setStringValue( String value )
		{
			super.setStringValue( value );
		}

		@Override
		public void setValue( float value, CSSUnits units )
		{
			super.setValue(
				value,
				units );
		}

		@Override
		public void setQuantity( float value )
		{
			super.setQuantity( value );
		}

	};

	public float getOpacity()
	{
		if (opacity.isSet())
		{
			return opacity.getFloatValue();
		}

		ParentAttribute parentOpacityWrapper = getStyleFromParent( "opacity" );

		if (parentOpacityWrapper != null)
		{
			CSSQuantityAttribute parentOpacity = (CSSQuantityAttribute) parentOpacityWrapper.getParentValue();
			if (parentOpacity.isSet())
			{
				return parentOpacity.getFloatValue();
			}
		}

		return 1;
	}

	//    private URL imageSrc = null;
	private AffineTransform imageXform = new AffineTransform();

	private boolean boundsSet = false;

	private Rectangle2D bounds = new Rectangle2D.Double();

	/**
	 * Creates a new instance of Font
	 */
	public ImageSVG()
	{
		addPresentationAttributes(
			x,
			y,
			width,
			height,
			xlinkhref );
		bindStyleAttributes( opacity );
		width.setDefault( new CSSQuantityDefaultValue()
		{

			@Override
			public CSSQuantity get()
			{
				BufferedImage bi = xlinkhref.getImageValue();
				return new CSSQuantity( bi != null ? bi.getWidth() : 0 );
			}

		} );
		height.setDefault( new CSSQuantityDefaultValue()
		{

			@Override
			public CSSQuantity get()
			{
				BufferedImage bi = xlinkhref.getImageValue();
				return new CSSQuantity( bi != null ? bi.getWidth() : 0 );
			}

		} );

	}

	@Override
	public boolean initStyleAtttributeValue( String name, String value )
	{
		switch (name.charAt( 0 ))
		{
			case 'o':
				if (setStyleAttribute(
					name,
					value,
					opacity ))
				{
					return true;
				}
		}
		return super.initStyleAtttributeValue(
			name,
			value );
	}

	AlphaComposite comp = null;

	private AlphaComposite getComposite( float forValue )
	{
		if (comp == null || ( comp.getAlpha() != forValue ))
		{
			comp = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER,
				forValue );
		}
		return comp;
	}

	@Override
	public boolean initAtttributeValue( String name, String value )
	{
		switch (name.charAt( 0 ))
		{
			case 'x':
				if (addPresentationAttribute(
					name,
					value,
					x,
					xlinkhref ))
				{
					return true;
				}
			case 'y':
				if (addPresentationAttribute(
					name,
					value,
					y ))
				{
					return true;
				}
			case 'w':
				if (addPresentationAttribute(
					name,
					value,
					width ))
				{
					return true;
				}
			case 'h':
				if (addPresentationAttribute(
					name,
					value,
					height ))
				{
					return true;
				}
		}
		return super.initAtttributeValue(
			name,
			value );
	}

	@Override
	public void attributeUpdated( IStringAttribute source )
	{
		if (source == x || source == y || source == width || source == height
			|| source == xlinkhref)
		{
			updateBounds();

		}
	}

	public void updateBounds()
	{
		BufferedImage img = xlinkhref.getImageValue();
		if (img != null && x.isSet() && y.isSet() && width.isSet()
			&& height.isSet())
		{
			imageXform.setToIdentity();

			double dWidth = width.isSet() ? width.getFloatValue()
				: img.getWidth();
			double dHeight = height.isSet() ? height.getFloatValue()
				: img.getHeight();

			if (x.isSet() && y.isSet())
			{
				imageXform.translate(
					x.getFloatValue(),
					y.getFloatValue() );
			}

			if (width.isSet() && height.isSet())
			{
				imageXform.scale(
					dWidth / img.getWidth(),
					dHeight / img.getHeight() );
			}

			bounds.setFrame(
				x.getFloatValue(),
				y.getFloatValue(),
				dWidth,
				dHeight );
			boundsSet = true;
		}

	}

	void pick( Point2D point, boolean boundingBox, List retVec )
		throws SVGException
	{
		if (getBoundingBox().contains(
			point ))
		{
			retVec.add( getPath( null ) );
		}
	}

	@Override
	void pick( Rectangle2D pickArea,
		AffineTransform ltw,
		boolean boundingBox,
		List retVec ) throws SVGException
	{
		if (ltw.createTransformedShape(
			getBoundingBox() )
			.intersects(
				pickArea ))
		{
			retVec.add( getPath( null ) );
		}
	}

	@Override
	public void render( Graphics2D g ) throws SVGException
	{
		if (!isVisible())
		{
			return;
		}

		if (!boundsSet)
		{
			updateBounds();
		}
		if (!boundsSet)
		{
			return;
		}
		beginLayer( g );

		float opacityValue = getOpacity();

		if (opacityValue <= 0)
		{
			return;
		}

		Composite oldComp = null;

		if (opacityValue < 1)
		{
			oldComp = g.getComposite();
			g.setComposite( getComposite( opacityValue ) );
		}

		BufferedImage img = xlinkhref.getImageValue();
		//        try
		//        {
		//            img = diagram.getUniverse().getImage( imageSrc );
		//        }
		//        catch ( Exception e )
		//        {
		//            throw new SVGElementException( parent, e );
		//        }
		if (img == null)
		{
			img = getUnlinkedImage();
		}

		AffineTransform curXform = g.getTransform();
		if (imageXform != null)
		{
			g.transform( imageXform );
		}

		g.drawImage(
			img,
			0,
			0,
			null );

		g.setTransform( curXform );
		if (oldComp != null)
		{
			g.setComposite( oldComp );
		}

		finishLayer( g );
	}

	@Override
	public Rectangle2D getBoundingBox()
	{
		if (bounds.getWidth() == 0)
		{
			updateBounds();
		}
		return boundsToParent( bounds );
	}

	/**
	 * Updates all attributes in this diagram associated with a time event. Ie,
	 * all attributes with track information.
	 *
	 * @return - true if this node has changed state as a result of the time
	 * update
	 */
	@Override
	public boolean updateTime( double curTime ) throws SVGException
	{
		//        if (trackManager.getNumTracks() == 0) return false;
		boolean changeState = super.updateTime( curTime );

		//        //Get current values for parameters
		//        StyleAttribute sty = new StyleAttribute();
		//        boolean shapeChange = false;
		//
		//        if ( getPres( sty.setName( "x" ) ) )
		//        {
		//            float newVal = sty.getFloatValueWithUnits();
		//            if ( newVal != x )
		//            {
		//                x = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//
		//        if ( getPres( sty.setName( "y" ) ) )
		//        {
		//            float newVal = sty.getFloatValueWithUnits();
		//            if ( newVal != y )
		//            {
		//                y = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//
		//        if ( getPres( sty.setName( "width" ) ) )
		//        {
		//            float newVal = sty.getFloatValueWithUnits();
		//            if ( newVal != width )
		//            {
		//                width = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//
		//        if ( getPres( sty.setName( "height" ) ) )
		//        {
		//            float newVal = sty.getFloatValueWithUnits();
		//            if ( newVal != height )
		//            {
		//                height = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//
		//        try
		//        {
		//            if ( getPres( sty.setName( "xlink:href" ) ) )
		//            {
		//                URI src = sty.getURIValue( getXMLBase() );
		//                URL newVal = src.toURL();
		//
		//                if ( !newVal.equals( imageSrc ) )
		//                {
		//                    imageSrc = newVal;
		//                    shapeChange = true;
		//                }
		//            }
		//        }
		//        catch ( IllegalArgumentException ie )
		//        {
		//            new Exception( "Image provided with illegal value for href: \"" + sty.getStringValue() + '"', ie ).printStackTrace();
		//        }
		//        catch ( Exception e )
		//        {
		//            e.printStackTrace();
		//        }
		//
		//
		//        if ( shapeChange )
		//        {
		//            build();
		//            diagram.getUniverse().registerImage(imageSrc);
		//
		//            //Set widths if not set
		//            BufferedImage img = diagram.getUniverse().getImage(imageSrc);
		//            if (img == null)
		//            {
		//                xform = new AffineTransform();
		//                bounds = new Rectangle2D.Float();
		//            }
		//            else
		//            {
		//                if (width == 0) width = img.getWidth();
		//                if (height == 0) height = img.getHeight();
		//
		//                //Determine image xform
		//                xform = new AffineTransform();
		////                xform.setToScale(this.width / img.getWidth(), this.height / img.getHeight());
		////                xform.translate(this.x, this.y);
		//                xform.translate(this.x, this.y);
		//                xform.scale(this.width / img.getWidth(), this.height / img.getHeight());
		//
		//                bounds = new Rectangle2D.Float(this.x, this.y, this.width, this.height);
		//            }
		//
		//            return true;
		//        }

		return changeState;//|| shapeChange;
	}

}
