package com.cybernostics.lib.gui.windowcore;

import com.cybernostics.lib.gui.declarative.events.WhenPropertyChanges;
import com.cybernostics.lib.gui.layout.RelativeLayout;
import com.cybernostics.lib.gui.shapeeffects.ShapedPanel;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

/**
 * Implements a web2 style dialog which sits atop a full-screen translucent pane
 *
 * @author jasonw
 */
public class WebDialog extends ShapedPanel
	implements
	HasResponse,
	SupportsTransition
{

	private static Rectangle2D defaultBounds = new Rectangle2D.Double( 0.2,
		0.2,
		0.6,
		0.6 );

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

		WebDialog wd = new WebDialog( diag, defaultBounds );
		return wd.showDlg();
	}

	public static DialogResponses getConfirmation( String Question,
		String Title,
		final URL audioQuestion,
		boolean showCancel )
	{
		DialogPanel diag = new DialogPanel( Question, Title, audioQuestion,
			showCancel ? DialogPanel.DialogTypes.OK_CANCEL
				: DialogPanel.DialogTypes.OK );
		diag.setBorder( BorderFactory.createEmptyBorder(
			40,
			40,
			40,
			40 ) );

		WebDialog wd = new WebDialog( diag, defaultBounds );
		return wd.showDlg();
	}

	public static DialogResponses getConfirmation( JComponent toDisplay,
		String Title,
		final URL audioQuestion,
		boolean showCancel )
	{
		DialogPanel diag = new DialogPanel( toDisplay, Title, audioQuestion,
			showCancel ? DialogPanel.DialogTypes.OK_CANCEL
				: DialogPanel.DialogTypes.OK );
		diag.setBorder( BorderFactory.createEmptyBorder(
			40,
			40,
			40,
			40 ) );

		WebDialog wd = new WebDialog( diag, defaultBounds );
		return wd.showDlg();
	}

	private JComponent dialog = null;

	private boolean isDone = false;

	CountDownLatch doneLatch = new CountDownLatch( 1 );

	public WebDialog( JComponent dialogComponent, Rectangle2D bounds )
	{

		setLayout( new RelativeLayout() );

		add(
			dialogComponent,
			bounds );
		//		ComponentSprite dialogSprite = new ComponentSprite( dialogComponent );
		//		dialogSprite.setRelativeBounds( bounds );
		//		dialogSprite.setVisible( true );
		//		dialogSprite.setZ_order( 10 );
		//
		//		addSprites( dialogSprite );
		this.dialog = dialogComponent;

	}

	FocusListener focusgrabber = new FocusListener()
	{

		@Override
		public void focusGained( FocusEvent e )
		{
		}

		@Override
		public void focusLost( FocusEvent e )
		{
			if (topMost)
			{
				dialog.requestFocusInWindow();
			}
		}

	};

	public DialogResponses showDlg()
	{

		EventConsumer.apply( WebDialog.this );
		dialog.setVisible( true );
		ScreenStack.get()
			.pushScreen(
				WebDialog.this,
				null );
		dialog.requestFocusInWindow();

		new WhenPropertyChanges( "closeRequested", dialog )
		{

			@Override
			public void doThis( PropertyChangeEvent event )
			{
				ScreenStack.get()
					.popScreen();
			}

		};

		// The following code does the event displatch loop so it can
		// be called modally within the EDT and not cause and other UI
		// to get stuck.
		EventQueue queue = Toolkit.getDefaultToolkit()
			.getSystemEventQueue();
		try
		{
			while (!isDone)
			{
				if (EventQueue.isDispatchThread())
				{
					// The getNextEventMethod() issues wait() when no
					// event is available, so we don't need do explicitly wait().
					AWTEvent ev = queue.getNextEvent();
					// This mimics EventQueue.dispatchEvent(). We can't use
					// EventQueue.dispatchEvent() directly, because it is
					// protected, unfortunately.
					if (ev instanceof ActiveEvent)
					{
						( (ActiveEvent) ev ).dispatch();
					}
					else
						if (ev.getSource() instanceof Component)
						{
							( (Component) ev.getSource() ).dispatchEvent( ev );
						}
						else
							if (ev.getSource() instanceof MenuComponent)
							{
								( (MenuComponent) ev.getSource() ).dispatchEvent( ev );
							}
					// Other events are ignored as per spec in
					// EventQueue.dispatchEvent
				}
				else
				{
					// Give other threads a chance to become active.
					doneLatch.await();
				}
			}
		}
		catch (InterruptedException ex)
		{
			// If we get interrupted, then leave the modal state.
		}

		return getResponse();
	}

	private DialogResponses response = null;

	@Override
	public DialogResponses getResponse()
	{
		return response;
	}

	@Override
	public void doWhenPushed( TransitionCompleteListener listener )
	{
		if (dialog instanceof SupportsTransition)
		{
			( (SupportsTransition) dialog ).doWhenPushed( listener );
		}
		else
		{
			if (listener != null)
			{
				listener.transitionComplete();
			}

		}
		dialog.addFocusListener( focusgrabber );
	}

	boolean topMost = false;

	@Override
	public void doWhenPopped( TransitionCompleteListener listener )
	{
		if (dialog instanceof SupportsTransition)
		{
			( (SupportsTransition) dialog ).doWhenPopped( listener );
		}
		else
		{
			if (listener != null)
			{
				listener.transitionComplete();
			}
		}
		dialog.removeFocusListener( focusgrabber );
		isDone = true;
		doneLatch.countDown();
	}

	@Override
	public void doWhenObscured( TransitionCompleteListener listener )
	{
		dialog.removeFocusListener( focusgrabber );
	}

	@Override
	public void doWhenRevealed( TransitionCompleteListener listener )
	{
		dialog.addFocusListener( focusgrabber );
	}

}
