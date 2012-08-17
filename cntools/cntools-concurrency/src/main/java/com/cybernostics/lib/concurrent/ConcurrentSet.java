package com.cybernostics.lib.concurrent;

import com.cybernostics.lib.collections.IterableArray;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author jasonw
 */
public class ConcurrentSet< valueType > implements Set< valueType >
{

	private ConcurrentHashMap< valueType, Boolean > concSet = new ConcurrentHashMap< valueType, Boolean >();

	@Override
	public Iterator< valueType > iterator()
	{
		return concSet.keySet()
			.iterator();
	}

	@Override
	public Object[] toArray()
	{
		return concSet.keySet()
			.toArray();
	}

	@Override
	public < T > T[] toArray( T[] a )
	{
		return concSet.keySet()
			.toArray(
				a );
	}

	@Override
	public boolean add( valueType e )
	{
		concSet.put(
			e,
			Boolean.TRUE );
		return true;
	}

	@Override
	public boolean remove( Object o )
	{
		return concSet.remove( (valueType) o ) != null;
	}

	@Override
	public boolean containsAll( Collection< ? > c )
	{
		return concSet.keySet()
			.containsAll(
				c );
	}

	public boolean addAll( valueType... c )
	{
		for (valueType eachOne : IterableArray.get( c ))
		{
			add( eachOne );
		}
		return true;

	}

	@Override
	public boolean addAll( Collection< ? extends valueType > c )
	{

		for (valueType eachOne : c)
		{
			add( eachOne );
		}
		return true;
	}

	@Override
	public boolean retainAll( Collection< ? > c )
	{
		for (valueType eachOne : this)
		{
			if (!c.contains( eachOne ))
			{
				remove( eachOne );
			}
		}
		return true;
	}

	@Override
	public boolean removeAll( Collection< ? > c )
	{
		for (valueType eachOne : this)
		{
			if (c.contains( eachOne ))
			{
				remove( eachOne );
			}
		}
		return true;

	}

	@Override
	public int size()
	{
		return concSet.size();
	}

	@Override
	public boolean isEmpty()
	{
		return concSet.isEmpty();
	}

	@Override
	public boolean contains( Object o )
	{
		return concSet.containsKey( (valueType) o );
	}

	@Override
	public void clear()
	{
		concSet.clear();
	}

}
