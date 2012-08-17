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
package com.cybernostics.lib.animator.track;

import com.cybernostics.lib.animator.sprite.ISprite;
import java.awt.geom.Point2D;

/**
 * Listener for location changes of sprites
 * 
 * @author jasonw
 * 
 */
public interface LocationListener
{

	/**
	 * This should be called whenever the target object's position has changed
	 * 
	 * @param location
	 * @param target  
	 */
	public void setLocation( Point2D location, ISprite target );
}
