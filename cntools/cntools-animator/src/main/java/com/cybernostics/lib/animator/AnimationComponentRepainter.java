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
package com.cybernostics.lib.animator;

import com.cybernostics.lib.gui.RepaintListener;
import java.awt.Rectangle;

import javax.swing.JComponent;

/**
 * 
 * @author jasonw
 * 
 */
public class AnimationComponentRepainter implements RepaintListener
{

	JComponent animatedComponent;

	/**
	 * 
	 * @param animatedComponent
	 */
	public AnimationComponentRepainter( JComponent animatedComponent )
	{
		this.animatedComponent = animatedComponent;
	}

	/**
	 * 
	 */
	@Override
	public void repaint()
	{
		animatedComponent.repaint();
	}

	/**
	 * 
	 * @param r
	 */
	@Override
	public void repaint( Rectangle r )
	{
		animatedComponent.repaint( r );
	}

}
