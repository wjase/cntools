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
import java.applet.Applet;
import java.awt.*;
import javax.swing.JComponent;
import javax.swing.RepaintManager;

/**
 *
 * @author jasonw
 */
public class NoseyRepaintManager extends RepaintManager
{

	public NoseyRepaintManager( RepaintManager other )
	{
		inner = other;

	}

	public static void install( JComponent c )
	{
		//        NoseyRepaintManager nsr;
		//        RepaintManager rm = RepaintManager.currentManager( c );
		//        if ( !( rm instanceof NoseyRepaintManager ) )
		//        {
		//            nsr = new NoseyRepaintManager( RepaintManager.currentManager( c ) );
		//            RepaintManager.setCurrentManager( nsr );
		//        }
	}

	public RepaintManager inner = null;

	private void fireRepaintRequested( JComponent c, int x, int y, int w, int h )
	{
		Container parent = c.getParent();
		if (c instanceof ComponentSprite)
		{
			( (ComponentSprite) c ).update();
			return;
		}
		while (parent != null)
		{
			if (parent instanceof ComponentSprite)
			{
				( (ComponentSprite) parent ).update();
				return;
			}
			parent = parent.getParent();
		}

	}

	@Override
	public synchronized void addDirtyRegion( JComponent c,
		int x,
		int y,
		int w,
		int h )
	{
		fireRepaintRequested(
			c,
			x,
			y,
			w,
			h );
		super.addDirtyRegion(
			c,
			x,
			y,
			w,
			h );
	}

	@Override
	public void validateInvalidComponents()
	{
		inner.validateInvalidComponents();
	}

	@Override
	public synchronized String toString()
	{
		return inner.toString();
	}

	@Override
	public void setDoubleBufferingEnabled( boolean aFlag )
	{
		inner.setDoubleBufferingEnabled( aFlag );
	}

	@Override
	public void setDoubleBufferMaximumSize( Dimension d )
	{
		inner.setDoubleBufferMaximumSize( d );
	}

	@Override
	public synchronized void removeInvalidComponent( JComponent component )
	{
		inner.removeInvalidComponent( component );
	}

	@Override
	public synchronized void paintDirtyRegions()
	{
		inner.paintDirtyRegions();
	}

	@Override
	public void markCompletelyDirty( JComponent aComponent )
	{
		Rectangle r = aComponent.getBounds( null );
		fireRepaintRequested(
			aComponent,
			r.x,
			r.y,
			r.width,
			r.height );
		inner.markCompletelyDirty( aComponent );

	}

	@Override
	public void markCompletelyClean( JComponent aComponent )
	{
		inner.markCompletelyClean( aComponent );
	}

	@Override
	public boolean isDoubleBufferingEnabled()
	{
		return inner.isDoubleBufferingEnabled();
	}

	@Override
	public boolean isCompletelyDirty( JComponent aComponent )
	{
		return inner.isCompletelyDirty( aComponent );
	}

	@Override
	public Image getVolatileOffscreenBuffer( Component c,
		int proposedWidth,
		int proposedHeight )
	{
		return inner.getVolatileOffscreenBuffer(
			c,
			proposedWidth,
			proposedHeight );
	}

	@Override
	public Image getOffscreenBuffer( Component c,
		int proposedWidth,
		int proposedHeight )
	{
		return inner.getOffscreenBuffer(
			c,
			proposedWidth,
			proposedHeight );
	}

	@Override
	public Dimension getDoubleBufferMaximumSize()
	{
		return inner.getDoubleBufferMaximumSize();
	}

	@Override
	public Rectangle getDirtyRegion( JComponent aComponent )
	{
		return inner.getDirtyRegion( aComponent );
	}

	@Override
	public synchronized void addInvalidComponent( JComponent invalidComponent )
	{
		Rectangle r = invalidComponent.getBounds( null );
		fireRepaintRequested(
			invalidComponent,
			r.x,
			r.y,
			r.width,
			r.height );
		inner.addInvalidComponent( invalidComponent );
	}

	@Override
	public void addDirtyRegion( Applet applet, int x, int y, int w, int h )
	{
		inner.addDirtyRegion(
			applet,
			x,
			y,
			w,
			h );
	}

	@Override
	public void addDirtyRegion( Window window, int x, int y, int w, int h )
	{
		Rectangle wRect = new Rectangle( x, y, w, h );
		for (int index = 0; index < window.getComponentCount(); ++index)
		{
			Component c = window.getComponent( index );
			if (c.getBounds()
				.intersects(
					wRect ))
			{
				updateChildren( c );
			}

		}
		inner.addDirtyRegion(
			window,
			x,
			y,
			w,
			h );
	}

	private void updateChildren( Component c )
	{
		if (c instanceof ComponentSprite)
		{
			( (ComponentSprite) c ).update();
			return;
		}
		if (c instanceof Container)
		{
			Container cont = (Container) c;
			for (int index = 0; index < cont.getComponentCount(); ++index)
			{
				updateChildren( cont.getComponent( index ) );
			}
		}

	}

}
