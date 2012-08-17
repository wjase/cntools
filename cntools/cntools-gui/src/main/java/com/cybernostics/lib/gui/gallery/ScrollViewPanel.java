package com.cybernostics.lib.gui.gallery;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.cybernostics.lib.gui.ScreenRelativeDimension;

/**
 * This scroll panel allows a sliding window of components to be shown without having them all visible.
 * Components are added outside the visible window bounds and then scrolled into view.
 * Components can be added to the left or the right.
 * An odd number of components is shown so the central one is always the one selected.
 * 
 * 
 * 
 * @author jasonw
 *
 */
public class ScrollViewPanel extends JPanel
{

	static int mainScrollPosition = 0;

	public static void main( String[] args )
	{
		JFrame jf = new JFrame( "ScrollViewPanelTest" );
		final ScrollViewPanel p = new ScrollViewPanel();

		int componentCount = 50;

		JPanel view = p.getInternalView();
		p.setBorder( BorderFactory.createLineBorder(
			Color.black,
			5 ) );
		view.setLayout( new GridLayout( 1, componentCount ) );
		Dimension d = new ScreenRelativeDimension( 0.09f, 0.09f );
		for (int i = 0; i < componentCount; i++)
		{
			JButton jb = new JButton( "Button " + i );
			jb.setPreferredSize( d );
			jb.setBorder( BorderFactory.createLineBorder( Color.red ) );
			view.add( jb );
		}

		jf.getContentPane()
			.setLayout(
				new FlowLayout() );
		jf.getContentPane()
			.add(
				p );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		jf.pack();
		jf.setSize(
			400,
			400 );
		jf.setVisible( true );

		while (mainScrollPosition < 800)
		{
			try
			{
				Thread.sleep( 20 );
			}
			catch (InterruptedException e)
			{
				break;
			}
			SwingUtilities.invokeLater( new Runnable()
			{

				@Override
				public void run()
				{
					p.setScrollPosition( mainScrollPosition );
				}
			} );

			// p.repaint();
			mainScrollPosition += 1;
		}

	}

	/**
	 * When this is zero the view will be centered on the internal view
	 * 
	 * <PRE>
	 * ie: |-----------------internal view -------------|
	 *                |----visible view -----|
	 * 
	 *     &circ; 
	 *     | 
	 *  position 0
	 * </PRE>
	 * 
	 */
	private int scrollPosition = 0;

	/**
	 * This is the offset from the shoulder to the internal view.
	 */
	private int shoulderOffset = 0;

	/**
	 * The number of components to the left and right of the visible window
	 */
	private int shoulderSize = 1;

	/**
	 * The container for components which get scrolled.
	 */
	private final JPanel internalView = new JPanel();

	/**
	 * To stop warnings...
	 */
	private static final long serialVersionUID = -3344363399730819025L;

