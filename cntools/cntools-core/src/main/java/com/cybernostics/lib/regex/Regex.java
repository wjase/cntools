/*
 * #%L cntools-core %% Copyright (C) 2012 Cybernostics Pty Ltd %% Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. #L%
 */

package com.cybernostics.lib.regex;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jasonw
 */
public class Regex implements Iterable< String >
{

	private Pattern toMatch = null;

	private Matcher matcher = null;

	private boolean findCalled = false;

	public Regex( String pattern, int flags )
	{
		toMatch = Pattern.compile(
			pattern,
			flags );
	}

	public Regex( String pattern )
	{
		toMatch = Pattern.compile( pattern );
	}

	public Regex setInput( String input )
	{
		findCalled = false;
		matcher = toMatch.matcher( input );
		return this;
	}

	private void doImplicitFind()
	{
		if (findCalled == false)
		{
			find();
			findCalled = true;
		}
	}

	public void dumpGroups()
	{
		doImplicitFind();
		for (int i = 0; i <= matcher.groupCount(); ++i)
		{
			System.out.println( String.format(
				"Group %d: %s",
				i,
				matcher.group( i ) ) );
		}
	}

	public boolean find( String toMatchStr )
	{
		return setInput(
			toMatchStr ).find();
	}

	public boolean find( String toMatchStr, int pos )
	{

		return setInput(
			toMatchStr ).find(
			pos );
	}

	public boolean find()
	{
		findCalled = true;
		return matcher != null ? matcher.find() : false;
	}

	public boolean find( int pos )
	{
		findCalled = true;
		return matcher != null ? matcher.find( pos ) : false;
	}

	/**
	 * @see Matcher.start()
	 */
	public int start( int group )
	{
		doImplicitFind();
		return matcher.start( group );
	}

	/**
	 * @see Matcher.matches()
	 */
	public boolean matches()
	{
		findCalled = true;
		return matcher.matches();
	}

	/**
	 * @see Matcher.groupCount()
	 */
	public int groupCount()
	{
		doImplicitFind();
		return matcher.groupCount();
	}

	/**
	 * @see Matcher.group()
	 */
	public String group( int group )
	{
		doImplicitFind();
		return matcher.group( group );
	}

	public String replaceAll( String input, String replacement )
	{
		setInput( input );
		return matcher.replaceAll( replacement );
	}

	/**
	 * @see Matcher.group()
	 */
	public String group()
	{
		doImplicitFind();
		return matcher.group();
	}

	/**
	 * @see Matcher.end()
	 */
	public int end( int group )
	{
		doImplicitFind();
		return matcher.end( group );
	}

	/**
	 * @see Matcher.end()
	 */
	public int end()
	{
		doImplicitFind();
		return matcher.end();
	}

	/**
	 * @see Matcher.appendTail()
	 */
	public StringBuffer appendTail( StringBuffer sb )
	{
		doImplicitFind();
		return matcher.appendTail( sb );
	}

	/**
	 * @see Matcher.appendReplacement()
	 */
	public Matcher appendReplacement( StringBuffer sb, String replacement )
	{
		doImplicitFind();
		return matcher.appendReplacement(
			sb,
			replacement );
	}

	String match( String input )
	{
		if (find( input ))
		{
			return group();
		}
		return null;
	}

	void reset()
	{
		matcher.reset();
		findCalled = false;
	}

	/**
	 * Internal group iterator
	 */
	class GroupIterator implements Iterator< String >
	{

		Matcher mIt = null;

		GroupIterator( Matcher toMatchIterate )
		{
			mIt = toMatchIterate;
		}

		@Override
		public boolean hasNext()
		{
			return mIt.find();
		}

		@Override
		public String next()
		{
			return mIt.group();
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException( "Not supported." );
		}

	}

	@Override
	public Iterator< String > iterator()
	{
		return new GroupIterator( matcher );
	}

}
