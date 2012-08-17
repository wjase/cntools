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

package com.cybernostics.lib.logging;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;

/**
 * @author jasonw
 * 
 */
public class AppLog
{

	private AppLog()
	{
	}

	private static Logger logger = null;

	public static void init( String logLabel )
	{
		logger = Logger.getLogger( logLabel );
	}

	public static Logger getLogger( String logfileName )
	{
		if (logger == null)
		{
			if (logfileName == null)
			{
				logger = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME );
				UnhandledExceptionManager.handleException( new Exception( "No logfile set" ) );
			}
			else
			{
				logger = Logger.getLogger( logfileName );
			}
		}
		return logger;
	}

	public static String readLog( String name )
	{
		try
		{
			FileInputStream fis = new FileInputStream( name + ".log" ); // $NON
			BufferedReader br = new BufferedReader( new InputStreamReader( fis ) );
			String nextLine = null;
			String newline = System.getProperty( "line.separator" ); // $NON
			StringBuilder logfile = new StringBuilder();
			while (( nextLine = br.readLine() ) != null)
			{
				logfile.append(
					nextLine )
					.append(
						newline );
			}

			return logfile.toString();

		}
		catch (FileNotFoundException e)
		{
			return "";
		}
		catch (IOException e)
		{
			UnhandledExceptionManager.handleException( e );
		}
		return "";
	}

	private static FileHandler fh = null;

	public static FileHandler getHandler( String name )
	{
		if (fh == null)
		{
			try
			{
				fh = new FileHandler( name + ".log" );
				fh.setFormatter( new SimpleFormatter() );
			}
			catch (SecurityException e)
			{
				UnhandledExceptionManager.handleException( e );
			}
			catch (IOException e)
			{
				UnhandledExceptionManager.handleException( e );
			}
		}
		return fh;
	}

	public static void resetLogger()
	{
		logger.removeHandler( getHandler( null ) );
		fh.flush();
		fh.close();
		fh = null;
	}

	/**
	 * @return
	 */
	public static Logger getLogger()
	{
		return getLogger( null );
	}
}
