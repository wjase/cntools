package com.cybernostics.lib.gui.control;

import com.cybernostics.lib.concurrent.CanCheckComplete;
import java.util.concurrent.Future;
import javax.swing.DefaultListModel;

/**
 *
 * @author jasonw
 */
public class AsyncDefaultListModel extends DefaultListModel
	implements
	CanCheckComplete
{
	private Future< Object > theTask;

	public Future< Object > getTheTask()
	{
		return theTask;
	}

	public void setTheTask( Future< Object > theTask )
	{
		this.theTask = theTask;
	}

	@Override
	public boolean isComplete()
	{
		return theTask.isDone();
	}

}
