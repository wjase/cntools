package com.cybernostics.lib.gui.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * This class implements no-brainer state preservation for graphics objects
 * Usage:
 *
 * <pre>
 * StateSaver save = new StateSaver( g );
 * try
 * {
 * 	//do Graphics operations
 * }
 * finally
 * {
 * 	save.restore();
 * }
 * </pre>
 *
 * @author jasonw
 *
 */
public class StateSaver extends StateSaverGraphicsCreator
{

	public static StateSaver wrap( Graphics2D g2 )
	{
		return new StateSaver( g2 );
	}

	public static StateSaver wrap( Graphics g2 )
	{
		return new StateSaver( g2 );
	}

	//    public enum Interpolation
	//    {
	//
	//        NEAREST_NEIGHBOR,
	//        BILINEAR,
	//        BICUBIC
	//    }
	//
	//    private Graphics2D theContext = null;
	//
	//    private Shape savedClip = null;
	//
	//    private Stroke savedStroke = null;
	//
	//    private Paint savedPaint = null;
	//
	//    private Composite savedComposite = null;
	//
	//    private AffineTransform savedTransform = null;
	//
	//    private Object savedAliasHint = null;
	//
	//    private Object savedInterpolationHint = null;
	//
	//    public Graphics2D g()
	//    {
	//        return theContext;
	//    }
	//
	//    /**
	//     * 
	//     */
	//    public StateSaver( Graphics g )
	//    {
	//        theContext = ( Graphics2D ) g;
	//        savedClip = theContext.getClip();
	//        savedComposite = theContext.getComposite();
	//        savedPaint = theContext.getPaint();
	//        savedStroke = theContext.getStroke();
	//        savedTransform = theContext.getTransform();
	//        savedAliasHint = theContext.getRenderingHint( RenderingHints.KEY_ANTIALIASING );
	//        savedInterpolationHint = theContext.getRenderingHint( RenderingHints.KEY_INTERPOLATION );
	//
	//    }
	//
	//    public void setInterpolation( Interpolation value )
	//    {
	//        Object opt = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
	//        if ( value == Interpolation.BICUBIC )
	//        {
	//            opt = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
	//        }
	//        if ( value == Interpolation.NEAREST_NEIGHBOR )
	//        {
	//            opt = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
	//        }
	//
	//        theContext.setRenderingHint( RenderingHints.KEY_INTERPOLATION, opt );
	//    }
	//
	//    public void setAntiAlias( boolean useAntiAlias )
	//    {
	//        theContext.setRenderingHint( RenderingHints.KEY_ANTIALIASING, useAntiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF );
	//    }
	//
	//    public void restore()
	//    {
	//        // restore in reverse order...
	//        theContext.setTransform( savedTransform );
	//        theContext.setStroke( savedStroke );
	//        theContext.setPaint( savedPaint );
	//        theContext.setComposite( savedComposite );
	//        theContext.setClip( savedClip );
	//        if ( savedInterpolationHint != null )
	//        {
	//            theContext.setRenderingHint( RenderingHints.KEY_INTERPOLATION, savedInterpolationHint );
	//        }
	//        if ( savedAliasHint != null )
	//        {
	//            theContext.setRenderingHint( RenderingHints.KEY_ANTIALIASING, savedAliasHint );
	//        }
	//
	//    }

	private StateSaver( Graphics g )
	{
		super( (Graphics2D) g );
	}

	private Graphics2D g()
	{
		return this;
	}

}
