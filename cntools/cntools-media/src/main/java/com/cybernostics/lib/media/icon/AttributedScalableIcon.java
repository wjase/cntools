package com.cybernostics.lib.media.icon;

import com.cybernostics.lib.concurrent.GUIEventThread;
import com.cybernostics.lib.concurrent.WatchableWorkerTask;
import com.cybernostics.lib.concurrent.WorkerDoneListener;
import com.cybernostics.lib.media.image.ImageLoader;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jasonw
 */
public class AttributedScalableIcon
		implements
		ScalableIcon,
		Map< String, Object >,
		PreferredSizeListener
{

	public static AttributedScalableIcon create( URL u )
	{
		final AttributedScalableIcon asi = new AttributedScalableIcon( LoadingIcon.get() );
		asi.put(
				URL_KEY,
				u );
		asi.setIcon( LoadingIcon.get() );
		asi.startLoad( new ScalableIconLoaderClient()
		{

			@Override
			public void iconLoaded( com.cybernostics.lib.media.icon.ScalableIcon icon )
			{
				asi.setIcon( icon );
			}

		} );

		return asi;
	}

	public static final String URL_KEY = "URL";

	protected SoftReference< ScalableIcon > internal = null;
	// if we were initialised only with an icon but no URL, then
	// we need to hang onto it;

	private ScalableIcon permanentCopy = null;

	@Override
	public BufferedImage getImage()
	{
		return getIcon().getImage();
	}

	public ScalableIcon getIcon()
	{
		return internal.get();
	}

	private WatchableWorkerTask loadTask = null;

	public void waitLoad()
	{
		if (loadTask != null)
		{
			try
			{
				loadTask.get();
			}
			catch (Exception ex)
			{
				Logger.getLogger(
						AttributedScalableIcon.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex );
			}
		}
	}

	private static Map< String, ScalableIconURLLoader > loaders = null;

	public static Map< String, ScalableIconURLLoader > getLoader()
	{
		if (loaders == null)
		{
			loaders = new HashMap< String, ScalableIconURLLoader >();
			loaders.put(
					"svg",
					new ScalableIconURLLoader()
					{

						@Override
						public WatchableWorkerTask load( final URL toLoad )
						{
							return new WatchableWorkerTask( "loadIcon" + toLoad )
							{

								@Override
								protected Object doTask() throws Exception
								{
									return new ScalableSVGIcon( toLoad );
								}

							};
						}

					} );

			final ScalableIconURLLoader imgLoader = new ScalableIconURLLoader()
			{

				@Override
				public WatchableWorkerTask load( URL toLoad )
				{
					return ImageLoader.startLoadScalableImageIcon( toLoad );
				}

			};
			loaders.put(
					"png",
					imgLoader );
			loaders.put(
					"jpg",
					imgLoader );
			loaders.put(
					"gif",
					imgLoader );

		}
		return loaders;

	}

	/**
	 * Allows an icon to be loaded by a custom loader for new types of icon.
	 *
	 * eg eml extension could be an email icon which needs to be loaded.
	 *
	 * @param extension
	 * @param loader
	 */
	public static void registerLoader( String extension,
										ScalableIconURLLoader loader )
	{
		getLoader().put(
				extension,
				loader );
	}

	private static Pattern pExtension = Pattern.compile( "(\\S\\S\\S)$" );

	synchronized private void startLoad( final ScalableIconLoaderClient loaderClient )
	{
		if (loadTask == null)
		{
			URL toLoad = getURL();
			if (toLoad != null)
			{
				String filename = toLoad.getFile();
				Matcher m = pExtension.matcher( filename );
				if (m.find())
				{
					ScalableIconURLLoader loader = getLoader().get(
							m.group() );
					loadTask = loader.load( toLoad );
				}

				loadTask.addWorkerDoneListener( new WorkerDoneListener()
				{

					@Override
					public void taskDone( Future< Object > completed )
					{
						try
						{
							loaderClient.iconLoaded( (ScalableIcon) ( (WatchableWorkerTask) completed ).get() );
						}
						catch (Exception ex)
						{
							Logger.getLogger(
									AttributedScalableIcon.class.getName() )
								.log(
									Level.SEVERE,
									null,
									ex );
						}
					}

				} );

			}
			loadTask.start();
		}

	}

	public WatchableWorkerTask getLoadTask()
	{
		return loadTask;
	}

	@Override
	public void paintIcon( Component c, Graphics g, int x, int y )
	{
		if (c != null)
		{
			clients.add( c );
		}

		internal.get()
			.paintIcon(
				c,
				g,
				x,
				y );
	}

	private ScalableIcon getDelegateIcon()
	{
		if (internal == null || internal.get() == null)
		{
			startLoad( new ScalableIconLoaderClient()
			{

				@Override
				public void iconLoaded( ScalableIcon icon )
				{
					setIcon( icon );
				}

			} );
			return LoadingIcon.get();
		}
		return internal.get();
	}

	@Override
	public int getIconWidth()
	{
		return getDelegateIcon().getIconWidth();
	}

	@Override
	public int getIconHeight()
	{
		return getDelegateIcon().getIconHeight();
	}

	Dimension2D dPreferredSize = null;

	@Override
	public void setSize( Dimension2D d )
	{
		dPreferredSize = d;
		ScalableIcon i = getDelegateIcon();
		Dimension dCurrent = new Dimension( i.getIconWidth(), i.getIconHeight() );
		if (( dCurrent.width != d.getWidth() ) ||
					( dCurrent.height != d.getHeight() ))
		{
			getDelegateIcon().setSize(
					d );
		}
		repaintClients();

	}

	@Override
	public ScalableIcon copy()
	{
		return getDelegateIcon().copy();
	}

	List< PreferredSizeListener > listeners = new ArrayList< PreferredSizeListener >();

	@Override
	public void addPreferredSizeListener( PreferredSizeListener listener )
	{
		listeners.add( listener );
	}

	@Override
	public Collection< Object > values()
	{
		return properties.values();
	}

	@Override
	public int size()
	{
		return properties.size();
	}

	@Override
	public Object remove( Object key )
	{
		return properties.remove( key );
	}

	@Override
	public void putAll( Map< ? extends String, ? extends Object > m )
	{
		properties.putAll( m );
	}

	@Override
	public Object put( String key, Object value )
	{
		return properties.put(
				key,
				value );
	}

	@Override
	public Set< String > keySet()
	{
		return properties.keySet();
	}

	@Override
	public boolean isEmpty()
	{
		return properties.isEmpty();
	}

	@Override
	public int hashCode()
	{
		return properties.hashCode();
	}

	@Override
	public Object get( Object key )
	{
		return properties.get( key );
	}

	@Override
	public boolean equals( Object o )
	{
		return properties.equals( o );
	}

	@Override
	public Set< Entry< String, Object >> entrySet()
	{
		return properties.entrySet();
	}

	@Override
	public boolean containsValue( Object value )
	{
		return properties.containsValue( value );
	}

	@Override
	public boolean containsKey( Object key )
	{
		return properties.containsKey( key );
	}

	@Override
	public void clear()
	{
		properties.clear();
	}

	Map< String, Object > properties = new HashMap< String, Object >();

	public AttributedScalableIcon( ScalableIcon si )
	{
		internal = new SoftReference< ScalableIcon >( si );
		permanentCopy = si;

	}

	public URL getURL()
	{
		return (URL) properties.get( URL_KEY );
	}

	public void setURL( URL value )
	{
		properties.put(
				URL_KEY,
				value );
	}

	@Override
	public void preferredSizeChanged( Dimension2D newSize )
	{
		for (PreferredSizeListener eachListener : listeners)
		{
			eachListener.preferredSizeChanged( newSize );
		}
	}

	@Override
	public void setMinimumSize( Dimension d )
	{
		getDelegateIcon().setMinimumSize(
				d );
	}

	private void setIcon( ScalableIcon icon )
	{
		internal = new SoftReference< ScalableIcon >( icon );
		if (dPreferredSize != null)
		{
			icon.setSize( dPreferredSize );

		}
		icon.addPreferredSizeListener( this );
		repaintClients();
	}

	private void repaintClients()
	{
		GUIEventThread.runLater( new Runnable()
		{

			@Override
			public void run()
			{
				for (Component c : clients)
				{
					c.repaint();
				}
			}

		} );

	}

	private Set< Component > clients = new HashSet< Component >();

	@Override
	public Dimension getPreferredSize()
	{
		return getDelegateIcon().getPreferredSize();
	}

	@Override
	public void addPropertyChangeListener( PropertyChangeListener listener )
	{
		changes.addPropertyChangeListener( listener );
	}

	PropertyChangeSupport changes = new PropertyChangeSupport( this );
}
