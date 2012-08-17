package com.cybernostics.lib.template;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jasonw
 */
public class TemplateProcessor
{

	public static String lineEndChars = "\n\r";

	public static String process( String inp, Map< String, String > replacements )
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		InputStream is = new ByteArrayInputStream( inp.getBytes() );
		process(
			is,
			bos,
			"|%",
			"%|",
			replacements );
		return bos.toString();

	}

	public static boolean endsWith( StringBuilder haystack, String needle )
	{
		int haystackLength = haystack.length();
		int needleLength = needle.length();

		if (haystackLength >= needleLength)
		{
			String endBit = haystack.substring(
				haystackLength - needleLength,
				haystackLength );
			return endBit.equals( needle );
		}

		return false;
	}

	public static String replaceAllOnLine( String toProcess,
		Map< String, String > replacements )
	{
		for (Entry< String, String > eachRep : replacements.entrySet())
		{
			toProcess = toProcess.replaceAll(
				"%" + eachRep.getKey() + "%",
				eachRep.getValue() );
		}

		return toProcess;
	}

	enum StreamState
	{

		OUTSIDE, // not reading tag or tag contents
		STARTTAG, // reading start tag contents
		INSIDE, ENDTAG
	}

	public static void process( InputStream inp,
		OutputStream out,
		String startTag,
		String endTag,
		Map< String, String > replacements )
	{
		process(
			inp,
			out,
			startTag,
			endTag,
			replacements,
			false );
	}

	public static void process( InputStream inp,
		OutputStream out,
		String startTag,
		String endTag,
		Map< String, String > replacements,
		boolean keepTags )
	{
		String[] startTags =
		{ startTag };
		String[] endtags =
		{ endTag };

		process(
			inp,
			out,
			startTags,
			endtags,
			replacements,
			keepTags );
	}

	public static void process( InputStream inp,
		OutputStream out,
		String[] startTags,
		String[] endTags,
		Map< String, String > replacements )
	{
		process(
			inp,
			out,
			startTags,
			endTags,
			replacements,
			false );
	}

	public static void process( InputStream inp,
		OutputStream out,
		String[] startTags,
		String[] endTags,
		Map< String, String > replacements,
		boolean keepTags )
	{
		DataOutputStream dos = new DataOutputStream( out );

		StringBuilder currentTag = new StringBuilder();
		StringBuilder currentTagValue = new StringBuilder();
		int tagIndex = -1;
		String currentStartTag = null;
		String currentEndTag = null;

		StreamState currentState = StreamState.OUTSIDE;

		try
		{
			int nextChar = -1;
			readloop: while (( nextChar = inp.read() ) != -1)
			{
				char currentChar = (char) nextChar;

				checkChar: switch (currentState)
				{
					case OUTSIDE:
						for (int i = 0; i < startTags.length; ++i)
						{
							if (currentChar == startTags[ i ].charAt( 0 ))
							{
								tagIndex = i;
								currentStartTag = startTags[ i ];
								currentEndTag = endTags[ i ];
							}
						}
						if (tagIndex == -1)
						{
							out.write( currentChar );
							break checkChar;
						}

						currentState = StreamState.STARTTAG;
						// fall through if we think we're in a start tag
					case STARTTAG:

						currentTag.append( currentChar );
						if (currentTag.toString()
							.equals(
								currentStartTag ))
						{
							currentState = StreamState.INSIDE;
							currentTag.delete(
								0,
								currentTag.length() );
							continue readloop; // go to next char
						}
						else
						{
							// if they are not equal but the same length then this wasn't
							// a genuine start tag - write the bytes and switch to OUTSIDE
							if (currentTag.length() == currentStartTag.length())
							{
								out.write( currentTag.charAt( 0 ) );
								currentTag.deleteCharAt( 0 );
								do
								{
									char c = currentTag.charAt( 0 );
									// could this be a potential start tag?
									for (int i = 0; i < startTags.length; ++i)
									{
										if (c == startTags[ i ].charAt( 0 ))
										{
											tagIndex = i;
											currentStartTag = startTags[ i ];
											currentEndTag = endTags[ i ];
											continue readloop;
										}
									}

									out.write( c );
									currentTag.deleteCharAt( 0 );
								}
								while (currentTag.length() > 0);

								currentState = StreamState.OUTSIDE;
								tagIndex = -1;
							}
						}
						break;
					case INSIDE:
						if (currentChar != currentEndTag.charAt( 0 ))
						{
							currentTagValue.append( currentChar );
							continue readloop;
						}
						else
						{
							currentState = StreamState.ENDTAG;
						}
						// fall through - potential end tag
					case ENDTAG:
						currentTag.append( currentChar );
						if (currentTag.toString()
							.equals(
								currentEndTag ))
						{
							// we're back outside...
							currentState = StreamState.OUTSIDE;
							tagIndex = -1;
							currentTag.delete(
								0,
								currentTag.length() );

							// insert the replacement text in the stream
							String replacement = replacements.get( currentTagValue.toString() );
							if (replacement != null)
							{
								if (keepTags)
								{
									out.write( currentStartTag.getBytes() );
								}
								out.write( replacement.getBytes() );
								if (keepTags)
								{
									out.write( currentEndTag.getBytes() );
								}
							}
							else
							{
								out.write( currentStartTag.getBytes() );
								out.write( currentTagValue.toString()
									.getBytes() );
								out.write( currentEndTag.getBytes() );
							}
							// reset the tag value
							currentTagValue.delete(
								0,
								currentTagValue.length() );
							continue readloop; // go to next char
						}
						else
						{
							// if they are not equal but the same length then this wasn't
							// a genuine end tag - write the bytes and switch to OUTSIDE
							if (currentTag.length() == currentEndTag.length())
							{
								currentTagValue.append( currentTag.charAt( 0 ) );
								currentTag.deleteCharAt( 0 );
								currentState = StreamState.INSIDE;
								do
								{
									char c = currentTag.charAt( 0 );
									// could this be a potential end tag?
									if (c == currentEndTag.charAt( 0 ))
									{
										continue readloop;
									}
									currentTagValue.append( c );
									currentTag.deleteCharAt( 0 );
								}
								while (currentTag.length() > 0);
							}
						}

						break;
				}

			}

		}
		catch (IOException ex)
		{
			Logger.getLogger(
				TemplateProcessor.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		finally
		{
			try
			{
				dos.flush();
				inp.close();
				out.flush();
				out.close();
			}
			catch (IOException ex)
			{
				Logger.getLogger(
					TemplateProcessor.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex );
			}
		}
	}

}
