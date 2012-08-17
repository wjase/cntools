package com.cybernostics.lib.gui.window;

import com.cybernostics.lib.gui.windowcore.SceneSelector;
import com.cybernostics.lib.animator.track.Sequencer;
import com.cybernostics.lib.collections.IterableArray;
import com.cybernostics.lib.concurrent.GUIEventThread;
import com.cybernostics.lib.gui.ParentSize;
import com.cybernostics.lib.gui.declarative.events.WhenMadeVisible;
import com.cybernostics.lib.gui.shapeeffects.ColorFill;
import com.cybernostics.lib.gui.shapeeffects.ShapedPanel;
import com.cybernostics.lib.gui.windowcore.ScreenStack;
import com.cybernostics.lib.gui.windowcore.RetainedInstanceScreenFactory;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class CNApplication extends FullScreenFrame
{

	/**
	 *
	 */
	private static final long serialVersionUID = 4106392589393882605L;

	static Color appBackground;

	/**
	 * The application directory used for saving config info
	 */
	private static String appDir;

	protected static Font appFont = null;

	/**
	 * Gets a standard kid type font for the whole app. TODO: need to check mac
	 * and unix
	 *
	 * @return
	 */
	public static Font getAppFont()
	{
		if (appFont == null)
		{
			appFont = new Font( "Comic Sans MS", Font.BOLD, 20 );
		}
		return appFont;
	}

	private static Object appObject = null;

	public static void setAppObject( Object obj )
	{
		appObject = obj;
	}

	public static Object getAppObject()
	{
		return appObject;
	}

	public static String getApplicationDir()
	{
		if (appDir == null)
		{
			URL appdirURL = appObject.getClass()
				.getClassLoader()
				.getResource(
					"" );
			String urlPath = appdirURL.getPath();
			if (urlPath.indexOf( '!' ) != -1)
			{
				// Its a jar resource
				Pattern p = Pattern.compile( ":(.+)[^\\\\\\/]+!" );
				Matcher m = p.matcher( urlPath );
				if (m.find())
				{
					appDir = m.group( 1 );
				}
			}
			else
			{
				appDir = appdirURL.getPath();
				// For dos-style paths chop the first slash
				if (( appDir.indexOf( ':' ) != -1 )
					&& ( appDir.charAt( 0 ) == '/' ))
				{
					appDir = appDir.substring( 1 );
				}
			}
		}

		return appDir;
	}

	/**
	 * Triggers a named gui action, like showing a bit of UI
	 *
	 * @param actionName
	 */
	//public abstract void doAction( String actionName );
	public static Color getBackgroundColor()
	{
		appBackground = ( appBackground == null ) ? new Color( 34, 139, 34 )
			: appBackground;

		return appBackground;
	}

	protected static String appname = "app";

	public static void setUIBackground()
	{
		Color backColor = getBackgroundColor();
		java.util.Enumeration< Object > keys = UIManager.getDefaults()
			.keys();
		while (keys.hasMoreElements())
		{
			Object key = keys.nextElement();
			if (key.toString()
				.indexOf(
					"ackground" ) != -1)
			{
				if (( key.toString()
					.indexOf(
						"anel" ) != -1 ) || ( key.toString()
					.indexOf(
						"indow" ) != -1 )
					|| ( key.toString()
						.indexOf(
							"rame" ) != -1 ) || ( key.toString()
						.indexOf(
							"ialog" ) != -1 ))
				{
					UIManager.put(
						key,
						backColor );
				}
			}
		}

	}

	public static void setUIFont( Font f )
	{
		java.util.Enumeration< Object > keys = UIManager.getDefaults()
			.keys();
		while (keys.hasMoreElements())
		{

			Object key = keys.nextElement();

			Object value = UIManager.get( key );
			if (value instanceof javax.swing.plaf.FontUIResource)
			{
				UIManager.put(
					key,
					f );
			}
		}
	}

	public File getDataDir()
	{
		return new File( System.getProperty( "user.home" ), "."
			+ this.getName() );
	}

	public static String getHomeDir()
	{
		return System.getProperty( "user.home" );
	}

	private ShapedPanel background = new ShapedPanel();

	public CNApplication( String name )
	{
		super( name );
		ScreenStack scr = ScreenStack.get();
		setContentPane( scr.getLayeredPanel() );
		ParentSize.bind(
			this,
			scr.getLayeredPanel() );
		background.setBackgroundPainter( new ColorFill( Color.black ) );
		scr.getLayeredPanel()
			.add(
				background,
				new Integer( -100 ) );
		ParentSize.bind(
			this,
			background );

	}

	/**
	 * Closed the application
	 */
	public static void shutdown( int status )
	{
		System.exit( status );
	}

	/**
	 * takes a variable number of panels and shows them as a navigable stack
	 */
	public static CNApplication testScreenStack( JPanel... tst )
	{
		// assume the first is the main one
		final String screenName = tst[ 0 ].getName();
		Sequencer.get()
			.start();
		CNApplication jf = new CNApplication( "frame test" );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		for (JPanel eachPanel : IterableArray.get( tst ))
		{
			ScreenStack.get()
				.register(
					eachPanel.getName(),
					new RetainedInstanceScreenFactory( eachPanel ) );
		}

		GUIEventThread.show( jf );

		new WhenMadeVisible( jf )
		{

			@Override
			public void doThis( AWTEvent e )
			{
				ScreenStack.get()
					.showScreen(
						screenName );
			}

		};

		return jf;
	}

	public static CNApplication testScreen( final String toShow )
	{
		// assume the first is the main one
		Sequencer.get()
			.start();
		CNApplication jf = new CNApplication( "Scene test" );
		jf.setResizable( true );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		jf.getRootPane()
			.setUI(
				null );
		jf.getRootPane()
			.setDoubleBuffered(
				true );

		GUIEventThread.show( jf );

		new WhenMadeVisible( jf )
		{

			@Override
			public void doThis( AWTEvent e )
			{
				if (toShow == null)
				{
					ScreenStack.get()
						.pickScreen();
				}
				else
				{
					ScreenStack.get()
						.showScreen(
							toShow );
				}
			}

		};

		return jf;
	}

}