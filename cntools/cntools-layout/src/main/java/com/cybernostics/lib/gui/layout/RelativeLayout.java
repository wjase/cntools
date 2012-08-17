package com.cybernostics.lib.gui.layout;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

/**
 * The
 * <code>RelativeLayout</code> class is a layout manager that lays out a
 * container's components using fractions relative to the bounds of the parent.
 * As the parent is resized then the child will be resized too<P>
 * <code><pre>
 * import java.awt.*;
 * import java.applet.Applet;
 *
 * public class ButtonGrid extends Applet {
 *   public void init() {
 *     setLayout(new RelativeLayout());
 *     // Add a 1x1 Rect at (0,0) which is .1 width and .1 height of parent
 *     add(new Button(&quot;1&quot;), new Rectangle2D.Double(0, 0, .1, .1));
 *   }
 * }
 * </pre></code>
 *
 * @author Jason Wraxall
 */
public class RelativeLayout implements LayoutManager2
{

	private HashMap< Component, Rectangle2D > relativeBounds; // constraints (Rectangles)

	/**
	 * Creates a graph paper layout with the given grid size and padding.
	 *
	 * @param gridSize size of the graph paper in logical units (n x m)
	 * @param hgap horizontal padding
	 * @param vgap vertical padding
	 */
	public RelativeLayout()
	{
		relativeBounds = new HashMap< Component, Rectangle2D >();
	}

	/**
	 * For efficiency we actually add the ref to the constraints param rather than
	 * copy it. That way you can change the bounds using that object
	 * @param comp
	 * @param constraints 
	 */
	public void setConstraints( Component comp, Rectangle2D constraints )
	{
		relativeBounds.put(
			comp,
			constraints );
	}

	/**
	 * Adds the specified component with the specified name to the layout. This
	 * does nothing in RelativeLayout, since constraints are required.
	 */
	public void addLayoutComponent( String name, Component comp )
	{
	}

	/**
	 * Removes the specified component from the layout.
	 *
	 * @param comp the component to be removed
	 */
	public void removeLayoutComponent( Component comp )
	{
		relativeBounds.remove( comp );
	}

	/**
	 * Calculates the preferred size dimensions for the specified panel given
	 * the components in the specified parent container.
	 *
	 * @param parent the component to be laid out
	 *
	 * @see #minimumLayoutSize
	 */
	public Dimension preferredLayoutSize( Container parent )
	{
		return getLayoutSize(
			parent,
			true );
	}

	/**
	 * Calculates the minimum size dimensions for the specified panel given the
	 * components in the specified parent container.
	 *
	 * @param parent the component to be laid out
	 * @see #preferredLayoutSize
	 */
	public Dimension minimumLayoutSize( Container parent )
	{
		return getLayoutSize(
			parent,
			false );
	}

	/**     *
	 * @param parent the container in which to do the layout.
	 * @param isPreferred true for calculating preferred size, false for
	 * calculating minimum size.
	 * @return the dimensions to lay out the subcomponents of the specified
	 * container.
	 * @see java.awt.RelativeLayout#getLargestCellSize
	 */
	protected Dimension getLayoutSize( Container parent, boolean isPreferred )
	{

		return isPreferred ? parent.getPreferredSize() : parent.getSize();
	}

	/**
	 * Lays out the container in the specified container.
	 *
	 * @param parent the component which needs to be laid out
	 */
	@Override
	public void layoutContainer( Container parent )
	{
		synchronized (parent.getTreeLock())
		{
			Insets insets = parent.getInsets();
			int ncomponents = parent.getComponentCount();

			if (ncomponents == 0)
			{
				return;
			}

			// Total parent dimensions, less insets
			Dimension parentSize = parent.getSize();
			int totalW = parentSize.width - ( insets.left + insets.right );
			int totalH = parentSize.height - ( insets.top + insets.bottom );

			for (int i = 0; i < ncomponents; ++i)
			{
				Component c = parent.getComponent( i );
				Rectangle2D rect = relativeBounds.get( c );
				if (rect != null)
				{
					int x = (int) ( insets.left + ( totalW * rect.getMinX() ) );
					int y = (int) ( insets.top + ( totalH * rect.getMinY() ) );
					int w = (int) ( totalW * rect.getWidth() );
					int h = (int) ( totalH * rect.getHeight() );
					c.setBounds(
						x,
						y,
						w,
						h );
				}
			}
		}
	}

	// LayoutManager2 /////////////////////////////////////////////////////////
	/**
	 * Adds the specified component to the layout, using the specified
	 * constraint object.
	 *
	 * @param comp the component to be added
	 * @param constraints where/how the component is added to the layout.
	 */
	@Override
	public void addLayoutComponent( Component comp, Object constraints )
	{
		if (constraints instanceof Rectangle2D)
		{
			Rectangle2D rect = (Rectangle2D) constraints;
			setConstraints(
				comp,
				rect );
		}
		else
			if (constraints != null)
			{
				throw new IllegalArgumentException( "cannot add to RelativeLayout: constraint must be a Rectangle2D" );
			}
	}

	/**
	 * Returns the maximum size of this component.
	 *
	 * @see java.awt.Component#getMinimumSize()
	 * @see java.awt.Component#getPreferredSize()
	 * @see LayoutManager
	 */
	public Dimension maximumLayoutSize( Container target )
	{
		return new Dimension( Integer.MAX_VALUE, Integer.MAX_VALUE );
	}

	/**
	 * Returns the alignment along the x axis. This specifies how the component
	 * would like to be aligned relative to other components. The value should
	 * be a number between 0 and 1 where 0 represents alignment along the
	 * origin, 1 is aligned the furthest away from the origin, 0.5 is centered,
	 * etc.
	 */
	public float getLayoutAlignmentX( Container target )
	{
		return 0.5f;
	}

	/**
	 * Returns the alignment along the y axis. This specifies how the component
	 * would like to be aligned relative to other components. The value should
	 * be a number between 0 and 1 where 0 represents alignment along the
	 * origin, 1 is aligned the furthest away from the origin, 0.5 is centered,
	 * etc.
	 */
	public float getLayoutAlignmentY( Container target )
	{
		return 0.5f;
	}

	/**
	 * Invalidates the layout, indicating that if the layout manager has cached
	 * information it should be discarded.
	 */
	public void invalidateLayout( Container target )
	{
		// Do nothing
	}

}
