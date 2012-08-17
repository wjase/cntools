package com.cybernostics.lib.svg;

import java.util.Map;

/**
 * As you write a stream of images, some images are gathered for packaging
 * and say compiled into a zip or given directory. Others are left as is.
 * This class takes care of the re-jigging of the names
 * For example an image with an xref=url(file:///c:/some/path/on your/pc/filename)
 * could be transformed into either url(some/path/on your/pc/filename))
 * @author jasonw
 */
public interface URLRewriter
{
	String transformURL( String sToChange );

	Map< String, String > getRewrites();
}
