package com.cybernostics.lib.io.stream;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author jasonw
 */
public interface InputStreamOperation
{

	public void process( InputStream is ) throws IOException;

}
