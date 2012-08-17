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

import com.cybernostics.lib.svg.SubRegionContainer;
import java.awt.geom.*;

/**
 *
 * @author jasonw
 */
public class ParentArea
{

	Rectangle2D inner = null;

	public static Rectangle2D get( SubRegionContainer parent, String areaId )
	{
		Rectangle2D inner = parent.getItemRectangle( areaId );
		if (inner == null)
		{
			inner = new Rectangle2D.Double( 0, 0, 0, 0 );
		}
		return inner;
	}

}
