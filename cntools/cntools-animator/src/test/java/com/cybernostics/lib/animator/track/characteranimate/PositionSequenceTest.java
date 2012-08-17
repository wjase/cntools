/*
 * #%L cntools-animator %% Copyright (C) 2012 Cybernostics Pty Ltd %% Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. #L%
 */

package com.cybernostics.lib.animator.track.characteranimate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import com.cybernostics.lib.persist.xml.ObjectSerialiser;

/**
 * @author jasonw
 * 
 */
public class PositionSequenceTest
{
	@Test
	public void loadSequenceTest()
	{
		PositionSequence ms = new PositionSequence();
		ms.addEvent(
			"head-front.mouth-open",
			300 );
		ms.addEvent(
			"head-front.mouth-closed",
			500 );
		ms.addEvent(
			"head-front.mouth-mmm",
			1500 );

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try
		{
			ObjectSerialiser.jaxbWriteObjectAsXML(
				ms,
				bos );
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String streamWrite1 = bos.toString();

		ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
		try
		{
			ObjectSerialiser.jaxbWriteObjectAsXML(
				ms,
				bos2 );
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		String streamWrite2 = bos2.toString();

		Assert.assertEquals(
			streamWrite1,
			streamWrite2 );

		System.out.printf(
			"%s\n",
			streamWrite1 );

	}

}
