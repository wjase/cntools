/*
 * Stop.java
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
 * Created on January 26, 2004, 1:56 AM
 */

package com.kitfox.svg.elements;

import com.kitfox.svg.SVGException;
import com.kitfox.svg.xml.attributes.IStringAttribute;
import com.kitfox.svg.xml.attributes.RectangleAttribute;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
public class Symbol extends Group
{
	AffineTransform viewXform = new AffineTransform();

	//Rectangle2D viewBox;
	RectangleAttribute viewBox = new RectangleAttribute( "viewbox" );

	/** Creates a new instance of Stop */
	public Symbol()
	{
		viewBox.getRect()
			.setRect(
				0,
				0,
				1,
				1 );
		addPresentationAttributes( viewBox );
	}

	/*
	 public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent)
	 {
	 //Load style string
	 super.loaderStartElement(helper, attrs, parent);

	 String viewBoxStrn = attrs.getValue("viewBox");
	 if (viewBoxStrn != null)
	 {
	 float[] dim = XMLParseUtil.parseFloatList(viewBoxStrn);
	 viewBox = new Rectangle2D.Float(dim[0], dim[1], dim[2], dim[3]);
	 }
	 }
	 */
	/*
	public void loaderEndElement(SVGLoaderHelper helper)
	{
	    if (viewBox == null)
	    {
	        viewBox = super.getBoundingBox();
	    }

	    //Transform pattern onto unit square
	    viewXform = new AffineTransform();
	    viewXform.scale(1.0 / viewBox.getWidth(), 1.0 / viewBox.getHeight());
	    viewXform.translate(-viewBox.getX(), -viewBox.getY());
	}
	 */

	@Override
	public boolean initAtttributeValue( String name, String value )
	{
		switch (name.charAt( 0 ))
		{
			case 'v':
				if (addPresentationAttribute(
					name,
					value,
					viewBox ))
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
		if (source == viewBox)
		{
			Rectangle2D.Float rect = viewBox.getRect();
			viewXform.scale(
				1.0 / rect.getWidth(),
				1.0 / rect.getHeight() );
			viewXform.translate(
				-rect.getX(),
				-rect.getY() );

		}
		super.attributeUpdated( source );
	}

	//    protected void build() throws SVGException
	//    {
	//        super.build();
	//        
	//        StyleAttribute sty = new StyleAttribute();
	//        
	////        sty = getPres("unicode");
	////        if (sty != null) unicode = sty.getStringValue();
	//
	//
	////        if (getPres(sty.withName("viewBox")))
	////        {
	////            float[] dim = sty.getFloatList();
	////            viewBox = new Rectangle2D.Float(dim[0], dim[1], dim[2], dim[3]);
	////        }
	//        
	////        if (viewBox == null)
	////        {
	//////            viewBox = super.getBoundingBox();
	////            viewBox = new Rectangle(0, 0, 1, 1);
	////        }
	//
	//        //Transform pattern onto unit square
	//    }

	@Override
	protected boolean outsideClip( Graphics2D g ) throws SVGException
	{
		g.getClipBounds( clipBounds );
		Rectangle2D rect = super.getBoundingBox();
		if (rect.intersects( clipBounds ))
		{
			return false;
		}

		return true;

	}

	public void render( Graphics2D g ) throws SVGException
	{
		AffineTransform oldXform = g.getTransform();
		g.transform( viewXform );

		super.render( g );

		g.setTransform( oldXform );
	}

	public Shape getShape()
	{
		Shape shape = super.getShape();
		return viewXform.createTransformedShape( shape );
	}

	public Rectangle2D getBoundingBox() throws SVGException
	{
		Rectangle2D rect = super.getBoundingBox();
		return viewXform.createTransformedShape(
			rect )
			.getBounds2D();
	}

	/**
	 * Updates all attributes in this diagram associated with a time event.
	 * Ie, all attributes with track information.
	 * @return - true if this node has changed state as a result of the time
	 * update
	 */
	public boolean updateTime( double curTime ) throws SVGException
	{
		//        if (trackManager.getNumTracks() == 0) return false;
		boolean changeState = super.updateTime( curTime );

		//View box properties do not change

		return changeState;
	}

}
