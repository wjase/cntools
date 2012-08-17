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

import com.cybernostics.lib.animator.sprite.Anchor.Position;
import com.cybernostics.lib.animator.sprite.IconTransformer.FlipType;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author jasonw
 */
public class NOPSpriteWatcher implements ISpriteWatcher
{

	private static SingletonInstance< ISpriteWatcher > watcher = new SingletonInstance< ISpriteWatcher >()
	{

		@Override
		protected ISpriteWatcher createInstance()
		{
			return new NOPSpriteWatcher();
		}

	};

	public static ISpriteWatcher get()
	{
		return watcher.get();
	}

	private NOPSpriteWatcher()
	{
	}

	@Override
	public void changedId( String name )
	{
	}

	@Override
	public void changedRelativeBounds( Rectangle2D rect )
	{
	}

	@Override
	public void changedRotationAngle( double toRotate )
	{
	}

	@Override
	public void changedScale( double toScale )
	{
	}

	@Override
	public void changedZ_order( int z_order )
	{
	}

	@Override
	public void changedTransparency( double f )
	{
	}

	@Override
	public void changedAnchor( Position locationAnchor )
	{
	}

	@Override
	public void changedFlip( FlipType flip )
	{
	}

}
