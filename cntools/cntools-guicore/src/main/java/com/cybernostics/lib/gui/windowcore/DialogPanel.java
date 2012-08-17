package com.cybernostics.lib.gui.windowcore;

import com.cybernostics.lib.gui.ButtonFactory;
import com.cybernostics.lib.gui.IconFactory;
import com.cybernostics.lib.gui.IconFactory.StdButtonType;
import com.cybernostics.lib.gui.declarative.events.WhenMadeVisible;
import com.cybernostics.lib.gui.shapeeffects.ShapedPanel;
import com.cybernostics.lib.media.SoundEffect;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.*;

/**
 *
 * @author jasonw
 */
public class DialogPanel extends ShapedPanel implements SupportsTransition
{

	private ActionListener getResponse = new ActionListener()
	{

		@Override
		public void actionPerformed( ActionEvent e )
		{
			response = (DialogResponses) ( (JComponent) e.getSource() ).getClientProperty( "response" );
			firePropertyChange(
				"closeRequested",
				false,
				true );
		}

	};

	private JButton makeButton( String text,
		StdButtonType stdButtonType,
		DialogResponses answerType )
	{
		JButton btn = ButtonFactory.getStdButton(
			stdButtonType,
			null );
		btn.setName( text );
		btn.putClientProperty(
			"response",
			answerType );
		btn.addActionListener( getResponse );
		return btn;
	}

	public enum DialogTypes
	{

		YES_NO, YES_NO_CANCEL, OK, OK_CANCEL
	}

	private DialogResponses response = null;

	public DialogResponses getResponse()
	{
		return response;
	}

	private SoundEffect audioQuery = null;

	public DialogPanel(
		String prompt,
		String Title,
		final URL audioQuestion,
		DialogTypes dialog_type )
	{
		this( new JLabel( prompt ), Title, audioQuestion, dialog_type );
	}

	public DialogPanel(
		JComponent prompt,
		String Title,
		final URL audioQuestion,
		DialogTypes diaog_type )
	{

		setLayout( new BorderLayout() );

		if (audioQuestion != null)
		{
			audioQuery = new SoundEffect( audioQuestion );

			new WhenMadeVisible( this )
			{

				@Override
				public void doThis( AWTEvent e )
				{
					audioQuery.play();
				}

			};

		}

		prompt.setAlignmentX( 0.5f );
		add(
			prompt,
			BorderLayout.CENTER );

		JPanel buttons = new JPanel();
		buttons.setOpaque( false );
		BoxLayout bl = new BoxLayout( buttons, BoxLayout.X_AXIS );
		buttons.setLayout( bl );
		buttons.add( Box.createHorizontalGlue() );

		switch (diaog_type)
		{
			case OK:
				buttons.add( makeButton(
					"OK",
					IconFactory.StdButtonType.YES,
					DialogResponses.OK_ANSWER ) );
				break;
			case OK_CANCEL:
				buttons.add( makeButton(
					"OK",
					IconFactory.StdButtonType.YES,
					DialogResponses.OK_ANSWER ) );
				buttons.add( Box.createHorizontalStrut( 10 ) );
				buttons.add( makeButton(
					"Cancel",
					IconFactory.StdButtonType.CANCEL,
					DialogResponses.CANCEL_ANSWER ) );
				break;
			case YES_NO:
				buttons.add( makeButton(
					"Yes",
					IconFactory.StdButtonType.YES,
					DialogResponses.YES_ANSWER ) );
				buttons.add( Box.createHorizontalStrut( 10 ) );
				buttons.add( makeButton(
					"No",
					IconFactory.StdButtonType.NO,
					DialogResponses.NO_ANSWER ) );
				break;
			case YES_NO_CANCEL:
				buttons.add( makeButton(
					"Yes",
					IconFactory.StdButtonType.YES,
					DialogResponses.YES_ANSWER ) );
				buttons.add( Box.createHorizontalStrut( 10 ) );
				buttons.add( makeButton(
					"No",
					IconFactory.StdButtonType.NO,
					DialogResponses.NO_ANSWER ) );
				buttons.add( Box.createHorizontalStrut( 10 ) );
				buttons.add( makeButton(
					"Cancel",
					IconFactory.StdButtonType.CANCEL,
					DialogResponses.CANCEL_ANSWER ) );
				break;
		}

		buttons.add( Box.createHorizontalGlue() );
		buttons.setBorder( BorderFactory.createEmptyBorder(
			10,
			10,
			10,
			10 ) );

		add(
			buttons,
			BorderLayout.SOUTH );

	}

	@Override
	public void doWhenPushed( TransitionCompleteListener listener )
	{
		setVisible( true );
		if (listener != null)
		{
			listener.transitionComplete();
		}

	}

	@Override
	public void doWhenPopped( TransitionCompleteListener listener )
	{
		setVisible( false );
		if (listener != null)
		{
			listener.transitionComplete();
		}

	}

	@Override
	public void doWhenObscured( TransitionCompleteListener listener )
	{
	}

	@Override
	public void doWhenRevealed( TransitionCompleteListener listener )
	{
	}

}
