package com.cybernostics.lib.gui.shapeeffects;

import com.cybernostics.lib.gui.GraphicsConfigurationSource;
import com.cybernostics.lib.gui.shape.ShapeUtils2D;
import com.cybernostics.lib.media.image.BitmapMaker;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;

/**
 * Renders to an accelerated video buffer which can be manually updated/cleared
 * if requested
 *
 * @author jasonw
 */
public class BufferedEffect implements IBufferedEffect
{

	private GraphicsConfigurationSource gcSource;

	public GraphicsConfigurationSource getGcSource()
	{
		return gcSource;
	}

	public void setGcSource( GraphicsConfigurationSource gcSource )
	{
		this.gcSource = gcSource;
	}

	private ShapeEffect drawEffect = null;

	private BufferedImage buffer = null;

	private GraphicsConfiguration gConfig = null;

	private CountDownLatch clearBuff = new CountDownLatch( 1 );

	public ShapeEffect getInternal()
	{
		return drawEffect;
	}

	public BufferedEffect(
		GraphicsConfigurationSource gcSource,
		ShapeEffect toDraw )
	{
		this.gcSource = gcSource;
		drawEffect = toDraw;

	}

	@Override
	public void draw( Graphics2D g2, Shape s )
	{
		if (clearBuff.getCount() <= 0)
		{
			clearBuff = new CountDownLatch( 1 );
			buffer = null;
		}
		Rectangle2D bounds = ShapeUtils2D.getBounds( s );

		if (buffer == null || buffer.getWidth() != bounds.getWidth()
			|| buffer.getHeight() != bounds.getHeight()
			|| buffer.getWidth() != Math.round( bounds.getWidth() )
			|| buffer.getHeight() != Math.round( bounds.getHeight() ))
		{
			renderToBuffer(
				bounds,
				s,
				true );
		}

		//
		if (g2.drawImage(
			buffer,
			(int) bounds.getMinX(),
			(int) bounds.getMinY(),
			null ))
		{
			return;
		}

		// if we made it here there was some issue with the buffer so draw it normally
		drawEffect.draw(
			g2,
			s );

	}

	private void renderToBuffer( Rectangle2D bounds,
		Shape s,
		boolean createBuffer )
	{
		if (createBuffer)
		{
			buffer = BitmapMaker.createFastImage(
				(int) bounds.getWidth(),
				(int) bounds.getHeight(),
				BufferedImage.TRANSLUCENT );
		}

		AffineTransform move = AffineTransform.getTranslateInstance(
			-bounds.getMinX(),
			-bounds.getMinY() );
		Area bufferShape = new Area( s );
		bufferShape = bufferShape.createTransformedArea( move );

		if (drawEffect != null)
		{
			drawEffect.draw(
				(Graphics2D) buffer.getGraphics(),
				bufferShape );
		}

	}

	/**
	 * Sets a thread-safe flag to request the buffer to be cleared before it is
	 * next rendered
	 */
	public void clear()
	{
		clearBuff.countDown();
	}

}
