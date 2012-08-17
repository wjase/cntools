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
package com.cybernostics.lib.animator.sprite;

import com.cybernostics.lib.maths.DimensionFloat;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 * @author jasonw
 *
 */
public class BitmapSprite extends IconSprite
{

	/**
	 *
	 */
	//  protected BufferedImage iconImage = null;
	/**
	 * Stops warnings - this needs to be defined to support Serialisation
	 */
	private static final long serialVersionUID = 1042786878169664455L;

	/**
	 * Scaled (and possibly rotated) versions of each frame for current sprite
	 * size
	 */
	//    protected BufferedImage[] renderedFrames;
	/**
	 * The ratio of the sprites width to its height
	 */
	//    double aspectRatio = 1.0;
	//    /**
	//     * This is the scale transform to scale the source image to the destination
	//     * size
	//     */
	//    protected AffineTransform sourceImageScaler = new AffineTransform();
	//    /**
	//     *
	//     */
	//    protected void updateImageCreator()
	//    {
	//        if ( rotationAngle == 0 )
	//        {
	//            frameCreatorTransform = sourceImageScaler;
	//        }
	//        else
	//        {
	//            AffineTransform rotateAndScale = ( AffineTransform ) sourceImageScaler.clone();
	//
	//            rotateAndScale.concatenate( AffineTransform.getRotateInstance( rotationAngle, getSourceImage( 0 ).getWidth() / 2.0, getSourceImage( 0 ).getHeight() / 2.0 ) );
	//
	//            frameCreatorTransform = rotateAndScale;
	//
	//        }
	//
	//    }
	//    /*
	//     * (non-Javadoc)
	//     *
	//     * @see com.cybernostics.lib.animator.Sprite#getFrameCount()
	//     */
	//    /**
	//     *
	//     * @return
	//     */
	//    @Override
	//    public int getFrameCount()
	//    {
	//        if ( renderedFrames != null )
	//        {
	//            return renderedFrames.length;
	//        }
	//        return -1;
	//    }
	/**
	 * Unscaled source images provided to constructor
	 */
	//private BufferedImage[] sourceImages;
	/**
	 * Sets an aspect-ratio safe multiplier for scale
	 */
	//private AffineTransform spriteScaler = new AffineTransform();
	//    BufferedImage getSourceImage( int index )
	//    {
	//        return sourceImages[ index];
	//    }
	/**
	 * Constructs a single frame AnimSprite
	 *
	 * @param name the name of the sprite in the sequence
	 * @param source - an image for the sprite
	 * @param size
	 * @see #setScale(double)
	 *
	 * @throws Exception
	 */
	public BitmapSprite( String name, BufferedImage source, DimensionFloat size )
		throws Exception
	{
		super( name );
		setRelativeSize( size );
		setIcon( new ImageIcon( source ) );

		//        BufferedImage[] sourceArray =
		//        {
		//            source
		//        };
		//    init( name, sourceArray, size );

	}

	/**
	 * Constructs an AnimSprite
	 *
	 * @param name
	 * @param source - an array of image frames for the sprite
	 * @param size - the "full" size of the sprite in screen units
	 * @see
	 *            {@link #setSize(DimensionFloat)}
	 *
	 * This size can be further changed by calling setScale.
	 * @see #setScale(double)
	 * @throws Exception
	 */
	//    public BitmapSprite( String name, BufferedImage[] source, DimensionFloat size ) throws Exception
	//    {
	//        super( name, size );
	//        init( name, source, size );
	//    }
	/**
	 * @param string
	 * @param img
	 * @param itemRectangle
	 * @throws Exception
	 */
	public BitmapSprite(
		String string,
		BufferedImage img,
		Rectangle2D itemRectangle ) throws Exception
	{
		this( string, img, new DimensionFloat( itemRectangle.getWidth(),
			itemRectangle.getHeight() ) );
		setRelativeLocation(
			(float) itemRectangle.getMinX(),
			(float) itemRectangle.getMinY() );
	}

}
