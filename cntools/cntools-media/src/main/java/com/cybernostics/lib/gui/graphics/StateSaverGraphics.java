package com.cybernostics.lib.gui.graphics;

import java.awt.RenderingHints.Key;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.LinkedList;
import java.util.Map;

public class StateSaverGraphics extends Graphics2D
{

	private LinkedList< Runnable > undoOperations = new LinkedList< Runnable >();

	private Graphics2D inner = null;

	StateSaverGraphics( Graphics2D g )
	{
		this.inner = g;
		if (this.inner == null)
		{
			throw new RuntimeException( "Bad Inner" );
		}
	}

	StateSaverGraphics( Graphics g )
	{
		this.inner = (Graphics2D) g;
		if (this.inner == null)
		{
			throw new RuntimeException( "Bad Inner" );
		}
	}

	abstract class SavedState< ContextType > implements Runnable
	{

		ContextType saved;

		SavedState( ContextType toSave )
		{
			saved = toSave;
		}

	}

	public void saveTransform()
	{
		undoOperations.add( new SavedState< AffineTransform >(
			inner.getTransform() )
		{

			@Override
			public void run()
			{
				inner.setTransform( saved );
			}

		} );

	}

	public void saveClip()
	{
		saveTransform();

		undoOperations.add( new SavedState< Shape >( inner.getClip() )
		{

			@Override
			public void run()
			{
				inner.setClip( saved );
			}

		} );

	}

	public void saveStroke()
	{
		undoOperations.add( new SavedState< Stroke >( inner.getStroke() )
		{

			@Override
			public void run()
			{
				inner.setStroke( saved );
			}

		} );

	}

	public void savePaint()
	{
		if (inner == null)
		{
			return;
		}

		undoOperations.add( new SavedState< Paint >( inner.getPaint() )
		{

			@Override
			public void run()
			{
				inner.setPaint( saved );
			}

		} );

	}

	public void saveComposite()
	{
		undoOperations.add( new SavedState< Composite >( inner.getComposite() )
		{

			@Override
			public void run()
			{
				inner.setComposite( saved );
			}

		} );

	}

	public void saveHints()
	{

		undoOperations.add( new SavedState< RenderingHints >(
			inner.getRenderingHints() )
		{

			@Override
			public void run()
			{
				inner.setRenderingHints( saved );
			}

		} );

	}

	public void saveBackground()
	{
		undoOperations.add( new SavedState< Color >( inner.getBackground() )
		{

			@Override
			public void run()
			{
				inner.setBackground( saved );
			}

		} );

	}

	public void saveForeground()
	{
		undoOperations.add( new SavedState< Color >( inner.getColor() )
		{

			@Override
			public void run()
			{
				inner.setColor( saved );
			}

		} );

	}

	public void saveFont()
	{
		undoOperations.add( new SavedState< Font >( inner.getFont() )
		{

			@Override
			public void run()
			{
				inner.setFont( saved );
			}

		} );

	}

	/**
	 * Restores any saved properties
	 */
	public void restore()
	{
		if (inner == null)
		{
			return;
		}
		// reset the Graphics object from back to front
		while (!undoOperations.isEmpty())
		{
			undoOperations.removeLast()
				.run();
		}

	}

	@Override
	public String toString()
	{
		return inner.toString();
	}

	@Override
	public void setXORMode( Color c1 )
	{
		inner.setXORMode( c1 );
	}

	@Override
	public void setPaintMode()
	{
		inner.setPaintMode();
	}

	@Override
	public void setFont( Font font )
	{
		saveFont();
		inner.setFont( font );
	}

	@Override
	public void setColor( Color c )
	{
		saveForeground();
		inner.setColor( c );
	}

	@Override
	public void setClip( Shape clip )
	{
		saveClip();
		inner.setClip( clip );
	}

	@Override
	public void setClip( int x, int y, int width, int height )
	{
		saveClip();
		inner.setClip(
			x,
			y,
			width,
			height );
	}

	@Override
	public boolean hitClip( int x, int y, int width, int height )
	{
		return inner.hitClip(
			x,
			y,
			width,
			height );
	}

	@Override
	public FontMetrics getFontMetrics( Font f )
	{
		return inner.getFontMetrics( f );
	}

	@Override
	public FontMetrics getFontMetrics()
	{
		return inner.getFontMetrics();
	}

	@Override
	public Font getFont()
	{
		return inner.getFont();
	}

	@Override
	public Color getColor()
	{
		return inner.getColor();
	}

	@Override
	public Rectangle getClipRect()
	{
		return inner.getClipRect();
	}

	@Override
	public Rectangle getClipBounds( Rectangle r )
	{
		return inner.getClipBounds( r );
	}

	@Override
	public Rectangle getClipBounds()
	{
		return inner.getClipBounds();
	}

	@Override
	public Shape getClip()
	{
		return inner.getClip();
	}

	@Override
	public void fillRoundRect( int x,
		int y,
		int width,
		int height,
		int arcWidth,
		int arcHeight )
	{
		inner.fillRoundRect(
			x,
			y,
			width,
			height,
			arcWidth,
			arcHeight );
	}

