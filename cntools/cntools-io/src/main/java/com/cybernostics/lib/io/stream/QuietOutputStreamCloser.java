package com.cybernostics.lib.io.stream;

import com.cybernostics.lib.exceptions.ErrorHandlerAction;
import java.io.IOException;
import java.io.OutputStream;

public class QuietOutputStreamCloser
{

	public static void close( OutputStream toClose )
	{
		close(
			toClose,
			ErrorHandlerAction.IGNORE );
	}

	public static void close( OutputStream toClose, ErrorHandlerAction toDo )
	{
		try
		{
			if (toClose != null)
			{
				toClose.close();
			}

		}
		catch (IOException ex)
		{
			if (toDo == ErrorHandlerAction.THROW)
			{
				throw new RuntimeException( ex );
			}// 
		}
	}

}