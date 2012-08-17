package com.cybernostics.lib.html;

import java.awt.image.BufferedImage;
import java.util.TreeMap;

import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;
import org.xhtmlrenderer.swing.ImageReplacedElement;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;

/**
 * 
 * Returns a JLabel image for and cid:<tagname> urls
 */
public class ImageReplaceElementFactory implements ReplacedElementFactory
{

	TreeMap< String, BufferedImage > imageResources = new TreeMap< String, BufferedImage >();

	public ReplacedElement createReplacedElement( LayoutContext c,
		BlockBox box,
		UserAgentCallback uac,
		int cssWidth,
		int cssHeight )
	{

		try
		{
			Element elem = box.getElement();

			// String path = elem.getAttribute( "data" );

			if (elem.getTagName()
				.equalsIgnoreCase(
					"img" ))
			{
				String srcId = elem.getAttribute( "src" );
				BufferedImage imageToSwap = imageResources.get( srcId );
				if (imageToSwap != null)
				{
					// if the csswidth isn't specified then use the image
					// dimensions
					int width = cssWidth > 0 ? cssWidth
						: imageToSwap.getWidth();
					int height = cssHeight > 0 ? cssHeight
						: imageToSwap.getHeight();
					return new ImageReplacedElement( imageToSwap, width, height );
				}
			}

		}
		catch (Exception e)
		{
			// TODO: handle exception
			UnhandledExceptionManager.handleException( e );
		}
		return null;
	}

	public void putImage( String id, BufferedImage theImage )
	{
		imageResources.put(
			id,
			theImage );
	}

	// private JComponent getDefaultJComponent( String content, int width, int
	// height )
	// {
	// JPanel panel = new JPanel();
	// panel.setLayout( new GridLayout() );
	// JLabel comp = new JLabel( content );
	// panel.add( comp );
	// panel.setOpaque( false );
	// if ( width > 0 && height > 0 )
	// {
	// panel.setPreferredSize( new Dimension( width, height ) );
	// panel.setSize( panel.getPreferredSize() );
	// }
	// else
	// {
	// panel.setPreferredSize( comp.getPreferredSize() );
	// panel.setSize( comp.getPreferredSize() );
	// }
	// return panel;
	// }

	public void reset()
	{

	}

	public void remove( Element e )
	{

	}

	/* (non-Javadoc)
	 * @see org.xhtmlrenderer.extend.ReplacedElementFactory#setFormSubmissionListener(org.xhtmlrenderer.simple.extend.FormSubmissionListener)
	 */
	@Override
	public void setFormSubmissionListener( FormSubmissionListener arg0 )
	{
		// TODO Auto-generated method stub

	}
}
