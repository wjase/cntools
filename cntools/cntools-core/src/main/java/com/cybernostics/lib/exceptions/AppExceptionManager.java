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

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;
import javax.swing.JOptionPane;
import javax.swing.JWindow;

/**
 * Allows a pluggable ExceptionHandler - which needs to handle exceptions in
 * some GUI fashion (rather than just logging and forgetting)
 * 
 * @author jasonw
 * 
 */
public class AppExceptionManager
{

	private static final String endl = System.getProperty( "line.separator" );

	private static Deque< JWindow > parents = new LinkedBlockingDeque< JWindow >();

	private static AppExceptionHandler theHandler = new AppExceptionHandler()
	{/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.cybernostics.lib.exceptions.ExceptionHandler#handleException(java
		 * .lang.Throwable)
		 */

		@Override
		public void handleException( Throwable t, Object source, JWindow parent )
		{
			StringBuilder sb = new StringBuilder();
			sb.append( t.getLocalizedMessage() );
			Throwable tt = t.getCause();
			if (tt != null)
			{
				sb.append( endl );
				sb.append( tt.getLocalizedMessage() );
				sb.append( endl );
			}
			sb.append( String.format(
				"Source %s",
				source.toString() ) );
			sb.append( endl );

			JOptionPane.showConfirmDialog(
				parent,
				sb.toString() );
			System.err.println( sb.toString() );

			try
			{
				t.printStackTrace();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				t.printStackTrace();
			}

		}

	};

	public static void pushParent( JWindow w )
	{
		parents.addFirst( w );
	}

	public static void popParent()
	{
		parents.removeFirst();
	}

	/**
	 * @param theHandler
	 *            the theHandler to set
	 */
	public static void setTheHandler( AppExceptionHandler theHandler )
	{
		AppExceptionManager.theHandler = theHandler;
	}

	public static void handleException( Throwable t, Object source )
	{
		if (theHandler != null)
		{
			theHandler.handleException(
				t,
				source,
				parents.peek() );
		}
	}

}