	@Override
	public void fillRect( int x, int y, int width, int height )
	{
		inner.fillRect(
			x,
			y,
			width,
			height );
	}

	@Override
	public void fillPolygon( Polygon p )
	{
		inner.fillPolygon( p );
	}

	@Override
	public void fillPolygon( int[] xPoints, int[] yPoints, int nPoints )
	{
		inner.fillPolygon(
			xPoints,
			yPoints,
			nPoints );
	}

	@Override
	public void fillOval( int x, int y, int width, int height )
	{
		inner.fillOval(
			x,
			y,
			width,
			height );
	}

	@Override
	public void fillArc( int x,
		int y,
		int width,
		int height,
		int startAngle,
		int arcAngle )
	{
		inner.fillArc(
			x,
			y,
			width,
			height,
			startAngle,
			arcAngle );
	}

	@Override
	public void drawRoundRect( int x,
		int y,
		int width,
		int height,
		int arcWidth,
		int arcHeight )
	{
		inner.drawRoundRect(
			x,
			y,
			width,
			height,
			arcWidth,
			arcHeight );
	}

	@Override
	public void drawRect( int x, int y, int width, int height )
	{
		inner.drawRect(
			x,
			y,
			width,
			height );
	}

	@Override
	public void drawPolyline( int[] xPoints, int[] yPoints, int nPoints )
	{
		inner.drawPolyline(
			xPoints,
			yPoints,
			nPoints );
	}

	@Override
	public void drawPolygon( Polygon p )
	{
		inner.drawPolygon( p );
	}

	@Override
	public void drawPolygon( int[] xPoints, int[] yPoints, int nPoints )
	{
		inner.drawPolygon(
			xPoints,
			yPoints,
			nPoints );
	}

	@Override
	public void drawOval( int x, int y, int width, int height )
	{
		inner.drawOval(
			x,
			y,
			width,
			height );
	}

	@Override
	public void drawLine( int x1, int y1, int x2, int y2 )
	{
		inner.drawLine(
			x1,
			y1,
			x2,
			y2 );
	}

	@Override
	public boolean drawImage( Image img,
		int dx1,
		int dy1,
		int dx2,
		int dy2,
		int sx1,
		int sy1,
		int sx2,
		int sy2,
		Color bgcolor,
		ImageObserver observer )
	{
		return inner.drawImage(
			img,
			dx1,
			dy1,
			dx2,
			dy2,
			sx1,
			sy1,
			sx2,
			sy2,
			bgcolor,
			observer );
	}

	@Override
	public boolean drawImage( Image img,
		int dx1,
		int dy1,
		int dx2,
		int dy2,
		int sx1,
		int sy1,
		int sx2,
		int sy2,
		ImageObserver observer )
	{
		return inner.drawImage(
			img,
			dx1,
			dy1,
			dx2,
			dy2,
			sx1,
			sy1,
			sx2,
			sy2,
			observer );
	}

	@Override
	public boolean drawImage( Image img,
		int x,
		int y,
		int width,
		int height,
		Color bgcolor,
		ImageObserver observer )
	{
		return inner.drawImage(
			img,
			x,
			y,
			width,
			height,
			bgcolor,
			observer );
	}

	@Override
	public boolean drawImage( Image img,
		int x,
		int y,
		Color bgcolor,
		ImageObserver observer )
	{
		return inner.drawImage(
			img,
			x,
			y,
			bgcolor,
			observer );
	}

	@Override
	public boolean drawImage( Image img,
		int x,
		int y,
		int width,
		int height,
		ImageObserver observer )
	{
		return inner.drawImage(
			img,
			x,
			y,
			width,
			height,
			observer );
	}

	@Override
	public boolean drawImage( Image img, int x, int y, ImageObserver observer )
	{
		return inner.drawImage(
			img,
			x,
			y,
			observer );
	}

	@Override
	public void drawChars( char[] data, int offset, int length, int x, int y )
	{
		inner.drawChars(
			data,
			offset,
			length,
			x,
			y );
	}

	@Override
	public void drawBytes( byte[] data, int offset, int length, int x, int y )
	{
		inner.drawBytes(
			data,
			offset,
			length,
			x,
			y );
	}

	@Override
	public void drawArc( int x,
		int y,
		int width,
		int height,
		int startAngle,
		int arcAngle )
	{
		inner.drawArc(
			x,
			y,
			width,
			height,
			startAngle,
			arcAngle );
	}

	@Override
	public void dispose()
	{
		inner.dispose();
		//inner = null;
	}

	@Override
	public Graphics create( int x, int y, int width, int height )
	{
		return inner.create(
			x,
			y,
			width,
			height );
	}

	@Override
	public Graphics create()
	{
		return inner.create();
	}

	@Override
	public void copyArea( int x, int y, int width, int height, int dx, int dy )
	{
		inner.copyArea(
			x,
			y,
			width,
			height,
			dx,
			dy );
	}

	@Override
	public void clipRect( int x, int y, int width, int height )
	{
		saveClip();
		inner.clipRect(
			x,
			y,
			width,
			height );
	}

