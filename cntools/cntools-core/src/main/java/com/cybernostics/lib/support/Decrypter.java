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

public class Decrypter extends Cyph
{

	public Decrypter( String keyStr )
	{
		super( keyStr );
		// TODO Auto-generated constructor stub
	}

	/**
	 * Decode a String encoded with encr().
	 * 
	 * @param sCyph
	 *            a <code>String</code> value
	 * @return a <code>String</code> value
	 * @see encr()
	 */
	public String deco( String sCyph )
	{
		if (( sCyph.length() == 0 ) || ( sCyph.charAt( 0 ) != '[' ))
		{
			return sCyph;
		}
		if (sCyph.equals( "[]" ))
		{
			return "";
		}

		// System.out.println( sCyph );

		byte[] baCyph = fromStringBlock( sCyph );
		byte[] baDeco = new byte[ baCyph.length ];

		if (baCyph.length == 0)
		{
			return sCyph;
		}

		for (int iCurrentOffset = 0; iCurrentOffset < baCyph.length; iCurrentOffset += 8)
		{
			decrypt(
				baCyph,
				iCurrentOffset,
				baDeco,
				iCurrentOffset );
		}

		StringBuffer sbTemp = new StringBuffer( new String( baDeco ) );

		while (sbTemp.charAt( sbTemp.length() - 1 ) == '%')
		{
			sbTemp.deleteCharAt( sbTemp.length() - 1 );
		}

		return sbTemp.toString();
	}

	// / Decrypt a block of bytes.
	public void decrypt( byte[] cipherText, byte[] clearText )
	{
		decrypt(
			cipherText,
			0,
			clearText,
			0 );
	}

	public void decrypt( byte[] cipherText,
		int cipherOff,
		byte[] clearText,
		int clearOff )
	{
		squashBytesToInts(
			cipherText,
			cipherOff,
			tempInts,
			0,
			2 );
		des(
			tempInts,
			tempInts,
			decryptKeys );
		spreadIntsToBytes(
			tempInts,
			0,
			clearText,
			clearOff,
			2 );
	}

	@Override
	public String process( String source )
	{
		return deco( source );
	}

}
