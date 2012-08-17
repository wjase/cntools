package com.cybernostics.lib.gui.panel;

import com.cybernostics.lib.concurrent.CanCheckComplete;
import com.cybernostics.lib.gui.lookandfeel.NimbusLook;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.cybernostics.lib.gui.declarative.events.SwingTaskListener;
import com.cybernostics.lib.gui.ScreenRelativeDimension;
import com.cybernostics.lib.gui.layout.FlowScrollLayout;
import com.cybernostics.lib.media.AsyncListModel;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.media.icon.AttributedScalableIcon;
import com.cybernostics.lib.media.image.ImageLoader;
import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JToggleButton;

/**
 * 
 * @author jasonw
 */
public class ImagePane extends JPanel
{

	/**
	 * 
	 */
	public static final String ITEMS_LOADED = "itemsloaded";
	/**
	 * 
	 */
	public static final String SELECTION_CHANGED = "selectionChanged";

	void setRenderer( ImagePanelRenderer imagePaneConceptRenderer )
	{
		this.renderer = imagePaneConceptRenderer;
	}

	/**
	 * 
	 */
	public enum SelectionType
	{

		/**
		 * 
		 */
		SINGLE,
		/**
		 * 
		 */
		MULTIPLE
	};

	private SelectionType selectionMode = SelectionType.SINGLE;
	/**
	 * 
	 */
	private static final long serialVersionUID = -780255716885850775L;

