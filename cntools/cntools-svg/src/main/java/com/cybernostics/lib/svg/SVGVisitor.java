package com.cybernostics.lib.svg;

import com.kitfox.svg.elements.SVGElement;

/**
 *
 * @author jasonw
 */
public interface SVGVisitor
{

	public void process( SVGElement el );

}
