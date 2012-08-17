package com.cybernostics.lib.svg.editor;

import com.cybernostics.lib.concurrent.ConcurrentSet;
import com.cybernostics.lib.gui.RepaintListener;
import com.cybernostics.lib.media.icon.PreferredSizeListener;
import com.cybernostics.lib.media.icon.ScalableIcon;
import com.cybernostics.lib.svg.ReferencedURLRewriter;
import com.cybernostics.lib.svg.SVGWriter;
import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGElementException;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.animation.AnimationElement;
import com.kitfox.svg.app.beans.SVGIcon;
import com.kitfox.svg.elements.*;
import com.kitfox.svg.xml.CSSUnits;
import com.kitfox.svg.xml.attributes.HasBounds;
import com.kitfox.svg.xml.attributes.IStringAttribute;
import com.kitfox.svg.xml.attributes.StringAttribute;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jasonw
 */
public class SVGDrawing extends SVGIcon implements ScalableIcon
{

	public static final String BACKGROUNDELEM = "backgroundElem";

	public static final String DOM_ADDELEMENT = "dom.addElement";

	public static final String DOM_ATTRCHANGE = "dom.attrChange";

	public static final String DOM_NEW = "dom.new";

	public static final String DOM_ROOTSIZE = "dom.rootSize";

	public static final String DOM_STYLECHANGE = "dom.styleChange";

	public static final String MODEFILLCOLOUR = "mode.fillColour";

	public static final String MODE_FILLED = "mode.filled";

	public static final String MODE_STROKECOLOUR = "mode.strokeColour";

	public static final String MODE_STROKED = "mode.stroked";

	public static final String MODE_STROKEWIDTH = "mode.strokeWidth";

	public SVGDrawing( InputStream is )
	{
		InputStreamReader reader = new InputStreamReader( is );
		URI uri1 = SVGCache.getSVGUniverse()
			.loadSVG(
				reader,
				"stream" );
		setSvgURI( uri1 );
		setScaleToFit( true );
		setAntiAlias( true );

		addPropertyChangeListener( drawingChangeWatcher );
	}

	PropertyChangeListener drawingChangeWatcher = new PropertyChangeListener()
	{

		@Override
		public void propertyChange( PropertyChangeEvent evt )
		{
			SVGDiagram diag = SVGDrawing.this.diag;
			//clearCache();
			if (diag != null)
			{
				fireElementChange( diag.getRoot() );
			}

		}

	};

	int paint = 0;

	@Override
	public void paintIcon( Component comp, Graphics gg, int x, int y )
	{
		super.paintIcon(
			comp,
			gg,
			x,
			y );

	}

	public SVGDrawing( String name )
	{
		initNewDrawing( name );
		addPropertyChangeListener( drawingChangeWatcher );
	}

	final protected void initNewDrawing( String name )
	{
		String blankDoc = makeEmptySVG();
		StringReader reader = new StringReader( blankDoc );
		URI uri1 = SVGCache.getSVGUniverse()
			.loadSVG(
				reader,
				name );
		setSvgURI( uri1 );
		setScaleToFit( true );
		setAntiAlias( true );
		changes.firePropertyChange(
			DOM_NEW,
			!filled,
			filled );

	}

	public SVGDrawing( URI source )
	{

		setSvgURI( source );
		addPropertyChangeListener( drawingChangeWatcher );
	}

	@Override
	public final void setSvgURI( URI svgURI )
	{
		super.setSvgURI( svgURI );
		diag = null;
		drawingOpsGroup = null;
		backgroundGroup = null;
	}

	private SVGDiagram diag = null;

	private Group drawingOpsGroup = null;

	private Group backgroundGroup = null;

	private ArrayList< RepaintListener > repaintClients = new ArrayList< RepaintListener >();

	private boolean filled = false;

	public boolean isFilled()
	{
		return filled;
	}

	public void setFilled( boolean filled )
	{
		this.filled = filled;
		changes.firePropertyChange(
			MODE_FILLED,
			!filled,
			filled );
	}

	public boolean isStroked()
	{
		return stroked;
	}

