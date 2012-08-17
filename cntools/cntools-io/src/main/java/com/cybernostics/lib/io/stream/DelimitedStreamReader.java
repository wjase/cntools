package com.cybernostics.lib.io.stream;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.io.StructuredDataReader;

public class DelimitedStreamReader implements StructuredDataReader
{

	private char separater = ',';

	public static void main( String[] args )
	{
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream( "c:/temp/addresses.tab" );
		}
		catch (FileNotFoundException e)
		{
			UnhandledExceptionManager.handleException( e );
		}
		DelimitedStreamReader csvr = new DelimitedStreamReader( fis, true, '\t' );

		String[] headers = csvr.getHeaders();

		for (String[] eachRecord : csvr)
		{
			int index = 0;
			for (String eachOne : eachRecord)
			{
				if (eachOne.length() != 0)
				{
					System.out.println( headers[ index ] + "= " + eachOne );
				}
				index++;
			}
		}
	}

	String[] headers;
	boolean hasHeader = false;

	BufferedReader csvData;

	boolean gotHeader = false;

	public DelimitedStreamReader(
		InputStream is,
		boolean hasHeader,
		char separator )
	{
		this.hasHeader = hasHeader;
		this.separater = separator;
		csvData = new BufferedReader( new InputStreamReader( is ) );

	}

	public DelimitedStreamReader( InputStream is, boolean hasHeader )
	{
		csvData = new BufferedReader( new InputStreamReader( is ) );
		this.hasHeader = hasHeader;
	}

	private void readHeader()
	{
		if (hasHeader && !gotHeader)
		{
			try
			{
				String nextLine = csvData.readLine();
				headers = split( nextLine );
			}
			catch (IOException e)
			{
				UnhandledExceptionManager.handleException( e );
			}
			gotHeader = true;
		}
	}

	public String[] getHeaders()
	{
		readHeader();
		return headers;
	}

	public boolean hasMoreItems()
	{
		try
		{
			return csvData.ready();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			UnhandledExceptionManager.handleException( e );
		}
		return false;
	}

	public String[] nextRecord()
	{
		readHeader();

		String nextLine;
		try
		{
			nextLine = csvData.readLine();
			return split( nextLine );
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			UnhandledExceptionManager.handleException( e );
		}
		return null;

	}

	private String[] split( String sIn )
	{
		ArrayList< String > items = new ArrayList< String >();

		int currentStart = 0;
		int currentStop = 0;
		int searchStart = 0;
		int quoteCount = 0;

		while (currentStop != -1)
		{
			currentStop = sIn.indexOf(
				separater,
				searchStart );
			if (currentStop == -1)// end of the line
			{
				items.add( sIn.substring( currentStart ) );
			}
			else
			{
				// use search start to save going over old ground to count
				// quotes
				for (int index = searchStart; index <= currentStop; ++index)
				{
					if (sIn.charAt( index ) == '\"')
					{
						++quoteCount;
					}
				}

				// update the search to start after the current comma
				searchStart = currentStop;
				++searchStart;

				// each token will contain an even number of quotes
				if (( quoteCount % 2 ) == 0)
				{
					// remove any double quotes and enclosing quotes
					String nextToken = sIn.substring(
						currentStart,
						currentStop );
					if (( nextToken.length() > 0 )
						&& ( nextToken.charAt( 0 ) == '\"' ))
					{
						final Pattern doubleQuote = Pattern.compile( "\"\"" );
						Matcher m = doubleQuote.matcher( nextToken.substring(
							1,
							nextToken.length() - 1 ) );
						nextToken = m.replaceAll( "\"" );

					}

					items.add( nextToken );

					currentStart = searchStart;
					quoteCount = 0;
				}
			}

			// otherwise find the next comma and try again.

		}

		String[] itemArray = new String[ items.size() ];
		for (int index = 0; index < items.size(); ++index)
		{
			itemArray[ index ] = items.get( index );
		}
		return itemArray;

	}

	@Override
	public Iterator< String[] > iterator()
	{

		return new Iterator< String[] >()
		{

			@Override
			public boolean hasNext()
			{
				return hasMoreItems();
			}

			@Override
			public String[] next()
			{
				return nextRecord();
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public void close()
	{

		try
		{
			csvData.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			UnhandledExceptionManager.handleException( e );
		}
	}

}
