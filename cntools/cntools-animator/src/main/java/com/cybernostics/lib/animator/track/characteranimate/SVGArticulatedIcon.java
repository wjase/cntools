/*
 * #%L cntools-animator %% Copyright (C) 2012 Cybernostics Pty Ltd %% Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. #L%
 */

package com.cybernostics.lib.animator.track.characteranimate;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Pattern;

import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import com.kitfox.svg.elements.SVGElement;
import java.awt.Component;
import java.awt.Graphics;

/**
 * 
 * This class is designed to animate SVGICons which have a hierarchy of animated
 * bits and various states for those bits.
 * 
 * The hierarchy would looks something like this
 * 
 * <pre>
 * body-front.head-front.mouth-closed 
 * body-front.head-front.mouth-EEE
 * body-front.head-front.mouth-OOO 
 * body-front.head-front.mouth-AHH
 * body-front.head-front.eyes-wide 
 * body-front.head-front.eyes-open
 * body-front.head-front.eyes-shut 
 * body-front.rightarm-raised.hand-open
 * body-front.rightarm-raised.hand-fist 
 * body-side.head-front.mouth-closed etc
 * </pre>
 * 
 * the format is <part>-<position>.<sub-part>-<position> etc etc
 * 
 * Each would correspond to an object or group in the SVG.
 * 
 * The method setPosition(String position) is used to show and hide elements
 * based on the hierarchy By moving from say body.front to body.side all the sub
 * elements of body.front would be hidden and any corresponding elements of body
 * side would be shown. So to turn the head while the mouth is open only
 * requires one call from body.front.head.front to body.front.head.side
 * 
 * @author jasonw
 * 
 */
public class SVGArticulatedIcon extends ScalableSVGIcon
{

	private SVGHierachyElementDisplayer elementDisplayer = new SVGHierachyElementDisplayer();
	// matches feature-aspect.subfeature-aspect.subsubfeature.aspect etc etc
	private Pattern patPositionPattern = Pattern.compile( "^([^\\-]+-[^\\-]+)(\\.[^.\\-]+-[^.\\-]+)*$" );

	public static SVGArticulatedIcon get( URL toLoad )
		throws URISyntaxException
	{
		return get( new URI( toLoad.toString() ) );
	}

	public static SVGArticulatedIcon get( URI toLoad )
	{
		SVGArticulatedIcon icon = new SVGArticulatedIcon();
		icon.setSvgURI( toLoad );
		return icon;

	}

	/**
	 */
	protected SVGArticulatedIcon()
	{
	}

	/**
	 * 
	 * @param pathToLoad
	 */
	protected SVGArticulatedIcon( URI pathToLoad )
	{
		super();
	}

	/**
	 * Recursively descends into object finding any with id in the right format
	 */
	private void collectAnimatedElements( SVGElement e )
	{
		if (e == null || e.getId() == null)
		{
			return;
		}
		if (patPositionPattern.matcher(
			e.getId() )
			.matches())
		{
			PartPositionPath newPath = new PartPositionPath( e.getId() );
			newPath.setAssociatedElement( e );
			elementDisplayer.addControlledElement( newPath );
		}

		Vector< SVGElement > children = new Vector< SVGElement >();
		e.getChildren( children );

		for (SVGElement eachChild : children)
		{
			collectAnimatedElements( eachChild );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kitfox.svg.app.beans.SVGIcon#setSvgURI(java.net.URI)
	 */
	@Override
	public void setSvgURI( URI svgURI )
	{
		super.setSvgURI( svgURI );
		elementDisplayer.clear();
		collectAnimatedElements( getSvgUniverse().getDiagram(
			getSvgURI() )
			.getRoot() );
	}

	/**
	 * 
	 * @return
	 */
	public Set< PartPositionPath > getAnimatedElements()
	{
		return elementDisplayer.allControlledElements.keySet();
	}

	List< DisplayedElementsListener > displayedElementsChanges = new ArrayList< DisplayedElementsListener >();

	/**
	 * Changes the items displayed based on the position descriptor
	 * 
	 * @param newPosition
	 */
	public void changePosition( String newPosition )
	{
		elementDisplayer.changePosition( newPosition );
		fireDisplayedElementsChanged();
	}

	/**
	 * 
	 * @param listener
	 */
	public void addDisplayedElementsListener( DisplayedElementsListener listener )
	{
		displayedElementsChanges.add( listener );
	}

	/**
	 * 
	 */
	public void fireDisplayedElementsChanged()
	{
		for (DisplayedElementsListener eachListener : displayedElementsChanges)
		{
			eachListener.displayedElementChanged();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.kitfox.svg.app.beans.SVGIcon#paintIcon(java.awt.Component,
	 * java.awt.Graphics, int, int)
	 */

	@Override
	public void paintIcon( Component comp, Graphics gg, int x, int y )
	{
		super.paintIcon(
			comp,
			gg,
			x,
			y );
	}
}
