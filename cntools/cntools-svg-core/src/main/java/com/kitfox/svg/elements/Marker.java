/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.elements;

import com.kitfox.svg.SVGException;
import com.kitfox.svg.xml.attributes.*;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 *
 * @author kitfox
 */
public class Marker extends Group
{

	private AffineTransform viewXform = new AffineTransform();

	private AffineTransform markerXform = new AffineTransform();

	private RectangleAttribute viewBox = new RectangleAttribute( "viewBox" );

	CSSQuantityAttribute refX = new CSSQuantityAttribute( "refX" );

	CSSQuantityAttribute refY = new CSSQuantityAttribute( "refY" );

	CSSQuantityAttribute markerWidth = new CSSQuantityAttribute( "markerWidth" );// = 3;

	CSSQuantityAttribute markerHeight = new CSSQuantityAttribute( "markerHeight" );// = 3;

	CSSQuantityAttribute orient = new CSSQuantityAttribute( "orient" ); //= Float.NaN;

	//FloatListAttribute viewBox = new FloatListAttribute( "viewBox");
	public Marker()
	{
		markerWidth.setDefault( new CSSQuantityDefaultConstant( 3 ) );
		markerHeight.setDefault( new CSSQuantityDefaultConstant( 3 ) );

		orient.setDefault( new CSSQuantityDefaultValue()
		{

			@Override
			public CSSQuantity get()
			{
				return new CSSQuantity();
			}

		} );

		addPresentationAttributes(
			refX,
			refY,
			markerHeight,
			markerWidth,
			orient,
			viewBox );
	}

	@Override
	public void attributeUpdated( IStringAttribute source )
	{
		if (source == viewBox)
		{
			Rectangle2D rect = viewBox.getRect();
			viewXform.setToIdentity();
			viewXform.scale(
				1.0 / rect.getWidth(),
				1.0 / rect.getHeight() );
			viewXform.translate(
				-rect.getX(),
				-rect.getY() );
			return;

		}

		if (source == refX || source == refY || source == markerWidth
			|| source == markerHeight)
		{
			markerXform.setToIdentity();
			markerXform.scale(
				markerWidth.getNormalisedValue(),
				markerHeight.getNormalisedValue() );
			markerXform.concatenate( viewXform );
			markerXform.translate(
				-refX.getNormalisedValue(),
				-refY.getNormalisedValue() );

		}
		super.attributeUpdated( source );
	}

