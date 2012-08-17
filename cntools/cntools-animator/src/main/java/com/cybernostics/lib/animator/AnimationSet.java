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

package com.cybernostics.lib.animator;

import com.cybernostics.lib.animator.sprite.ISprite;
import com.cybernostics.lib.animator.track.Track;
import com.cybernostics.lib.animator.track.characteranimate.AnimatedCharacter;
import com.cybernostics.lib.animator.ui.AnimatedScene;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * Collects tasks, characters and sprites for an animation
 *
 * @author jasonw
 */
public class AnimationSet
{

	private Collection< ISprite > sprites = new HashSet< ISprite >();

	private Collection< Track > tracks = new HashSet< Track >();

	private Collection< AnimatedCharacter > characters = new HashSet< AnimatedCharacter >();

	private AnimatedScene ownerPanel = null;

	public AnimationSet( AnimatedScene thePanel )
	{
		ownerPanel = thePanel;
	}

	public void addSprite( ISprite... toAdd )
	{
		this.sprites.addAll( Arrays.asList( toAdd ) );
	}

	public void addCharacter( AnimatedCharacter... toAdd )
	{
		this.characters.addAll( Arrays.asList( toAdd ) );
	}

	public void addCharacter( Track... toAdd )
	{
		this.tracks.addAll( Arrays.asList( toAdd ) );
	}

	public void stop()
	{
		for (Track eachOne : tracks)
		{
			eachOne.stop( false );
		}
		ISprite[] a = new ISprite[ 1 ];
		ownerPanel.removeSprites( sprites.toArray( a ) );
		ownerPanel.removeCharacters( characters );
	}

}
