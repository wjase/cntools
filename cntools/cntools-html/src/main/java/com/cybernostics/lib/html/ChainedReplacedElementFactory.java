package com.cybernostics.lib.html;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

/**
 * 
 */
public class ChainedReplacedElementFactory implements ReplacedElementFactory
{

	private final List< ReplacedElementFactory > factoryList;

	public ChainedReplacedElementFactory()
	{
		this.factoryList = new ArrayList< ReplacedElementFactory >();
	}

	public ReplacedElement createReplacedElement( LayoutContext c,
		BlockBox box,
		UserAgentCallback uac,
		int cssWidth,
		int cssHeight )
	{
		ReplacedElement re = null;
		for (Iterator< ReplacedElementFactory > it = factoryList.iterator(); it.hasNext();)
		{
			ReplacedElementFactory ref = it.next();
			re = ref.createReplacedElement(
				c,
				box,
				uac,
				cssWidth,
				cssHeight );
			if (re != null)
			{
				break;
			}
		}
		return re;
	}

	public void addFactory( ReplacedElementFactory ref )
	{
		this.factoryList.add( ref );
	}

	public void reset()
	{
		for (Iterator< ReplacedElementFactory > i = this.factoryList.iterator(); i.hasNext();)
		{
			ReplacedElementFactory factory = i.next();
			factory.reset();
		}
	}

	public void remove( Element e )
	{
		for (Iterator< ReplacedElementFactory > i = this.factoryList.iterator(); i.hasNext();)
		{
			ReplacedElementFactory factory = i.next();
			factory.remove( e );
		}
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
