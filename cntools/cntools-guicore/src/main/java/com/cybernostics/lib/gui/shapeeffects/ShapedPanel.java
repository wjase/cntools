package com.cybernostics.lib.gui.shapeeffects;

import com.cybernostics.lib.gui.ComponentGraphicsConfigurationSource;
import com.cybernostics.lib.gui.graphics.StateSaver;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.plaf.PanelUI;

/**
 * A Panel whose clip region is set by the border, and whose background is
 * painted by ShapeEffects rather than hard coded
 *
 * @author jasonw
 */
public class ShapedPanel extends JPanel
{

	public ShapedPanel()
	{
		setOpaque( false );
	}

	/**
	 * Resets the UI property with a value from the current look and feel.
	 *
	 * @see JComponent#updateUI
	 */
	@Override
	public void updateUI()
	{
		setUI( ShapedPanelUI.createUI( this ) );
	}

	@Override
	public PanelUI getUI()
	{
		return null;
	}

	@Override
	public void setSize( int width, int height )
	{
		super.setSize(
			width,
			height );
		bgRect = new Rectangle2D.Double( 0, 0, width, height );
	}

	@Override
	public void setSize( Dimension d )
	{
		super.setSize( d );
		bgRect = new Rectangle2D.Double( 0, 0, d.width, d.height );
	}

	@Override
	public void setBounds( int x, int y, int width, int height )
	{
		super.setBounds(
			x,
			y,
			width,
			height );
		bgRect = new Rectangle2D.Double( 0, 0, width, height );
	}

	@Override
	public void setBounds( Rectangle r )
	{
		super.setBounds( r );
		bgRect.setFrame( r );
	}

	Color clearColor = new Color( 1.0f, 1.0f, 1.0f, 1.0f );

	private ShapeEffect backgroundPainter = NOPEffect.get();

	private ShapeEffect foregroundPainter = NOPEffect.get();

	private ShapeEffect contentPainter = NOPEffect.get();

	public ShapeEffect getContentPainter()
	{
		return contentPainter;
	}

	public void setContentPainter( ShapeEffect contentPainter )
	{
		this.contentPainter = contentPainter;
	}

	public ShapeEffect getForegroundPainter()
	{
		return foregroundPainter;
	}

	public void setForegroundPainter( ShapeEffect foregroundPainter )
	{
		this.foregroundPainter = foregroundPainter;
	}

	public ShapeEffect getBackgroundPainter()
	{
		return backgroundPainter;
	}

	public void setBackgroundPainter( ShapeEffect painter )
	{
		if (painter == null)
		{
			return;
		}
		if (!( painter instanceof NOPEffect ))
		{
			// backgrounds are big and usually static so buffer them
			if (!( painter instanceof IBufferedEffect ))
			{
				painter = new BufferedEffect( ComponentGraphicsConfigurationSource.create( this ),
					painter );
			}
			else
			{
				IBufferedEffect buf = (IBufferedEffect) painter;
				buf.setGcSource( ComponentGraphicsConfigurationSource.create( this ) );
			}
		}
		this.backgroundPainter = painter;
		setOpaque( false ); // we'll handle the background thanks
		revalidate();
		repaint();
	}

	private Rectangle2D bgRect = new Rectangle2D.Double( 0,
		0,
		getWidth(),
		getHeight() );

	//    @Override
	//    protected void paintComponent( Graphics g )
	//    {
	//        paint( g );
	//    }
	/**
	 * The behaviour of painting this component can be overridden by calling
	 * setBackgroundPainter to paint the background and SetBorder with a
	 * ShapedBorder to set the panel clip.
	 */
	@Override
	public void paintComponent( Graphics g )
	{
		if (bgRect.getWidth() == 0 || bgRect.getHeight() == 0)
		{
			bgRect = new Rectangle2D.Double( 0, 0, getWidth(), getHeight() );
			if (bgRect.getWidth() == 0 || bgRect.getHeight() == 0)
			{
				return;
			}
		}

		StateSaver g2 = StateSaver.wrap( g );
		try
		{
			Border b = getBorder();
			if (b != null && b instanceof ShapedBorder)
			{
				Shape clip = ( (ShapedBorder) b ).getBackgroundClipShape( this );
				g2.setClip( clip );

			}

			if (backgroundPainter != null)
			{
				setOpaque( false );

				backgroundPainter.draw(
					g2,
					bgRect );
			}

			contentPainter.draw(
				g2,
				bgRect );

			StateSaver g2Children = StateSaver.wrap( g2 );
			paintChildren( g2Children );
			g2Children.restore();

			paintBorder( g2 );

		}
		finally
		{
			g2.restore();
		}

	}

	@Override
	public void paint( Graphics g )
	{
		super.paint( g );
		if (foregroundPainter != null)
		{
			StateSaver g2 = StateSaver.wrap( g );
			try
			{
				foregroundPainter.draw(
					g2,
					bgRect );
			}
			finally
			{
				g2.restore();
			}
		}

	}

	@Override
	public boolean isOpaque()
	{
		return super.isOpaque() || getBackgroundPainter() != NOPEffect.get();
	}

}
