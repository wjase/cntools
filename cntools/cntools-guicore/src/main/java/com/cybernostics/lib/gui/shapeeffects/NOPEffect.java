package com.cybernostics.lib.gui.shapeeffects;

import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 *
 * If you have an effect which may or may not be activated instead of assigning
 * it to null - assign it to NOPEffect. Then you never need to check for null
 * before calling it. The NOPEffect simply does nothing.
 * 
 * eg.
 *   Instead of:
 * 
 *  ShapeEffect myOptionalEffect = null;
 * 
 *   ... later
 * 
 *   if( myOptionalEffect != null )
 *   {
 *      myOptionalEffect.draw(g2,r);
 *   }
 * 
 * 
 *   Do this instead:
 * 
 *  ShapeEffect myOptionalEffect = NOPEffect.get();
 * 
 *   ... later
 *
 *  myOptionalEffect.draw(g2,r);  // should be no need to check for null
 * 
 * @author jasonw
 */
public class NOPEffect implements ShapeEffect
{

	private static final SingletonInstance< NOPEffect > theEffect = new SingletonInstance< NOPEffect >()
	{

		@Override
		protected NOPEffect createInstance()
		{
			return new NOPEffect();
		}

	};

	public static ShapeEffect get()
	{
		return theEffect.get();
	}

	@Override
	public void draw( Graphics2D g2, Shape s )
	{
	}

}
