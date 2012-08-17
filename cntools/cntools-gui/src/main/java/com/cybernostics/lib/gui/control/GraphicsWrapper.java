package com.cybernostics.lib.gui.control;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

/**
 * @author jasonw
 *
 */
public class GraphicsWrapper extends Graphics2D
{
	Graphics2D g2;

	public GraphicsWrapper( Graphics2D g2 )
	{
		this.g2 = g2;
	}

	public void addRenderingHints( Map< ? , ? > hints )
	{
		g2.addRenderingHints( hints );
	}

	public void clearRect( int x, int y, int width, int height )
	{
		g2.clearRect(
			x,
			y,
			width,
			height );
	}

	public void clip( Shape s )
	{
		g2.clip( s );
	}

	public void clipRect( int x, int y, int width, int height )
	{
		g2.clipRect(
			x,
			y,
			width,
			height );
	}

	public void copyArea( int x, int y, int width, int height, int dx, int dy )
	{
		g2.copyArea(
			x,
			y,
			width,
			height,
			dx,
			dy );
	}

	public Graphics create()
	{
		return new GraphicsWrapper( (Graphics2D) g2.create() );
	}

	public Graphics create( int x, int y, int width, int height )
	{
		return new GraphicsWrapper( (Graphics2D) g2.create(
			x,
			y,
			width,
			height ) );
	}

	public void dispose()
	{
		g2.dispose();
	}

	public void draw( Shape s )
	{
		g2.draw( s );
	}

	public void draw3DRect( int x, int y, int width, int height, boolean raised )
	{
		g2.draw3DRect(
			x,
			y,
			width,
			height,
			raised );
	}

	public void drawArc( int x,
		int y,
		int width,
		int height,
		int startAngle,
		int arcAngle )
	{
		g2.drawArc(
			x,
			y,
			width,
			height,
			startAngle,
			arcAngle );
	}

	public void drawBytes( byte[] data, int offset, int length, int x, int y )
	{
		g2.drawBytes(
			data,
			offset,
			length,
			x,
			y );
	}

	public void drawChars( char[] data, int offset, int length, int x, int y )
	{
		g2.drawChars(
			data,
			offset,
			length,
			x,
			y );
	}

	public void drawGlyphVector( GlyphVector g, float x, float y )
	{
		g2.drawGlyphVector(
			g,
			x,
			y );
	}

	public void drawImage( BufferedImage img, BufferedImageOp op, int x, int y )
	{
		g2.drawImage(
			img,
			op,
			x,
			y );
	}

	public boolean drawImage( Image img,
		AffineTransform xform,
		ImageObserver obs )
	{
		return g2.drawImage(
			img,
			xform,
			obs );
	}

	public boolean drawImage( Image img,
		int x,
		int y,
		Color bgcolor,
		ImageObserver observer )
	{
		return g2.drawImage(
			img,
			x,
			y,
			bgcolor,
			observer );
	}

	public boolean drawImage( Image img, int x, int y, ImageObserver observer )
	{
		return g2.drawImage(
			img,
			x,
			y,
			observer );
	}

	public boolean drawImage( Image img,
		int x,
		int y,
		int width,
		int height,
		Color bgcolor,
		ImageObserver observer )
	{
		return g2.drawImage(
			img,
			x,
			y,
			width,
			height,
			bgcolor,
			observer );
	}

