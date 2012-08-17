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

import java.awt.GraphicsConfiguration;
import java.awt.geom.AffineTransform;

/**
 * This scales an items Dimension relative to some parent.
 * When scale is called with a dimension, the size is transformed to be relative to 
 * some parent. So for a parent 100 x 100 pixels wide, calling scale with ( 0.5,0.6 )
 * will result in the dimension having ( 50,60 ).
 * 
 * @author jasonw
 */
public interface SpriteOwner
{
	public void addUpdateListener( OwnerSizeListener listener );

	public AffineTransform getTransform();

	public GraphicsConfiguration getGraphicsConfiguration();

}
