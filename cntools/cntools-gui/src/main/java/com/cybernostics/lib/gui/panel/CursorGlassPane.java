package com.cybernostics.lib.gui.panel;

import java.awt.AWTEvent;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CursorGlassPane extends JPanel implements AWTEventListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4663001666507504961L;
	private JFrame frame = null;
	private Point point = new Point();
	private Point shadowOffset = new Point();
	private Rectangle lastPaintedRegion = new Rectangle();

	public Point getShadowOffset()
	{
		return shadowOffset;
	}

	public void setShadowOffset( Point shadowOffset )
	{
		if (shadowOffset != null)
		{
			this.shadowOffset = shadowOffset;
		}
	}

	private BufferedImage cursorShadow;

	public Image getCursorShadow()
	{
		return cursorShadow;
	}

	public void setCursorShadow( BufferedImage cursorShadow )
	{
		this.cursorShadow = cursorShadow;
		lastPaintedRegion.width = ( cursorShadow.getWidth() * 2 );
		lastPaintedRegion.height = ( cursorShadow.getHeight() * 2 );
		repaint( lastPaintedRegion );
	}

	public CursorGlassPane( JFrame frame )
	{
		super( null );
		this.frame = frame;
		setOpaque( false );
		getToolkit().addAWTEventListener(
			this,
			AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK );
	}

	public void setPoint( Point point )
	{
		this.point = point;
	}

	final Composite seeThrough = AlphaComposite.getInstance(
		AlphaComposite.SRC_OVER,
		0.7f );

	@Override
	protected void paintComponent( Graphics g )
	{
		// super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (( point != null ) && ( cursorShadow != null ))
		{
			//g2.setComposite( seeThrough );
			g2.drawImage(
				cursorShadow,
				point.x + shadowOffset.x,
				point.y + shadowOffset.y,
				null );
			lastPaintedRegion.x = point.x + shadowOffset.x - 20;
			lastPaintedRegion.y = point.y + shadowOffset.y - 20;
		}
		g2.dispose();
	}

	@Override
	public void eventDispatched( AWTEvent event )
	{
		if (event instanceof MouseEvent)
		{
			MouseEvent me = (MouseEvent) event;
			if (!SwingUtilities.isDescendingFrom(
				me.getComponent(),
				frame ))
			{
				return;
			}
			if (( me.getID() == MouseEvent.MOUSE_EXITED )
				&& ( me.getComponent() == frame ))
			{
				if (( cursorShadow != null ) && ( point != null ))
				{
					repaint( lastPaintedRegion );
				}
				point = null;
			}
			else
			{
				if (( cursorShadow != null ) && ( point != null ))
				{
					repaint( lastPaintedRegion );
				}
				MouseEvent converted = SwingUtilities.convertMouseEvent(
					me.getComponent(),
					me,
					frame.getGlassPane() );
				point = converted.getPoint();

				if (( cursorShadow != null ) && ( point != null ))
				{
					repaint( lastPaintedRegion );
				}
			}

		}
	}

	/**
	 * If someone adds a mouseListener to the GlassPane or set a new cursor we
	 * expect that he knows what he is doing and return the super.contains(x, y)
	 * otherwise we return false to respect the cursors for the underneath
	 * components
	 */
	@Override
	public boolean contains( int x, int y )
	{
		if (( getMouseListeners().length == 0 )
			&& ( getMouseMotionListeners().length == 0 )
			&& ( getMouseWheelListeners().length == 0 )
			&& ( getCursor() == Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) ))
		{
			return false;
		}
		return super.contains(
			x,
			y );
	}
}
