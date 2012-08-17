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

package com.cybernostics.lib.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author jasonw
 */
public class StackString
{

	private StackString()
	{
	}

	public static String get()
	{
		return get( "" );
	}

	public static String get( final String filter )
	{
		StringWriter sw = new StringWriter();

		PrintWriter pw = new PrintWriter( sw )
		{
			boolean stopAtNext = false;
			boolean stopWriting = false;

			@Override
			public void println( char x )
			{
				super.println( "boo" + x );
			}

			@Override
			public void println( String str )
			{
				if (str.indexOf( "StackString" ) != -1)
				{
					return;
				}
				if (stopWriting)
				{
					return;
				}
				super.println( str );
				if (stopAtNext == true)
				{
					stopWriting = true;
				}
				if (str.indexOf( filter ) == -1)
				{
					stopAtNext = true;
				}
			}

		};
		Exception e = new Exception();
		e.printStackTrace( pw );
		pw.flush();
		return sw.toString()
			.replaceFirst(
				"java.lang.Exception",
				"" );
		//                        if ( s.indexOf( "AnimatedFrame" ) == -1 )
		//                        {
		//                            System.out.println( s );
		//                        }
	}

}
