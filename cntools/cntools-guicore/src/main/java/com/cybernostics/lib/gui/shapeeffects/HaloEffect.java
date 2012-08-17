package com.cybernostics.lib.gui.shapeeffects;

import com.cybernostics.lib.gui.graphics.StateSaver;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.util.ArrayList;

/**
 *
 * @author jasonw
 */
public class HaloEffect implements ShapeEffect
{

	private final int NUM_BANDS = 3;

	public HaloEffect( int width, Paint haloPaint )
	{
		this.haloPaint = haloPaint;
		int finalWidth = 2 * width;
		int strokeDelta = finalWidth / NUM_BANDS; // three halo steps

		ArrayList< Stroke > strokesTemp = new ArrayList< Stroke >();

		for (int index = 0; index < NUM_BANDS; ++index)
		{
			int currentStrokeWidth = finalWidth - ( index * strokeDelta );
			strokesTemp.add( new BasicStroke( currentStrokeWidth,
				BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND ) );
		}

		this.strokes = new Stroke[ NUM_BANDS ];

		strokesTemp.toArray( strokes );

	}

	AlphaComposite[] bands =
	{ AlphaComposite.getInstance(
		AlphaComposite.SRC_OVER,
		0.3f ),
		AlphaComposite.getInstance(
			AlphaComposite.SRC_OVER,
			0.7f ),
		AlphaComposite.getInstance(
			AlphaComposite.SRC_OVER,
			1.0f ) };
	Paint haloPaint = null;
	Stroke[] strokes = null;

	@Override
	public void draw( Graphics2D g2, Shape s )
	{
		StateSaver sg = StateSaver.wrap( g2 );
		try
		{
			// we only want our halo outside the item
			// so set the clip to exclude it
			Area clipArea = new Area( sg.getClip() );
			Area withoutShape = (Area) clipArea.clone();
			withoutShape.subtract( new Area( s ) );
			sg.setClip( withoutShape );
			sg.setPaint( haloPaint );
			for (int bandIndex = 0; bandIndex < NUM_BANDS; ++bandIndex)
			{
				sg.setComposite( bands[ bandIndex ] );
				sg.setStroke( strokes[ bandIndex ] );
				sg.draw( s );
			}

		}
		finally
		{
			sg.restore();
		}
	}
}
