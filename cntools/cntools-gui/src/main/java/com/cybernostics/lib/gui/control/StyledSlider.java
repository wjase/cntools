package com.cybernostics.lib.gui.control;

import com.cybernostics.lib.animator.SVGBackgroundImage;
import com.cybernostics.lib.gui.graphics.StateSaver;
import com.cybernostics.lib.gui.layout.RelativeLayout;
import com.cybernostics.lib.gui.shapeeffects.CompositeAdjust;
import com.cybernostics.lib.gui.shapeeffects.ShapedPanel;

import com.cybernostics.lib.media.icon.IconLoadClient;
import com.cybernostics.lib.media.icon.SVGIconLoader;
import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import com.cybernostics.lib.svg.SVGUtil;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.net.URL;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

public class StyledSlider extends ShapedPanel
{

	private ScalableSVGIcon thumbHandle = null;

	public void setThumbHandle( ScalableSVGIcon thumbHandle )
	{
		this.thumbHandle = thumbHandle;
	}

	private SingletonInstance< JSlider > jsl = new SingletonInstance< JSlider >()
	{

		@Override
		protected JSlider createInstance()
		{
			JSlider js = new JSlider( 0, 100 );

			js.setOpaque( false );
			BasicSliderUI bui = new BasicSliderUI( js )
			{

				private CompositeAdjust adj = new CompositeAdjust( 0.8 );

				@Override
				public void paintTrack( Graphics g )
				{
					StateSaver g2 = StateSaver.wrap( g );
					try
					{
						adj.draw(
							g2,
							null );
						super.paintTrack( g2 );
					}
					finally
					{
						g2.restore();
					}
				}

				@Override
				protected Dimension getThumbSize()
				{
					if (thumbHandle != null)
					{
						Dimension d = thumbHandle.getPreferredSize();
						d.width += 1;
						d.height += 1;
						return d;
					}
					return super.getThumbSize();
				}

				@Override
				public void paintThumb( Graphics g )
				{

					if (thumbHandle != null)
					{
						Graphics2D g2 = (Graphics2D) g;
						AffineTransform at = g2.getTransform();
						g.translate(
							thumbRect.x,
							thumbRect.y );
						thumbHandle.paintIcon(
							slider,
							g,
							0,
							0 );
						g2.setTransform( at );
					}
					else
					{
						super.paintThumb( g );
					}
					//                StateSaver g2 = StateSaver.wrap( g );
					//                g2.setClip( thumbRect );
					//                thumbImage.paintIcon( slider, g, thumbRect.x, thumbRect.y );
					//                g2.restore();
				}

			};
			js.setUI( bui );
			return js;
		}

	};

	private ScalableSVGIcon background = null;

	public JSlider getSlider()
	{
		return jsl.get();
	}

	public StyledSlider( final String regionId, final URL backgroundImageURL )
	{

		setLayout( new RelativeLayout() );
		SVGIconLoader.load(
			backgroundImageURL,
			new IconLoadClient()
		{

			@Override
			public void iconLoaded( ScalableSVGIcon svgsi )
			{
				setBackgroundPainter( new SVGBackgroundImage( svgsi ) );
				add(
					jsl.get(),
					SVGUtil.getSubItemRectangle(
						regionId,
						svgsi.getDiagram() ) );
				revalidate();
				repaint();
			}

		} );

		//        ScalableSVGIcon ic = ScalableSVGIcon.loadIcon( backgroundImageURL )
		//        setAssetLoader( new SceneLoader()
		//		{
		//
		//			public ShapeEffect loadBackGround()
		//			{
		//				background = new ScalableSVGIcon( backgroundImageURL );
		//				return new SVGBackgroundImage( background );
		//			}
		//
		//			public void loadAssets( Scene panel )
		//			{
		//				addWithinBounds( jsl.get(), SVGUtil.getSubItemRectangle( regionId, background.getDiagram() ) );
		//				invalidate();
		//				repaint();
		//
		//			}
		//
		//		} );

	}

	public int getValue()
	{
		return getSlider().getValue();
	}

	public void setValue( int value )
	{
		getSlider().setValue(
			value );
	}

}
