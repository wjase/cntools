package com.cybernostics.lib.gui;

import com.cybernostics.lib.gui.declarative.events.WhenMadeVisible;
import com.cybernostics.lib.gui.declarative.events.WhenResized;
import com.cybernostics.lib.maths.DimensionFloat;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;

/**
 * Manages the size of a container. The constructor attaches itself to the
 * component as a client property
 *
 * @author jasonw
 *
 */
public class RelativeBoundsManager
{

	public static void addWithinBounds( JComponent parent,
		JComponent comp,
		Rectangle2D area )
	{
		parent.add( comp );
		RelativeBoundsManager boundsMgr = (RelativeBoundsManager) comp
			.getClientProperty( RelativeBoundsManager.PROPERTY );
		if (boundsMgr == null)
		{
			boundsMgr = RelativeBoundsManager.bind(
				comp,
				parent,
				area );
			comp.putClientProperty(
				RelativeBoundsManager.PROPERTY,
				boundsMgr );
		}
		else
		{
			boundsMgr.setBounds( area );
		}

	}

	/**
	 *
	 */
	public static final String PROPERTY = "boundsManager";

	private Dimension containerSize = null;

	private JComponent managed = null;

	private Container relativeParent = null;

	private Rectangle2D bounds = new Rectangle2D.Float();

	private Rectangle tempBoundsInstance = new Rectangle(); // to save newing

	// one every time
	/**
	 *
	 * @return
	 */
	public Rectangle2D getParentBounds()
	{
		Rectangle2D rectParent = new Rectangle2D.Double( 0, 0, 1, 1 );

		if (relativeParent != managed.getParent())
		{
			Point pParent = managed.getParent()
				.getLocationOnScreen();
			Point rParent = relativeParent.getLocationOnScreen();

			// Offset by the immediate parents bounds
			Rectangle absRect = managed.getParent()
				.getBounds();
			absRect.x = ( pParent.x - rParent.x );
			absRect.y = ( pParent.y - rParent.y );

			// convert to relative of outer window
			rectParent.setFrame(
				1.0 * absRect.x / containerSize.width,
				1.0 * absRect.y / containerSize.height,
				1.0
					* absRect.width / containerSize.width,
				1.0 * absRect.height / containerSize.height );

		}
		return rectParent;
	}

	/**
	 *
	 * @return
	 */
	public Rectangle2D getBounds()
	{
		return bounds;
	}

	/**
	 * The current absolute bounds of the region relative to its parent
	 */
	private Rectangle absoluteBounds = new Rectangle();

	public static RelativeBoundsManager bind( JComponent managed,
		Container cRelativeTo )
	{
		RelativeBoundsManager rbm = new RelativeBoundsManager( managed );
		managed.putClientProperty(
			PROPERTY,
			rbm );
		rbm.bindParent( cRelativeTo );
		return rbm;

	}

	public static RelativeBoundsManager bind( JComponent managed,
		Container cRelativeTo,
		Rectangle2D rect )
	{
		RelativeBoundsManager rbm = new RelativeBoundsManager( managed, rect );
		managed.putClientProperty(
			PROPERTY,
			rbm );
		rbm.bindParent( cRelativeTo );
		return rbm;

	}

	/**
	 *
	 * @param managed
	 * @param cRelativeTo
	 */
	protected RelativeBoundsManager( JComponent managed )
	{
		this( managed, new Rectangle2D.Double( 0, 0, 0, 0 ) );
	}

	/**
	 *
	 * @param managed
	 * @param cRelativeTo
	 * @param newBounds
	 */
	protected RelativeBoundsManager( JComponent managed, Rectangle2D newBounds )
	{
		this.managed = managed;
		this.bounds = (Rectangle2D) newBounds.clone();

	}

	protected void bindParent( Container cRelativeTo )
	{
		relativeParent = ( cRelativeTo != null ) ? cRelativeTo
			: managed.getParent();

		if (relativeParent.isVisible())
		{
			updateToParent();
		}
		else
		{
			new WhenMadeVisible( relativeParent )
			{

				@Override
				public void doThis( AWTEvent e )
				{
					updateToParent();
				}

			};

		}

		new WhenResized( relativeParent )
		{

			@Override
			public void doThis( ComponentEvent e )
			{
				updateToParent();
			}

		};

	}

