package com.cybernostics.lib.gui.control;

import com.cybernostics.lib.gui.ButtonFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.UIManager;

public class LAFResourceFactory
{

	public static Icon getIcon( String s )
	{
		return UIManager.getDefaults()
			.getIcon(
				s );
	}

	public static JButton getIconButton( String resourceId )
	{
		JButton jb = ButtonFactory.getButton(
			getIcon( resourceId ),
			null );

		return jb;
	}
}
