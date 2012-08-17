/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGLoaderClient;
import com.kitfox.svg.SVGLoaderTask;
import com.kitfox.svg.SVGUniverse;
import com.kitfox.svg.elements.ElementHierachyListener;
import com.kitfox.svg.elements.SVGElement;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;

/**
 *
 * @author jasonw
 */
public class ImageRefAttribute extends RelativeURIAttribute
{

	public ImageRefAttribute( String name )
	{
		super( name );
	}

	public ImageRefAttribute( String name, SVGElement parent )
	{
		super( name, parent );
	}

	private BufferedImage imageValue = null;

	public BufferedImage getImageValue()
	{
		return imageValue;
	}

	public void setValue( BufferedImage imageValue )
	{
		this.imageValue = imageValue;
		fireUpdate();
	}

	@Override
	public void setStringValue( String value )
	{
		super.setStringValue( value );

		/**
		 * If our parent element hasn't been added to a diagram
		 * wait until it has.
		 */
		if (parent.getParent() == null)
		{
			parent.addHierachyListener( new ElementHierachyListener()
			{

				@Override
				public void addedToParent( SVGElement parent )
				{
					loadImage();
				}
			} );
		}
		else
		{
			loadImage();
		}

	}

	URL imageRef = null;

	private void loadImage()
	{
		SVGDiagram diagram = parent.getDiagram();
		SVGUniverse svu = diagram.getUniverse();

		imageRef = svu.registerImage( getURI() );
		SVGLoaderTask loader = svu.getLoaderQueue()
			.get(
				getURI() );

		SVGLoaderClient processImage = new SVGLoaderClient()
		{

			@Override
			public void imageLoaded( URI loadedURI )
			{
				try
				{
					//Set widths if not set
					setValue( parent.getDiagram()
						.getUniverse()
						.getImage(
							imageRef ) );
				}
				catch (Exception e)
				{
					throw new RuntimeException( e );
				}

				if (imageValue == null)
				{
					//xform = new AffineTransform();
					//bounds = new Rectangle2D.Float();
					return;
				}

				//                    if ( width == 0 )
				//                    {
				//                        width = img.getWidth();
				//                    }
				//                    if ( height == 0 )
				//                    {
				//                        height = img.getHeight();
				//                    }

				//Determine image xform
				//xform = new AffineTransform();
				//        xform.setToScale(this.width / img.getWidth(), this.height / img.getHeight());
				//        xform.translate(this.x, this.y);
				//xform.translate( ImageSVG.this.x, ImageSVG.this.y );
				//                    xform.scale( ImageSVG.this.width / img.getWidth(), ImageSVG.this.height / img.getHeight() );

				//                    bounds = new Rectangle2D.Float( ImageSVG.this.x, ImageSVG.this.y, ImageSVG.this.width,
				//                                                    ImageSVG.this.height );
			}

		};
		// if loading a nested SVG file it will be queued, so
		// we need to post process it once it is loaded.
		if (loader != null)
		{
			loader.setDoAfter( processImage );
		}
		else
		{
			// for bitmaps we just process it directly
			processImage.imageLoaded( getURI() );
		}
	}

}
