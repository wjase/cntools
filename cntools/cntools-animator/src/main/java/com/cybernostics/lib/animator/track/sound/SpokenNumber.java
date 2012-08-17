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

package com.cybernostics.lib.animator.track.sound;

import com.cybernostics.lib.Application.AppResources;
import com.cybernostics.lib.animator.track.BasicTrack;
import com.cybernostics.lib.concurrent.TimeStamp;
import com.cybernostics.lib.gui.declarative.events.WhenPropertyChanges;
import com.cybernostics.lib.media.SoundEffect;
import com.cybernostics.lib.resourcefinder.Finder;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import java.beans.PropertyChangeEvent;

/**
 * @author jasonw
 * 
 */
public class SpokenNumber extends BasicTrack
{

	private Finder loader = AppResources.getFinder();
	// static
	// {
	// 
	// }
	// TODO : put this into test classes with resources
	// public static void main( String[] args )
	// {
	//		
	// Sequencer seq = new Sequencer(50);
	//		
	// for( int i = 61; i < 71; ++i)
	// {
	// seq.addAndStartTrack( new SpokenNumber(i) );
	// seq.waitTillDone();
	// try
	// {
	// Thread.sleep( 300 );
	// }
	// catch ( InterruptedException e )
	// {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//		
	// System.exit( 0 );
	//		
	// }

	private SoundEffect tensPart = null;

	private SoundEffect onesPart = null;

	private SoundEffect activeEffect = null;

	String numberFilePattern = "speech/numbers/%d.mp3";

	/**
	 * 
	 * @param pat
	 */
	public void setResourcePattern( String pat )
	{
		numberFilePattern = pat;
	}

	/**
	 * 
	 * @param numToSay
	 */
	public SpokenNumber( int numToSay ) throws ResourceFinderException
	{
		super( "Spoken Number:" + numToSay );
		say(
			numToSay,
			numberFilePattern );
	}

	/**
	 * 
	 * @param numToSay
	 * @param numberClipPattern
	 */
	public SpokenNumber( int numToSay, String numberClipPattern )
		throws ResourceFinderException
	{
		super( "Spoken Number:" + numToSay );
		say(
			numToSay,
			numberClipPattern );
	}

	/**
	 * 
	 * @param numToSay
	 * @param numberClipPattern
	 */
	public void say( int numToSay, String numberClipPattern )
		throws ResourceFinderException
	{
		if (numToSay < 20)
		{
			onesPart = new SoundEffect( loader.getResource( String.format(
				numberFilePattern,
				numToSay ) ), 0.9 );
		}
		else
		{
			int onesNum = numToSay % 10;
			int tensNum = numToSay - onesNum;
			if (onesNum > 0)
			{
				onesPart = new SoundEffect( loader.getResource( String.format(
					numberFilePattern,
					onesNum ) ), 0.9 );
			}
			tensPart = new SoundEffect( loader.getResource( String.format(
				numberFilePattern,
				tensNum ) ), 0.9 );
		}

		// link the bits so they flow together
		if (tensPart != null)
		{
			// 21,31,41... has a tens bit plus a ones bit
			if (onesPart != null)
			{
				new WhenPropertyChanges( SoundEffect.STOPPED,
					onesPart.getPropertySupport() )
				{

					@Override
					public void doThis( PropertyChangeEvent event )
					{
						SpokenNumber.this.stop( true );
						activeEffect = null;
					}

				};
				new WhenPropertyChanges( SoundEffect.STOPPED,
					tensPart.getPropertySupport() )
				{

					@Override
					public void doThis( PropertyChangeEvent event )
					{
						onesPart.play();
						activeEffect = onesPart;

					}

				};
			}
			else
			{
				// no ones twenty, thirty, forty etc
				new WhenPropertyChanges( SoundEffect.STOPPED,
					tensPart.getPropertySupport() )
				{

					@Override
					public void doThis( PropertyChangeEvent event )
					{
						SpokenNumber.this.stop( true );
						activeEffect = null;

					}

				};
			}
		}
		else
		{
			// 1-19
			new WhenPropertyChanges( SoundEffect.STOPPED,
				onesPart.getPropertySupport() )
			{

				@Override
				public void doThis( PropertyChangeEvent event )
				{
					SpokenNumber.this.stop( true );
					activeEffect = null;
				}

			};

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cybernostics.lib.animator.track.BasicTrack#start()
	 */
	@Override
	public void start()
	{
		if (tensPart != null)
		{
			tensPart.play();
			activeEffect = tensPart;
		}
		else
		{
			if (onesPart != null)
			{
				onesPart.play();
				activeEffect = onesPart;
			}
		}
		super.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cybernostics.lib.animator.track.Track#update(com.cybernostics.lib
	 * .animator.TimeStamp)
	 */
	@Override
	public void update( TimeStamp timeCode )
	{
	}

	/**
	 * @param loader
	 *            the loader to set
	 */
	public void setLoader( ResourceFinder loader )
	{
		this.loader = loader;
	}

	/**
	 * @return the loader
	 */
	public Finder getLoader()
	{
		if (loader == null)
		{
			loader = AppResources.getFinder();
		}
		return loader;
	}

	@Override
	public void pause()
	{
		throw new UnsupportedOperationException( "Not supported yet." );
	}

	@Override
	public void resume()
	{
		throw new UnsupportedOperationException( "Not supported yet." );
	}

}
