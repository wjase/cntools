package com.cybernostics.lib.media.icon;

import com.cybernostics.lib.svg.SVGUtil;
import com.kitfox.svg.elements.SVGElement;
import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;

/**
 *
 * @author jasonw
 */
public class MultiframeSVGIcon implements Icon
{

	private ScalableSVGIcon internal;

	private int frame = 0;

	private List< SVGElement > frames = null;

	public static List< Icon > getFrames( final ScalableSVGIcon s )
	{
		final List< SVGElement > frameElements = collectFrameElements( s );

		List< Icon > frames = new ArrayList< Icon >();
		for (int index = 0; index < frameElements.size(); ++index)
		{
			frames.add( new MultiframeSVGIcon( s, index, frameElements ) );
		}

		return frames;
	}

	private static final String framepattern = "frame_%d";

	private MultiframeSVGIcon(
		ScalableSVGIcon toRender,
		int frameNo,
		List< SVGElement > frames )
	{
		this.internal = toRender;
		this.frame = frameNo;
		this.frames = frames;

	}

	@Override
	public void paintIcon( Component comp, Graphics gg, int x, int y )
	{
		setFrame( frame );
		internal.paintIcon(
			comp,
			gg,
			x,
			y );
	}

	private static List< SVGElement > collectFrameElements( ScalableSVGIcon s )
	{
		int frameNumber = 0;

		List< SVGElement > frames = new ArrayList< SVGElement >();

		SVGElement nextFrame = null;
		while (null != ( nextFrame = s.getDiagram()
			.getElement(
				String.format(
					framepattern,
					frameNumber ) ) ))
		{
			frames.add( nextFrame );
			++frameNumber;
		}
		return frames;
	}

	private void setFrame( int iFrame )
	{
		// hide em all
		for (int index = 0; index < frames.size(); ++index)
		{
			SVGUtil.setElementVisible(
				frames.get( index ),
				index == iFrame );
		}

	}

	@Override
	public int getIconWidth()
	{
		return internal.getIconWidth();
	}

	@Override
	public int getIconHeight()
	{
		return internal.getIconHeight();
	}

}
