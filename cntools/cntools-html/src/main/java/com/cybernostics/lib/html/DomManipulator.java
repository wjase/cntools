package com.cybernostics.lib.html;

import com.sun.org.apache.xpath.internal.NodeSet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author jasonw
 */
public class DomManipulator
{

	public static void setStyleAttr( Element toUpdate,
		String styleAttr,
		String val )
	{
		if (toUpdate == null)
		{
			return;
		}
		String attr = toUpdate.getAttribute( "style" );
		String newStyle = "";
		if (attr != null)
		{
			newStyle = attr.replaceAll(
				styleAttr + ":[^;]+;",
				"" ) + styleAttr + ":" + val + ";";// + ;
		}
		else
		{
			newStyle = styleAttr + ":" + val + ";";// + ;
		}
		toUpdate.setAttribute(
			"style",
			newStyle );

	}

	public static void setStyleAttr( NodeList toUpdate,
		String styleAttr,
		String val )
	{
		int len = toUpdate.getLength();
		for (int itemNo = 0; itemNo < len; ++itemNo)
		{
			Node nd = toUpdate.item( itemNo );
			if (nd instanceof Element)
			{
				setStyleAttr(
					(Element) nd,
					styleAttr,
					val );
			}
		}

	}

	public static NodeList byClass( Document doc,
		String tagName,
		String classValue )
	{
		NodeList candidates = doc.getElementsByTagName( tagName );

		NodeSet ns = new NodeSet();
		int len = candidates.getLength();
		for (int itemNo = 0; itemNo < len; ++itemNo)
		{
			Node nd = candidates.item( itemNo );
			Node classNode = nd.getAttributes()
				.getNamedItem(
					"class" );
			if (classNode != null && classNode.getNodeValue()
				.contains(
					classValue ))
			{
				ns.addElement( nd );
			}
		}

		return ns;

	}

}
