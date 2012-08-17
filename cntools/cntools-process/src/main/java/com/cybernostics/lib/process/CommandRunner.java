package com.cybernostics.lib.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;

/**
 * Runs system processes in a shell and gets the stdout in a InputStream
 * 
 * @author jasonw
 * 
 */
public class CommandRunner
{

	/**
	 * Copies a script stored as an internal jar resource to an external file so
	 * it can be executed.
	 * 
	 * @param path
	 * @param scriptSource
	 */
	public static void createScriptFile( String path, InputStream scriptSource )
	{

		File fTest = new File( path );
		if (fTest.exists())
		{
			return; // already there
		}

		BufferedReader isr = new BufferedReader( new InputStreamReader( scriptSource ) );
		String nextLine = null;
		try
		{
			FileOutputStream fos = new FileOutputStream( path );
			PrintWriter pw = new PrintWriter( fos );
			while (( nextLine = isr.readLine() ) != null)
			{
				pw.println( nextLine );
			}
			pw.flush();
			fos.close();
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}

	}

	public static void main( String[] args )
	{
		System.out.println( readAll( RunShellCommand( "dir" ) ) );
		System.out.println( readAll( RunCommand(
			"cscript.exe",
			"/?" ) ) );

	}

	public static String readAll( InputStream is )
	{
		StringBuffer sbOutput = new StringBuffer();

		BufferedReader isr = new BufferedReader( new InputStreamReader( is ) );
		String nextLine = null;
		try
		{
			while (( nextLine = isr.readLine() ) != null)
			{
				sbOutput.append( nextLine );
				sbOutput.append( '\n' );
			}
		}
		catch (IOException e)
		{
			UnhandledExceptionManager.handleException( e );
		}

		return sbOutput.toString();

	}

	/**
	 * Run a command-line process To run a shell command use RunShellCommand()
	 * which invokes a separate shell first
	 * 
	 * @param commandArguments
	 *            - the first must be the name of the shell command
	 * @return
	 */
	public static InputStream RunCommand( String... commandArguments )
	{
		try
		{
			Process p = Runtime.getRuntime()
				.exec(
					commandArguments );
			try
			{
				p.waitFor();
			}
			catch (InterruptedException e)
			{
				UnhandledExceptionManager.handleException( e );

				return null;
			}
			return p.getInputStream();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			UnhandledExceptionManager.handleException( e );
		}
		return null;

	}

	/**
	 * Run a shell command ( like dir which is not a separate program but a
	 * command in the shell) To run a command line process use RunCommand which
	 * does not invoke a seperate shell first
	 * 
	 * @param commandArguments
	 *            - the first must be the name of the shell command
	 * @return
	 */
	public static InputStream RunShellCommand( String... commandArguments )
	{
		ArrayList< String > args = new ArrayList< String >();
		args.add( System.getenv( "COMSPEC" ) );
		args.add( "/c" );
		for (String eachCmd : commandArguments)
		{
			args.add( eachCmd );
		}

		String[] argsList = new String[ args.size() ];
		return RunCommand( args.toArray( argsList ) );

	}

}
