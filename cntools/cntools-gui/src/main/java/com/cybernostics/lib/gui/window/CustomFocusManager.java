package com.cybernostics.lib.gui.window;

import com.cybernostics.lib.gui.panel.IterableContainer;
import com.cybernostics.lib.patterns.singleton.DefaultConstructorSingleton;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.JComponent;

/**
 *
 * @author jasonw
 */
public class CustomFocusManager extends FocusTraversalPolicy
{

	public static void setTabOrder( JComponent c, int order )
	{
		c.putClientProperty(
			tabOrderKey,
			order );
	}

	public static final String tabOrderKey = "TAB_ORDER";

	private static int id = 10000;

	private Vector< JComponent > tabOrder = new Vector< JComponent >();

	/**
	 * This is an unusual sort order of components. If they have a property tab
	 * order (by calling this class's static setTabOrder( JComponent c, int
	 * order )) then that order is used. Otherwise an index is assigned in the
	 * order they are encountered.
	 */
	private static final Comparator< JComponent > sorter = new Comparator< JComponent >()
	{

		@Override
		public int compare( JComponent o1, JComponent o2 )
		{
			Integer i1 = (Integer) o1.getClientProperty( tabOrderKey );
			if (i1 == 0)
			{
				i1 = new Integer( ++id );
				o1.putClientProperty(
					tabOrderKey,
					i1 );
			}
			Integer i2 = (Integer) o2.getClientProperty( tabOrderKey );
			if (i2 == 0)
			{
				i2 = new Integer( ++id );
				o2.putClientProperty(
					tabOrderKey,
					i2 );
			}
			int comp = i1.compareTo( i2 );
			if (comp == 0)
			{
				if (o1 != o2)
				{
					return 1;
				}
			}
			return comp;
		}

	};

	private static SingletonInstance< CustomFocusManager > manager = new DefaultConstructorSingleton< CustomFocusManager >(
		CustomFocusManager.class );

	public static CustomFocusManager get()
	{
		return manager.get();
	}

	private CustomFocusManager()
	{
	}

	public void setCurrentContainer( Container c )
	{
		this.tabOrder.clear();
		// we only handle containers which implement CustomFocusPanel
		if (!( c instanceof CustomFocusPanel ))
		{
			return;
		}
		Set< JComponent > components = findTabbable(
			null,
			c );
		this.tabOrder.addAll( components );

	}

	public Component getComponentAfter( Container focusCycleRoot,
		Component aComponent )
	{
		if (tabOrder.size() == 0)
		{
			return null;// no tabbing here
		}
		int idx = ( tabOrder.indexOf( aComponent ) + 1 ) % tabOrder.size();
		return tabOrder.get( idx );
	}

	public Component getComponentBefore( Container focusCycleRoot,
		Component aComponent )
	{
		if (tabOrder.size() == 0)
		{
			return null;// no tabbing here
		}
		int idx = tabOrder.indexOf( aComponent ) - 1;
		if (idx < 0)
		{
			idx = tabOrder.size() - 1;
		}
		return tabOrder.get( idx );
	}

	public Component getDefaultComponent( Container focusCycleRoot )
	{
		if (tabOrder.size() == 0)
		{
			return null;// no tabbing here
		}
		return tabOrder.get( 0 );
	}

	public Component getLastComponent( Container focusCycleRoot )
	{
		if (tabOrder.size() == 0)
		{
			return null;// no tabbing here
		}
		return tabOrder.lastElement();
	}

	public Component getFirstComponent( Container focusCycleRoot )
	{
		if (tabOrder.size() == 0)
		{
			return null;// no tabbing here
		}
		return tabOrder.get( 0 );
	}

	private Set< JComponent > findTabbable( Set< JComponent > comps, Container c )
	{
		if (comps == null)
		{
			comps = new TreeSet< JComponent >( sorter );
		}
		for (Component eachComponent : IterableContainer.get( c ))
		{
			if (eachComponent instanceof Container)
			{
				findTabbable(
					comps,
					(Container) eachComponent );
			}
			else
			{
				comps.add( (JComponent) eachComponent );
			}
		}
		return comps;
	}

}
