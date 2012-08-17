// ################################################################################
//
// Copyright 2012 WoodLookAndFeel based on the EASYNTH (www.easynth.com) LookAndFeel
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// ################################################################################
package com.cybernostics.laf.wood;

import com.cybernostics.lib.cntoolsakbarfont.AkbarFont;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.UIManager;
import javax.swing.plaf.synth.SynthLookAndFeel;

/**
 * This look and feel class is extended from SynthLookAndFeel, can be used as a template 
 * for exporting look and feel bundle.  It will load the synth XML definition file at the
 * same package.
 * 
 * @author EASYNTH
 */
public class WoodLookAndFeel extends SynthLookAndFeel
{

	public static String LAF_CLASS = "com.cybernostics.laf.wood.WoodLookAndFeel";

	public static void setUI()
	{
		try
		{
			AkbarFont.get( 24 );
			UIManager.setLookAndFeel( LAF_CLASS );
		}
		catch (Exception e)
		{
			throw new Error( "Failed to set Wood Look and Feel", e );
		}

	}

	private static final long serialVersionUID = 6942508771080867071L;
	private String synthXml;

	public WoodLookAndFeel()
	{
		super();
		try
		{
			// Load the "easynth.xml" file as the synth XML definition file
			load(
				getClass().getResourceAsStream(
					"wood-laf.xml" ),
				getClass() );
		}
		catch (Exception e)
		{
			throw new Error( e );
		}
	}

	/**
	 * Return the description of the look and feel.  This description can be customized 
	 * from the export GUI in Wood Look And Feel Designer. 
	 * 
	 * @return 
	 * 		the description
	 */
	@Override
	public String getDescription()
	{
		return "Wood Synth Look and Feel, based loosely on Wood Look and Feel.";
	}

	/**
	 * Return the simple class name as the ID of the look and feel.  The class name can be
	 * customized from the export GUI in Wood Look And Feel Designer. 
	 * 
	 * @return 
	 * 		the ID
	 */
	@Override
	public String getID()
	{
		return getClass().getSimpleName();
	}

	/**
	 * Return the simple class name as the name of the look and feel.  The class name can be
	 * customized from the export GUI in Wood Look And Feel Designer. 
	 * 
	 * @return 
	 * 		the name
	 */
	@Override
	public String getName()
	{
		return getClass().getSimpleName();
	}

	/**
	 * Return true if this look and feel is a native look and feel.
	 * 
	 * @return 
	 * 		always return false
	 */
	@Override
	public boolean isNativeLookAndFeel()
	{
		return false;
	}

	/**
	 * Return true if this look and feel is supported.
	 * 
	 * @return 
	 * 		always return true
	 */
	@Override
	public boolean isSupportedLookAndFeel()
	{
		return true;
	}

	/**
	 * Set the default font for this look and feel
	 */
	public void setDefaultFont( String fontName,
		int size,
		boolean isBold,
		boolean isItalic )
	{
		final StringBuilder replaceBuf = new StringBuilder();
		replaceBuf.append( "$1" );
		replaceBuf.append( fontName );
		replaceBuf.append( "$3" );
		replaceBuf.append( size );
		replaceBuf.append( "$5" );
		if (isBold || isItalic)
		{
			replaceBuf.append( " style=\"" );
			if (isBold)
			{
				replaceBuf.append( "BOLD" );
				if (isItalic)
				{
					replaceBuf.append( ' ' );
				}
			}
			if (isItalic)
			{
				replaceBuf.append( "ITALIC" );
			}
			replaceBuf.append( "\"" );
		}
		replaceBuf.append( "$7" );
		replaceXmlSegment(
				"(<style id=\"Default\">\\s*<font name=\")([^\"]*)(\" size=\")([^\"]*)(\")([^/]*)(/>)",
				replaceBuf.toString() );
	}

	/**
	 * Replace the specify segment in Synth XML (easynth.xml), the change will be applied immediately.
	 * 
	 * This method is suitable when we want to replace one segment.
	 * 
	 * @param regex
	 *		The regular expression to match the segment to replace
	 * @param replacement
	 * 		The content to replace the segment
	 */
	public void replaceXmlSegment( String regex, String replacement )
	{
		// modify in cache
		replaceXmlSegmentInCache(
			regex,
			replacement );
		// apply the changes
		applyCachedSynthXml();
	}

	/**
	 * Replace the specify segment in Synth XML (easynth.xml), the new data will not 
	 * take effect, until invoking the applyCachedSynthXml() method.
	 * 
	 * This method is suitable when we need to replace multiple segements.
	 * 
	 * @param regex
	 *		The regular expression to match the segment to replace
	 * @param replacement
	 * 		The content to replace the segment
	 */
	public void replaceXmlSegmentInCache( String regex, String replacement )
	{
		// get the Synth xml string
		synchronized (this)
		{
			if (synthXml == null)
			{
				synthXml = getSynthXmlAsString();
			}
		}
		// replace the xml segment
		final Pattern p = Pattern.compile( regex );
		final Matcher m = p.matcher( synthXml );
		synthXml = m.replaceFirst( replacement );
	}

	/**
	 * Load the cached Synth xml into look and feel, all changes will take effect
	 */
	public void applyCachedSynthXml()
	{
		// apply the synth xml in cache
		try
		{
			InputStream is = new ByteArrayInputStream( synthXml.getBytes( "UTF-8" ) );
			load(
				is,
				getClass() );
		}
		catch (Exception e)
		{
			throw new Error( e );
		}
	}

	/**
	 * Load the synth xml as a string
	 * 
	 * @return
	 */
	public String getSynthXmlAsString()
	{
		final InputStream is = getClass().getResourceAsStream(
			"easynth.xml" );
		if (is != null)
		{
			try
			{
				final BufferedReader reader = new BufferedReader( new InputStreamReader( is,
					"UTF-8" ) );
				String line = null;
				final StringBuilder strBuf = new StringBuilder();
				do
				{
					line = reader.readLine();
					if (line != null)
					{
						strBuf.append( line.trim() );
						strBuf.append( System.getProperty( "line.separator" ) );
					}
				}
				while (line != null);
				String resourceStr = strBuf.toString();
				return resourceStr;
			}
			catch (Exception e)
			{
				throw new Error( e );
			}
		}
		return null;
	}
}
