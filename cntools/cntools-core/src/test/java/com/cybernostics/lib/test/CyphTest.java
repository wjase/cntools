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
/**
 * 
 */

package com.cybernostics.lib.test;

import org.junit.Assert;
import org.junit.Test;

import com.cybernostics.lib.support.Decrypter;
import com.cybernostics.lib.support.Encrypter;

/**
 * @author jasonw
 * 
 */
public class CyphTest
{

	/**
	 * Test method for
	 * {@link com.cybernostics.lib.support.Encrypter#process(java.lang.String)}.
	 */
	@Test
	public void testProcess()
	{
		Encrypter c = new Encrypter( "MyKey789" );
		String sBefore = "A Text String to Encode";
		System.out.println( "Before:" + sBefore );
		String sEncoded = c.process( sBefore );
		System.out.println( "Coded:" + sEncoded );
		Decrypter dc = new Decrypter( "MyKey789" );
		String sAfter = dc.process( sEncoded );
		System.out.println( "After:" + sAfter );
		Assert.assertEquals(
			"Decrypted equals encrypted",
			sBefore,
			sAfter );
	}

}
