package com.cybernostics.lib.gui.border.sun;

import com.cybernostics.lib.Application.AppResources;
import com.cybernostics.lib.resourcefinder.Finder;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.kitfox.svg.app.beans.SVGIcon;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.net.URI;
import java.net.URL;

/**
 * @author jasonw
 * 
 */
public class SVGFill extends Fill
{

	SVGIcon toRender = new SVGIcon();

	public SVGIcon getIcon()
	{
		return toRender;
	}

	public SVGFill( String path )
	{
		this( AppResources.getFinder(), path );
	}

	public SVGFill( Finder loader, String path )
	{
		URL toLoad = loader.getResource( path );
		loadDrawing( toLoad );
	}

	public SVGFill( URL toLoad )
	{
		loadDrawing( toLoad );
	}

	public SVGFill( SVGIcon toFill )
	{
		toRender = toFill;
	}

	private void loadDrawing( URL toLoad )
	{
		URI loadURI = toRender.getSvgUniverse()
			.loadSVG(
				toLoad );
		toRender.setAntiAlias( true );
		toRender.setScaleToFit( true );
		toRender.setInterpolation( SVGIcon.INTERP_BICUBIC );
		toRender.setSvgURI( loadURI );
	}

	@Override
	public void paintFill( Component c, Graphics g, Rectangle r )
	{
		toRender.setPreferredSize( r.getSize() );
		toRender.paintIcon(
			c,
			g,
			r.x,
			r.y );

	}

}
