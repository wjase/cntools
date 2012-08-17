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

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.Icon;

/**
 * Encapsulates an icon with a scale rotate and flip. The ScaledRotatedIcon is
 * an example of this. It caches the result which then only needs to be blitted
 * to the screen.
 *
 * @author jasonw
 */
public interface IconTransformer extends Icon
{

	public void setAspectRatio( double d );

	public enum FlipType
	{

		HORIZONTAL, VERTICAL, BOTH, NONE
	}

	public FlipType getFlip();

	public void setFlip( FlipType f );

	public Rectangle2D getTransformedBounds();

	public double getScale();

	public Icon getIcon();

	public void setIcon( Icon toSet );

	public void setAngle( double angle );

	public double getAngle();

	public AffineTransform getTransform();

	public void setNeedsRender( boolean needsRender );

	public void setUnScaledSize( Dimension unscaled );

	public void setScale( double scale );

	public void setAnchor( Anchor.Position locationAnchor );

	public Anchor getAnchor();

	public Point2D getHotspot();

	/**
	 * Update the scale and rotation and size to match the provided item
	 *
	 * @param current
	 */
	public void updateScaleRotationWith( IconTransformer current );

	Dimension getUnscaledSize();

}
