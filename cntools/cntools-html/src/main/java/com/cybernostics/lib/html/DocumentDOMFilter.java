package com.cybernostics.lib.html;

import org.w3c.dom.Document;

/**
 * Interface to update html documents before rendering them
 * @author jasonw
 */
public interface DocumentDOMFilter
{
	public void process( Document doc );
}
