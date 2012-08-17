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

package com.cybernostics.lib.animator.sprite;

import com.cybernostics.lib.collections.IterableArray;
import com.cybernostics.lib.gui.GraphicsConfigurationSource;
import com.cybernostics.lib.gui.shape.IconRect;
import com.cybernostics.lib.media.icon.MultiframeSVGIcon;
import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;

/**
 *
 * @author jasonw
 */
public class FrameIconSprite extends IconSprite
	implements
	IFrameSprite,
	GraphicsConfigurationSource,
	Icon
{

	public FrameIconSprite()
	{
	}

	/**
	 * Add either a collection of icons (one per frame) or a single icon which
	 * may have frames in layer...
	 *
	 * @param toAdd
	 */
	public void addFrames( Icon... toAdd )
	{
		if (toAdd.length == 1)
		{
			Icon item = toAdd[ 0 ];
			Dimension2D dIcon = IconRect.getDimension( item );

			if (item instanceof ScalableSVGIcon)
			{
				for (Icon eachIcon : MultiframeSVGIcon.getFrames( (ScalableSVGIcon) item ))
				{
					frames.add( new ScaledRotatedIcon( eachIcon, dIcon, this ) );
				}

			}
			else
			{
				frames.add( new ScaledRotatedIcon( item, dIcon, this ) );
			}

		}
		else
		{
			Dimension2D dIcon = null;
			for (Icon eachIcon : IterableArray.get( toAdd ))
			{
				if (dIcon == null)
				{
					dIcon = IconRect.getDimension( eachIcon );
				}
				frames.add( new ScaledRotatedIcon( eachIcon, dIcon, this ) );
			}
		}

		if (frames.size() > 0)
		{
			setCurrentFrameNumber( 0 );
		}

	}

	private List< IconTransformer > frames = new ArrayList< IconTransformer >();

	private int currentFrameNumber = 0;

	@Override
	public int getFrameCount()
	{
		return frames.size();
	}

	@Override
	public int getCurrentFrameNumber()
	{
		return currentFrameNumber;
	}

	@Override
	public void setCurrentFrameNumber( int number )
	{
		currentFrameNumber = number;
		currentFrameNumber = currentFrameNumber % getFrameCount();

		IconTransformer current = getIconRenderer();
		IconTransformer next = frames.get( currentFrameNumber );
		next.updateScaleRotationWith( current );
		setIconRenderer( next );

	}

	//    @Override
	//    public void ownerSizeUpdated()
	//    {
	//        internal.ownerSizeUpdated();
	//        IconTransformer it = internal.getIconRenderer();
	//        Dimension d = it.getUnscaledSize();
	//        for ( IconTransformer eachFrame : frames )
	//        {
	//            if ( eachFrame != it )
	//            {
	//                eachFrame.setUnScaledSize( d );
	//            }
	//        }
	//    }
}
