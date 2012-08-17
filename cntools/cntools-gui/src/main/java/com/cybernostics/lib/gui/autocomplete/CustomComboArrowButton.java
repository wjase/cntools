package com.cybernostics.lib.gui.autocomplete;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;

import com.cybernostics.lib.gui.declarative.events.WhenResized;

public class CustomComboArrowButton extends ArrowButton
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 291417013100281460L;
	Dimension d = null;

	public CustomComboArrowButton(
		int direction,
		Color background,
		Color shadow,
		Color darkShadow,
		Color highlight )
	{
		super( direction,
			( background == null ) ? Color.lightGray : background,
			( shadow == null ) ? Color.gray : shadow,
			( darkShadow == null ) ? Color.darkGray
				: darkShadow,
			( highlight == null ) ? Color.lightGray.brighter()
				.brighter()
				.brighter() : highlight );

	}

	public CustomComboArrowButton( int direction )
	{
		this( direction, null, null, null, null );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.basic.BasicArrowButton#getMaximumSize()
	 */
	@Override
	public Dimension getPreferredSize()
	{
		return d != null ? d : super.getPreferredSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.basic.BasicArrowButton#getMaximumSize()
	 */
	@Override
	public Dimension getMaximumSize()
	{
		return d;
	}

	private void updateSizeFromAssociate( JComponent associate )
	{
		Dimension r = associate.getPreferredSize();
		r.height *= 0.90;
		if (d == null || r.getHeight() != d.height)
		{
			d = new Dimension( r.height, r.height );

			if (getParent() != null)
			{
				getParent().validate();
			}
		}

	}

	public void setAssociatedComponent( JComponent jc )
	{
		updateSizeFromAssociate( jc );
		new WhenResized( jc )
		{

			@Override
			public void doThis( ComponentEvent e )
			{
				updateSizeFromAssociate( (JComponent) e.getComponent() );
			}
		};

	}
}
