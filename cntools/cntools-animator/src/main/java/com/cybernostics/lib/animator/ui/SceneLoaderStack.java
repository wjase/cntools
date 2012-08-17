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

package com.cybernostics.lib.animator.ui;

import com.cybernostics.lib.gui.shapeeffects.NOPEffect;
import com.cybernostics.lib.gui.shapeeffects.ShapeEffect;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jasonw
 */
public class SceneLoaderStack implements SceneLoader
{

	List< SceneLoader > loaders = new ArrayList< SceneLoader >();

	public void addLoader( SceneLoader toAdd )
	{
		loaders.add( toAdd );
	}

	@Override
	public ShapeEffect loadBackGround()
	{
		// start with the latest and find the first non-null background 
		for (int index = loaders.size() - 1; index >= 0; --index)
		{
			ShapeEffect se = loaders.get(
				index )
				.loadBackGround();
			if (se != null)
			{
				return se;
			}

		}
		return NOPEffect.get();

	}

	@Override
	public void loadAssets( Scene panel )
	{
		// Assets are loaded from oldest to newest loader
		for (SceneLoader eachLoader : loaders)
		{
			eachLoader.loadAssets( panel );

		}
	}

}
