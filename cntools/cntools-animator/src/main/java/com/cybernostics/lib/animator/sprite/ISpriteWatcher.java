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

import java.awt.geom.Rectangle2D;

/**
 * A sprite is an image which is rendered within the bounds of a parent
 * component. The size, location are both in the range 0->1.0 which is scaled to
 * the dimensions of the owner component
 *
 * @author jasonw
 */
public interface ISpriteWatcher
{

	public void changedId( String name );

	public void changedRelativeBounds( Rectangle2D rect );

	public void changedRotationAngle( double toRotate );

	public void changedScale( double toScale );

	public void changedZ_order( int z_order );

	public void changedTransparency( double f );

	public void changedAnchor( Anchor.Position locationAnchor );

	public void changedFlip( IconTransformer.FlipType flip );

}