	public void setStroked( boolean stroked )
	{
		this.stroked = stroked;
		changes.firePropertyChange(
			MODE_STROKED,
			!stroked,
			stroked );
	}

	private boolean stroked = false;

	ConcurrentSet< SVGElementListener > listeners = new ConcurrentSet< SVGElementListener >();

	public void addElementListener( SVGElementListener listener )
	{
		listeners.add( listener );
	}

	public void addRepaintListener( RepaintListener listener )
	{
		repaintClients.add( listener );
	}

	public void fireElementChange( SVGElement e )
	{
		for (SVGElementListener eachListener : listeners)
		{
			eachListener.elementUpdated( e );
		}
	}

	@Override
	public void setSize( Dimension2D d )
	{
		super.setPreferredSize( new Dimension( (int) d.getWidth(),
			(int) d.getHeight() ) );
	}

	public void setDwgSize( Dimension d )
	{
		SVGRoot root = getDiagram().getRoot();
		root.getWidth()
			.setQuantity(
				d.width );
		root.getHeight()
			.setQuantity(
				d.height );
		changes.firePropertyChange(
			DOM_ROOTSIZE,
			null,
			getDiagram().getRoot() );
	}

	public void setAttribute( SVGElement e, String property, Double value )
	{
		setAttribute(
			e,
			property,
			String.format(
				"%.3g",
				value ) );
	}

	private StringAttribute sty = new StringAttribute( "" );

	IStringAttribute getPresentationAttribute( SVGElement e, String propName )
	{
		try
		{
			if (e.getPres( sty.withName( propName ) ))
			{
				return sty;
			}
		}
		catch (SVGException ex)
		{
			throw new RuntimeException( ex );
		}
		return null;
	}

	IStringAttribute getStyleAttribute( SVGElement e, String propName )
	{
		return e.getStyle( propName );
	}

	public void setAttribute( SVGElement e, String property, String value )
	{
		IStringAttribute sa = e.getStyle( value );
		sa.setStringValue( value );
		changes.firePropertyChange(
			DOM_ATTRCHANGE,
			null,
			e );
	}

	public void setStyleAttribute( SVGElement e, String property, Double value )
	{
		setStyleAttribute(
			e,
			property,
			String.format(
				"%.3g",
				value ) );
	}

