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

import com.cybernostics.lib.animator.sprite.component.ComponentSprite;
import com.cybernostics.lib.animator.sprite.ISprite;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import javax.swing.JComponent;
import javax.swing.RepaintManager;

/**
 * UnRepaintManager is a RepaintManager that removes the functionality of the
 * original RepaintManager for us so we don't have to worry about Java
 * repainting on it's own.
 */
class ActiveRenderRepaintManager extends RepaintManager
{

	private static SingletonInstance< RepaintManager > onlyOne = new SingletonInstance< RepaintManager >()
	{

		@Override
		protected RepaintManager createInstance()
		{
			return new ActiveRenderRepaintManager();
		}

	};

	public static RepaintManager get()
	{
		return onlyOne.get();
	}

	/**
	 * static or derived only
	 */
	protected ActiveRenderRepaintManager()
	{
	}

	private void updateSprite( JComponent jc )
	{
		ISprite is = ComponentSprite.getFrom( jc );
		if (is != null)
		{
			is.update();
		}

	}

	@Override
	public void addDirtyRegion( JComponent c, int x, int y, int w, int h )
	{
		updateSprite( c );
	}

	@Override
	public void addInvalidComponent( JComponent invalidComponent )
	{
		updateSprite( invalidComponent );
	}

	@Override
	public void markCompletelyDirty( JComponent aComponent )
	{
		updateSprite( aComponent );
	}

	@Override
	public void paintDirtyRegions()
	{
	}

}