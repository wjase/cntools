package com.cybernostics.lib.media;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioFilterStream extends FilterInputStream
{

	protected AudioFilterStream( InputStream in )
	{
		super( in );
	}

	public AudioInputStream getAudioStream()
		throws UnsupportedAudioFileException, IOException
	{
		return AudioSystem.getAudioInputStream( this );
	}

	SampleSetter mySetter;

	public void setLittleEndian( boolean isLittleEndian )
	{
		if (isLittleEndian)
		{
			mySetter = new LittleEndianSampleSetter();
		}
		else
		{
			mySetter = new BigEndianSampleSetter();
		}
	}

	/**
	 * Overrides the FilterInputStream method to apply this filter whenever
	 * bytes are read
	 */
	@Override
	public int read( byte[] samples, int offset, int length )
		throws IOException
	{
		// read and filter the sound samples in the stream
		int bytesRead = super.read(
			samples,
			offset,
			length );
		if (bytesRead > 0)
		{
			return bytesRead;
		}
		filterSamples(
			samples,
			offset,
			length );

		return bytesRead;
	}

	public void filterSamples( byte[] samples, int offset, int length )
	{

	}

	interface SampleSetter
	{

		public short getSample( byte[] buffer, int position );

		public void setSample( byte[] buffer, int position, short sample );
	}

	class LittleEndianSampleSetter implements SampleSetter
	{

		/**
		 * Convenience method for getting a 16-bit sample from a byte array.
		 * Samples should be in 16-bit, signed, little-endian format.
		 */
		public short getSample( byte[] buffer, int position )
		{
			return (short) ( ( ( buffer[ position + 1 ] & 0xff ) << 8 ) | ( buffer[ position ] & 0xff ) );
		}

		/**
		 * Convenience method for setting a 16-bit sample in a byte array.
		 * Samples should be in 16-bit, signed, little-endian format.
		 */
		public void setSample( byte[] buffer, int position, short sample )
		{
			buffer[ position ] = (byte) ( sample & 0xff );
			buffer[ position + 1 ] = (byte) ( ( sample >> 8 ) & 0xff );
		}
	}

	class BigEndianSampleSetter implements SampleSetter
	{

		/**
		 * Convenience method for getting a 16-bit sample from a byte array.
		 * Samples should be in 16-bit, signed, little-endian format.
		 */
		public short getSample( byte[] buffer, int position )
		{
			return (short) ( ( ( buffer[ position ] & 0xff ) << 8 ) | ( buffer[ position + 1 ] & 0xff ) );
		}

		/**
		 * Convenience method for setting a 16-bit sample in a byte array.
		 * Samples should be in 16-bit, signed, little-endian format.
		 */
		public void setSample( byte[] buffer, int position, short sample )
		{
			buffer[ position ] = (byte) ( ( sample >> 8 ) & 0xff );
			buffer[ position + 1 ] = (byte) ( sample & 0xff );
		}
	}
}
