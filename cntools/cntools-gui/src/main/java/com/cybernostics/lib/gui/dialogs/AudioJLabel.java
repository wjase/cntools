package com.cybernostics.lib.gui.dialogs;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.cybernostics.lib.media.SoundEffect;

public class AudioJLabel extends JLabel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 519016318528106807L;
	private SoundEffect audioPrompt = null;
	private TimerTask askQuestion;
	private Timer questionAsker = null;

	public AudioJLabel( Icon image, int horizontalAlignment )
	{
		super( image, horizontalAlignment );
		setupAudioListeners();
	}

	public AudioJLabel( Icon image )
	{
		super( image );
		setupAudioListeners();
	}

	public AudioJLabel( String text, Icon icon, int horizontalAlignment )
	{
		super( text, icon, horizontalAlignment );
		setupAudioListeners();
	}

	public AudioJLabel( String text, int horizontalAlignment )
	{
		super( text, horizontalAlignment );
		setupAudioListeners();
	}

	public AudioJLabel( String text )
	{
		super( text );
		setupAudioListeners();
	}

	public AudioJLabel()
	{
		setupAudioListeners();
	}

	private void setupAudioListeners()
	{

		addAncestorListener( new AncestorListener()
		{

			@Override
			public void ancestorAdded( AncestorEvent event )
			{
				askQuestion = new TimerTask()
				{

					@Override
					public void run()
					{
						if (audioPrompt != null)
						{
							audioPrompt.play();
						}
					}
				};
				questionAsker = new Timer();
				questionAsker.schedule(
					askQuestion,
					0,
					10000 );
			}

			@Override
			public void ancestorMoved( AncestorEvent event )
			{
			}

			@Override
			public void ancestorRemoved( AncestorEvent event )
			{
				questionAsker.cancel();
			}
		} );

	}

	public void setAudioPrompt( SoundEffect audioPrompt )
	{
		this.audioPrompt = audioPrompt;

	}

	public SoundEffect getAudioPrompt()
	{
		return audioPrompt;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable
	{

		super.finalize();
		questionAsker.cancel();
		questionAsker = null;
	}
}
