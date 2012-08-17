package com.cybernostics.lib.media.icon;

import com.cybernostics.lib.concurrent.WatchableWorkerTask;
import java.net.URL;

/**
 *
 * @author jasonw
 */
public interface ScalableIconURLLoader
{

	WatchableWorkerTask load( URL toLoad );
}
