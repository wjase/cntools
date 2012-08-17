package com.cybernostics.lib.svg.editor.actions;

import com.cybernostics.lib.concurrent.GUIEventThread;
import com.cybernostics.lib.svg.editor.SVGDrawing;
import com.cybernostics.lib.svg.editor.SVGEditor;
import java.awt.event.ActionEvent;
import javax.swing.Action;

/**
 *
 * @author jasonw
 */
public class StrokeFillModeAction extends SVGEditorAction
{

	public enum FillStrokeMode
	{

		STROKED, FILLED_AND_STROKED, FILLED
	}

	public static final String ACTION_NAME_KEY = "Fill or Outline";

	public StrokeFillModeAction( SVGEditor editor )
	{
		super( editor );
		putValue(
			Action.SHORT_DESCRIPTION,
			ACTION_NAME_KEY );
	}

	// The direction ensures a physical switch-like behaviour where the ordinal
	// follows a 0 1 2 1 0 1 2... etc pattern.
	private int direction = 1;

	@Override
	public void onActionPerformed( ActionEvent e )
	{
		FillStrokeMode mode = getMode( getEditor().getDrawing() );
		switch (mode)
		{
			case STROKED:
				direction = 1;
				break;
			case FILLED:
				direction = -1;
				break;
			default:
				break;
		}
		int nextMode = mode.ordinal() + direction;

		setStrokeFillMode( FillStrokeMode.values()[ nextMode ] );

	}

	public void setStrokeFillMode( FillStrokeMode mode )
	{
		setMode(
			mode,
			getEditor().getDrawing() );
	}

	FillStrokeMode mode = null;

	public void update()
	{
		final FillStrokeMode newMode = getMode( getEditor().getDrawing() );
		if (mode != newMode)
		{
			mode = newMode;
			GUIEventThread.run( new Runnable()
			{

				@Override
				public void run()
				{
					setStrokeFillMode( newMode );
				}
			} );

		}
	}

	public static void setMode( FillStrokeMode mode, SVGDrawing dwg )
	{
		switch (mode)
		{
			case FILLED:
				dwg.setFilled( true );
				dwg.setStroked( false );
				break;
			case FILLED_AND_STROKED:
				dwg.setFilled( true );
				dwg.setStroked( true );
				break;
			case STROKED:
				dwg.setStroked( true );
				dwg.setFilled( false );
				break;
			default:
				break;
		}
	}

	public static FillStrokeMode getMode( SVGDrawing dwg )
	{
		boolean filled = dwg.isFilled();
		boolean stroked = dwg.isStroked();

		FillStrokeMode current = FillStrokeMode.STROKED;

		if (stroked && filled)
		{
			current = FillStrokeMode.FILLED_AND_STROKED;
		}
		else
			if (stroked)
			{
				current = FillStrokeMode.STROKED;
			}
			else
			{
				current = FillStrokeMode.FILLED;
			}

		return current;

	}
}