	public void updateToParent()
	{
		containerSize = relativeParent.getSize();
		updateBounds();

	}

	/**
	 *
	 */
	protected void updateBounds()
	{
		updateSize();
		updateLocation();

	}

	/**
	 *
	 */
	private void updateLocation()
	{
		if (( containerSize == null ) || ( managed.getParent() == null )
			|| ( bounds == null ) || ( bounds.getWidth() == 0 )) // no
		// container
		// yet
		{
			return;
		}

		absoluteBounds.x = (int) ( bounds.getMinX() * containerSize.width );
		absoluteBounds.y = (int) ( bounds.getMinY() * containerSize.height );

		// If we are setting location relative to other than my direct parent
		if (relativeParent != managed.getParent())
		{
			Point relativeParentOrigin = relativeParent.getLocationOnScreen();
			Point parentOrigin = managed.getParent()
				.getLocationOnScreen();
			absoluteBounds.x -= ( parentOrigin.x - relativeParentOrigin.x );
			absoluteBounds.y -= ( parentOrigin.y - relativeParentOrigin.y );
		}

		repaintParent();
		managed.setLocation(
			absoluteBounds.x,
			absoluteBounds.y );
		managed.revalidate();
		repaintParent();

	}

	private void repaintParent()
	{
		managed.getBounds( tempBoundsInstance );
		Container parent = managed.getParent();
		if (parent != null)
		{
			parent.repaint(
				tempBoundsInstance.x,
				tempBoundsInstance.y,
				tempBoundsInstance.width,
				tempBoundsInstance.height );
		}
	}

	/**
	 *
	 */
	private void updateSize()
	{
		if (( containerSize == null ) || ( bounds == null )
			|| ( bounds.getWidth() == 0 ) || containerSize.width == 0) // no
		// container
		// yet
		{
			return;
		}
		repaintParent( absoluteBounds );
		absoluteBounds.width = (int) ( bounds.getWidth() * containerSize.width );
		absoluteBounds.height = (int) ( bounds.getHeight() * containerSize.height );
		managed.setSize(
			absoluteBounds.width,
			absoluteBounds.height );

		repaintParent( absoluteBounds );
	}

	/**
	 * @param absoluteBounds2
	 */
	private void repaintParent( Rectangle absoluteBounds2 )
	{
		// TODO Auto-generated method stub
	}

	/**
	 *
	 * @param bounds
	 */
	public void setBounds( Rectangle2D bounds )
	{
		this.bounds = bounds;
		updateBounds();
	}

	/**
	 *
	 * @param size
	 */
	public void setSize( DimensionFloat size )
	{
		this.bounds.setRect(
			bounds.getMinX(),
			bounds.getMinY(),
			size.getWidth(),
			size.getHeight() );
		setBounds( bounds );
	}

	/**
	 *
	 * @param width
	 * @param height
	 */
	public void setSize( double width, double height )
	{
		this.bounds.setRect(
			bounds.getMinX(),
			bounds.getMinY(),
			width,
			height );
		setBounds( bounds );
	}

	/**
	 *
	 * @param location
	 */
	public void setLocation( Point2D location )
	{
		this.bounds.setRect(
			location.getX(),
			location.getY(),
			bounds.getWidth(),
			bounds.getHeight() );
		setBounds( bounds );

	}

	/**
	 *
	 * @param x
	 * @param y
	 */
	public void setLocation( double x, double y )
	{
		this.bounds.setRect(
			x,
			y,
			bounds.getWidth(),
			bounds.getHeight() );
		setBounds( bounds );
	}

	/**
	 * @param someRect
	 * @return
	 */
	public Dimension getAbsoluteSize( Rectangle2D someRect )
	{
		// absoluteBounds.x = (int)(someRect.getMinX()* containerSize.width);
		// absoluteBounds.y = (int)(someRect.getMinY()* containerSize.height);
		absoluteBounds.width = (int) ( someRect.getWidth() * containerSize.width );
		absoluteBounds.height = (int) ( someRect.getHeight() * containerSize.height );
		return absoluteBounds.getSize();
	}

	/**
	 * Convenience method for setting bounds without a Rectangle2D
	 *
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public void setBounds( double x, double y, double w, double h )
	{
		setLocation(
			x,
			y );
		setSize(
			w,
			h );

	}

}
