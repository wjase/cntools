package com.cybernostics.lib.gui.gallery;

import com.cybernostics.lib.concurrent.GUIEventThread;
import com.cybernostics.lib.gui.ButtonFactory;
import com.cybernostics.lib.gui.IconFactory.StdButtonType;
import com.cybernostics.lib.gui.declarative.events.WhenClicked;
import com.cybernostics.lib.gui.declarative.events.WhenMadeVisible;
import com.cybernostics.lib.gui.grouplayoutplus.GroupLayoutPlus;
import com.cybernostics.lib.gui.grouplayoutplus.PARALLEL;
import com.cybernostics.lib.gui.grouplayoutplus.SEQUENTIAL;
import com.cybernostics.lib.gui.grouplayoutplus.SIZING;
import com.cybernostics.lib.gui.shapeeffects.ShapedPanel;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Implements a lazy list component which is like those used in photo galleries
 * Components are created on the fly and only enough to show in the visible
 * areas are retained
 *
 * To add your own kind of gallery items, add your own list model and
 * GalleryCell renderer
 *
 * The renderer implements component recycling to save continually creating new
 * components.
 *
 * Unlike the Swing JList, the renderer is not used to "stamp" each item so you
 * can add tool tips and handlers which will be valid as long as the component
 * is currently visible in the strip.
 *
 * Handlers should be setup by the renderer.
 *
 * Depends on cybernostics.animator package.
 *
 * @author jasonw
 *
 */
public class ComponentGallery extends ShapedPanel
{

	/**
	 *
	 */
	public static final String CURRENT_INDEX = "currentIndex";

	/**
	 *
	 */
	private static final long serialVersionUID = 7938413183199357855L;

	ListSelectionModel selectionModel = new ComponentGallerySelectionModel( this );

	public ListSelectionModel getSelectionModel()
	{
		return selectionModel;
	}

	/**
	 * @param dgcr
	 */
	public void setCellRenderer( GalleryCellRenderer dgcr )
	{
		scroller.setCellRenderer( dgcr );

	}

	private JButton scrollLeftButton = ButtonFactory.getStdButton(
		StdButtonType.PREV,
		null );

	private JButton scrollRightButton = ButtonFactory.getStdButton(
		StdButtonType.NEXT,
		null );

	private ComponentGalleryScrollPane scroller = null;

	public static ComponentGallery create()
	{
		return create( new DefaultListModel() );
	}

	public static ComponentGallery create( ListModel lm )
	{
		ComponentGallery cg = new ComponentGallery();
		cg.initComponent();
		cg.setModel( lm );
		return cg;
	}

	protected ComponentGallery()
	{
		scroller = new ComponentGalleryScrollPane();

		setOpaque( true );

		setBackground( Color.black );

		scroller.addPropertyChangeListener(
			CURRENT_INDEX,
			new PropertyChangeListener()
		{

			@Override
			public void propertyChange( PropertyChangeEvent evt )
			{
				ComponentGallery.this.firePropertyChange(
					evt.getPropertyName(),
					evt.getOldValue(),
					evt.getNewValue() );
			}

		} );

		//scroller.setBackground( Color.black );
		scroller.setOpaque( false );

		new WhenMadeVisible( this )
		{

			@Override
			public void doThis( AWTEvent e )
			{
				scroller.setButtons(
					scrollLeftButton,
					scrollRightButton );
				validate();

			}

		};
		new WhenClicked( scrollLeftButton )
		{

			@Override
			public void doThis( ActionEvent e )
			{
				scroller.scrollLeft();
			}

		};

		new WhenClicked( scrollRightButton )
		{

			@Override
			public void doThis( ActionEvent e )
			{
				scroller.scrollRight();
			}

		};

	}

	public void setInnerBackground( Color c )
	{
		if (c == null)
		{
			this.scroller.setOpaque( false );
		}
		else
		{
			this.scroller.setOpaque( true );
			this.scroller.setBackground( c );
		}
	}

	public void add( Object o )
	{

		( (DefaultListModel) scroller.getModel() ).addElement( o );
	}

	public void refresh()
	{
		scroller.updateAll();
	}

	/**
	 * @return the cellRenderer
	 */
	public GalleryCellRenderer getCellRenderer()
	{
		return scroller.getCellRenderer();
	}

	/**
	 * @return the currentIndex
	 */
	public int getCurrentIndex()
	{
		return scroller.getSelectedIndex();
	}

	/**
	 * @return the model
	 */
	public ListModel getModel()
	{
		return scroller.getModel();
	}

	/**
	 * @return the scrollLeftButton
	 */
	public JButton getScrollLeftButton()
	{
		return scrollLeftButton;
	}

	/**
	 * @return the scrollrightButton
	 */
	public JButton getScrollRightButton()
	{
		return scrollRightButton;
	}

	public Object getSelectedItem()
	{
		try
		{
			return scroller.getModel()
				.getElementAt(
					scroller.getSelectedIndex() );
		}
		catch (Exception e)
		{
			return null;
		}
	}

	private void initComponent()
	{
		setCellRenderer( new DefaultGalleryCellRenderer() );

		GroupLayoutPlus glp = new GroupLayoutPlus( this );

		glp.setHorizontalGroup( SEQUENTIAL.group(
			getScrollLeftButton(),
			SIZING.fill( scroller ),
													getScrollRightButton() ) );
		glp.setVerticalGroup( PARALLEL.group(
			Alignment.CENTER,
			getScrollLeftButton(),
			SIZING.fill( scroller ),
												getScrollRightButton() ) );

		//scroller.setBorder( BorderFactory.createLineBorder( Color.red ) );
		validate();

	}

	/**
	 * @param model the model to set
	 */
	public void setModel( final ListModel model )
	{
		scroller.setModel( model );
		//scroller.repaint( scroller.getBounds() );
		//scroller.invalidate();

		if (model.getSize() == 0)
		{
			model.addListDataListener( new ListDataListener()
			{

				@Override
				public void intervalAdded( ListDataEvent e )
				{
					model.removeListDataListener( this );
					setSelectedIndex( 0 );
					scroller.revalidate();
				}

				@Override
				public void intervalRemoved( ListDataEvent e )
				{
				}

				@Override
				public void contentsChanged( ListDataEvent e )
				{
				}

			} );
		}
		else
		{
			setSelectedIndex( 0 );
			scroller.revalidate();

		}

		GUIEventThread.run( new Runnable()
		{

			@Override
			public void run()
			{
				if (scroller.getModel()
					.getSize() > 0)
				{

					setSelectedIndex( 0 );
				}

			}

		} );

	}

	/**
	 * @param scrollLeftButton the scrollLeftButton to set
	 */
	public void setScrollLeftButton( JButton scrollLeftButton )
	{
		this.scrollLeftButton = scrollLeftButton;

		new WhenClicked( scrollLeftButton )
		{

			@Override
			public void doThis( ActionEvent e )
			{
				scroller.scrollLeft();
			}

		};
	}

	/**
	 * @param scrollRightButton the scrollrightButton to set
	 */
	public void setScrollRightButton( JButton scrollRightButton )
	{
		this.scrollRightButton = scrollRightButton;
		new WhenClicked( scrollRightButton )
		{

			@Override
			public void doThis( ActionEvent e )
			{
				scroller.scrollRight();
			}

		};
	}

	/**
	 * @param i
	 */
	public void setSelectedIndex( int i )
	{
		scroller.setSelectedIndex( i );

	}

	/**
	 * @return
	 */
	public int getSelectedIndex()
	{
		return scroller.getSelectedIndex();
	}

}
