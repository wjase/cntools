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

package com.cybernostics.lib.support;

import java.util.Map;
import java.util.Properties;

/**
 * @author jasonw
 * 
 */
public class PropertyParser
{
	Properties props = null;

	public PropertyParser( Properties toParse )
	{
		props = toParse;
	}

	/**
	 * @param props2
	 */
	public PropertyParser( Map< String, Object > props2 )
	{
		props = new Properties();
		props.putAll( props2 );
	}

	public boolean getBoolValue( String key, boolean defaultValue )
	{
		if (props == null)
		{
			return defaultValue;
		}
		if (props.containsKey( key ))
		{
			return Boolean.parseBoolean( props.getProperty( key ) );
		}
		else
		{
			return defaultValue;
		}
	}

	public String getStringValue( String key, String defaultValue )
	{
		if (props == null)
		{
			return defaultValue;
		}
		if (props.containsKey( key ))
		{
			return props.getProperty( key );
		}
		else
		{
			return defaultValue;
		}
	}

	public int getIntValue( String key, int defaultValue )
	{
		if (props == null)
		{
			return defaultValue;
		}
		if (props.containsKey( key ))
		{
			return Integer.parseInt( props.getProperty( key ) );
		}
		else
		{
			return defaultValue;
		}
	}

}
