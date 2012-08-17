package com.cybernostics.lib.gui.dialogs;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.gui.declarative.events.WhenHiddenOrClosed;
import com.cybernostics.lib.gui.declarative.events.WhenPropertyChanges;
import com.cybernostics.lib.gui.windowcore.DialogPanel;
import com.cybernostics.lib.gui.windowcore.DialogResponses;
import com.cybernostics.lib.gui.windowcore.WebDialog;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class DialogBox extends JDialog
{

	public static int getValue( final JPanel toShow )
	{
		DialogBox jdDialog = new DialogBox();
		jdDialog.setContentPane( toShow );
		jdDialog.setVisible( true );

		return 0;
	}

	public static DialogResponses getValue( final JComponent content,
		String title )
	{
		DialogShower shower = new DialogShower( content, title );
		try
		{
			if (SwingUtilities.isEventDispatchThread())
			{
				shower.run();
			}
			else
			{
				SwingUtilities.invokeAndWait( shower );
			}

		}
		catch (InterruptedException e)
		{
			UnhandledExceptionManager.handleException( e );
		}
		catch (InvocationTargetException e)
		{
			UnhandledExceptionManager.handleException( e );
		}

		return shower.theResponse;
	}

	public static DialogResponses getYesNoResponse( String Question,
		String Title,
		final URL audioQuestion,
		boolean showCancel )
	{
		DialogPanel diag = new DialogPanel( Question, Title, audioQuestion,
			showCancel ? DialogPanel.DialogTypes.YES_NO_CANCEL
				: DialogPanel.DialogTypes.YES_NO );
		diag.setBorder( BorderFactory.createEmptyBorder(
			40,
			40,
			40,
			40 ) );
		return getValue(
			diag,
			Title );
	}

	private DialogResponses DialogResponse;

	/**
	 *
	 */
	private static final long serialVersionUID = -1878226500161639880L;

	public DialogBox()
	{
		setModalityType( ModalityType.DOCUMENT_MODAL );
		setUndecorated( true );

	}

	;

	public DialogResponses GetResponse()
	{
		return DialogResponse;
	}

}

class DialogShower implements Runnable
{

	DialogResponses theResponse;

	JComponent content;

	String dialogTitle;

	DialogShower( final JComponent content, String title )
	{
		this.content = content;
		this.dialogTitle = title;
	}

	@Override
	public void run()
	{

		final DialogBox jdDialog = new DialogBox()
		{

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			/*
			 * (non-Javadoc) @see java.awt.Container#paint(java.awt.Graphics)
			 */
			@Override
			public void paint( Graphics g )
			{
				paintComponents( g );

			}

		};

		jdDialog.addWindowListener( new WindowAdapter()
		{/*
			 * (non-Javadoc) @see
			 * java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
			 */

			@Override
			public void windowOpened( WindowEvent e )
			{
				Window w = e.getWindow();
				Shape s = new RoundRectangle2D.Double( 0,
					0,
					w.getWidth(),
					w.getHeight(),
					40,
					40 );
				// TODO: Update this when supporting java 7 as a minimum
				com.sun.awt.AWTUtilities.setWindowShape(
					w,
					s );

			}

		} );

		jdDialog.setContentPane( content );

		if (dialogTitle != null)
		{
			jdDialog.setTitle( dialogTitle );
		}
		else
		{
			jdDialog.setUndecorated( true );
		}
		jdDialog.pack();
		jdDialog.setLocationRelativeTo( null );

		if (content instanceof DialogPanel)
		{
			new WhenPropertyChanges( "closeRequested", content )
			{

				@Override
				public void doThis( PropertyChangeEvent event )
				{
					jdDialog.setVisible( false );
					theResponse = ( (DialogPanel) content ).getResponse();
				}

			};

		}
		else
		{
			new WhenHiddenOrClosed( content )
			{

				@Override
				public void doThis()
				{
					jdDialog.setVisible( false );
					theResponse = (DialogResponses) content.getClientProperty( "response" );
					if (content instanceof WebDialog)
					{
						theResponse = ( (WebDialog) content ).getResponse();
					}
				}

			};
		}
		jdDialog.setVisible( true );
	}

}
