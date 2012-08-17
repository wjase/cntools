package com.cybernostics.lib.concurrent;

/**
 *
 * @author jasonw
 */
public interface ResourceFactory< T >
{
	public T create() throws ResourceCreationException;
}
