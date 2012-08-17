package com.cybernostics.lib.gui.control;

import com.cybernostics.lib.Application.AppResources;
import com.cybernostics.lib.animator.sprite.SVGSprite;
import com.cybernostics.lib.animator.sprite.SpriteCollection;
import com.cybernostics.lib.animator.track.characteranimate.SVGArticulatedIcon;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.media.icon.AttributedScalableIcon;
import com.cybernostics.lib.resourcefinder.Finder;
import com.cybernostics.lib.svg.SubRegionContainer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingWorker;

/**
 * @author jasonw
 * 
 */
public class IconGrabber extends SwingWorker< Void, SVGSprite >
{

	private Finder loader = AppResources.getFinder();;
	private SpriteCollection spriteContainer = null;

	class SpriteLoadData
	{

		SVGSprite toPosition;
		String parentName;
		String region;
		String resourceLocation;
		URL resource;

		public SpriteLoadData(
			SVGSprite toPosition,
			URL resource,
			String parentName,
			String region )
		{
			this.toPosition = toPosition;
			this.parentName = parentName;
			this.region = region;
			this.resource = resource;
		}
	}

	SubRegionContainer parentRegion = null;
	ArrayList< SpriteLoadData > iconsToGrab = new ArrayList< SpriteLoadData >();

	public IconGrabber( SubRegionContainer parentRegion )
	{
		this.parentRegion = parentRegion;
		if (parentRegion instanceof SpriteCollection)
		{
			this.spriteContainer = (SpriteCollection) parentRegion;
		}
	}

	public SVGSprite addToLoad( String name,
		URL resource,
		String parentName,
		String regionName )
	{
		SVGSprite nextSprite = new SVGSprite( name );
		iconsToGrab.add( new SpriteLoadData( nextSprite,
			resource,
			parentName,
			regionName ) );
		return nextSprite;
	}

	public SVGSprite addToLoad( String name, URL resource )
	{
		SVGSprite nextSprite = new SVGSprite( name );
		iconsToGrab.add( new SpriteLoadData( nextSprite, resource, null, null ) );
		return nextSprite;
	}

	@Override
	protected Void doInBackground() throws Exception
	{
		Map< String, SVGSprite > loaded = new HashMap< String, SVGSprite >();

		for (SpriteLoadData eachToLoad : iconsToGrab)
		{

			if (eachToLoad.toPosition.isAnimatedRegions())
			{
				eachToLoad.toPosition.setIcon( SVGArticulatedIcon.get( eachToLoad.resource ) );
			}
			else
			{
				eachToLoad.toPosition.setIcon( AttributedScalableIcon.create( eachToLoad.resource ) );
			}

			loaded.put(
				eachToLoad.toPosition.getId(),
				eachToLoad.toPosition );

			if (eachToLoad.region != null)
			{
				SubRegionContainer parentArea = (SubRegionContainer) ( ( eachToLoad.parentName != null ) ? loaded
					.get( eachToLoad.parentName )
					: parentRegion );
				eachToLoad.toPosition.setRelativeBounds( parentArea.getItemRectangle( eachToLoad.region ) );
			}

			if (spriteContainer != null)
			{
				this.spriteContainer.addSprites( eachToLoad.toPosition );
			}
		}
		return null;
	}

	public void runWhenDone( final Runnable toRun )
	{
		this.getPropertyChangeSupport()
			.addPropertyChangeListener(
				new PropertyChangeListener()
			{

				@Override
				public void propertyChange( PropertyChangeEvent evt )
			{
				if (evt.getNewValue()
					.toString()
					.equalsIgnoreCase(
						"DONE" ))
				{
					toRun.run();
				}
			}
			} );
	}

	/**
	 * @param loader
	 *            the loader to set
	 */
	public void setLoader( ResourceFinder loader )
	{
		this.loader = loader;
	}

	/**
	 * @return the loader
	 */
	public Finder getLoader()
	{
		return loader;
	}
}