	public void setStyleAttribute( SVGElement e, String property, String value )
	{
		try
		{
			if (e.getStyle( sty.withName( property ) ))
			{
				e.setAttribute(
					property,
					AnimationElement.AT_CSS,
					value );
			}
			else
			{
				e.addAttribute(
					property,
					AnimationElement.AT_CSS,
					value );
			}
		}
		catch (SVGException ex)
		{
			Logger.getLogger(
				SVGDrawing.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		changes.firePropertyChange(
			DOM_STYLECHANGE,
			null,
			getDiagram().getRoot() );
	}

	public SVGDiagram getDiagram()
	{
		if (diag == null)
		{
			diag = getSvgUniverse().getDiagram(
				getSvgURI() );
		}
		return diag;
	}

	SVGElement getElement( String sName )
	{
		return getDiagram().getElement(
			sName );
	}

	public void addElement( SVGElement el, String group )
	{
		try
		{
			Group gOwner = ( group != null ) ? (Group) getElement( group )
				: getDrawingOpsGroup();

			gOwner.loaderAddChild(
				null,
				el );
			el.updateTime( 0 );
		}
		catch (SVGException ex)
		{
			Logger.getLogger(
				SVGDrawing.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}

		changes.firePropertyChange(
			DOM_ADDELEMENT,
			!filled,
			filled );

		fireElementChange( el );
	}

	public void makeFilled( SVGElement el )
	{
		setStyleAttribute(
			el,
			"fill",
			currentFillColourStr );
		setStyleAttribute(
			el,
			"opacity",
			currentOpacityStr );
	}

	public void makeStroked( SVGElement el )
	{
		setStyleAttribute(
			el,
			"stroke",
			currentStrokeColourStr );
		setStyleAttribute(
			el,
			"stroke-width",
			strokewidth );
	}

	private int strokeWidthMultiple = 1;

	public int getStrokeWidthMultiple()
	{
		return strokeWidthMultiple;
	}

	public void setStrokeWidthMultiple( int strokeWidthMultiple )
	{
		int oldVal = this.strokeWidthMultiple;
		this.strokeWidthMultiple = strokeWidthMultiple;
		strokewidth = minStrokeWidth * strokeWidthMultiple;
		changes.firePropertyChange(
			MODE_STROKEWIDTH,
			oldVal,
			strokeWidthMultiple );
	}

	private String currentFillColourStr = "black";

	private String currentStrokeColourStr = "black";

	private String currentOpacityStr = "1.0";

	private double minStrokeWidth = 0.001;

	private String drawingOpsGroupId = "drawingOperations";

	private String backgroundGroupId = "backgroundGroup";

	public Group getBackgroundGroup()
	{
		if (backgroundGroup == null)
		{
			backgroundGroup = (Group) getElement( backgroundGroupId );
		}
		return backgroundGroup;
	}

	private double strokewidth = minStrokeWidth * strokeWidthMultiple;

	public Color getFillColour()
	{
		return SVGColor.parseColor( getFillColourText() );
	}

	public String getFillColourText()
	{
		return currentFillColourStr;
	}

	public void setFillColour( Color currentColour )
	{
		String old = this.currentFillColourStr;
		this.currentFillColourStr = SVGColor.getSVGColorSpec( currentColour );
		changes.firePropertyChange(
			MODEFILLCOLOUR,
			old,
			this.currentFillColourStr );
	}

	public void setStrokeColour( Color currentColour )
	{
		String old = this.currentStrokeColourStr;
		this.currentStrokeColourStr = SVGColor.getSVGColorSpec( currentColour );
		changes.firePropertyChange(
			MODE_STROKECOLOUR,
			old,
			this.currentStrokeColourStr );
	}

	public Color getStrokeColour()
	{
		return SVGColor.parseColor( getStrokeColourText() );
	}

	public String getStrokeColourText()
	{
		return currentStrokeColourStr;
	}

	public double getMinStrokeWidth()
	{
		return minStrokeWidth;
	}

	public void setMinStrokeWidth( double currentStrokeWidth )
	{
		this.minStrokeWidth = currentStrokeWidth;
		strokewidth = minStrokeWidth * strokeWidthMultiple;
	}

	public void setPaperColor( Color currentColour )
	{
		SVGElement bg = getBackgroundPaper();
		setStyleAttribute(
			bg,
			"fill",
			SVGColor.getSVGColorSpec( currentColour ) );
		update( bg );

	}

	public Color getPaperColor()
	{
		SVGElement bg = getBackgroundPaper();
		IStringAttribute style = getStyleAttribute(
			bg,
			"fill" );
		if (style != null)
		{
			return Color.getColor( style.getStringValue() );
		}
		return null;

	}

	public SVGElement getBackgroundPaper()
	{
		return getElement( BACKGROUNDELEM );
	}

	public Group getDrawingOpsGroup()
	{
		if (drawingOpsGroup == null)
		{
			drawingOpsGroup = (Group) getElement( drawingOpsGroupId );
		}

		return drawingOpsGroup;
	}

	// creates a new SVG element which corresponds to the shape
	public Path createShape( Shape s, boolean filled, boolean stroked )
	{
		Path p = new Path();

		p.getPath2D()
			.append(
				s,
				true );
		//setAttribute( p, "d", getSVGPathSpec( s ) );
		strokeAndFill(
			p,
			filled,
			stroked );
		return p;

	}

	// creates a new SVG element which corresponds to the shape
	public Rect createRect( Rectangle2D r, boolean filled, boolean stroked )
	{
		Rect p = new Rect();
		updateBounds(
			p,
			r );
		strokeAndFill(
			p,
			filled,
			stroked );
		return p;

	}

	public Path createLine( Line2D.Double theLine,
		boolean filled,
		boolean stroked )
	{
		Path p = new Path();

		setAttribute(
			p,
			"d",
			String.format(
				"M %8.3g,%8.3g,%8.3g,%8.3g",
				theLine.x1,
				theLine.y1,
				theLine.x2,
												theLine.y2 ) );
		strokeAndFill(
			p,
			filled,
			stroked );

		return p;
	}

	public void strokeAndFill( SVGElement e, boolean filled, boolean stroked )
	{
		if (stroked)
		{
			makeStroked( e );
		}
		if (filled)
		{
			makeFilled( e );
		}

	}

	public ImageSVG createBitmapRef( URL imageRef, Rectangle2D rect )
	{
		ImageSVG e = new ImageSVG();
		e.getX()
			.setValue(
				(float) rect.getMinX(),
				CSSUnits.UT_UNITLESS );
		e.getY()
			.setValue(
				(float) rect.getMinY(),
				CSSUnits.UT_UNITLESS );
		e.getWidth()
			.setValue(
				(float) rect.getWidth(),
				CSSUnits.UT_UNITLESS );
		e.getHeight()
			.setValue(
				(float) rect.getHeight(),
				CSSUnits.UT_UNITLESS );
		e.getXlinkHref()
			.setStringValue(
				imageRef.toExternalForm() );

		return e;
	}

	public Use createUseRef( URL imageRef, Rectangle2D rect )
	{
		Use e = new Use();
		e.getX()
			.setValue(
				(float) rect.getMinX(),
				CSSUnits.UT_UNITLESS );
		e.getY()
			.setValue(
				(float) rect.getMinY(),
				CSSUnits.UT_UNITLESS );
		e.getWidth()
			.setValue(
				(float) rect.getWidth(),
				CSSUnits.UT_UNITLESS );
		e.getHeight()
			.setValue(
				(float) rect.getHeight(),
				CSSUnits.UT_UNITLESS );
		e.getXRef()
			.setStringValue(
				imageRef.toExternalForm() );

		return e;
	}

	public Ellipse createEllipse( Ellipse2D ellipse,
		boolean filled,
		boolean stroked )
	{
		Ellipse e = new Ellipse();
		Rectangle2D bounds = ellipse.getBounds2D();
		updateEllipseBounds(
			e,
			bounds );
		strokeAndFill(
			e,
			filled,
			stroked );
		update( e );
		return e;
	}

	public void updateEllipseBounds( Ellipse e, Rectangle2D ellipse )
	{
		float cx = (float) ellipse.getCenterX();
		float cy = (float) ellipse.getCenterY();
		float rx = (float) ( cx - ellipse.getMinX() );
		float ry = (float) ( cy - ellipse.getMinY() );

		e.getCx()
			.setQuantity(
				cx );
		e.getCy()
			.setQuantity(
				cy );
		e.getRx()
			.setQuantity(
				rx );
		e.getRy()
			.setQuantity(
				ry );

		fireElementChange( e );
	}

	//    public String getSVGCoordPair( String prefix, Point2D pt )
	//    {
	//        return getSVGCoordPair( prefix, pt.getX(), pt.getY() );
	//    }

	//    public String getSVGCoordPair( String prefix, double x, double y )
	//    {
	//        return String.format( "%s%.3g,%.3g", prefix, x, y );
	//    }

	//    public String getSVGPathSpec( Shape s )
	//    {
	//        StringBuilder pathSpec = new StringBuilder( "" );
	//
	//        float[] coords = new float[ 6 ];
	//        PathIterator iter = s.getPathIterator( null );
	//        while ( !iter.isDone() )
	//        {
	//            switch ( iter.currentSegment( coords ) )
	//            {
	//                case PathIterator.SEG_MOVETO:
	//                    pathSpec.append( getSVGCoordPair( "M ", coords[0], coords[1] ) );
	//                    break;
	//                case PathIterator.SEG_LINETO:
	//                    pathSpec.append( getSVGCoordPair( ",", coords[0], coords[1] ) );
	//                    break;
	//                case PathIterator.SEG_QUADTO:
	//                    //TODO
	//                    break;
	//                case PathIterator.SEG_CUBICTO:
	//                    //TODO
	//                    break;
	//                case PathIterator.SEG_CLOSE:
	//                    pathSpec.append( String.format( " z" ) );
	//                    break;
	//            }
	//            iter.next();
	//        }
	//
	//        return pathSpec.toString();
	//    }

	private static final DecimalFormat decF = new DecimalFormat( "#.####" );

	private static String makeEmptySVG()
	{
		return makeEmptySVG(
			1,
			1 );
	}

	private static String makeEmptySVG( float width, float height )
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter( sw );

		pw.print( "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" width=\"" );
		pw.print( decF.format( width ) );
		pw.print( "\" height=\"" );
		pw.print( decF.format( height ) );
		pw.println( "\" >" );
		pw.println(
				"    <g id=\"drawingOperations\" style=\"width:100%;height:100%;fill:none;stroke-linecap: round;stroke-linejoin: round;\">" );
		pw.println(
				"    <g id=\"drawingBackground\" style=\"width:100%;height:100%;fill:none;stroke-linecap: round;stroke-linejoin: round;\">" );
		pw.println(
				"       <rect x=\"0\" y=\"0\" width=\"100%\" height=\"100%\" id=\"backgroundElem\" style=\"fill:white\"/>" );
		pw.println( "    </g>" );
		pw.println( "    </g>" );
		pw.println( "</svg>" );

		pw.close();
		return sw.toString();
	}

	public void addPathPoint( SVGElement currentObject, Point2D nextPoint )
	{
		if (currentObject instanceof Path)
		{
			Path pathObj = (Path) currentObject;
			pathObj.lineTo( nextPoint );
		}
		fireElementChange( currentObject );
	}

	public void update( SVGElement currentElement )
	{
		try
		{
			currentElement.updateTime( 0 );
		}
		catch (SVGException ex)
		{
			throw new RuntimeException( ex );
		}
		fireElementChange( currentElement );
	}

	public void remove( SVGElement currentElement )
	{
		if (currentElement != null)
		{
			try
			{
				SVGElement parent = currentElement.getParent();
				parent.removeChild( currentElement );
				parent.updateTime( 0 );

			}
			catch (SVGException ex)
			{
				Logger.getLogger(
					SVGDrawing.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex );
			}
		}
	}

	public void updateBounds( SVGElement e, Rectangle2D r )
	{
		if (e instanceof HasBounds)
		{
			HasBounds toChange = (HasBounds) e;
			toChange.getX()
				.setQuantity(
					(float) r.getMinX() );
			toChange.getY()
				.setQuantity(
					(float) r.getMinY() );
			toChange.getWidth()
				.setQuantity(
					(float) r.getWidth() );
			toChange.getHeight()
				.setQuantity(
					(float) r.getHeight() );

		}

	}

	public void write( PrintWriter pw )
	{
		SVGWriter.writeSVGFIle(
			getDiagram(),
			pw,
			new ReferencedURLRewriter() );
		pw.flush();
	}

	/**
	 * Writes the drawing data to a stream
	 *
	 * @param os
	 */
	public void saveToStream( OutputStream os )
	{
		SVGWriter.writeSVGFIle(
			getDiagram(),
			new PrintWriter( os ),
			new ReferencedURLRewriter() );
		try
		{
			os.flush();
		}
		catch (IOException ex)
		{
			Logger.getLogger(
				SVGDrawing.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
	}

	public void clear()
	{
		Group gOwner = getDrawingOpsGroup();
		List< SVGElement > elements = new ArrayList< SVGElement >();

		gOwner.getChildren( elements );

		for (SVGElement eachElement : elements)
		{
			try
			{
				gOwner.removeChild( eachElement );
			}
			catch (SVGElementException ex)
			{
			}
		}

	}

	@Override
	public void setMinimumSize( Dimension d )
	{
		//throw new UnsupportedOperationException( "Not supported yet." );
	}

	@Override
	public ScalableIcon copy()
	{
		//throw new UnsupportedOperationException( "Not supported yet." );
		return null;
	}

	@Override
	public void addPreferredSizeListener( PreferredSizeListener listener )
	{
		//
	}

	@Override
	public BufferedImage getImage()
	{
		throw new UnsupportedOperationException( "Not supported yet." );
	}

}
