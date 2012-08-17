package com.cybernostics.lib.persist.xml;

import java.awt.geom.Path2D;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class PathAdapter extends XmlAdapter< PathFloat, Path2D.Float >
{

	@Override
	public PathFloat marshal( Path2D.Float v ) throws Exception
	{
		if (v != null)
		{
			return new PathFloat( v );
		}

		return null;
	}

	@Override
	public Path2D.Float unmarshal( PathFloat v ) throws Exception
	{
		if (v != null)
		{
			return PathFloat.toPath2D( v );
		}
		return null;
	}

}