	/**
	 * 
	 * @param args
	 */
	public static void main( String[] args )
	{
		try
		{

			final JFrame jf = new JFrame( "image pane" );
			final ImagePane ip = new ImagePane();

			ip.addPropertyChangeListener(
				SELECTION_CHANGED,
				new PropertyChangeListener()
			{

				@Override
				public void propertyChange( PropertyChangeEvent evt )
				{
					System.out.printf(
						"%d Items Selected\n",
						ip.getSelectedItems()
							.size() );
					for (Object item : ip.getSelectedItems())
					{
						URL itemU = (URL) item;
						System.out.printf(
							"%s\n",
							itemU.toString() );
					}

				}
			} );
			//        ip.setButtonCallback( new ActionListener()
			//        {
			//
			//            @Override
			//            public void actionPerformed( ActionEvent e )
			//            {
			//                System.out.printf( "%d Items Selected\n", ip.getSelectedItems().size() );
			//                for ( Object item : ip.getSelectedItems() )
			//                {
			//                    System.out.printf( "%s\n", item );
			//                }
			//            }
			//        } );

			//		ip.setSelectionMode( SelectionType.MULTIPLE );
			ListModel lm = ImageLoader.getThumbIcons(
				new URL( "file:///C:/data/images/Photographs/2008/2008_01_17" ),
				new SwingTaskListener()
				{

					@Override
					public void taskDone( Object task )
					{
						try
						{
							ip.setSelectedItems( new URL(
								"file:///C:/data/images/Photographs/2008/2008_01_17/IMG_1206.jpg" ) );
						}
						catch (MalformedURLException ex)
						{
							Logger.getLogger(
								ImagePane.class.getName() )
								.log(
									Level.SEVERE,
									null,
									ex );
						}

					}
				},
				null );

			ip.setModel( lm );
			jf.getContentPane()
				.setLayout(
					new GridLayout() );
			jf.getContentPane()
				.add(
					ip );
			jf.getContentPane()
				.setBackground(
					Color.red );
			jf.setSize( new ScreenRelativeDimension( 0.2f, 0.5f ) );
			jf.setVisible( true );
			ip.setOpaque( false );
			jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		}
		catch (MalformedURLException ex)
		{
			Logger.getLogger(
				ImagePane.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
	}

	private ActionListener clientButtonAction;
	private JScrollPane jsp = new JScrollPane();
	private JPanel internal = new JPanel();
	private CanCheckComplete listModel = null;

	/**
	 * 
	 */
	public ImagePane()
	{
		init();
	}

	/**
	 * 
	 * @param listitems
	 * @param al
	 */
	public ImagePane( ListModel listitems, ActionListener al )
	{

		this();
		clientButtonAction = al;

		setModel( listitems );

		setOpaque( false );

		internal.setOpaque( false );
		internal.setBorder( BorderFactory.createEmptyBorder(
			20,
			20,
			20,
			20 ) );
	}

	private Set< Object > selectionSet = new HashSet< Object >();

	/**
	 * 
	 * @return
	 */
	public Set< Object > getSelectedItems()
	{
		return selectionSet;
	}

	private void init()
	{
		jsp.setViewportView( internal );

		setLayout( new GridLayout( 1, 1 ) );

		add( jsp );
		jsp.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

		FlowScrollLayout fsl = new FlowScrollLayout( jsp );
		internal.setLayout( fsl );

		jsp.setOpaque( false );
		jsp.getViewport()
			.setOpaque(
				false );
		internal.setOpaque( false );

		validate();
	}

	boolean updatingSelection = false;

	/**
	 * 
	 * @param values
	 */
	public void setSelectedItems( URL... values )
	{
		int matches = 0;
		for (URL eachValue : values)
		{
			if (selectionSet.contains( eachValue ))
			{
				++matches;
			}
		}
		if (( matches > 0 ) && matches == selectionSet.size())
		{
			// current selection matches values - nothing to do
			return;
		}
		updatingSelection = true;
		selectionSet.clear();
		selectionSet.addAll( Arrays.asList( values ) );
		firePropertyChange(
			SELECTION_CHANGED,
			(Boolean) false,
			(Boolean) true );
		updateButtonStates();
		updatingSelection = false;
	}

	private void addToSelected( Object toAdd )
	{
		selectionSet.add( toAdd );
		firePropertyChange(
			SELECTION_CHANGED,
			(Boolean) false,
			(Boolean) true );
	}

	private void removeFromSelected( Object toAdd )
	{
		selectionSet.remove( toAdd );
		firePropertyChange(
			SELECTION_CHANGED,
			(Boolean) false,
			(Boolean) true );
	}

	public void updateButtonStates()
	{
		Set< Object > valueSet = new HashSet< Object >();
		valueSet.addAll( selectionSet );

		boolean scrolledToSelected = false;

		for (Component butComp : IterableContainer.get( internal ))
		{
			AbstractButton current = (AbstractButton) butComp;
			AttributedScalableIcon asi = (AttributedScalableIcon) current.getIcon();
			URL itemURL = asi.getURL();
			boolean selected = valueSet.contains( itemURL );

			if (selected == true && scrolledToSelected == false)
			{
				if (scrolledToSelected == true)
				{
					// scroll the first one we find into view
					internal.scrollRectToVisible( current.getBounds() );
					scrolledToSelected = false;
				}
				current.setSelected( selected );
				//                selectionSet.add(  current.getClientProperty( ImagePanelRenderer.OBJ_PROPERTY ) );
				valueSet.remove( itemURL );
				// no need to search for this one any more
				if (valueSet.isEmpty())
				{
					break; // all done no more to select
				}

			}
		}

	}

	/**
	 * 
	 * @param listener
	 */
	public void setButtonCallback( ActionListener listener )
	{
		clientButtonAction = listener;
	}

	private ButtonGroup buttonManager = new ButtonGroup();

	/**
	 * 
	 * @param lm
	 */
	@SuppressWarnings("unchecked")
	public void setModel( ListModel lm )
	{
		internal.removeAll();
		selectionSet.clear();
		repaint();
		if (listModel != null)
		{
			if (listModel instanceof AsyncListModel< ? >)
			{
				( (AsyncListModel< ? >) listModel ).cancelLoad();
			}
			listModel = null;

		}
		// remove the items from the ButtonGroup - there is no removeAll !
		for (Component butComp : IterableContainer.get( internal ))
		{
			buttonManager.remove( (AbstractButton) butComp );
		}

		if (lm == null)
		{
			return;
		}

		if (lm instanceof CanCheckComplete)
		{
			listModel = (CanCheckComplete) lm;
		}

		internal.removeAll();

		if (lm.getSize() > 0)
		{
			updateFromList( lm );
		}

		lm.addListDataListener( new ListDataListener()
		{

			@Override
			public void contentsChanged( ListDataEvent e )
			{
				if (selectionSet.isEmpty() && listModel != null
					&& listModel.isComplete())
				{
					firePropertyChange(
						ITEMS_LOADED,
						(Boolean) false,
						(Boolean) true );
				}
			}

			@Override
			public void intervalAdded( ListDataEvent e )
			{
				updateFromList(
					e.getIndex0(),
					e.getIndex1(),
					(ListModel) e.getSource() );
			}

			@Override
			public void intervalRemoved( ListDataEvent e )
			{
			}
		} );

	}

	protected void updateFromList( ListModel lm )
	{
		updateFromList(
			0,
			lm.getSize() - 1,
			lm );

	}

	protected void updateFromList( int startIndex, int endIndex, ListModel lm )
	{
		ArrayList< AbstractButton > buttonList = new ArrayList< AbstractButton >();

		for (int index = startIndex; index <= endIndex; ++index)
		{
			Object item = lm.getElementAt( index );

			AbstractButton btnIcon = renderer.getImagePaneButton( item );

			if (clientButtonAction != null)
			{
				btnIcon.addActionListener( clientButtonAction );
			}

			buttonList.add( btnIcon );

			// use a button group to set mutual exclusion
			if (selectionMode == SelectionType.SINGLE)
			{
				buttonManager.add( btnIcon );
			}
			internal.validate();
			internal.repaint();
			repaint();
			jsp.getVerticalScrollBar()
				.revalidate();
		}

		if (selectionSet.isEmpty() && listModel != null
			&& listModel.isComplete())
		{
			firePropertyChange(
				ITEMS_LOADED,
				(Boolean) false,
				(Boolean) true );
		}

		addButtons( buttonList );

	}

	ImagePanelRenderer renderer = new DefaultImagePanelRenderer();

	/**
	 * @param selectionMode
	 *            the selectionMode to set
	 */
	public void setSelectionMode( SelectionType selectionMode )
	{
		this.selectionMode = selectionMode;
	}

	ItemListener selectWatcher = new ItemListener()
	{

		@Override
		public void itemStateChanged( ItemEvent e )
		{
			if (updatingSelection)
			{
				return;
			}
			JToggleButton.ToggleButtonModel bm = (JToggleButton.ToggleButtonModel) e.getSource();

			AbstractButton ab = null;
			for (Component butComp : IterableContainer.get( internal ))
			{
				AbstractButton but = (AbstractButton) butComp;
				if (but.getModel() == bm)
				{
					ab = but;
					break;
				}

			}

			if (ab != null)
			{
				AttributedScalableIcon asi = (AttributedScalableIcon) ab.getIcon();
				URL changed = asi.getURL();
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					addToSelected( changed );
				}
				else
				{
					removeFromSelected( changed );
				}

			}

		}
	};

	public void addButtons( List< AbstractButton > buttons )
	{
		jsp.setOpaque( false );
		jsp.getViewport()
			.setOpaque(
				false );
		internal.setOpaque( false );

		for (AbstractButton eachButton : buttons)
		{
			internal.add( eachButton );
			eachButton.getModel()
				.addItemListener(
					selectWatcher );
		}
	}

	/**
	 * @return the selectionMode
	 */
	public SelectionType getSelectionMode()
	{
		return selectionMode;
	}

	/**
	 * 
	 */
	public void selectFirstItem()
	{
		if (internal.getComponentCount() == 0)
		{
			return;
		}
		AbstractButton ab = (AbstractButton) internal.getComponent( 0 );
		AttributedScalableIcon asi = (AttributedScalableIcon) ab.getIcon();
		setSelectedItems( asi.getURL() );

	}
}