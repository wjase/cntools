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

package com.cybernostics.lib.animator.track.ordering;

import com.cybernostics.lib.animator.track.Track;
import com.cybernostics.lib.animator.ui.AnimatedScene;
import java.awt.Container;
import javax.swing.JComponent;

/**
 *
 * @author jasonw
 */
public class TrackEndedActions
{

	public static TrackEndedListener hideComponent( final JComponent jc )
	{
		return new TrackEndedListener()
		{

			@Override
			public void trackEnded( Track source )
			{
				jc.setVisible( false );
			}

		};
	}

	public static TrackEndedListener removeComponent( final JComponent jc )
	{
		return new TrackEndedListener()
		{

			@Override
			public void trackEnded( Track source )
			{
				Container c = jc.getParent();
				if (c != null)
				{
					c.remove( jc );
				}

			}

		};
	}

}
