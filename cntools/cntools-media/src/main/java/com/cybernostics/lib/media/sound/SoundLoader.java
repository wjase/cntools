package com.cybernostics.lib.media.sound;

import com.cybernostics.lib.io.JarUrlFix;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author jasonw
 */
public class SoundLoader
{

	/**
	 * Loaded the clip from the specified URL
	 * 
	 * @param location
	 * @return
	 * @throws Exception
	 */
	public static Clip loadClip( URL location ) throws Exception
	{
		if (location == null)
		{

			throw new NullPointerException();
		}

		Clip theClip = null;

		AudioInputStream stream = null;

		if (theClip == null)
		{
			try
			{
				// link an audio stream to the sound clip's file
				stream = AudioSystem.getAudioInputStream( JarUrlFix.getURLStream( location ) );

				AudioFormat format = stream.getFormat();

				// Convert compressed audio data to uncompressed PCM format.
				if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED)
				{
					AudioFormat newFormat = new AudioFormat( AudioFormat.Encoding.PCM_SIGNED,
						format.getSampleRate(),
						16,
						format.getChannels(),
						format.getChannels() * 2,
						format.getSampleRate(),
						false );
					// System.out.println( "Converting audio format to " +
					// newFormat );
					AudioInputStream newStream = AudioSystem.getAudioInputStream(
						newFormat,
						stream );
					format = newFormat;
					stream = newStream;
				}

				DataLine.Info info = new DataLine.Info( Clip.class, format );

				// make sure sound system supports data line
				if (!AudioSystem.isLineSupported( info ))
				{
					throw new IOException( "Unsupported Clip File: "
						+ location.toString() );
				}

				// get clip line resource
				theClip = (Clip) AudioSystem.getLine( info );

				// listen to clip for events
				// theClip.addLineListener( this );
				theClip.open( stream ); // open the sound file as a clip

				theClip.setFramePosition( 0 );
				// clip.setMicrosecondPosition(0);

				//alreadyLoadedClips.put( location.toString(), theClip );
			} // end of try block
			catch (UnsupportedAudioFileException audioException)
			{
				throw new Exception( "Unsupported audio file: "
					+ location.toString(), audioException );
			}
			catch (LineUnavailableException noLineException)
			{
				throw new Exception( "No audio line available for : "
					+ location.toString(), noLineException );
			}
			catch (IOException ioException)
			{
				throw new Exception( "Could not read: " + location.toString(),
					ioException );
			}
			catch (Exception e)
			{
				throw new Exception( "Could not read: " + location.toString(),
					e );
			}
			finally
			{
				if (stream != null)
				{
					stream.close(); // we're done with the input stream // new
				}

			}
		}

		return theClip;
	}
}
