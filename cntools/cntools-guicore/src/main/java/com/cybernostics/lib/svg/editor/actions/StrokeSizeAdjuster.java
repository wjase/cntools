package com.cybernostics.lib.svg.editor.actions;

import com.cybernostics.lib.svg.editor.SVGEditor;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 *
 * @author jasonw
 */
public class StrokeSizeAdjuster implements MouseWheelListener
{

	SVGEditor theEditor = null;

	public StrokeSizeAdjuster( SVGEditor e )
	{
		theEditor = e;
	}

	SVGEditor getEditor()
	{
		return theEditor;
	}

	@Override
	public void mouseWheelMoved( MouseWheelEvent e )
	{

		if (e.getWheelRotation() > 0)
		{
			increaseStroke();
		}
		else
		{
			decreaseStroke();
		}
	}

	public void setStrokeSize( int multiple )
	{
		getEditor().getDrawing()
			.setStrokeWidthMultiple(
				multiple );
	}

	public int getStrokeSize()
	{
		return getEditor().getDrawing()
			.getStrokeWidthMultiple();
	}

	public void increaseStroke()
	{
		getEditor().getDrawing()
			.setStrokeWidthMultiple(
				getEditor().getDrawing()
					.getStrokeWidthMultiple() + 1 );
	}

	public void decreaseStroke()
	{
		int strokeMultiple = getEditor().getDrawing()
			.getStrokeWidthMultiple();
		--strokeMultiple;

		// only update if greater than zero...
		if (strokeMultiple > 0)
		{
			getEditor().getDrawing()
				.setStrokeWidthMultiple(
					strokeMultiple );
		}

	}

}
