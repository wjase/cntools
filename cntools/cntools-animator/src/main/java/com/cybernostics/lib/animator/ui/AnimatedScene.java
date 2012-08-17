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

import com.cybernostics.lib.animator.sprite.ISprite;
import com.cybernostics.lib.animator.sprite.OwnerSizeListener;
import com.cybernostics.lib.animator.sprite.WatchableSprite;
import com.cybernostics.lib.animator.sprite.animators.SpriteFader;
import com.cybernostics.lib.animator.sprite.component.ComponentSprite;
import com.cybernostics.lib.animator.sprite.component.JLabelSprite;
import com.cybernostics.lib.animator.track.Sequencer;
import com.cybernostics.lib.animator.track.Track;
import com.cybernostics.lib.animator.track.TrackGroup;
import com.cybernostics.lib.animator.track.characteranimate.AnimatedCharacter;
import com.cybernostics.lib.animator.track.ordering.TrackContainer;
import com.cybernostics.lib.concurrent.CallableWorkerTask;
import com.cybernostics.lib.concurrent.GUIEventThread;
import com.cybernostics.lib.gui.ParentSize;
import com.cybernostics.lib.gui.shapeeffects.ShapeEffect;
import com.cybernostics.lib.gui.shapeeffects.ShapedPanel;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import com.cybernostics.lib.svg.SubRegionContainer;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author jasonw
 */
