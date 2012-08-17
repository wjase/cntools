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

import com.cybernostics.lib.animator.sprite.SpriteCollection;
import com.cybernostics.lib.animator.sprite.SpriteOwner;
import com.cybernostics.lib.animator.track.Track;

/**
 *
 * @author jasonw
 */
public interface Scene extends SpriteOwner, SpriteCollection
{
	public void initSceneElements();

	public void fireSceneReady();

	public void startTrack( Track t );

	public void addTrack( Track t );

	public void stop();

}
