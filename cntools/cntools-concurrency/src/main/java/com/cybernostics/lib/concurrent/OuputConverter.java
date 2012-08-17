package com.cybernostics.lib.concurrent;

/**
 * Used to transform the output of async tasks without
 * having to create nested tasks calling get() and returning something different
 * 
 * By adding a converter to the task it can return a massaged value which is set at runtime
 * @author jasonw
 */
public interface OuputConverter
{
	public Object convert( Object in );
}
