package com.cybernostics.lib.gui.control;

import com.cybernostics.lib.animator.sprite.component.ComponentSprite;
import java.awt.Rectangle;
import javax.swing.JComponent;

/**
 *
 * @author jasonw
 */
public class ComponentDump
{

	public static void dump( JComponent jc )
	{
		String id = (String) jc.getClientProperty( ComponentSprite.ID_PROP );
		System.out.printf(
			"Component: id:%s \n",
			id != null ? id : "" );
		String name = jc.getName();
		System.out.printf(
			"           name:%s \n",
			name != null ? name : "" );
		Rectangle bounds = jc.getBounds();
		System.out.printf(
			"bounds: %d,%d,%d,%d\n",
			bounds.width,
			bounds.height,
			bounds.width,
			bounds.height );
		System.out.printf(
			"type: %s %s\n",
			jc.getClass()
				.getCanonicalName(),
			jc.toString() );
		System.out.printf( "Children:\n" );
		for (int index = 0; index < jc.getComponentCount(); ++index)
		{
			dump( (JComponent) jc.getComponent( index ) );
		}
	}

}
