package com.cybernostics.lib.gui.control;

import com.cybernostics.lib.gui.ButtonFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.border.Border;

import com.cybernostics.lib.gui.declarative.events.SupportsPropertyChanges;
import com.cybernostics.lib.gui.border.RendersBackgroundBorder;
import com.cybernostics.lib.gui.border.RoundedBorder;
import com.cybernostics.lib.gui.border.TubeBorder;
import com.cybernostics.lib.gui.IconFactory.StdButtonType;
import com.cybernostics.lib.gui.declarative.events.WhenClicked;
import com.cybernostics.lib.gui.grouplayoutplus.GroupLayoutPlus;
import com.cybernostics.lib.gui.grouplayoutplus.PARALLEL;
import com.cybernostics.lib.gui.grouplayoutplus.SEQUENTIAL;
import com.cybernostics.lib.gui.grouplayoutplus.SIZING;

/**
 * Implements a large formatted sing-item list for small lists with next/last
 * buttons to navigate
 * 
 * @author jasonw
 * 
 */
@SuppressWarnings("serial")
public class NextLastListBox extends JPanel implements SupportsPropertyChanges
{
	private JButton nextButton = ButtonFactory.getStdButton( StdButtonType.NEXT );
	private JButton prevButton = ButtonFactory.getStdButton( StdButtonType.PREV );
	private JPanel centralArea = new JPanel()
	{
		public void paint( Graphics g )
		{
			( (RendersBackgroundBorder) getBorder() ).paintBackground(
				this,
				g,
				0,
				0,
				getWidth(),
				getHeight() );
			super.paint( g );
		}
	};

	private ListCellRenderer renderer = new DefaultNextLastRenderer();

	public void setFont( Font f )
	{
		if (renderer instanceof JComponent)
		{
			( (JComponent) renderer ).setFont( f );
		}
		super.setFont( f );
	}

	public NextLastListBox()
	{
		new WhenClicked( nextButton )
		{
			@Override
			public void doThis( ActionEvent e )
			{
				setSelectedIndex( selectedItemIndex + 1 );
				updateButtons();
			}
		};

		new WhenClicked( prevButton )
		{
			@Override
			public void doThis( ActionEvent e )
			{
				setSelectedIndex( selectedItemIndex - 1 );
				updateButtons();
			}
		};

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#addNotify()
	 */
	@Override
	public void addNotify()
	{
		super.addNotify();
		initUI();
	}

	//	

	private void updateButtons()
	{
		nextButton.setEnabled( ( model != null )
			&& selectedItemIndex < ( model.getSize() - 1 ) );
		prevButton.setEnabled( ( model != null ) && selectedItemIndex > ( 0 ) );
	}

	/**
	 * 
	 */
	protected void initUI()
	{

		updateButtons();

		setBorder( new TubeBorder( 40 ) );
		setOpaque( false );
		// the rendered component can fill this area
		centralArea.setLayout( new GridLayout() );
		centralArea.setBorder( new RoundedBorder( 30 ) );
		centralArea.setOpaque( false );
		centralArea.setBackground( Color.white );
		JPanel filler = new JPanel();
		filler.setMaximumSize( new Dimension( Integer.MAX_VALUE, 2 ) );
		GroupLayoutPlus glp = new GroupLayoutPlus( this );
		glp.setHorizontalGroup( PARALLEL.group(
			SEQUENTIAL.group(
				prevButton,
				SIZING.fill( filler ),
				nextButton ),
			centralArea ) );
		glp.setVerticalGroup( SEQUENTIAL.group(
			PARALLEL.group(
				prevButton,
				filler,
				nextButton ),
			SIZING
				.fill( centralArea ) ) );

		validate();

	}

	/**
	 * @param i
	 */
	public void setSelectedIndex( int iNextIndex )
	{
		if (iNextIndex >= 0 && iNextIndex < model.getSize())
		{
			int oldValue = selectedItemIndex;
			selectedItemIndex = iNextIndex;

			JComponent rendered = (JComponent) renderer.getListCellRendererComponent(
				null,
				model
					.getElementAt( selectedItemIndex ),
				selectedItemIndex,
				true,
				true );

			rendered.setFont( getFont() );
			if (( centralArea.getComponentCount() == 0 )
				|| centralArea.getComponent( 0 ) != rendered)
			{
				centralArea.removeAll();
				centralArea.add( rendered );
				centralArea.validate();
			}
			updateButtons();

			firePropertyChange(
				"selectedIndex",
				oldValue,
				selectedItemIndex );
		}

	}

	/**
	 * eclipse generated id
	 */
	private static final long serialVersionUID = 5806485812438549391L;

	/**
	 * The index of the currently selected item
	 */
	int selectedItemIndex = -1;

	/**
	 * Model to hold the list of items
	 */
	private ListModel model = null;

	/**
	 * @return the current model or null if none
	 */
	public ListModel getModel()
	{
		return model;
	}

	/**
	 * Clears the model to null
	 */
	public void clear()
	{
		selectedItemIndex = -1;
		model = null;
		centralArea.removeAll();
		repaint();
	}

	/**
	 * Sets the current model for the list
	 */
	public void setModel( ListModel model )
	{
		this.model = model;
		setSelectedIndex( 0 );
		repaint();
	}

	/**
	 * Gets the selected item
	 * 
	 * @return the selected item or null if none selected
	 */
	public Object getSelectedItem()
	{
		if (selectedItemIndex != -1 && model != null)
		{
			return model.getElementAt( selectedItemIndex );
		}
		return null;
	}

	public void addItems( Object... itemsToAdd )
	{
		if (model == null)
		{
			model = new DefaultListModel();
		}

		if (model instanceof DefaultListModel)
		{
			DefaultListModel dlm = (DefaultListModel) model;
			for (Object item : itemsToAdd)
			{
				dlm.addElement( item );
			}

		}
		if (selectedItemIndex == -1)
		{
			setSelectedIndex( 0 );
		}
		repaint();
	}

	/**
	 * @param renderer
	 *            the renderer to set
	 */
	public void setRenderer( ListCellRenderer renderer )
	{
		this.renderer = renderer;
	}

	// @Override
	// public void paint( Graphics g )
	// {
	// paintBorder( g );
	// super.paint( g );
	// }

	public void paint( Graphics g )
	{
		Border border = getBorder();
		if (border instanceof RendersBackgroundBorder)
		{
			( (RendersBackgroundBorder) border ).paintBackground(
				this,
				g,
				0,
				0,
				getWidth(),
				getHeight() );
		}
		super.paint( g );
	}

	/**
	 * @return
	 */
	public ListCellRenderer getRenderer()
	{
		return renderer;
	}

	/**
	 * @return
	 */
	public int getSelectedIndex()
	{
		return selectedItemIndex;
	}
}
