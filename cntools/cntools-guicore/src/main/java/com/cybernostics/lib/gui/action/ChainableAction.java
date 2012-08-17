package com.cybernostics.lib.gui.action;

import com.cybernostics.lib.concurrent.ConcurrentSet;
import com.cybernostics.lib.gui.declarative.events.ActionEventSource;
import com.cybernostics.lib.media.icon.ActionIconLoader;
import com.cybernostics.lib.media.icon.IconLoadClient;
import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

/**
 * This implements the Action interface but allows other listeners to join the
 * party when an action event occurs.
 *
 * @author jasonw
 */
public class ChainableAction extends AbstractAction
	implements
	IconLoadClient,
	ActionEventSource
{

	public static ChainableAction get( String id )
	{
		ChainableAction ca = (ChainableAction) ActionMap.get( id );
		if (ca == null)
		{
			ca = new ChainableAction( id );
		}
		return ca;
	}

	public static final String ID_PROPERTY = "id";

	public static final String TOOLTIPTEXT_PROPERTY = "tooltext";

	public static final String TOOLTIPAUDIO_PROPERTY = "toolaudio";

	protected ChainableAction()
	{
	}

	protected ChainableAction( String name )
	{
		super( name );
		setId( name );
	}

	protected ChainableAction( String name, String id, Icon icon )
	{
		super( name, icon );
		setId( id );
		putValue(
			Action.LARGE_ICON_KEY,
			icon );

	}

	protected ChainableAction( String name, String id, URL iconURL )
	{
		this( name, id, iconURL, null );
	}

	protected ChainableAction(
		String name,
		String id,
		URL iconURL,
		IconLoadClient client )
	{
		super( name );
		setId( id );
		loadIcon(
			iconURL,
			client );

	}

	private Set< ActionListener > listeners = new ConcurrentSet< ActionListener >();

	private Component defaultSource = null;

	public Component getDefaultSource()
	{
		return defaultSource;
	}

	public void setDefaultSource( Component defaultSource )
	{
		this.defaultSource = defaultSource;
	}

	/**
	 * We make this final so derived classes wont clobber the chaining behavior.
	 * Instead they need to override the onActionPerformed
	 *
	 * @param e
	 */
	@Override
	public final void actionPerformed( ActionEvent e )
	{
		// do default actions
		onActionPerformed( e );
		// call other listeners
		fireActionPerformed( e );
	}

	@Override
	public void iconLoaded( ScalableSVGIcon svgsi )
	{
		setIcon( svgsi );
	}

	/**
	 * By overriding this instead of ActionPerformed you get to respond to the
	 * action before any other listeners are called.
	 *
	 * @param e
	 */
	protected void onActionPerformed( ActionEvent e )
	{
	}

	public final void addActionListener( ActionListener toAdd )
	{
		listeners.add( toAdd );
	}

	public void onClick( ActionListener toAdd )
	{
		addActionListener( toAdd );
	}

	public void removeActionListener( ActionListener toRemove )
	{
		listeners.remove( toRemove );
	}

	public void fireActionPerformed( ActionEvent e )
	{
		for (ActionListener actionListener : listeners)
		{
			actionListener.actionPerformed( e );
		}
	}

	/**
	 * Programmatically triggers this action as if a button were clicked The
	 * event source is the default component set by setDefaultSource()
	 *
	 */
	public void trigger()
	{
		trigger( null );
	}

	/**
	 * Programmatically triggers this action as if a button were clicked The
	 * event source is the cSource or the default one if it is null
	 */
	public void trigger( Component cSource )
	{
		Component source = cSource != null ? cSource : defaultSource;
		ActionEvent ae = new ActionEvent( (Object) source,
			ActionEvent.ACTION_PERFORMED,
			"" );
		Toolkit.getDefaultToolkit()
			.getSystemEventQueue()
			.postEvent(
				ae );
	}

	public final void setId( String id )
	{
		ActionMap.put(
			id,
			this );
		putValue(
			ID_PROPERTY,
			id );
		Object name = getValue( Action.NAME );
		if (name == null)
		{
			putValue(
				Action.NAME,
				id );
		}
	}

	public void setIcon( URL resource )
	{
		if (resource != null)
		{
			loadIcon(
				resource,
				null );
		}
	}

	public void setIcon( Icon pic )
	{
		if (pic != null)
		{
			putValue(
				Action.LARGE_ICON_KEY,
				pic );
		}
	}

	public final void loadIcon( URL resource, final IconLoadClient whenLoaded )
	{
		ActionIconLoader.setIcon(
			this,
			resource,
			new IconLoadClient()
		{

			@Override
			public void iconLoaded( ScalableSVGIcon svgsi )
			{
				svgsi.setFitToParent( true );
				ChainableAction.this.iconLoaded( svgsi );
				if (whenLoaded != null)
				{
					whenLoaded.iconLoaded( svgsi );
				}
			}

		} );
	}

	public String getId()
	{
		Object val = getValue( ID_PROPERTY );
		return val != null ? val.toString() : "null";
	}

}
