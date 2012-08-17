/**
 * 
 */

package com.cybernostics.lib.persist.xml.test;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import junit.framework.Assert;

import org.junit.Test;

import com.cybernostics.lib.persist.xml.LineAdapter;
import com.cybernostics.lib.persist.xml.LineFloat;
import com.cybernostics.lib.persist.xml.PathFloat;
import com.cybernostics.lib.persist.xml.RectFloat;
import com.cybernostics.lib.persist.xml.RectangleAdapter;
import com.cybernostics.lib.persist.xml.ShapeAdapter;

/**
 * @author jasonw
 * 
 */
// @XmlSeeAlso(
// { LineFloat.class, RectFloat.class, PointFloat.class, PathFloat.class })
@XmlRootElement
public class shapes2DTest
{

	@Test
	public void testxmlSaveLoad()
	{
		shapes2DTest test = new shapes2DTest();
		test.name = "jase";
		test.aValue = 234;
		test.someotherValue = 2.5;

		try
		{
			ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
			JAXBContext jc = JAXBContext.newInstance( shapes2DTest.class );

			Marshaller m = jc.createMarshaller();

			m.setProperty(
				"jaxb.formatted.output",
				true );
			m.marshal(
				test,
				bos1 );

			// System.out.println( bos1.toString() );

			ByteArrayInputStream bis = new ByteArrayInputStream( bos1.toByteArray() );
			Unmarshaller um = jc.createUnmarshaller();
			shapes2DTest reloaded = (shapes2DTest) um.unmarshal( bis );

			ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
			Marshaller m2 = jc.createMarshaller();
			m2.setProperty(
				"jaxb.formatted.output",
				true );
			m2.marshal(
				reloaded,
				bos2 );
			Assert.assertEquals(
				new String( bos1.toByteArray() ),
				new String( bos2.toByteArray() ) );

		}
		catch (Exception e)
		{
			// TODO: handle exception
		}

	}

	@XmlElement
	@XmlJavaTypeAdapter(value = RectangleAdapter.class, type = Rectangle2D.Float.class)
	Rectangle2D.Float theRect = new Rectangle2D.Float( 20, 30, 40, 50 );

	@XmlElement
	@XmlJavaTypeAdapter(value = LineAdapter.class, type = Line2D.Float.class)
	Line2D.Float aline;

	@XmlElement
	int aValue;

	@XmlElement
	String name;
	@XmlElement
	double someotherValue;
	@XmlElement
	// @XmlElementRefs(
	// { @XmlElementRef(name = "base", type = ABase.class), @XmlElementRef(name
	// = "derived", type = DerivedClass.class)
	// })
	ABase ownedBaseElement = new DerivedClass();

	// @XmlElement
	// Point2D.Float aPoint = new Point2D.Float( 2.5f, 2.5f );

	@XmlElement
	Line2D.Float myShape = new Line2D.Float( 0f, 0f, 10f, 34f );

	private ArrayList< Shape > shapes = new ArrayList< Shape >();

	// @XmlElement
	// @XmlElementRefs(
	// { @XmlElementRef(name = "base", type = ABase.class), @XmlElementRef(name
	// = "derived", type = DerivedClass.class)
	// })
	private ArrayList< ABase > items = new ArrayList< ABase >();

	public shapes2DTest()
	{
		shapes.add( myShape );
		shapes.add( new Rectangle2D.Float( 0, 0, 40, 50 ) );

		Path2D.Float p2 = new Path2D.Float();
		p2.moveTo(
			10,
			10 );
		p2.lineTo(
			2,
			2 );
		p2.lineTo(
			23,
			25 );
		p2.lineTo(
			34,
			45 );

		shapes.add( p2 );

		for (int index = 0; index < 5; ++index)
		{
			items.add( new ABase( "Item " + index ) );

		}

		DerivedClass dc = new DerivedClass();
		dc.setaName( "the derived" );
		dc.aNewIntValue = 678;

		items.add( dc );

	}

	@XmlElementWrapper
	@XmlMixed
	@XmlElementRefs(
	{ @XmlElementRef(name = "base", type = ABase.class),
		@XmlElementRef(name = "derived", type = DerivedClass.class) })
	public ArrayList< ABase > getItems()
	{
		return items;
	}

	@XmlElementWrapper
	@XmlElementRefs(
	{ @XmlElementRef(name = "line", type = LineFloat.class),
		@XmlElementRef(name = "rect", type = RectFloat.class),
		@XmlElementRef(name = "path", type = PathFloat.class) })
	@XmlJavaTypeAdapter(value = ShapeAdapter.class, type = Shape.class)
	public ArrayList< Shape > getShapes()
	{
		return shapes;
	}

	public void setItems( ArrayList< ABase > items )
	{
		this.items = items;
	}

	public void setShapes( ArrayList< Shape > shapes )
	{
		this.shapes = shapes;
	}
}