	public boolean drawImage( Image img,
		int x,
		int y,
		int width,
		int height,
		ImageObserver observer )
	{
		return g2.drawImage(
			img,
			x,
			y,
			width,
			height,
			observer );
	}

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
		return g2.drawImage(
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
		return g2.drawImage(
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

	public void drawLine( int x1, int y1, int x2, int y2 )
	{
		g2.drawLine(
			x1,
			y1,
			x2,
			y2 );
	}

	public void drawOval( int x, int y, int width, int height )
	{
		g2.drawOval(
			x,
			y,
			width,
			height );
	}

	public void drawPolygon( int[] points, int[] points2, int points3 )
	{
		g2.drawPolygon(
			points,
			points2,
			points3 );
	}

	public void drawPolygon( Polygon p )
	{
		g2.drawPolygon( p );
	}

	public void drawPolyline( int[] points, int[] points2, int points3 )
	{
		g2.drawPolyline(
			points,
			points2,
			points3 );
	}

	public void drawRect( int x, int y, int width, int height )
	{
		g2.drawRect(
			x,
			y,
			width,
			height );
	}

	public void drawRenderableImage( RenderableImage img, AffineTransform xform )
	{
		g2.drawRenderableImage(
			img,
			xform );
	}

	public void drawRenderedImage( RenderedImage img, AffineTransform xform )
	{
		g2.drawRenderedImage(
			img,
			xform );
	}

	public void drawRoundRect( int x,
		int y,
		int width,
		int height,
		int arcWidth,
		int arcHeight )
	{
		g2.drawRoundRect(
			x,
			y,
			width,
			height,
			arcWidth,
			arcHeight );
	}

	public void drawString( AttributedCharacterIterator iterator,
		float x,
		float y )
	{
		g2.drawString(
			iterator,
			x,
			y );
	}

	public void drawString( AttributedCharacterIterator iterator, int x, int y )
	{
		g2.drawString(
			iterator,
			x,
			y );
	}

	public void drawString( String str, float x, float y )
	{
		g2.drawString(
			str,
			x,
			y );
	}

	public void drawString( String str, int x, int y )
	{
		g2.drawString(
			str,
			x,
			y );
	}

	public boolean equals( Object obj )
	{
		return g2.equals( obj );
	}

	public void fill( Shape s )
	{
		g2.fill( s );
	}

	public void fill3DRect( int x, int y, int width, int height, boolean raised )
	{
		g2.fill3DRect(
			x,
			y,
			width,
			height,
			raised );
	}

	public void fillArc( int x,
		int y,
		int width,
		int height,
		int startAngle,
		int arcAngle )
	{
		g2.fillArc(
			x,
			y,
			width,
			height,
			startAngle,
			arcAngle );
	}

	public void fillOval( int x, int y, int width, int height )
	{
		g2.fillOval(
			x,
			y,
			width,
			height );
	}

	public void fillPolygon( int[] points, int[] points2, int points3 )
	{
		g2.fillPolygon(
			points,
			points2,
			points3 );
	}

	public void fillPolygon( Polygon p )
	{
		g2.fillPolygon( p );
	}

	public void fillRect( int x, int y, int width, int height )
	{
		g2.fillRect(
			x,
			y,
			width,
			height );
	}

	public void fillRoundRect( int x,
		int y,
		int width,
		int height,
		int arcWidth,
		int arcHeight )
	{
		g2.fillRoundRect(
			x,
			y,
			width,
			height,
			arcWidth,
			arcHeight );
	}

	public void finalize()
	{
		g2.finalize();
	}

	public Color getBackground()
	{
		return g2.getBackground();
	}

	public Shape getClip()
	{
		return g2.getClip();
	}

	public Rectangle getClipBounds()
	{
		return g2.getClipBounds();
	}

	public Rectangle getClipBounds( Rectangle r )
	{
		return g2.getClipBounds( r );
	}

	@SuppressWarnings("deprecation")
	public Rectangle getClipRect()
	{
		return g2.getClipRect();
	}

	public Color getColor()
	{
		return g2.getColor();
	}

	public Composite getComposite()
	{
		return g2.getComposite();
	}

	public GraphicsConfiguration getDeviceConfiguration()
	{
		return g2.getDeviceConfiguration();
	}

	public Font getFont()
	{
		return g2.getFont();
	}

	public FontMetrics getFontMetrics()
	{
		return g2.getFontMetrics();
	}

	public FontMetrics getFontMetrics( Font f )
	{
		return g2.getFontMetrics( f );
	}

	public FontRenderContext getFontRenderContext()
	{
		return g2.getFontRenderContext();
	}

	public Paint getPaint()
	{
		return g2.getPaint();
	}

	public Object getRenderingHint( Key hintKey )
	{
		return g2.getRenderingHint( hintKey );
	}

	public RenderingHints getRenderingHints()
	{
		return g2.getRenderingHints();
	}

	public Stroke getStroke()
	{
		return g2.getStroke();
	}

	public AffineTransform getTransform()
	{
		return g2.getTransform();
	}

	public int hashCode()
	{
		return g2.hashCode();
	}

	public boolean hit( Rectangle rect, Shape s, boolean onStroke )
	{
		return g2.hit(
			rect,
			s,
			onStroke );
	}

	public boolean hitClip( int x, int y, int width, int height )
	{
		return g2.hitClip(
			x,
			y,
			width,
			height );
	}

	public void rotate( double theta, double x, double y )
	{
		g2.rotate(
			theta,
			x,
			y );
	}

	public void rotate( double theta )
	{
		g2.rotate( theta );
	}

	public void scale( double sx, double sy )
	{
		g2.scale(
			sx,
			sy );
	}

	public void setBackground( Color color )
	{
		g2.setBackground( color );
	}

	public void setClip( int x, int y, int width, int height )
	{
		g2.setClip(
			x,
			y,
			width,
			height );
	}

	public void setClip( Shape clip )
	{
		g2.setClip( clip );
	}

	public void setColor( Color c )
	{
		g2.setColor( c );
	}

	public void setComposite( Composite comp )
	{
		g2.setComposite( comp );
	}

	public void setFont( Font font )
	{
		g2.setFont( font );
	}

	public void setPaint( Paint paint )
	{
		g2.setPaint( paint );
	}

	public void setPaintMode()
	{
		g2.setPaintMode();
	}

	public void setRenderingHint( Key hintKey, Object hintValue )
	{
		g2.setRenderingHint(
			hintKey,
			hintValue );
	}

	public void setRenderingHints( Map< ? , ? > hints )
	{
		g2.setRenderingHints( hints );
	}

	public void setStroke( Stroke s )
	{
		g2.setStroke( s );
	}

	public void setTransform( AffineTransform Tx )
	{
		g2.setTransform( Tx );
	}

	public void setXORMode( Color c1 )
	{
		g2.setXORMode( c1 );
	}

	public void shear( double shx, double shy )
	{
		g2.shear(
			shx,
			shy );
	}

	public String toString()
	{
		return g2.toString();
	}

	public void transform( AffineTransform Tx )
	{
		g2.transform( Tx );
	}

	public void translate( double tx, double ty )
	{
		g2.translate(
			tx,
			ty );
	}

	public void translate( int x, int y )
	{
		g2.translate(
			x,
			y );
	}

}
