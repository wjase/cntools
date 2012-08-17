package com.cybernostics.lib.media.icon;

import com.cybernostics.lib.regex.Regex;
import com.cybernostics.lib.svg.SVGUtil;
import com.cybernostics.lib.svg.SVGVisitor;
import com.kitfox.svg.elements.SVGElement;
import java.awt.Component;
import java.awt.Graphics;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jasonw
 */
public class SVGSelectedElementIcon extends ScalableSVGIcon
{

	private List< SVGElement > toShow = new ArrayList< SVGElement >();

	private List< SVGElement > toHide = new ArrayList< SVGElement >();

	public SVGSelectedElementIcon(
		URL toLoad,
		final Regex showFilter,
		final Regex hideFilter )
	{
		super( toLoad );
		SVGUtil.traverse(
			getDiagram().getRoot(),
			new SVGVisitor()
		{

			@Override
			public void process( SVGElement el )
			{
				String id = el.getId();
				if (hideFilter.find( id ))
				{
					toHide.add( el );
				}
				if (showFilter.find( id ))
				{
					toShow.add( el );
				}

			}

		} );

	}

	@Override
	public void paintIcon( Component comp, Graphics gg, int x, int y )
	{
		for (SVGElement eachElement : toShow)
		{
			SVGUtil.setElementVisible(
				eachElement,
				true );
		}
		for (SVGElement eachElement : toHide)
		{
			SVGUtil.setElementVisible(
				eachElement,
				false );
		}
		super.paintIcon(
			comp,
			gg,
			x,
			y );
	}

}
