package com.cybernostics.lib.gui.panel;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author jasonw
 */
public class BoxPanel extends JPanel
{

	public enum Direction
	{

		HORIZONTAL, VERTICAL
	}

	Direction myDirection = Direction.HORIZONTAL;

	public static BoxPanel get( Direction dir )
	{
		BoxPanel bp = new BoxPanel();
		bp.setLayout( new BoxLayout( bp,
			dir == Direction.HORIZONTAL ? BoxLayout.X_AXIS : BoxLayout.Y_AXIS ) );
		return bp;
	}

	public static BoxPanel get( Direction dir, JComponent... comps )
	{
		BoxPanel bp = new BoxPanel();
		bp.setLayout( new BoxLayout( bp,
			dir == Direction.HORIZONTAL ? BoxLayout.X_AXIS : BoxLayout.Y_AXIS ) );
		for (JComponent comp : comps)
		{
			bp.add( comp );

		}
		return bp;
	}

	private BoxPanel()
	{
	}

	public void addGlue()
	{
		add( Box.createGlue() );
	}

	public void addStrut( int size )
	{
		if (myDirection == Direction.HORIZONTAL)
		{
			add( Box.createHorizontalStrut( size ) );
		}
		else
		{
			add( Box.createVerticalStrut( size ) );
		}
	}

}