public class AnimatedScene extends ShapedPanel
	implements
	SubRegionContainer,
	Scene
{

	public static final String BACKGROUND = "background";

	protected JLayeredPane layers = new JLayeredPane();

	private static final SingletonInstance< SubRegionContainer > NOPContainer =
																new SingletonInstance< SubRegionContainer >()
	{

		@Override
		protected SubRegionContainer createInstance()
		{
			return new SubRegionContainer()
			{

				private Rectangle2D nullRect = new Rectangle2D.Double( 0,
					0,
					0,
					0 );

				@Override
				public Rectangle2D getItemRectangle( String regionName )
				{
					return nullRect;
				}

			};
		}

	};

	private SubRegionContainer regionContainer = NOPContainer.get();

	public SubRegionContainer getSubRegionContainer()
	{
		return regionContainer;
	}

	public void setSubRegionContainer( SubRegionContainer value )
	{
		regionContainer = value;
	}

	public ShapedPanel getBackgroundComponent()
	{
		ShapedPanel bg = (ShapedPanel) getChildById( BACKGROUND );
		if (bg == null)
		{
			bg = new ShapedPanel();
			bg.setUI( null );
			bg.putClientProperty(
				ComponentSprite.ID_PROP,
				BACKGROUND );
			ParentSize.bind(
				AnimatedScene.this,
				bg );
			layers.add(
				bg,
				new Integer( -100 ),
				0 );
		}

		return bg;
	}

	private void setSceneBackground( final ShapeEffect loadBackGround )
	{
		ShapedPanel backGround = getBackgroundComponent();
		backGround.setBackgroundPainter( loadBackGround );
		backGround.revalidate();
		backGround.repaint();

		if (loadBackGround instanceof SubRegionContainer)
		{
			setSubRegionContainer( (SubRegionContainer) loadBackGround );
		}

		revalidate();
		repaint();
	}

	/**
	 * Allows class hierarchies to "override" asset loading
	 */
	private SceneLoaderStack assetLoaders = new SceneLoaderStack();

	public SceneLoader getAssetLoader()
	{
		return assetLoaders;
	}

	// Kicks off the thread which initialises this scene by loading everything
	public void setAssetLoader( SceneLoader assetLoader )
	{
		this.assetLoaders.addLoader( assetLoader );
	}

	/**
	 * Call this method explicitly after the constructor to start loading scene
	 * assets as soon as possible or it will be called when this panel is added
	 * to a container. (It will only be called once)
	 */
	public void loadAssets()
	{
		if (initTask == null)
		{
			initTask = initialisation.get()
				.start();
		}

	}

	private Future< Object > initTask = null;

	private SingletonInstance< CallableWorkerTask > initialisation = new SingletonInstance< CallableWorkerTask >()
	{

		@Override
		protected CallableWorkerTask createInstance()
		{
			return new CallableWorkerTask( "" )
			{

				@Override
				protected Object doTask() throws Exception
				{
					setSceneBackground( assetLoaders.loadBackGround() );
					assetLoaders.loadAssets( AnimatedScene.this );
					initSceneElements();
					start();
					fireSceneReady();
					revalidate();
					repaint();
					return null;
				}

			};
		}

	};

	private void init( Sequencer seq )
	{
		setUI( null );
		tracks.setSequencer( seq );

		add( layers );

		addComponentListener( new ComponentAdapter()
		{

			@Override
			public void componentResized( ComponentEvent e )
			{
				fireSizeUpdated();
			}

		} );

		addAncestorListener( new AncestorListener()
		{

			@Override
			public void ancestorAdded( AncestorEvent event )
			{
				GUIEventThread.runLater( new Runnable()
				{

					@Override
					public void run()
					{
						ParentSize.bind(
							AnimatedScene.this,
							layers );
						loadAssets();
						try
						{
							initialisation.get()
								.get();
						}
						catch (Exception ex)
						{
							Logger.getLogger(
								AnimatedScene.class.getName() )
								.log(
									Level.SEVERE,
									null,
									ex );
						}
						fireSizeUpdated();
						fireSceneReady();
					}

				} );
			}

			@Override
			public void ancestorRemoved( AncestorEvent event )
			{
			}

			@Override
			public void ancestorMoved( AncestorEvent event )
			{
			}

		} );

		NoseyRepaintManager.install( this );
	}

	public AnimatedScene( Sequencer seq )
	{
		init( seq );
		layers.putClientProperty(
			ComponentSprite.ID_PROP,
			"layerPanel" );
	}

	public AnimatedScene()
	{
		init( null );

	}

	public void fireSizeUpdated()
	{
		atScaler.setToScale(
			getWidth(),
			getHeight() );

		for (int index = 0; index < layers.getComponentCount(); ++index)
		{
			Component child = layers.getComponent( index );
			if (child instanceof ISprite)
			{
				( (ISprite) child ).ownerSizeUpdated();
			}

		}
		revalidate();
		repaint();
	}

	@Override
	public void addUpdateListener( OwnerSizeListener listener )
	{
	}

	AffineTransform atScaler = new AffineTransform();

	@Override
	public AffineTransform getTransform()
	{
		return atScaler;
	}

	@Override
	public void removeSprites( ISprite... toRemove )
	{
		for (ISprite eachSprite : toRemove)
		{
			String id = eachSprite.getId();
			ComponentSprite cs = (ComponentSprite) getChildById( id );
			layers.remove( cs );
		}
	}

	@Override
	public void addSprites( ISprite... toAdd )
	{
		for (ISprite eachSprite : toAdd)
		{
			if (eachSprite instanceof JComponent)
			{
				layers.add(
					(JComponent) eachSprite,
					new Integer( eachSprite.getZ_order() ),
					0 );
			}
			else
			{
				if (eachSprite instanceof WatchableSprite)
				{
					ISprite eachToAdd = ( (WatchableSprite) eachSprite ).getWatched();
					addSprites( eachToAdd );
				}
				else
				{
					layers.add(
						JLabelSprite.create( eachSprite ),
						new Integer( eachSprite.getZ_order() ),
						0 );
				}

			}

			eachSprite.setOwner( this );
		}
		revalidate();
		repaint();
		fireSizeUpdated();
	}

	@Override
	public ISprite getById( String id )
	{
		JComponent child = getChildById( id );
		if (child instanceof ISprite)
		{
			return (ISprite) child;
		}
		return null; // not a sprite

	}

	public JComponent getChildById( String id )
	{
		for (int index = 0; index < layers.getComponentCount(); ++index)
		{
			Component c = layers.getComponent( index );
			if (c instanceof JComponent)
			{
				if (c instanceof ComponentSprite)
				{
					if (( (ComponentSprite) c ).getId()
						.equals(
							id ))
					{
						return (JComponent) c;
					}
				}
				JComponent child = (JComponent) c;
				Object propVal = child.getClientProperty( ComponentSprite.ID_PROP );

				if (propVal != null)
				{
					if (propVal.toString()
						.equals(
							id ))
					{
						return child;
					}
				}

			}
		}
		return null;
	}

	@Override
	public void initSceneElements()
	{
	}

	@Override
	public void fireSceneReady()
	{
		fireSizeUpdated();
	}

	@Override
	public Rectangle2D getItemRectangle( String regionName )
	{
		return getSubRegionContainer().getItemRectangle(
			regionName );
	}

	public Track fadeIn()
	{
		Track t = null;
		ComponentSprite cs = ComponentSprite.getFrom( this );
		if (cs != null)
		{
			t = SpriteFader.fadeIn(
				cs,
				500 );
			Sequencer.get()
				.addAndStartTrack(
					t );

		}
		return t;
	}

	public Track fadeOut()
	{
		Track t = null;
		ComponentSprite cs = ComponentSprite.getFrom( this );
		if (cs != null)
		{
			t = SpriteFader.fadeOut(
				cs,
				500 );
			Sequencer.get()
				.addAndStartTrack(
					t );

		}
		return t;
	}

	private TrackGroup tracks = new TrackGroup();

	@Override
	public void addTrack( Track t )
	{
		tracks.add( t );
	}

	public TrackContainer getTrackGroup()
	{
		return tracks;
	}

	@Override
	public void startTrack( Track t )
	{
		if (t.getSequencer() == null)
		{
			tracks.add( t );

		}
		t.start();
	}

	@Override
	public void stop()
	{
		tracks.stop();
	}

	public void pause()
	{
		tracks.pause();
	}

	public void resume()
	{
		tracks.resume();
	}

	private final Map< String, AnimatedCharacter > characters = new TreeMap< String, AnimatedCharacter >();

	/**
	 *
	 * @param toAdd
	 */
	public void addCharacter( AnimatedCharacter toAdd )
	{
		characters.put(
			toAdd.getName(),
			toAdd );
		addSprites( toAdd.getSprite() );
	}

	public Component addWithinBounds( JComponent comp, Rectangle2D bounds2D )
	{
		ComponentSprite cs = ComponentSprite.getFrom( comp );
		if (cs == null)
		{
			cs = new ComponentSprite( comp );
		}
		addSprite(
			cs,
			bounds2D );
		return comp;
	}

	/**
	 *
	 * @param newSprite
	 * @param svgBoxName
	 */
	public void addSprite( ISprite newSprite, Rectangle2D componentSVGRect )
	{
		addSprites( newSprite );
		newSprite.setRelativeBounds( componentSVGRect );
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	public AnimatedCharacter getCharacter( String name )
	{
		return characters.get( name );
	}

	public void removeCharacters( Collection< AnimatedCharacter > toRemove )
	{
		for (AnimatedCharacter eachChar : toRemove)
		{
			characters.remove( eachChar.getName() );
		}
	}

}
