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

public class Encrypter extends Cyph
{

	public Encrypter( String keyStr )
	{
		super( keyStr );
		// TODO Auto-generated constructor stub
	}

	/**
	 * Encypt the string with the key.
	 * 
	 * @param sPlain
	 *            a <code>String</code> value containinf the string to encode
	 * @return a <code>String</code> value with the encoded string
	 */
	public String encr( String sPlain )
	{
		StringBuffer sbPlain = new StringBuffer( sPlain );

		// pad to 8 byte boundary with # char
		String sPad = "%%%%%%%%";
		int iTrailingChars = ( sbPlain.length() % 8 );
		if (iTrailingChars > 0)
		{
			sbPlain.append( sPad.substring( iTrailingChars ) );
		}

		byte[] baPlain = sbPlain.toString()
			.getBytes();
		// Now allocate a buffer to accomodate the encrypted string of bytes
		byte[] baCyph = new byte[ sbPlain.length() ];

		// Now encrypt the string in 8-byte blocks
		// int iBlocks = sbPlain.length() / 8;

		for (int iCurrentOffset = 0; iCurrentOffset < sbPlain.length(); iCurrentOffset += 8)
		{
			encrypt(
				baPlain,
				iCurrentOffset,
				baCyph,
				iCurrentOffset );
		}

		return toStringBlock( baCyph );
	}

	public void encrypt( byte[] clearText, byte[] cipherText )
	{
		encrypt(
			clearText,
			0,
			cipherText,
			0 );
	}

	public void encrypt( byte[] clearText,
		int clearOff,
		byte[] cipherText,
		int cipherOff )
	{
		squashBytesToInts(
			clearText,
			clearOff,
			tempInts,
			0,
			2 );
		des(
			tempInts,
			tempInts,
			encryptKeys );
		spreadIntsToBytes(
			tempInts,
			0,
			cipherText,
			cipherOff,
			2 );
	}

	@Override
	public String process( String source )
	{
		return encr( source );
	}
}
