package com.cybernostics.lib.gui.shapeeffects;

import com.cybernostics.lib.gui.graphics.StateSaver;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * Applies a multiple set of effects
 * @author jasonw
 */
public class ShapeEffectStack implements ShapeEffect
{

	public static ShapeEffect get( ShapeEffect... effectList )
	{
		return new ShapeEffectStack( effectList );
	}

	private List< ShapeEffect > effects = new ArrayList< ShapeEffect >();

	public ShapeEffectStack( ShapeEffect... effectList )
	{
		add( effectList );
	}

	@Override
	public void draw( Graphics2D g2, Shape s )
	{
		StateSaver saver = StateSaver.wrap( g2 );
		try
		{
			for (ShapeEffect eachEffect : effects)
			{
				eachEffect.draw(
					g2,
					s );
			}
		}
		finally
		{
			saver.restore();
		}
	}

	public final void add( ShapeEffect... shapeEffects )
	{
		effects.addAll( Arrays.asList( shapeEffects ) );
	}

	/**
	 * Adds the effects at the start of the effect stack, preserving
	 * the order of the effects passed to this argument. i.e the first effect in the
	 * passed in list will be first in the entire stack.
	 * @param shapeEffects one or more effects to pass in
	 */
	public void prepend( ShapeEffect... shapeEffects )
	{
		// need to iterate back through the passed in collection
		// so when they are inserted at the start of the main stack
		// the order is preserved.
		ListIterator< ShapeEffect > backIt = Arrays.asList(
			shapeEffects )
			.listIterator();
		while (backIt.hasPrevious())
		{
			effects.add(
				0,
				backIt.previous() );
		}

	}

}