	@Override
	public boolean initAtttributeValue( String name, String value )
	{
		switch (name.charAt( 0 ))
		{
			case 'r':
				if (addPresentationAttribute(
					name,
					value,
					refX,
					refY ))
				{
					return true;
				}
			case 'm':
				if (addPresentationAttribute(
					name,
					value,
					markerHeight,
					markerWidth ))
				{
					return true;
				}
			case 'o':
				if (addPresentationAttribute(
					name,
					value,
					orient ))
				{
					return true;
				}
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

	//    protected void build() throws SVGException
	//    {
	//        super.build();
	//
	//        StyleAttribute sty = new StyleAttribute();

	//        if ( getPres( sty.setName( "refX" ) ) )
	//        {
	//            refX = sty.getFloatValueWithUnits();
	//        }
	//        if ( getPres( sty.setName( "refY" ) ) )
	//        {
	//            refY = sty.getFloatValueWithUnits();
	//        }
	//        if ( getPres( sty.setName( "markerWidth" ) ) )
	//        {
	//            markerWidth = sty.getFloatValueWithUnits();
	//        }
	//        if ( getPres( sty.setName( "markerHeight" ) ) )
	//        {
	//            markerHeight = sty.getFloatValueWithUnits();
	//        }
	//
	//        if ( getPres( sty.setName( "orient" ) ) )
	//        {
	//            if ( "auto".equals( sty.getStringValue() ) )
	//            {
	//                orient = Float.NaN;
	//            }
	//            else
	//            {
	//                orient = sty.getFloatValue();
	//            }
	//        }

	//        if ( getPres( sty.setName( "viewBox" ) ) )
	//        {
	//            float[] dim = sty.getFloatList();
	//            viewBox = new Rectangle2D.Float( dim[0], dim[1], dim[2], dim[3] );
	//        }
	//
	//        if ( viewBox == null )
	//        {
	//            viewBox = new Rectangle( 0, 0, 1, 1 );
	//        }

	//        //Transform pattern onto unit square
	//        viewXform = new AffineTransform();
	//        viewXform.scale( 1.0 / viewBox.getWidth(), 1.0 / viewBox.getHeight() );
	//        viewXform.translate( -viewBox.getX(), -viewBox.getY() );
	//
	//        markerXform = new AffineTransform();
	//        markerXform.scale( markerWidth, markerHeight );
	//        markerXform.concatenate( viewXform );
	//        markerXform.translate( -refX, -refY );
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

	@Override
	public void render( Graphics2D g ) throws SVGException
	{
		AffineTransform oldXform = g.getTransform();
		g.transform( markerXform );

		super.render( g );

		g.setTransform( oldXform );
	}

	public void render( Graphics2D g, MarkerPos pos, float strokeWidth )
		throws SVGException
	{
		AffineTransform cacheXform = g.getTransform();

		g.translate(
			pos.x,
			pos.y );
		g.scale(
			strokeWidth,
			strokeWidth );
		g.rotate( Math.atan2(
			pos.dy,
			pos.dx ) );

		g.transform( markerXform );

		super.render( g );

		g.setTransform( cacheXform );
	}

	public Shape getShape()
	{
		Shape shape = super.getShape();
		return markerXform.createTransformedShape( shape );
	}

	@Override
	public Rectangle2D getBoundingBox() throws SVGException
	{
		Rectangle2D rect = super.getBoundingBox();
		return markerXform.createTransformedShape(
			rect )
			.getBounds2D();
	}

	/**
	 * Updates all attributes in this diagram associated with a time event.
	 * Ie, all attributes with track information.
	 *
	 * @return - true if this node has changed state as a result of the time
	 * update
	 */
	@Override
	public boolean updateTime( double curTime ) throws SVGException
	{
		boolean changeState = super.updateTime( curTime );

		//Marker properties do not change
		return changeState;
	}

	//--------------------------------
	public static final int MARKER_START = 0;

	public static final int MARKER_MID = 1;

	public static final int MARKER_END = 2;

	public static class MarkerPos
	{

		int type;

		double x;

		double y;

		double dx;

		double dy;

		public MarkerPos( int type, double x, double y, double dx, double dy )
		{
			this.type = type;
			this.x = x;
			this.y = y;
			this.dx = dx;
			this.dy = dy;
		}

	}

	public static class MarkerLayout
	{

		private ArrayList markerList = new ArrayList();

		boolean started = false;

		public void layout( Shape shape )
		{
			double px = 0;
			double py = 0;
			double[] coords = new double[ 6 ];
			for (PathIterator it = shape.getPathIterator( null ); !it.isDone(); it.next())
			{
				switch (it.currentSegment( coords ))
				{
					case PathIterator.SEG_MOVETO:
						px = coords[ 0 ];
						py = coords[ 1 ];
						started = false;
						break;
					case PathIterator.SEG_CLOSE:
						started = false;
						break;
					case PathIterator.SEG_LINETO:
					{
						double x = coords[ 0 ];
						double y = coords[ 1 ];
						markerIn(
							px,
							py,
							x - px,
							y - py );
						markerOut(
							x,
							y,
							x - px,
							y - py );
						px = x;
						py = y;
						break;
					}
					case PathIterator.SEG_QUADTO:
					{
						double k0x = coords[ 0 ];
						double k0y = coords[ 1 ];
						double x = coords[ 2 ];
						double y = coords[ 3 ];
						markerIn(
							px,
							py,
							k0x - px,
							k0y - py );
						markerOut(
							x,
							y,
							x - k0x,
							y - k0y );
						px = x;
						py = y;
						break;
					}
					case PathIterator.SEG_CUBICTO:
					{
						double k0x = coords[ 0 ];
						double k0y = coords[ 1 ];
						double k1x = coords[ 2 ];
						double k1y = coords[ 3 ];
						double x = coords[ 4 ];
						double y = coords[ 5 ];
						markerIn(
							px,
							py,
							k0x - px,
							k0y - py );
						markerOut(
							x,
							y,
							x - k1x,
							y - k1y );
						px = x;
						py = y;
						break;
					}
				}
			}

			for (int i = 1; i < markerList.size(); ++i)
			{
				MarkerPos prev = (MarkerPos) markerList.get( i - 1 );
				MarkerPos cur = (MarkerPos) markerList.get( i );

				if (cur.type == MARKER_START)
				{
					prev.type = MARKER_END;
				}
			}
			MarkerPos last = (MarkerPos) markerList.get( markerList.size() - 1 );
			last.type = MARKER_END;
		}

		private void markerIn( double x, double y, double dx, double dy )
		{
			if (started == false)
			{
				started = true;
				markerList.add( new MarkerPos( MARKER_START, x, y, dx, dy ) );
			}
		}

		private void markerOut( double x, double y, double dx, double dy )
		{
			markerList.add( new MarkerPos( MARKER_MID, x, y, dx, dy ) );
		}

		/**
		 * @return the markerList
		 */
		public ArrayList getMarkerList()
		{
			return markerList;
		}

	}
}
