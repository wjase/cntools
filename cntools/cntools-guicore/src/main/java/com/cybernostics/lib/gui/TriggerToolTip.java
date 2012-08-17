/**
 * 
 */

package com.cybernostics.lib.gui;

import com.cybernostics.lib.gui.declarative.events.WhenHiddenOrClosed;
import com.kitfox.svg.elements.Font;
import java.awt.Graphics;
import javax.swing.JToolTip;

/**
 * @author jasonw
 * 
 */
public class TriggerToolTip extends JToolTip
{
	public static final String TOOL_ACTION_PROPERTY = "tool_action";

	public static ScreenRelativeDimension fontHeight = new ScreenRelativeDimension( 0.09f,
		0.09f );
	private static Font tipFont = null;

	public static Font getTipFont()
	{
		return tipFont;
	}

	public static void setTipFont( Font tipFont )
	{
		TriggerToolTip.tipFont = tipFont;
	}

	public TriggerToolTip()
	{
		setOpaque( false );
		new WhenHiddenOrClosed( this )
		{

			@Override
			public void doThis()
			{
				armed = true;

			}
		};
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5045161476913943936L;

	boolean armed = true;

	@Override
	public void paintComponent( Graphics g )
	{
		Object toolAction = getComponent().getClientProperty(
			TOOL_ACTION_PROPERTY );
		if (toolAction != null)
		{
			if (armed)
			{
				//TODO UNCOMMENT THIS
				//armed = false;
				( (TooltipListener) toolAction ).doTooltipFor( getComponent() );
			}

		}
	}

	@Override
	public void updateUI()
	{
		super.updateUI();
	}

}
