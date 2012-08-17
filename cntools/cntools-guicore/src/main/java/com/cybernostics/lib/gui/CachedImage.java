package com.cybernostics.lib.gui;

import com.cybernostics.lib.gui.graphics.StateSaver;
import com.cybernostics.lib.gui.shape.ImageRect;
import com.cybernostics.lib.gui.shapeeffects.ShapeEffect;
import com.cybernostics.lib.gui.shapeeffects.TransparentFill;
import com.cybernostics.lib.media.image.BitmapMaker;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.Semaphore;

/**
 *
 * @author jasonw
 */
public class CachedImage
{

	private Dimension dSize = new Dimension();

	private ShapeEffect renderer = null;

	public synchronized void setNeedsRender( boolean needsRenderFlag )
	{
		needsRender = needsRenderFlag;

	}

	public void setRenderer( ShapeEffect renderer )
	{
		this.renderer = renderer;
		setNeedsRender( true );
	}

	public CachedImage()
	{
	}

	private BufferedImage cached = null;

	private boolean needsRender = true;

	private Image rendered = null;

	public synchronized void draw( Graphics2D g2, int x, int y )
	{
		{
			//final StateSaver g_2 = StateSaver.wrap( g2 );
			if (!sem.tryAcquire())
			{
				sem.acquireUninterruptibly();
			}
			try
			{
				updateImage();
				{
					if (renderer == null)
					{
						return;
					}

					//renderer.draw( g2, null );
					g2.drawImage(
						rendered,
						x,
						y,
						null );
					//                    if ( !g2.drawImage( rendered, x, y, null ) )
					//                    {
					//                        /**
					//                         * This seems to occur when the surface onto which we
					//                         * are drawing evaporates... so we need to get a new one
					//                         * in higher level drawing code
					//                         */
					//                        throw new DrawFailedException();
					//                    }
				}
			}
			finally
			{
				sem.release();
				//g_2.restore();
			}

		}
	}

	public synchronized void setSize( int width, int height )
	{
		dSize.width = width;
		dSize.height = height;
	}

	private java.util.concurrent.Semaphore sem = new Semaphore( 1 );

	private synchronized void updateImage()
	{
		try
		{
			if (renderer == null)
			{
				return;
			}
			if (imageNeedsRepaint( null ))
			{
				BufferedImage im = (BufferedImage) getDrawableImage( null );
				StateSaver g2 = StateSaver.wrap( im.getGraphics() );
				TransparentFill.apply(
					(Graphics2D) g2.create(),
					ImageRect.get( im ) );
				renderer.draw(
					g2,
					null );
				needsRender = false;

				g2.dispose();

				rendered = im;

			}

		}
		finally
		{
		}

	}

	private synchronized Image getDrawableImage( GraphicsConfiguration gc )
	{
		BufferedImage im = cached;
		if (im == null || im.getWidth() != dSize.width
			|| im.getHeight() != dSize.height)
		{
			cached = BitmapMaker.createFastImage(
				dSize.width,
				dSize.height,
				Transparency.TRANSLUCENT );
			needsRender = true;
		}
		return cached;
	}

	private boolean imageNeedsRepaint( GraphicsConfiguration gc )
	{
		if (needsRender == true)
		{
			return true;
		}

		if (rendered == null)
		{
			return true;
		}

		return false;
	}

	public void setSize( Dimension size )
	{
		setSize(
			size.width,
			size.height );
	}

	public boolean isNeedsRender()
	{
		return needsRender;
	}

}
