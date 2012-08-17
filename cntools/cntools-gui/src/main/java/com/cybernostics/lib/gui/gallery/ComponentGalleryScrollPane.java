package com.cybernostics.lib.gui.gallery;

import com.cybernostics.lib.animator.paramaterised.SinusoidChangeInteger;
import com.cybernostics.lib.animator.track.ReflectionPropertyAnimatorTrack;
import com.cybernostics.lib.animator.track.Sequencer;
import com.cybernostics.lib.animator.track.Track;
import com.cybernostics.lib.animator.track.ordering.TrackEndedListener;
import com.cybernostics.lib.concurrent.GUIEventThread;
import com.cybernostics.lib.gui.ScreenRelativeDimension;
import com.cybernostics.lib.gui.border.TubeBorder;
import com.cybernostics.lib.gui.declarative.events.RunLater;
import com.cybernostics.lib.gui.declarative.events.WhenClicked;
import com.cybernostics.lib.gui.declarative.events.WhenMadeVisible;
import com.cybernostics.lib.gui.declarative.events.WhenResized;
import com.cybernostics.lib.gui.graphics.StateSaver;
import com.cybernostics.lib.gui.grouplayoutplus.GroupLayoutPlus;
import com.cybernostics.lib.gui.grouplayoutplus.PARALLEL;
import com.cybernostics.lib.gui.grouplayoutplus.SEQUENTIAL;
import com.cybernostics.lib.gui.shapeeffects.ShapedBorder;
import com.cybernostics.lib.gui.shapeeffects.ShapedPanel;

import com.cybernostics.lib.media.image.ImageLoader;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.ListModel;
import javax.swing.border.Border;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 *
 * This Panel maintains a scrolling window list of items from a ListModel. It
 * creates and consumes items offscreen and then scrolls them into view. That
 * way it can render extremely large lists without huge overhead.
 *
 * @author jasonw
 *
 */
