/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.cybernostics.lib.animator.sprite.component;

import com.cybernostics.lib.animator.sprite.ISprite;
import com.cybernostics.lib.animator.sprite.animators.SpriteMover;
import com.cybernostics.lib.animator.track.AdapterAnimatorTrack;
import com.cybernostics.lib.animator.track.Sequencer;
import com.cybernostics.lib.animator.track.Track;
import com.cybernostics.lib.animator.track.ordering.TrackEndedListener;
import com.cybernostics.lib.animator.ui.AnimatedScene;
import com.cybernostics.lib.animator.ui.transitions.EasingFunction;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 *
 * @author jasonw
 */
public class ToolBarSlider
{

	public enum Dock
	{
		NORTH,
		SOUTH,
		EAST,
		WEST
	}

	public enum States
	{
		IN, OUT, SLIDING
	}

	private AnimatedScene parent;

	private ISprite toMove;

	private Line2D.Double path = new Line2D.Double();

	private boolean easeOut = true;

	private AdapterAnimatorTrack< Point2D > slideInOut;

	private static final long duration = 800;

	private States state = States.OUT;

	public States getState()
	{
		return state;
	}

	public ToolBarSlider( AnimatedScene parent, ISprite comp, Dock where )
	{
		this.parent = parent;
		this.toMove = comp;

		slideInOut = SpriteMover.moveLine(
			comp,
			comp.getRelativeLocation(),
			getEndPoint(
				comp,
				where ),
			duration );
		slideInOut.setTransition( EasingFunction.easeOutBounce.get() );

		slideInOut.addTrackEndedListener( new TrackEndedListener()
		{

			@Override
			public void trackEnded( Track source )
			{
				toggleDirection();
			}

		} );

	}

	private void toggleDirection()
	{
		if (easeOut)
		{
			slideInOut.setTransition( EasingFunction.easeInBounce.get() );
			easeOut = false;
			state = States.OUT;
		}
		else
		{
			slideInOut.setTransition( EasingFunction.easeOutBounce.get() );
			easeOut = true;
			state = States.IN;
		}
	}

	private Point2D getEndPoint( ISprite comp, Dock where )
	{
		Point2D pStart = comp.getRelativeLocation();
		Point2D pEnd = new Point2D.Double();

		switch (where)
		{
			case EAST:
				pEnd.setLocation(
					0.99 + comp.getRelativeSize()
						.getWidth(),
					pStart.getY() );
				break;
			case NORTH:
				pEnd.setLocation(
					pStart.getX(),
					0.01 - comp.getRelativeSize()
						.getHeight() );
				break;
			case WEST:
				pEnd.setLocation(
					0.01 - comp.getRelativeSize()
						.getWidth(),
					pStart.getY() );
				break;
			case SOUTH:
				pEnd.setLocation(
					pStart.getX(),
					0.99 + comp.getRelativeSize()
						.getHeight() );
				break;
		}
		return pEnd;
	}

	public void slideInOut()
	{
		state = States.SLIDING;
		slideInOut.reset();
		Sequencer.get()
			.addAndStartTrack(
				slideInOut );
	}

}
