package com.cybernostics.lib.io;

import java.util.Iterator;

public interface StructuredDataReader extends Iterable< String[] >
{

	public String[] getHeaders();

	public boolean hasMoreItems();

	public String[] nextRecord();

	Iterator< String[] > iterator();

	public void close();
}
