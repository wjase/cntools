package com.cybernostics.lib.media;

import com.cybernostics.lib.Application.AppResources;
import com.cybernostics.lib.exceptions.AppExceptionManager;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;
import javax.swing.SwingUtilities;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.io.JarUrlFix;

public class MidiPlayer implements MetaEventListener
{

	public static void main( String[] args )
	{

		SwingUtilities.invokeLater( new Runnable()
		{

			@Override
			public void run()
			{
				MidiPlayer p = null;
				try
				{
					p = new MidiPlayer( "sound/drawmusic.mid", 0.1 );
					// p = new MidiPlayer(
					// "animations/sendrecieve/sounds/kookaburra.mid", 0.7 );
					Thread.sleep( 200 );
				}
				catch (Exception e1)
				{
					// TODO Auto-generated catch block
					UnhandledExceptionManager.handleException( e1 );
				}

				if (p != null)
				{
					p.play();

					p.setVolumeLater();

				}
				try
				{
					Thread.sleep( 5000 );
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					UnhandledExceptionManager.handleException( e );
				}

				System.exit( 0 );

			}

		} );
	}

	private Sequence midiSequence;

	double volume;
	// midi meta-event constant used to signal the end of a track

	private static final int END_OF_TRACK = 47;

	private static final int RESET = 88;

	private static final int VOLUME_CONTROLLER = 7;

	private Sequencer sequencer;

	private Synthesizer synthesizer;

	private String filename;
	// holds the synthesizer's channels

	private MidiChannel[] channels;

	public MidiPlayer( String soundFile, double volume ) throws Exception
	{

		URL fileURL = AppResources.getResource( soundFile );
		initPlayer(
			fileURL,
			volume );
	}

	public MidiPlayer( URL soundFile, double volume )
	{
		initPlayer(
			soundFile,
			volume );

	}

	private void initPlayer( URL toPlay, double volume )
	{
		if (toPlay == null)
		{
			midiSequence = null;
			return;
		}

		try
		{
			midiSequence = MidiSystem.getSequence( JarUrlFix.getURLStream( toPlay ) );
			this.volume = volume;
		}
		catch (Exception e)
		{
			throw new RuntimeException( e );
		}

	}

	public void stop()
	{
		if (sequencer != null)
		{
			if (sequencer.isRunning())
			{
				sequencer.stop();
				sequencer.close();
				sequencer = null;
			}
		}
	}

	public void close()
	// Close down the sequencer and synthesizer
	{
		if (sequencer != null)
		{
			if (sequencer.isRunning())
			{
				sequencer.stop();
			}

			sequencer.removeMetaEventListener( this );
			sequencer.close();

			if (synthesizer != null)
			{
				synthesizer.close();
			}
		}
	} // end of close()

	private void initSequencer()
	/*
	 * Set up the MIDI sequencer, the sequencer's meta-event listener, and its
	 * synthesizer.
	 */
	{
		try
		{
			sequencer = obtainSequencer();

			if (sequencer == null)
			{
				AppExceptionManager.handleException(
					new Exception( "Cannot get a sequencer" ),
					getClass() );
			}

			sequencer.open();
			sequencer.addMetaEventListener( this );

			// maybe the sequencer is not the same as the synthesizer
			// so link sequencer --> synth (this is required in J2SE 1.5)
			if (!( sequencer instanceof Synthesizer ))
			{
				// System.out.println(
				// "Linking the MIDI sequencer and synthesizer" );
				synthesizer = MidiSystem.getSynthesizer();
				synthesizer.open(); // new
				Receiver synthReceiver = synthesizer.getReceiver();
				Transmitter seqTransmitter = sequencer.getTransmitter();
				seqTransmitter.setReceiver( synthReceiver );
			}
			else
			{
				synthesizer = (Synthesizer) sequencer;
			}
		}
		catch (MidiUnavailableException e)
		{
			// TODO : propagate issue
			System.out.println( "No sequencer available" );
		}
	} // end of initSequencer()

	public void meta( MetaMessage event )
	/*
	 * Meta-events trigger this method. The end-of-track meta-event signals that
	 * the sequence has finished
	 */
	{
		if (event.getType() == END_OF_TRACK)
		{
			System.out.println( "EndOfTrack" );
		}
		if (event.getType() == RESET)
		{
			setVolumeLater();
		}
		if (event.getType() != 5)
		{
			// System.out.println( event.getType() );
		}

	} // end of meta()

	private Sequencer obtainSequencer()
	/*
	 * This method handles a bug in J2SE 1.5.0 which retrieves the sequencer
	 * with getSequencer() but does not allow its volume to be changed.
	 */
	{
		// return MidiSystem.getSequencer();
		// okay in J2SE 1.4.2, but not in J2SE 1.5.0

		MidiDevice.Info[] mdi = MidiSystem.getMidiDeviceInfo();
		int seqPosn = -1;
		for (int i = 0; i < mdi.length; ++i)
		{
			// System.out.println( mdi[ i ].getName() );
			// if (mdi[i].getName().contains("Sequencer")) {
			if (mdi[ i ].getName()
				.indexOf(
					"Sequencer" ) != -1)
			{
				seqPosn = i; // found the Sequencer
				// System.out.println( "  Found Sequencer" );
			}
		}

		try
		{
			if (seqPosn != -1)
			{
				return (Sequencer) MidiSystem.getMidiDevice( mdi[ seqPosn ] );
			}
			else
			{
				return null;
			}
		}
		catch (MidiUnavailableException e)
		{
			return null;
		}
	} // end of obtainSequencer()

	public void play()
	{
		initSequencer();
		if (( sequencer != null ) && ( midiSequence != null ))
		{
			try
			{
				sequencer.setSequence( midiSequence ); // load MIDI into
				// sequencer
				sequencer.setLoopCount( Sequencer.LOOP_CONTINUOUSLY );
				sequencer.start(); // play it
				setVolumeLater();

				// showChannelVolumes();
			}
			catch (InvalidMidiDataException e)
			{
				// TODO: Log error
				System.out.println( "Corrupted/invalid midi file: " + filename );
			}
			catch (IllegalStateException ise)
			{
			}
		}
	} // end of play()

	public double getVolume()
	{
		return volume;
	}

	public void setVolume( double vol )
	// set all the controller's volume levels to vol
	{
		// System.out.println( "Set volumee to " + vol );
		final int MAX_VOLUME = 127;
		int intVolume = (int) ( MAX_VOLUME * vol );

		if (channels == null)
		{
			channels = synthesizer.getChannels();
		}
		for (int i = 0; i < channels.length; i++)
		{
			// System.out.println( synthesizer.isOpen() );
			channels[ i ].controlChange(
				VOLUME_CONTROLLER,
				intVolume );
		}

		// showChannelVolumes();
		volume = vol;
	}

	private void setVolumeLater()
	{
		TimerTask volSetter = new TimerTask()
		{

			@Override
			public void run()
			{
				setVolume( volume );

			}

		};

		Timer volSchedule = new Timer();
		// volSchedule.schedule( volSetter, 1 );
		volSchedule.schedule(
			volSetter,
			1 );
	}

}
