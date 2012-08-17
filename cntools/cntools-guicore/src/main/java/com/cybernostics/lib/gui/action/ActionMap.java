package com.cybernostics.lib.gui.action;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.Action;

/**
 *
 * Implements an application-wide action map Actions are registered by an id
 * which should be something like mainparentscreen.actionName - where
 * mainparentscreen is the principle panel the button appears on.
 *
 * eg. mainscreen.games
 *
 * @author jasonw
 */
public class ActionMap
{

	private static Map< String, Action > actions = new HashMap< String, Action >();

	public static void put( String actionId, Action toAdd )
	{
		actions.put(
			actionId,
			toAdd );
	}

	public static Action get( String actionId )
	{
		return actions.get( actionId );
	}

	public static void fireEvent( final String actionId, Object object )
	{
		//final AbstractAction act = get( actionId );
		ActionEvent av = new ActionEvent( object, 0, actionId );
		EventQueue queue = Toolkit.getDefaultToolkit()
			.getSystemEventQueue();
		queue.postEvent( av );

	}

	private ActionMap()
	{
	}

	public static Iterable< Action > getAll( final String prefix )
	{
		final Iterator< Entry< String, Action >> iter = actions.entrySet()
			.iterator();
		return new Iterable< Action >()
		{

			@Override
			public Iterator< Action > iterator()
			{
				return new Iterator< Action >()
				{

					Action next = null;

					@Override
					public boolean hasNext()
					{
						if (!iter.hasNext())
						{
							return false;
						}
						while (next == null && iter.hasNext())
						{
							Entry< String, Action > eachOne = iter.next();
							if (eachOne.getKey()
								.startsWith(
									prefix ))
							{
								next = eachOne.getValue();
								return true;
							}
						}
						return false;

					}

					@Override
					public Action next()
					{
						if (next != null)
						{
							Action ret = next;
							next = null;
							return ret;
						}
						if (hasNext())
						{
							return next();
						}
						return null;
					}

					@Override
					public void remove()
					{
						throw new UnsupportedOperationException( "Not supported yet." );
					}

				};
			}

		};
	}

}
