package com.cybernostics.lib.media;

import java.awt.Dimension;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.cybernostics.lib.concurrent.WatchableWorkerTask;
import com.cybernostics.lib.media.icon.ScalableIcon;
import com.cybernostics.lib.media.icon.ScalableImageIcon;
import com.cybernostics.lib.media.image.ImageLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jasonw
 *
 */
public class AsyncImageFetcher extends WatchableWorkerTask
{

	static ExecutorService fetcherTaskPool = null;

	static ExecutorService getImageFetcherExecutor()
	{
		if (fetcherTaskPool == null)
		{
			fetcherTaskPool = new ThreadPoolExecutor( 1,
				5,
				60,
				TimeUnit.SECONDS,
				new ArrayBlockingQueue< Runnable >( 10 ) );
		}
		return fetcherTaskPool;
	}

	private URL pathToLoad;
	Dimension requiredSize = null;

	//    public AsyncImageFetcher( String pathOrURL, Dimension requiredSize )
	//    {
	//        super( "Fetch image " + pathOrURL );
	//        this.setExecutor( getImageFetcherExecutor() );
	//        this.pathToLoad = pathOrURL;
	//        this.requiredSize = requiredSize;
	//    }
	public AsyncImageFetcher( URI remoteURL )
	{
		super( "fetch " + remoteURL.toString() );
		try
		{
			pathToLoad = remoteURL.toURL();
		}
		catch (MalformedURLException ex)
		{
			Logger.getLogger(
				AsyncImageFetcher.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
	}

	/**
	 * @param remoteURL
	 */
	public AsyncImageFetcher( URL remoteURL )
	{
		super( "fetch " + remoteURL.toExternalForm() );
		pathToLoad = remoteURL;
	}

	/* (non-Javadoc)
	 * @see com.cybernostics.lib.concurrent.WorkerTask#doTask()
	 */
	@Override
	protected ScalableIcon doTask() throws Exception
	{
		try
		{
			ScalableImageIcon image = new ScalableImageIcon( ImageLoader.loadBufferedImage( pathToLoad ) );
			return image;
		}
		catch (Exception ex)
		{
			throw ex;
		}
		//return ErrorImageIcon.get();
	}
}
