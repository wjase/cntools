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

/**
 * Convenience method for regular expressions to make them more like perl/php
 * 
 * Usage:
 * 
 * // Match one grouped occurrence
 * // result = "cat" after this statement
 * String result = Extractor.get("\(c.t\)", "The cat sat in a cot");
 * 
 * for(String eachMatch: Extractor.getAll("\(c.t\)", "The cat sat in a cot"))
 * {
 *      System.out.println( eachMatch );  // will print cat and cot on a line
 * }
 * @author jasonw
 */
public class Extractor
{

	/**
	 * Returns the first group matched by the re
	 * @param re
	 * @return 
	 */
	public static String get( String re, String input )
	{
		Regex ex = new Regex( re );
		return ex.match( input );
	}

	public static String get( Regex re, String input )
	{
		re.find( input );
		return re.group();
	}

	/**
	 * Returns the first group matched by the re
	 * @param re
	 * @return 
	 */
	public static String group( String re, String input )
	{
		Extractor ex = new Extractor( re );
		return ex.getGroup( input );
	}

	public static String group( Regex re, String input )
	{
		Extractor ex = new Extractor( re );
		return ex.getGroup( input );
	}

	public static Iterator< String > groups( String re, String input )
	{
		Extractor ex = new Extractor( re );
		return ex.getGroups( input );
	}

	public static Iterator< String > groups( Regex re, String input )
	{
		Extractor ex = new Extractor( re );
		return ex.getGroups( input );
	}

	Regex engine;

	public Extractor( String re )
	{
		engine = new Regex( re );
	}

	public Extractor( Regex re )
	{
		engine = re;
	}

	public String getGroup( String input )
	{
		if (engine.find( input ))
		{
			return engine.group( 1 );
		}
		return null;
	}

	public Iterator< String > getGroups( String input )
	{
		if (engine.find( input ))
		{
			return engine.iterator();
		}
		return null;
	}

}
