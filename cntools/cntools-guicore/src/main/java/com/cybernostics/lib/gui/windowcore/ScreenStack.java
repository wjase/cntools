package com.cybernostics.lib.gui.windowcore;

import com.cybernostics.lib.collections.ArrayUtils;
import com.cybernostics.lib.concurrent.RunnableObject;
import com.cybernostics.lib.gui.ParentSize;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;

/**
 * Usage: 1. Add the singleton instance (from get()) to your Frame. 2. Call
 * register to register one or more screen factories 3. Call pushScreen to show
 * the named screen
 *
 * @author jasonw
 */
public class ScreenStack
{

	public void pickScreen()
	{
		ScreenStack.get()
			.register(
				"picker",
				new SceneSelector() );
		ScreenStack.get()
			.showScreen(
				"picker" );
	}

	static class ScreenStackShower implements ActionListener
	{

		String screenToShow = null;

		ScreenStackShower( String screenName )
		{
			this.screenToShow = screenName;
		}

		@Override
		public void actionPerformed( ActionEvent e )
		{
			get().showScreen(
				screenToShow );
		}

	}

	public static ActionListener doShow( String games )
	{
		return new ScreenStackShower( games );
	}

	static class ScreenStackSwitcher implements ActionListener
	{

		String screenToShow = null;

		ScreenStackSwitcher( String screenName )
		{
			this.screenToShow = screenName;
		}

		@Override
		public void actionPerformed( ActionEvent e )
		{
			get().switchToScreen(
				screenToShow );
		}

	}

	public static ActionListener doSwitch( String games )
	{
		return new ScreenStackSwitcher( games );
	}

	private RunnableObject< String > noScreenAction = new RunnableObject< String >(
		null )
	{

		@Override
		public void run()
		{
			System.err.printf(
				"No Screen found to show with name %s\n",
				getObject() );
			System.out.printf(
				"Screens %s\n",
				ArrayUtils.implode(
					"\n",
					getScreenNames() ) );
			System.exit( 0 );
		}

	};

	private String nextScreenId = null;

	private static final TransitionCompleteListener NOPListener = new TransitionCompleteListener()
	{

		@Override
		public void transitionComplete()
		{
			// do nothing
		}

	};

	public String getNextScreenId()
	{
		return nextScreenId;
	}

	public void setnextScreenId( String screenId )
	{
		nextScreenId = screenId;
	}

	public void setNoScreenAction( RunnableObject< String > noScreenAction )
	{
		this.noScreenAction = noScreenAction;
	}

	private static final SingletonInstance< ScreenStack > theStack = new SingletonInstance< ScreenStack >()
	{

		@Override
		protected ScreenStack createInstance()
		{
			return new ScreenStack();
		}

	};

	public static ScreenStack get()
	{
		return theStack.get();
	}

	JLayeredPane layers = new JLayeredPane();

	public JLayeredPane getLayeredPanel()
	{
		return layers;
	}

	private int zOrder = 1;

	private Map< String, ScreenFactory > screenmakers = new HashMap< String, ScreenFactory >();

	public void register( String screenName, ScreenFactory maker )
	{
		screenmakers.put(
			screenName,
			maker );
	}

	public void register( String screenName, JComponent toReg )
	{
		screenmakers.put(
			screenName,
			new RetainedInstanceScreenFactory( toReg ) );
	}

	public void pushPanel( Component jp )
	{
		pushPanel(
			jp,
			NOPListener );
	}

	private void pushPanel( Component jp, TransitionCompleteListener whenDone )
	{
		final Component c = getTopmostComponent();

		ParentSize.bind(
			layers,
			jp );
		layers.add(
			jp,
			new Integer( ++zOrder ) );
		layers.revalidate();
		jp.validate();
		layers.repaint();
		jp.setFocusable( true );
		jp.requestFocusInWindow();

		if (jp instanceof SupportsTransition)
		{
			( (SupportsTransition) jp ).doWhenPushed( whenDone );
		}

		if (c != null)
		{
			c.setFocusable( false );
			if (c instanceof SupportsTransition)
			{
				( (SupportsTransition) c ).doWhenObscured( NOPListener );
			}
		}

	}

	public Component getTopmostComponent()
	{
		Component[] components = layers.getComponentsInLayer( zOrder );
		if (components.length > 0)
		{
			return components[ 0 ];
		}
		return null;
	}

	public Component popScreen()
	{
		final Component c = getTopmostComponent();

		c.setFocusable( false );
		if (c instanceof SupportsTransition)
		{
			( (SupportsTransition) c ).doWhenPopped( new TransitionCompleteListener()
			{

				@Override
				public void transitionComplete()
				{
					layers.remove( c );
					--zOrder;
					layers.revalidate();
					layers.repaint();
					EventQueue.invokeLater( openNext );
				}

			} );
		}
		else
		{
			layers.remove( c );
			--zOrder;
			layers.revalidate();
			layers.repaint();
			EventQueue.invokeLater( openNext );
		}

		Component cUnhide = getTopmostComponent();
		if (cUnhide != null)
		{
			cUnhide.setFocusable( true );
		}

		// note if a transition is active then this component may be doing
		// a little animation before it gets removed and hidden
		return c;
	}

	/**
	 * Task which takes the next id (if there is one) and shows it's screen.
	 */
	private Runnable openNext = new Runnable()
	{

		@Override
		public void run()
		{
			if (getNextScreenId() != null)
			{
				String nextOne = getNextScreenId();
				setnextScreenId( null );
				showScreen( nextOne );
			}
		}

	};

	/**
	 * Replaces the topmost screen with the specified one. Kind of like a goto
	 * instead of a function call
	 *
	 * @param screenName
	 */
	public void switchToScreen( String screenName )
	{
		setnextScreenId( screenName );
		popScreen();
	}

	/**
	 * Shows the specified screen on top of any existing ones
	 *
	 */
	public void showScreen( String screenName )
	{
		pushScreen( screenName );
	}

	public void pushScreen( String screenName )
	{
		pushScreen(
			screenName,
			NOPListener );
	}

	/**
	 * Shows the named screen. The screen must be registered by calling Register
	 *
	 * @param screenName
	 * @param whenDone
	 */
	public void pushScreen( String screenName,
		TransitionCompleteListener whenDone )
	{
		ScreenFactory maker = screenmakers.get( screenName );

		if (maker == null)
		{
			noScreenAction.run( screenName );
		}
		Component c = maker.create();
		c.setName( screenName );
		pushScreen(
			c,
			whenDone );

	}

	public void pushScreen( Component c, TransitionCompleteListener whenDone )
	{
		pushPanel(
			c,
			whenDone );

	}

	public Iterable< String > getScreenNames()
	{
		return screenmakers.keySet();
	}

}
