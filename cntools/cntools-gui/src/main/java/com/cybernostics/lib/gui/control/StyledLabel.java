package com.cybernostics.lib.gui.control;

import com.cybernostics.lib.gui.graphics.StateSaver;
import com.cybernostics.lib.gui.shapeeffects.ShapeEffect;
import java.awt.Graphics;
import javax.swing.JLabel;

public class StyledLabel extends JLabel
{

	/**
	 *
	 */
	private static final long serialVersionUID = -3740751353673377803L;

	public final static int NORMAL = 0;

	public final static int BOLD = 1;

	public final static int ITALIC = 2;

	public final static int GREY = 3;

	//	static Icon currentArrow = null;
	//
	//	static Icon tick = null;
	//
	//	static Icon blank = null;
	String basicText = "";

	private ShapeEffect paintEffect = null;

	public ShapeEffect getPaintEffect()
	{
		return paintEffect;
	}

	public void setPaintEffect( ShapeEffect paintEffect )
	{
		this.paintEffect = paintEffect;
	}

	@Override
	public void paint( Graphics g )
	{
		StateSaver g2 = StateSaver.wrap( g );
		try
		{
			if (paintEffect != null)
			{
				paintEffect.draw(
					g2,
					getBounds() );
			}
			super.paint( g2 );
		}
		finally
		{
			g2.restore();
		}
	}

	public StyledLabel()
	{
	}

	public StyledLabel( String labelText )
	{
		super( labelText );
		basicText = labelText;
		setNormal();
	}

	public StyledLabel( String labelText, int style )
	{
		this( labelText );
		setStyle( style );

	}

	public void setBold()
	{
		super.setText( "<html><b>" + basicText + "</b></html>" );
		setEnabled( true );
		//		setIcon( currentArrow );

	}

	public void setGreyed()
	{
		setNormal();
		setEnabled( false );
		//		setIcon( tick );
	}

	public void setItalic()
	{
		super.setText( "<html><i>" + basicText + "</i></html>" );
		setEnabled( true );
	}

	public void setNormal()
	{
		super.setText( basicText );
		setEnabled( true );
		//		setIcon( blank );
	}

	public void setStyle( int style )
	{
		switch (style)
		{
			case NORMAL:
				setNormal();
				break;
			case BOLD:
				setBold();
				break;
			case ITALIC:
				setItalic();
				break;
			case GREY:
				setGreyed();
				break;
		}

	}

	@Override
	public void setText( String text )
	{
		basicText = text;
		super.setText( text );
	}

	;
}
