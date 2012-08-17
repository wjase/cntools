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

package com.cybernostics.lib.animator.track;

import com.cybernostics.lib.animator.paramaterised.LinearPath;
import com.cybernostics.lib.animator.ui.AnimatedScene;
import com.cybernostics.lib.concurrent.DurationRequiredException;
import com.cybernostics.lib.concurrent.TimeStamp;
import com.cybernostics.lib.exceptions.AppExceptionManager;
import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.gui.RelativeBoundsManager;
import com.cybernostics.lib.gui.declarative.events.WhenClicked;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Animates the bounds of the specified component.
 *
 *
 * @author jasonw
 *
 */
public class ComponentZoomTrack extends BasicTimerTrack
{

	/**
	 *
	 * @param args
	 */
	public static void main( String[] args )
	{
		JFrame jf = new JFrame( "Component Zoom test" );
		jf.setSize(
			500,
			500 );
		final AnimatedScene ap = new AnimatedScene();
		jf.setContentPane( ap );
		ap.setLayout( null );

		JButton animateMe = new JButton( "AnimateME" );
		ap.add( animateMe );
		RelativeBoundsManager rbm = RelativeBoundsManager.bind(
			animateMe,
			null );
		rbm.setBounds( new Rectangle2D.Double( 0.5f, 0.5f, 0.1f, 0.1f ) );

		final ComponentZoomTrack ct = new ComponentZoomTrack( "ZoomOut",
			rbm,
			new Rectangle2D.Double( 0.5f, 0.5f, 0.1f,
				0.1f ),
			new Rectangle2D.Double( 0.0f, 0.0f, 1.0f, 1.0f ),
			200 );
		new WhenClicked( animateMe )
		{
			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * com.cybernostics.lib.gui.declarative.events.WhenClicked#doThis
			 * (java.awt.event.ActionEvent)
			 */

			@Override
			public void doThis( ActionEvent e )
			{
				Sequencer.get()
					.addAndStartTrack(
						ct );

			}

		};
		jf.setVisible( true );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

	}

	/**
	 * The component to be animated
	 */
	private RelativeBoundsManager toZoom = null;

	/**
	 * This object instance is reused
	 */
	Rectangle2D currentBounds = new Rectangle2D.Double();

	Rectangle2D startRelative = null;

	Rectangle2D finalRelative = null;

	/**
	 * this is the stop - start for width and height
	 */
	double widthDelta = 0;

	double heightDelta = 0;

	double locXDelta = 0;

	double locYDelta = 0;

	LinearPath locationPath = null;

	/**
	 *
	 * @param name
	 * @param rbm
	 * @param startRelative
	 * @param finalRelative
	 * @param duration
	 */
	public ComponentZoomTrack(
		String name,
		RelativeBoundsManager rbm,
		Rectangle2D startRelative,
		Rectangle2D finalRelative,
		long duration )
	{
		super( name, duration );

		this.toZoom = rbm;

		if (startRelative != null)
		{
			this.startRelative = startRelative.getBounds2D();
		}
		else
		{
			startRelative = rbm.getBounds();

		}
		Point2D.Double startLocation = new Point2D.Double( startRelative.getMinX(),
			startRelative.getMinY() );
		Point2D.Double finalLocation = new Point2D.Double( finalRelative.getMinX(),
			finalRelative.getMinY() );

		locationPath = new LinearPath( startLocation, finalLocation );

		this.finalRelative = finalRelative;

		widthDelta = finalRelative.getWidth() - startRelative.getWidth();
		heightDelta = finalRelative.getHeight() - startRelative.getHeight();

		locXDelta = finalLocation.x - startLocation.x;
		locYDelta = finalLocation.y - startLocation.y;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.cybernostics.lib.animator.track.BasicTrack#start()
	 */
	@Override
	public void start()
	{
		super.start();
	}

	/**
	 * @param fractionElapsed a value between 0 and 1 once the duration has
	 * elapsed
	 */
	@Override
	public void update( float fractionElapsed )
	{

		// System.out.printf("elapsed: %f\n",fractionElapsed);
		currentBounds.setFrame(
			startRelative.getMinX() + ( locXDelta * fractionElapsed ),
			startRelative.getMinY()
				+ ( locYDelta * fractionElapsed ),
			startRelative.getWidth() + ( widthDelta * fractionElapsed ),
			startRelative
				.getHeight()
				+ ( heightDelta * fractionElapsed ) );

		toZoom.setBounds( currentBounds );
	}

	/**
	 * @return
	 */
	public Rectangle2D getStopRect()
	{
		return finalRelative;
	}

	/**
	 *
	 * @return
	 */
	public Rectangle2D getStartRect()
	{
		return startRelative;
	}

}
