package com.cybernostics.lib.gui.panel;

import java.awt.Component;
import java.awt.Container;
import java.util.*;

public class IterableContainer implements Iterable< Component >
{

	public static IterableContainer get( Container c )
	{
		return new IterableContainer( c );
	}

	private final Container comp;

	public IterableContainer( Container comp )
	{
		this.comp = comp;

	}

	// return an adaptor for the Enumeration
	@Override
	public Iterator< Component > iterator()
	{
		return new Iterator< Component >()
		{

			int maxIndex = 0;
			int index = -1;

			{
				maxIndex = comp.getComponentCount() - 1;
			}

			@Override
			public boolean hasNext()
			{
				return ( maxIndex >= 0 ) && ( index < maxIndex );
			}

			@Override
			public Component next()
			{
				++index;
				Component c = comp.getComponent( index );
				return c;
			}

			@Override
			public void remove()
			{
				comp.remove( index );
			}
		};
	}
}