	public ScrollViewPanel()
	{
		setLayout( null ); // no layout manager please
		add( internalView );

		internalView.addContainerListener( new ContainerListener()
		{

			@Override
			public void componentAdded( ContainerEvent e )
			{
				Dimension size = internalView.getPreferredSize();
				internalView.setSize( size );
				updateInternalBounds();
				validateTree();

			}

			@Override
			public void componentRemoved( ContainerEvent e )
			{
				Dimension size = internalView.getPreferredSize();
				internalView.setSize( size );
				updateInternalBounds();

			}
		} );
		updateInternalBounds();

		setOpaque( false );
		internalView.setOpaque( false );

	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#addNotify()
	 */
	@Override
	public void addNotify()
	{
		// TODO Auto-generated method stub
		super.addNotify();
	}

	@Override
	public Color getBackground()
	{
		return UIManager.getColor( "Panel.background" );
	}

	/**
	 * Returns the component which is scrolled within this scroll view
	 * 
	 * @return
	 */
	public JPanel getInternalView()
	{
		return internalView;
	}

	/**
	 * Returns the offset from the internal view windows left edge to the first
	 * visible component
	 * 
	 * @return
	 */
	public int getLeftOffset()
	{
		if (shoulderSize == 0)
		{
			return 0;
		}

		int ComponentCount = internalView.getComponentCount();
		if (ComponentCount > ( shoulderSize * 2 ))
		{
			Point pLeftVisible = internalView.getComponent(
				shoulderSize )
				.getLocation();
			Point pLeft = internalView.getComponent(
				0 )
				.getLocation();

			return pLeftVisible.x - pLeft.x;
		}
		return 0;
	}

	@Override
	public Dimension getPreferredSize()
	{
		Dimension componentSize = internalView.getPreferredSize();
		Insets ins = getInsets();

		int ComponentCount = internalView.getComponentCount();

		if (ComponentCount > ( shoulderSize * 2 ))
		{
			int leftX = getLeftOffset();
			int rightX = getRightOffset();

			componentSize.width = rightX - leftX;
			componentSize.width = componentSize.width + ( ins.left + ins.right );
			componentSize.height = componentSize.height
				+ ( ins.top + ins.bottom );

			// this should be negative
			shoulderOffset = leftX * -1;

			//setPreferredSize( componentSize );

		}

		return componentSize;

	}

	public int getRightOffset()
	{
		if (shoulderSize == 0)
		{
			return 0;
		}

		int ComponentCount = internalView.getComponentCount();
		if (ComponentCount > ( shoulderSize * 2 ))
		{
			Point pLeftVisible = internalView.getComponent(
				ComponentCount - shoulderSize )
				.getLocation();
			Point pLeft = internalView.getComponent(
				0 )
				.getLocation();

			return pLeftVisible.x - pLeft.x;
		}
		return 0;

	}

	public int getScrollItemWidth()
	{
		if (internalView.getComponentCount() > 1)
		{
			Component c = internalView.getComponent( 0 );
			Component c2 = internalView.getComponent( 1 );
			return c2.getLocation().x - c.getLocation().x;
		}
		return 0;
	}

	/**
	 * @return the scrollPosition
	 */
	public int getScrollPosition()
	{
		return scrollPosition;
	}

	public int getShoulderSize()
	{
		return shoulderSize;
	}

	@Override
	protected void paintChildren( Graphics g )
	{
		super.paintChildren( g );
		paintBorder( g );
	}

	/**
	 * @param scrollPosition
	 *            the scrollPosition to set
	 */
	public void setScrollPosition( int scrollPosition )
	{
		this.scrollPosition = scrollPosition;
		updateInternalPosition();
		validate();
	}

	public void setShoulderSize( int shoulderSize )
	{
		this.shoulderSize = shoulderSize;
	}

	private void updateInternalBounds()
	{
		Dimension componentSize = internalView.getPreferredSize();

		int ComponentCount = internalView.getComponentCount();

		if (ComponentCount > ( shoulderSize * 2 ))
		{
			int leftX = getLeftOffset();
			int rightX = getRightOffset();

			componentSize.width = rightX - leftX;

			// this should be negative
			shoulderOffset = leftX * -1;

			// setPreferredSize( componentSize );
			setMaximumSize( getPreferredSize() );
			setMinimumSize( getPreferredSize() );

			updateInternalPosition();

		}
	}

	/**
	 * Assuming an add number of components in the internal component, this
	 * returns the central one.
	 * 
	 * @return
	 */
	public Component getCenterComponent()
	{
		int count = internalView.getComponentCount();

		// for (int index=0;index<internalView.getComponentCount();++index)
		// {
		// Rectangle r = internalView.getComponent( index ).getBounds();
		// System.out.printf( "component %d bounds (x,y,w,h)=(%d,%d,%d,%d)\n",
		// index, r.x,r.y,r.width,r.height );
		//            
		//			
		// }
		Component middleOne = internalView.getComponent( count / 2 );

		return middleOne;

	}

	public void getCenterBounds( Rectangle r )
	{

		Component c = getCenterComponent();
		c.getBounds( r );

	}

	public void updateInternalPosition()
	{
		Insets ins = getInsets();
		internalView.setLocation(
			scrollPosition + shoulderOffset,
			ins.top );
	}

	@Override
	public void validate()
	{
		super.validate();
		internalView.validate();
		updateInternalBounds();
	}

	public int getComponentIndex( Object source )
	{

		for (int index = 0; index < internalView.getComponentCount(); ++index)
		{
			if (internalView.getComponent( index ) == source)
			{
				return index;
			}
		}

		return -1;
	}
}