public class ComponentGalleryScrollPane extends ShapedPanel
	implements
	PropertyChangeListener
{

	public static void main( String[] args )
	{

		JFrame jf = new JFrame( "ScrollViewPanelTest" );
		final ComponentGalleryScrollPane gallery = new ComponentGalleryScrollPane();

		gallery.setCellRenderer( new DefaultGalleryCellRenderer() );

		// add a list of photos to the gallery
		gallery.setModel( ImageLoader.getIcons(
			"file:///C:/data/images/Photographs/2008/2008_01_17",
															ImageLoader.ScaleType.WIDTH_ONLY,
															new ScreenRelativeDimension( 0.1f,
																0.1f ) ) );

		gallery.setBackground( Color.DARK_GRAY );
		gallery.setBorder( new TubeBorder( 20 ) );

		JButton jbLeft = new JButton( "Left" );
		JButton jbRight = new JButton( "Right" );

		gallery.setButtons(
			jbLeft,
			jbRight );

		gallery.addPropertyChangeListener(
			"currentIndex",
			new PropertyChangeListener()
		{

			@Override
			public void propertyChange( PropertyChangeEvent evt )
			{
				System.out.printf(
					"new Index %d\n",
					evt.getNewValue() );

			}

		} );

		GroupLayoutPlus glp = new GroupLayoutPlus( jf.getContentPane() );

		glp.setHorizontalGroup( PARALLEL.group(
			Alignment.CENTER,
			gallery,
			SEQUENTIAL.group(
				jbLeft,
				jbRight ) ) );
		glp.setVerticalGroup( SEQUENTIAL.group(
			gallery,
			PARALLEL.group(
				jbLeft,
				jbRight ) ) );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		jf.pack();
		jf.setSize(
			400,
			400 );
		jf.setVisible( true );

		new WhenClicked( jbLeft )
		{

			@Override
			public void doThis( ActionEvent e )
			{
				gallery.scrollLeft();
			}

		};

		new WhenClicked( jbRight )
		{

			@Override
			public void doThis( ActionEvent e )
			{
				gallery.scrollRight();
			}

		};

		try
		{
			Thread.sleep( 5000 );
		}
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}

	}

	private final static String INDEX = "index";

	private JButton leftButton = null;

	private JButton rightButton = null;

	private ListModel model = null;

	private int selectedIndex = -1;

	/**
	 * Sets the select item index and scrolls accordingly
	 *
	 * @param selectedIndex
	 */
	public void setSelectedIndex( int selectedIndex )
	{
		if (selectedIndex >= model.getSize())
		{
			throw new IllegalArgumentException( String.format(
					"%d is greater than the number of elements in the gallery model (%d)",
				selectedIndex,
					model.getSize() ) );
		}
		int oldValue = this.selectedIndex;
		if (oldValue == selectedIndex)
		{
			firePropertyChange(
				"currentIndex",
				oldValue,
				selectedIndex );
			return; // already there nothing to do
		}
		this.selectedIndex = selectedIndex;

		setScrollPosition( getItemScrollPosition( selectedIndex ) );
		firePropertyChange(
			"currentIndex",
			oldValue,
			selectedIndex );
	}

	private boolean isScrolling = false;

	public int getSelectedIndex()
	{
		return selectedIndex;
	}

	private void updateButtonStates()
	{
		int selected = getSelectedIndex();
		boolean leftEnabled = selected > 0;
		leftButton.setEnabled( leftEnabled );
		int maxIndex = getModel().getSize() - 1;
		boolean rightEnabled = ( selected < maxIndex );
		rightButton.setEnabled( rightEnabled );
		if (!leftEnabled && !rightEnabled)
		{
			System.out.printf(
				"Both disabled: selected %d max %d\n",
				selected,
				maxIndex );
		}
	}

	/**
	 * Converts objects in the list to a JComponent
	 */
	private GalleryCellRenderer cellRenderer = null;

	public void setButtons( JButton left, JButton right )
	{
		leftButton = left;
		rightButton = right;
	}

	ReflectionPropertyAnimatorTrack< Integer > scrollerTrack = new ReflectionPropertyAnimatorTrack< Integer >( "scroller",
																											null,
		this,
																											"setScrollPosition",
																											400 ); // $NON-NLS

	/**
	 *
	 */
	public void scrollRight()
	{
		if (selectedIndex < getModel().getSize())
		{
			scrollTo( getScrollPosition() - getElementScrollDistance() );
		}
	}

	/**
	 * Initiates a scroll to a given position
	 *
	 * @param newPos
	 */
	public void scrollTo( int newPos )
	{
		scroll(
			new SinusoidChangeInteger( getScrollPosition(), newPos ),
			newPos < getScrollPosition() ? Sides.RIGHT :
					Sides.LEFT );
	}

	/**
	 *
	 */
	public void scrollLeft()
	{
		if (selectedIndex > 0)
		{
			scrollTo( getScrollPosition() + getElementScrollDistance() );
		}

	}

	private boolean isScrolling()
	{
		return scrollerTrack.isRunning();
	}

	private void scroll( SinusoidChangeInteger xMove, final Sides whichSide )
	{
		//        if ( isScrolling )
		//        {
		//            return;
		//        }
		if (isScrolling())
		{
			return;
		}
		scrollerTrack.reset();
		scrollerTrack.setAnimatorFunction( xMove );
		leftButton.setEnabled( false );
		rightButton.setEnabled( false );
		isScrolling = true;
		scrollerTrack.addTrackEndedListener( new TrackEndedListener()
		{

			@Override
			public void trackEnded( Track source )
			{
				scrollerTrack.removeTrackEndedListener( this );
				if (whichSide == Sides.RIGHT)
				{
					new RunLater()
					{

						@Override
						public void run( Object... args )
						{
							addToRight();
							removeFromLeft();
						}

					};
				}
				else
				{
					new RunLater()
					{

						@Override
						public void run( Object... args )
						{
							addToLeft();
							removeFromRight();
						}

					};
				}
				JComponent c = findCentralComponent();
				if (c != null && c != ComponentGalleryScrollPane.this)
				{
					final Integer iSelected = (Integer) c.getClientProperty( INDEX );
					if (iSelected != null)
					{
						if (selectedIndex != iSelected)
						{
							GUIEventThread.runLater( new Runnable()
							{

								@Override
								public void run()
								{
									int oldValue = selectedIndex;
									selectedIndex = iSelected;
									firePropertyChange(
										"currentIndex",
										oldValue,
										selectedIndex );
								}

							} );

						}
					}

				}
				updateButtonStates();
				isScrolling = false;
			}

		} );

		Sequencer.get()
			.addAndStartTrack(
				scrollerTrack );

	}

	private int scrollPosition = 0;

	/*
	 * Used for the gap between each component and between the top and bottom of
	 * the component list
	 */
	private int interComponentGap = 16;

	private JComponent centralComponent = null;

	/**
	 * Allows the scroll position 0 to centre item 0 on this panel
	 */
	private int midPointOffset = 0;

	/**
	 * To stop warnings...
	 */
	private static final long serialVersionUID = -3344363399730819025L;

	public ComponentGalleryScrollPane()
	{
		// super( 30 );

		setLayout( null ); // no layout manager please
		setOpaque( false );

		new WhenResized( this )
		{

			@Override
			public void doThis( ComponentEvent e )
			{
				updateAll();
			}

		};

		new WhenMadeVisible( this )
		{/*
			 * (non-Javadoc)
			 *
			 * @see
			 * com.cybernostics.lib.gui.declarative.events.WhenMadeVisible#doThis
			 * (java.awt.AWTEvent)
			 */

			@Override
			public void doThis( AWTEvent e )
			{
				updateAll();
				new RunLater()
				{/*
					 * (non-Javadoc)
					 *
					 * @see
					 * com.cybernostics.lib.gui.declarative.events.RunLater#run(
					 * java.lang.Object[])
					 */

					@Override
					public void run( Object... args )
					{
						firePropertyChange(
							INDEX,
							selectedIndex,
							selectedIndex );
					}

				};

			}

		};
	}

	public void updateAll()
	{
		if (isVisible())
		{
			if (getCellRenderer() != null)
			{
				Dimension absSize = getSize();
				interComponentGap = absSize.height / 10;
				int sqaureItemSize = (int) ( absSize.height - ( interComponentGap * 2 ) );
				getCellRenderer().setItemSize(
					new Dimension( sqaureItemSize, sqaureItemSize ) );

				removeAll();
				setScrollPosition( 0 );
				// selectedIndex = -1;
				// setSelectedIndex( selectedIndex );
				repositionMidPointOffset();
				updateRect();
				addToLeft();
				addToRight();
				updateHighLightRectangle();

			}

		}

	}

	/**
	 * Enumeration indicating left or right scroll direction
	 *
	 * @author jasonw
	 *
	 */
	public enum Sides
	{

		LEFT, RIGHT
	};

	/**
	 * Create a listener to allow people to click off-centre items to select
	 * them
	 */
	MouseListener itemScrollListener = new MouseAdapter()
	{

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked( MouseEvent e )
		{
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseReleased( MouseEvent e )
		{
			int currentSelection = getSelectedIndex();
			JComponent jb = (JComponent) e.getSource();
			int desiredIndex = (Integer) jb.getClientProperty( INDEX );
			if (desiredIndex != currentSelection)
			{
				int scrollDistance = ( currentSelection - desiredIndex )
					* getElementScrollDistance();
				scrollTo( getScrollPosition() + scrollDistance );

			}
		}

	};

	/**
	 * Get the scroll position to center this item
	 *
	 * @param itemIndex
	 * - the item for which we need a position
	 * @return
	 */
	public int getItemScrollPosition( int itemIndex )
	{
		Dimension dChild = cellRenderer.getItemSize();
		return 0 - ( (int) ( itemIndex * dChild.getWidth() ) + ( ( itemIndex > 0 ) ?
																	( ( itemIndex ) * interComponentGap )
			: 0 ) );
	}

	/**
	 * Adds a new component to this scrolling window (usually offscreen) Sets up
	 * client properties used to interact with the scroller.
	 *
	 * @param child
	 * - component to be added
	 * @param index
	 * - the item index from the model to add
	 * @param which
	 */
	private void addComponent( JComponent child, int index, Sides which )
	{
		if (getComponentCount() == 0)
		{
			selectedIndex = 0;
			firePropertyChange(
				"selectedIndex",
				-1,
				0 );
		}

		if (which == Sides.LEFT)
		{
			add(
				child,
				0 );
		}
		else
		{
			add( child );
		}

		Dimension dChild = child.getPreferredSize();

		Point origin = new Point(
				(int) ( index * dChild.getWidth() )
					+ ( ( index > 0 ) ? ( ( index ) * interComponentGap ) : 0 ),
				interComponentGap );

		// tag each component with its index
		child.putClientProperty(
			INDEX,
			index );
		child.putClientProperty(
			"origin",
			origin );

		child.setBounds(
			origin.x + scrollPosition + midPointOffset,
			origin.y,
			dChild.width,
			dChild.height );

		if (centralComponent == null)
		{
			centralComponent = child;
			repositionMidPointOffset();
		}

		child.addMouseListener( itemScrollListener );

		validate();

		updateActiveIndicies();
		// checkComponents();
	}

	/**
	 * Updates the maximum and minimum indicies active in the window. (i.e those
	 * for which a child component currenly exists) For a model containing a
	 * 1000 elements it will only ever have 3 horizontal panel's worth to allow
	 * a full panel scroll left or right.
	 */
	private void updateActiveIndicies()
	{
		int componentCount = getComponentCount();

		if (componentCount == 0)
		{
			minShownIndex = -1;
			maxShownIndex = -1;
			return;
		}
		minShownIndex = (Integer) ( (JComponent) getComponent( 0 ) ).getClientProperty( INDEX );

		if (componentCount > 1)
		{
			JComponent maxChild = (JComponent) getComponent( componentCount - 1 );
			if (maxChild != null)
			{
				Object ind = maxChild.getClientProperty( INDEX );
				if (ind != null)
				{
					maxShownIndex = (Integer) ind;
				}

			}
			else
			{
				maxShownIndex = minShownIndex;
			}

		}

	}

	/**
	 * Gets the distance between 2 elements in the list which comprises the unit
	 * of scrolling distance
	 *
	 * @return the width of a single item plus the gap between them.
	 */
	public int getElementScrollDistance()
	{

		if (cellRenderer == null)
		{
			return 0;
		}
		return (int) ( cellRenderer.getItemSize()
			.getWidth() + interComponentGap );
	}

	/**
	 * Returns the component at the center of this panel.
	 *
	 * @return the component or null if the central point has no component
	 */
	public JComponent findCentralComponent()
	{

		JComponent comp = (JComponent) getComponentAt(
			getWidth() / 2,
			getHeight() / 2 );

		return comp;

	}

	/**
	 * Adjusts the scroll offset to center the central component
	 */
	protected void repositionMidPointOffset()
	{
		if (cellRenderer != null)
		{
			Dimension itemSize = cellRenderer.getItemSize();

			midPointOffset = ( getWidth() / 2 ) - ( itemSize.width / 2 );

			// updateChildren();
			// addToLeft();
			// addToRight();
		}
	}

	/**
	 * These track the range of "active" elements currently rendered on or off
	 * screen in the wings
	 */
	private int minShownIndex = -1;

	private int maxShownIndex = -1;
	// This rectangle should include all active elements
	// ie all visible elements plus one each side

	private Rectangle myBounds = new Rectangle( 0, 0, getWidth(), getHeight() );

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.JComponent#addNotify()
	 */
	@Override
	public void addNotify()
	{
		super.addNotify();
		updateChildren();
		repositionMidPointOffset();
		addMouseWheelListener( new MouseWheelListener()
		{/*
			 * (non-Javadoc)
			 *
			 * @see
			 * java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.
			 * MouseWheelEvent)
			 */

			@Override
			public void mouseWheelMoved( MouseWheelEvent e )
			{
				if (e.getUnitsToScroll() < 0)
				{
					scrollLeft();
				}
				else
				{
					scrollRight();
				}

			}

		} );

	}

	/**
	 * Gets the current scroll position. Position 0 has item 0 in the List model
	 * centered on this panel.
	 *
	 * @return the current scrollPosition
	 */
	public int getScrollPosition()
	{
		return scrollPosition;
	}

	/**
	 * Sets the current scroll position to position child components. Scroll
	 * position 0 is center of the panel.
	 *
	 * @param scrollPosition
	 * the scrollPosition to set
	 */
	public void setScrollPosition( int scrollPosition )
	{
		// System.out.printf("%d\n",scrollPosition);
		this.scrollPosition = scrollPosition;
		updateChildren();

	}

	/**
	 * This rectangle includes the current location + a shoulder region which
	 * contains enough components to scroll into view. For a model containing a
	 * 1000 elements it will only ever have 3 horizontal panel's worth to allow
	 * a full panel scroll left or right.
	 */
	public void updateRect()
	{
		int width = getWidth();
		// leave width pixels to the left and right of the rect
		myBounds = new Rectangle( (int) ( 0 - ( width * 1.5 ) ),
			0,
			width * 4,
			getHeight() );

		repaint();

	}

	/**
	 * Updates the location of all child components based on:
	 * <ul>
	 * <li>their "origin" client property</li>
	 * <li>the current scroll offset</li>
	 * <li>the current mid-point offset</li>
	 * </ul>
	 *
	 * such that a component at scroll position 0 will always be horizontally
	 * centered on the panel
	 */
	public void updateChildren()
	{
		if (!isVisible())
		{
			return;
		}

		if (myBounds.width == 0)
		{
			return;
		}

		// keep note of the highest and lowest displayed index
		setIgnoreRepaint( true );

		for (int index = getComponentCount() - 1; index >= 0; --index)
		{
			JComponent eachComponent = (JComponent) getComponent( index );
			Point origin = (Point) eachComponent.getClientProperty( "origin" );
			eachComponent.setLocation(
				origin.x + scrollPosition + midPointOffset,
				origin.y );

		}

		setIgnoreRepaint( false );
		invalidate();

	}

	/**
	 * Adds new items to the right of the right-most displayed item when there
	 * is space in the window of scrollable items.
	 */
	public void addToRight()
	{
		if (model == null)
		{
			return;
		}

		int modelSize = model.getSize();
		// RIGHT
		// search for elements not shown which need to be
		for (int rightIndex = maxShownIndex + 1; rightIndex < modelSize; ++rightIndex)
		{
			if (( myBounds != null )
				&& myBounds.intersects( getItemRect( rightIndex ) ))
			{
				addComponent(
					cellRenderer.getRenderer(
						this,
						rightIndex,
						model.getElementAt( rightIndex ) ),
								rightIndex,
					Sides.RIGHT );
				continue;
			}
			break; // if its not visible then none of the ones to the left
			// will be
		}
		updateActiveIndicies();
		validate();
		repaint( getBounds() );

	}

	/**
	 * When scrolling items offscreen to the right then prune items which can't
	 * be immediately scrolled into view.
	 */
	public void removeFromRight()
	{
		if (getComponentCount() == 0)
		{
			return;
		}

		JComponent eachComponent = (JComponent) getComponent( getComponentCount() - 1 );

		while (!myBounds.intersects( eachComponent.getBounds() ))
		{
			remove( getComponentCount() - 1 );
			repaint( eachComponent.getBounds() );
			eachComponent = (JComponent) getComponent( getComponentCount() - 1 );
		}

		updateActiveIndicies();
		invalidate();

	}

	/**
	 * Add items to the left of the row
	 */
	public void addToLeft()
	{
		// search for elements not shown which need to be
		for (int leftIndex = minShownIndex - 1; leftIndex >= 0; --leftIndex)
		{
			if (myBounds.intersects( getItemRect( leftIndex ) ))
			{
				addComponent(
					cellRenderer.getRenderer(
						this,
						leftIndex,
						model.getElementAt( leftIndex ) ),
					leftIndex,
								Sides.LEFT );
				continue;
			}
			break; // if its not visible then none of the ones to the left will
			// be
		}
		updateActiveIndicies();
		validate();

	}

	/**
	 * When scrolling items offscreen to the left then prune items which can't
	 * be immediately scrolled into view.
	 */
	public void removeFromLeft()
	{

		if (getComponentCount() == 0)
		{
			return;
		}
		JComponent eachComponent = (JComponent) getComponent( 0 );

		while (!myBounds.intersects( eachComponent.getBounds() ))
		{
			remove( 0 );
			repaint( eachComponent.getBounds() );
			eachComponent = (JComponent) getComponent( 0 );
		}
		updateActiveIndicies();
		validate();

	}

	Rectangle centerHighlight = new Rectangle();

	/**
	 * Generate the rectangle which can be used to highlight the central item
	 */
	private void updateHighLightRectangle()
	{
		if (cellRenderer == null)
		{
			return;
		}
		int halfGap = interComponentGap / 2;
		Dimension d = cellRenderer.getItemSize();
		centerHighlight.x = ( ( getWidth() - d.width ) / 2 ) - halfGap;
		centerHighlight.y = halfGap;
		centerHighlight.width = d.width + interComponentGap;
		centerHighlight.height = d.height + interComponentGap;
	}

	/**
	 * Returns the rectangle for the bounds of the central component
	 *
	 * @param r
	 * - the bounds of the "selected" element
	 */
	public void getCenterBounds( Rectangle r )
	{
		Component c = findCentralComponent();
		if (c != null)
		{
			c.getBounds( r );
		}

	}

	/**
	 * Returns the index client property for the component at the specified
	 * location
	 *
	 * @param source
	 * @return
	 */
	public int getComponentIndex( JComponent source )
	{
		return (Integer) source.getClientProperty( INDEX );
	}

	/**
	 * @param interComponentGap
	 * the interComponentGap to set
	 */
	public void setInterComponentGap( int interComponentGap )
	{
		this.interComponentGap = interComponentGap;
		repositionMidPointOffset();
		updateChildren();
	}

	/**
	 * @return the interComponentGap
	 */
	public int getInterComponentGap()
	{
		return interComponentGap;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.JComponent#getMinimumSize()
	 */
	@Override
	public Dimension getMinimumSize()
	{
		return new Dimension( 0, 0 );
	}

	@Override
	public Dimension getPreferredSize()
	{
		Dimension d =
					new Dimension( (int) ( getCellRenderer().getItemSize()
						.getWidth() + ( 2 * interComponentGap ) ),
									(int) ( getCellRenderer().getItemSize()
										.getHeight() + ( 2 * interComponentGap ) ) );
		return d;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.JComponent#getMaximumSize()
	 */
	@Override
	public Dimension getMaximumSize()
	{
		return new ScreenRelativeDimension( 1.0f, 1.0f );
	}

	/**
	 * @param model
	 * the model to set
	 */
	public void setModel( ListModel model )
	{
		this.model = model;
		updateAll();

		model.addListDataListener( new ListDataListener()
		{

			@Override
			public void contentsChanged( ListDataEvent e )
			{
				addToRight();
			}

			@Override
			public void intervalAdded( ListDataEvent e )
			{
				addToRight();
			}

			@Override
			public void intervalRemoved( ListDataEvent e )
			{
				maxShownIndex = -1;
				minShownIndex = -1;
				selectedIndex = -1;
				removeAll();
				addToRight();
				scrollPosition = 0;
				validate();
				repaint();
			}

		} );
		if (model.getSize() > 0)
		{
			selectedIndex = 0;
			firePropertyChange(
				"currentIndex",
				-1,
				selectedIndex );
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint( Graphics g )
	{
		paintBorder( g );
		StateSaver g2 = StateSaver.wrap( g );
		try
		{
			Border b = getBorder();
			if (b instanceof ShapedBorder)
			{
				Shape clip = ( (ShapedBorder) b ).getBorderClipShape( this );
				g2.setClip( clip );
			}

			g2.setColor( Color.blue );
			g2.draw( centerHighlight );
			paintComponent( g2 );
			paintChildren( g2 );
		}
		finally
		{
			g2.restore();
		}
		paintBorder( g );

	}

	/**
	 * @return the model
	 */
	public ListModel getModel()
	{
		return model;
	}

	/**
	 * @param cellRenderer
	 * the cellRenderer to set
	 */
	public void setCellRenderer( GalleryCellRenderer cellRenderer )
	{
		if (this.cellRenderer != null)
		{
			this.cellRenderer.removePropertyChangeListener( this );
		}
		this.cellRenderer = cellRenderer;
		this.cellRenderer.addPropertyChangeListener(
			DefaultGalleryCellRenderer.ITEM_SIZE,
			this );
		updateDimensions();
	}

	public void updateDimensions()
	{
		invalidate();
		updateHighLightRectangle();
		updateRect();

	}

	/**
	 * @return the cellRenderer
	 */
	public GalleryCellRenderer getCellRenderer()
	{
		return cellRenderer;
	}

	/**
	 * Gets the rectangle containing the item adjusted for scroll position
	 *
	 * @param position
	 * index of the item for which to get the rect
	 * @return the items rectangle or null if there is no cell renderer
	 */
	public Rectangle getItemRect( int position )
	{
		if (cellRenderer == null)
		{
			return null;
		}
		Dimension itemSize = cellRenderer.getItemSize();

		int offset = scrollPosition + midPointOffset;

		Rectangle rect = new Rectangle( (int) ( position * itemSize.width )
			+
					( ( position > 0 ) ? ( ( position ) * interComponentGap )
						: 0 ) + offset, interComponentGap,
										itemSize.width,
										itemSize.height );
		return rect;
	}

	@Override
	public void propertyChange( PropertyChangeEvent evt )
	{
		removeAll();
		updateActiveIndicies();
		addToRight();
		updateDimensions();

	}

}
