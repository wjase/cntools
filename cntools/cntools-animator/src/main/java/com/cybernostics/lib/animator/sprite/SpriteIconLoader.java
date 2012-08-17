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

import com.cybernostics.lib.media.icon.IconLoadClient;
import com.cybernostics.lib.media.icon.SVGIconLoader;
import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.Future;

/**
 *
 * @author jasonw
 */
public class SpriteIconLoader implements IconLoadClient
{
	SVGSprite iconOwner = null;
	URL toLoad = null;

	public SpriteIconLoader( SVGSprite iconOwner, URL toLoad )
	{
		this.iconOwner = iconOwner;
		this.toLoad = toLoad;

	}

	public Future< URI > startLoad()
	{
		return SVGIconLoader.load(
			toLoad,
			this );
	}

	@Override
	public void iconLoaded( ScalableSVGIcon svgsi )
	{
		iconOwner.setIcon( svgsi );
	}

}