	@Override
	public void clearRect( int x, int y, int width, int height )
	{
		inner.clearRect(
			x,
			y,
			width,
			height );
	}

	@Override
	public void translate( double tx, double ty )
	{
		saveTransform();
		inner.translate(
			tx,
			ty );
	}

	@Override
	public void translate( int x, int y )
	{
		saveTransform();
		inner.translate(
			x,
			y );
	}

	@Override
	public void transform( AffineTransform Tx )
	{
		saveTransform();
		inner.transform( Tx );
	}

	@Override
	public void shear( double shx, double shy )
	{
		saveTransform();
		inner.shear(
			shx,
			shy );
	}

	@Override
	public void setTransform( AffineTransform Tx )
	{
		saveTransform();
		inner.setTransform( Tx );
	}

	@Override
	public void setStroke( Stroke s )
	{
		saveStroke();
		inner.setStroke( s );
	}

	@Override
	public void setRenderingHints( Map< ? , ? > hints )
	{
		saveHints();
		inner.setRenderingHints( hints );
	}

	@Override
	public void setRenderingHint( Key hintKey, Object hintValue )
	{
		saveHints();
		inner.setRenderingHint(
			hintKey,
			hintValue );
	}

	@Override
	public void setPaint( Paint paint )
	{
		savePaint();
		inner.setPaint( paint );
	}

	@Override
	public void setComposite( Composite comp )
	{
		saveComposite();
		inner.setComposite( comp );
	}

	@Override
	public void setBackground( Color color )
	{
		saveBackground();
		inner.setBackground( color );
	}

	@Override
	public void scale( double sx, double sy )
	{
		saveTransform();
		inner.scale(
			sx,
			sy );
	}

	@Override
	public void rotate( double theta, double x, double y )
	{
		saveTransform();
		inner.rotate(
			theta,
			x,
			y );
	}

	@Override
	public void rotate( double theta )
	{
		saveTransform();
		inner.rotate( theta );
	}

	@Override
	public boolean hit( Rectangle rect, Shape s, boolean onStroke )
	{
		return inner.hit(
			rect,
			s,
			onStroke );
	}

	@Override
	public AffineTransform getTransform()
	{
		return inner.getTransform();
	}

	@Override
	public Stroke getStroke()
	{
		return inner.getStroke();
	}

	@Override
	public RenderingHints getRenderingHints()
	{
		return inner.getRenderingHints();
	}

	@Override
	public Object getRenderingHint( Key hintKey )
	{
		return inner.getRenderingHint( hintKey );
	}

	@Override
	public Paint getPaint()
	{
		return inner.getPaint();
	}

	@Override
	public FontRenderContext getFontRenderContext()
	{
		return inner.getFontRenderContext();
	}

	@Override
	public GraphicsConfiguration getDeviceConfiguration()
	{
		return inner.getDeviceConfiguration();
	}

	@Override
	public Composite getComposite()
	{
		return inner.getComposite();
	}

	@Override
	public Color getBackground()
	{
		return inner.getBackground();
	}

	@Override
	public void fill3DRect( int x, int y, int width, int height, boolean raised )
	{
		inner.fill3DRect(
			x,
			y,
			width,
			height,
			raised );
	}

	@Override
	public void fill( Shape s )
	{
		inner.fill( s );
	}

	@Override
	public void drawString( AttributedCharacterIterator iterator,
		float x,
		float y )
	{
		inner.drawString(
			iterator,
			x,
			y );
	}

	@Override
	public void drawString( AttributedCharacterIterator iterator, int x, int y )
	{
		inner.drawString(
			iterator,
			x,
			y );
	}

	@Override
	public void drawString( String str, float x, float y )
	{
		inner.drawString(
			str,
			x,
			y );
	}

	@Override
	public void drawString( String str, int x, int y )
	{
		inner.drawString(
			str,
			x,
			y );
	}

	@Override
	public void drawRenderedImage( RenderedImage img, AffineTransform xform )
	{
		inner.drawRenderedImage(
			img,
			xform );
	}

	@Override
	public void drawRenderableImage( RenderableImage img, AffineTransform xform )
	{
		inner.drawRenderableImage(
			img,
			xform );
	}

	@Override
	public void drawImage( BufferedImage img, BufferedImageOp op, int x, int y )
	{
		inner.drawImage(
			img,
			op,
			x,
			y );
	}

	@Override
	public boolean drawImage( Image img,
		AffineTransform xform,
		ImageObserver obs )
	{
		return inner.drawImage(
			img,
			xform,
			obs );
	}

	@Override
	public void drawGlyphVector( GlyphVector g, float x, float y )
	{
		inner.drawGlyphVector(
			g,
			x,
			y );
	}

	@Override
	public void draw3DRect( int x, int y, int width, int height, boolean raised )
	{
		inner.draw3DRect(
			x,
			y,
			width,
			height,
			raised );
	}

	@Override
	public void draw( Shape s )
	{
		inner.draw( s );
	}

	@Override
	public void clip( Shape s )
	{
		saveClip();
		inner.clip( s );
	}

	@Override
	public void addRenderingHints( Map< ? , ? > hints )
	{
		saveHints();
		inner.addRenderingHints( hints );
	}

}