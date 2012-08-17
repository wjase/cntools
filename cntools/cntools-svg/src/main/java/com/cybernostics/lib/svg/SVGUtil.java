package com.cybernostics.lib.svg;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.animation.AnimationElement;
import com.kitfox.svg.app.beans.SVGIcon;
import com.kitfox.svg.elements.RenderableElement;
import com.kitfox.svg.elements.SVGElement;
import com.kitfox.svg.elements.ShapeElement;
import com.kitfox.svg.elements.TransformableElement;
import com.kitfox.svg.xml.attributes.StringAttribute;
import com.kitfox.svg.xml.attributes.StyleAttribute;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SVGUtil
{

	final static private String attName = "display";

	/**
	 * Tromps off down the hierarchy calling process on the visitor at each node
	 * along the way.
	 */
	public static void traverse( SVGElement start, SVGVisitor visitor )
	{
		visitor.process( start );
		int count = start.getNumChildren();
		for (int index = 0; index < count; ++index)
		{
			traverse(
				start.getChild( index ),
				visitor );
		}
	}

	public static void setElementVisible( SVGElement e, boolean visible )
	{
		if (e == null)
		{
			return;
		}
		String value = visible ? "inline" : "none";

		// System.out.printf( "%s %s\n", visible ? "show" : "hide", e.getId() );
		try
		{
			if (e.hasAttribute(
				attName,
				AnimationElement.AT_CSS ))
			{
				e.setAttribute(
					attName,
					AnimationElement.AT_CSS,
					value );
			}
			else
			{
				e.addAttribute(
					attName,
					AnimationElement.AT_CSS,
					value );
			}

		}
		catch (SVGException e1)
		{
			UnhandledExceptionManager.handleException( e1 );
		}
	}

	public static Rectangle2D getSubItemRectangle( String itemId, SVGIcon ic )
	{
		return getSubItemRectangle(
			itemId,
			ic.getSvgUniverse()
				.getDiagram(
					ic.getSvgURI() ) );
	}

	public static Rectangle2D getSubItemRectangle( String itemId, SVGDiagram dia )
	{
		SVGElement el = dia.getElement( itemId );
		return getSubItemRectangle(
			el,
			dia );
	}

	public static Rectangle2D getSubItemRectangle( SVGElement el, SVGDiagram dia )
	{

		SVGElement root = dia.getRoot();

		// create a scaled rectangle relative to this width and height
		float diaWidth = dia.getWidth();
		float diaHeight = dia.getHeight();

		if (el != null)
		{
			float xf = 0, yf = 0, width = 0, height = 0;

			if (el instanceof ShapeElement)
			{
				ShapeElement itemasElement = (ShapeElement) el;
				Rectangle2D rect2D = null;
				try
				{
					rect2D = itemasElement.getBoundingBox();
					SVGElement parent = itemasElement.getParent();

					while (parent != root)
					{
						if (parent instanceof TransformableElement)
						{
							TransformableElement transformableParent = (TransformableElement) parent;
							rect2D = transformableParent.boundsToParent( rect2D );
						}
						parent = parent.getParent();
					}

					xf = (float) rect2D.getMinX();
					yf = (float) rect2D.getMinY();
					width = (float) rect2D.getWidth();
					height = (float) rect2D.getHeight();

				}
				catch (SVGException e)
				{
					UnhandledExceptionManager.handleException( e );
				}
			}

			//
			Rectangle2D itemRect = new Rectangle2D.Double( ( xf / diaWidth ),
				( yf / diaHeight ),
				( width / diaWidth ),
															( height / diaHeight ) );

			return itemRect;
			//

		}
		return null;
	}

	public void setAttribute( SVGElement e, String property, String value )
	{
		StringAttribute sty = new StringAttribute( property );
		try
		{
			if (e.getPres( sty ))
			{
				e.setAttribute(
					property,
					AnimationElement.AT_XML,
					value );
			}
			else
			{
				e.addAttribute(
					property,
					AnimationElement.AT_XML,
					value );
			}
		}
		catch (SVGException ex)
		{
			Logger.getLogger(
				SVGUtil.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
	}

	public static void setStyleAttribute( SVGElement e,
		String property,
		String value )
	{
		try
		{
			if (e.hasAttribute(
				property,
				AnimationElement.AT_CSS ))
			{

				e.setAttribute(
					property,
					AnimationElement.AT_CSS,
					value );
			}
			else
			{
				e.addAttribute(
					property,
					AnimationElement.AT_CSS,
					value );
			}
		}
		catch (SVGException ex)
		{
			Logger.getLogger(
				SVGUtil.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
	}

	//    public static void addWithinBounds( JComponent parent, JComponent comp, String areaLabel, SubRegionContainer container )
	//    {
	//        // get the rectangle for the named element in the SVG image
	//        Rectangle2D componentSVGRect = container.getItemRectangle( areaLabel );
	//        addWithinBounds( parent, comp, componentSVGRect );
	//        comp.putClientProperty( "SVGBounds", areaLabel );
	//
	//    }
	//    /**
	//     *
	//     * @param comp
	//     * @param area
	//     * @return
	//     */
	public static Shape getItemShape( String itemId, SVGDiagram dia )
	{
		SVGElement el = dia.getElement( itemId );
		return getItemShape(
			el,
			dia );
	}

	public static Shape getItemShape( SVGElement el, SVGDiagram dia )
	{

		SVGElement root = dia.getRoot();
		Shape itemShape = null;

		// create a scaled rectangle relative to this width and height
		float diaWidth = dia.getWidth();
		float diaHeight = dia.getHeight();

		if (el != null)
		{
			if (el instanceof ShapeElement)
			{
				ShapeElement itemasElement = (ShapeElement) el;
				itemShape = itemasElement.getShape();

			}
			else
				if (el instanceof RenderableElement)
				{

					RenderableElement re = (RenderableElement) el;
					try
					{
						itemShape = re.getBoundingBox();
					}
					catch (SVGException ex)
					{
					}
				}

			if (itemShape == null)
			{
				return nullRect;
			}

			AffineTransform currentTransform = new AffineTransform();

			SVGElement parent = el.getParent();

			// work up the tree of parents and build the transform
			// to translate this to the parent space
			while (parent != null && parent != root)
			{
				if (parent instanceof TransformableElement)
				{
					TransformableElement transformableParent = (TransformableElement) parent;
					AffineTransform at = transformableParent.getTransform();
					if (at != null)
					{
						itemShape = at.createTransformedShape( itemShape );

						currentTransform.preConcatenate( at );
					}
				}
				parent = parent.getParent();
			}

			itemShape = currentTransform.createTransformedShape( itemShape );

		}

		AffineTransform diagramScaler = AffineTransform.getScaleInstance(
			1 / diaWidth,
			1 / diaHeight );

		return diagramScaler.createTransformedShape( itemShape );

	}

	private static Rectangle2D.Double nullRect = new Rectangle2D.Double();

	public static void update( SVGElement el )
	{
		try
		{
			el.updateTime( 0 );
		}
		catch (SVGException ex)
		{
			Logger.getLogger(
				SVGUtil.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
	}

}
