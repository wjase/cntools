package com.cybernostics.lib.gui.gallery;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.gui.declarative.events.SupportsPropertyChanges;
import com.cybernostics.lib.gui.border.GradientDropShadowBorder;
import com.cybernostics.lib.gui.ScreenRelativeDimension;
import com.cybernostics.lib.media.icon.ScalableIcon;
import com.cybernostics.lib.media.icon.ScalableImageIcon;

public class DefaultGalleryCellRenderer
	implements
	GalleryCellRenderer,
	SupportsPropertyChanges
{

	boolean scaleItem = true;

	public boolean isScaleItem()
	{
		return scaleItem;
	}

	public void setScaleItem( boolean scaleItem )
	{
		this.scaleItem = scaleItem;
	}

	@Override
	public Dimension getItemSize()
	{
		return itemSize;
	}

	public static final String ITEM_SIZE = "itemSize";

	@Override
	public void setItemSize( Dimension itemSize )
	{
		if (itemSize.equals( this.itemSize ))
		{
			return; // do nothing if no change
		}
		Dimension dOld = this.itemSize;
		this.itemSize = itemSize;
		changeSupport.firePropertyChange(
			ITEM_SIZE,
			dOld,
			itemSize );
	}

	private ArrayList< JComponent > unusedLabels = new ArrayList< JComponent >();
	private Border componentBorder = new GradientDropShadowBorder();
	protected Dimension itemSize = new ScreenRelativeDimension( 0.1f, 0.1f );
	Dimension small = new Dimension( 80, 80 );
	Dimension large = new Dimension( 3000, 3000 );
	/**
	 * 
	 */
	private static final long serialVersionUID = 3818737960957490464L;

	@Override
	public void finishedWith( JComponent comp )
	{
		if (comp instanceof JLabel)
		{
			JLabel jl = (JLabel) comp;
			jl.setText( "" );
			jl.setIcon( null );
		}
		if (!( comp instanceof javax.swing.Box.Filler ))
		{
			unusedLabels.add( comp );
		}

	}

	protected JComponent getNewComponentInstance()
	{
		JLabel newLabel = new JLabel()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			/* (non-Javadoc)
			 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
			 */
			@Override
			protected void paintComponent( Graphics g )
			{
				Insets i = getInsets();

				Rectangle rect = new Rectangle( i.left, i.top, getWidth()
					- i.left - i.right, getHeight() - i.top
					- i.bottom );
				Area clip = new Area( g.getClip() );
				clip.intersect( new Area( rect ) );
				Graphics2D g2 = (Graphics2D) g;
				g2.setClip( clip );
				super.paintComponent( g );
			}
		};
		newLabel.setPreferredSize( itemSize );
		newLabel.setMinimumSize( scaleItem ? small : itemSize );
		newLabel.setMaximumSize( scaleItem ? large : itemSize );
		newLabel.setBorder( componentBorder );
		newLabel.setText( "" );
		newLabel.setHorizontalAlignment( JLabel.CENTER );
		newLabel.setVerticalAlignment( JLabel.CENTER );
		return newLabel;
	}

	@Override
	public JComponent getRenderer( Object gl, int index, Object value )
	{
		// get or create a new object
		JLabel jl = (JLabel) getUnusedComponent();

		if (value != null)
		{
			if (value instanceof ScalableIcon)
			{
				ScalableIcon scaled = ( (ScalableIcon) value );
				scaled.setSize( getItemSize() );
				jl.setPreferredSize( getItemSize() );
				jl.setIcon( scaled );
			}
			else
				if (value instanceof Icon)
				{
					ScalableImageIcon scaled = new ScalableImageIcon( (Icon) value );
					scaled.setSize( getItemSize() );
					jl.setPreferredSize( getItemSize() );
					jl.setIcon( scaled );
				}
				else
				{
					jl.setText( value.toString() );
				}
		}
		else
		{
			UnhandledExceptionManager.handleException( new Exception( "rendered component is null" ) );
		}

		return jl;
	}

	protected JComponent getUnusedComponent()
	{
		if (unusedLabels.size() > 0)
		{
			return unusedLabels.remove( unusedLabels.size() - 1 );
		}

		return getNewComponentInstance();
	}

	@Override
	public JComponent getBlankComponent()
	{
		JLabel myLab = new JLabel();
		myLab.setMinimumSize( itemSize );
		myLab.setMaximumSize( itemSize );
		myLab.setPreferredSize( itemSize );
		myLab.setOpaque( false );

		return myLab;// Box.createRigidArea( itemSize );
	}

	PropertyChangeSupport changeSupport = new PropertyChangeSupport( this );

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cybernostics.lib.gui.control.SupportsPropertyChanges#
	 * addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	@Override
	public void addPropertyChangeListener( final PropertyChangeListener listener )
	{
		changeSupport.addPropertyChangeListener( listener );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cybernostics.lib.gui.control.SupportsPropertyChanges#
	 * addPropertyChangeListener(java.lang.String,
	 * java.beans.PropertyChangeListener)
	 */
	@Override
	public void addPropertyChangeListener( final String propertyName,
		final PropertyChangeListener listener )
	{
		changeSupport.addPropertyChangeListener(
			propertyName,
			listener );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cybernostics.lib.gui.control.SupportsPropertyChanges#
	 * removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	@Override
	public void removePropertyChangeListener( final PropertyChangeListener listener )
	{
		changeSupport.removePropertyChangeListener( listener );

	}
}
