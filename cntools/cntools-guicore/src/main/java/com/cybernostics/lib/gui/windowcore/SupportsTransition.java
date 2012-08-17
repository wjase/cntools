package com.cybernostics.lib.gui.windowcore;

/**
 *
 * @author jasonw
 */
public interface SupportsTransition
{

	/**
	 * Called when a screen is first pushed onto the stack
	 *
	 * @param listener
	 */
	public void doWhenPushed( TransitionCompleteListener listener );

	/**
	 * Called when a screen is first popped off the stack
	 *
	 * @param listener
	 */
	public void doWhenPopped( TransitionCompleteListener listener );

	/**
	 * Called when a screen obscured by another screen being pushed on the top
	 *
	 * @param listener
	 */
	public void doWhenObscured( TransitionCompleteListener listener );

	/**
	 * Called when an obscured screen becomes the top most again
	 *
	 * @param listener
	 */
	public void doWhenRevealed( TransitionCompleteListener listener );

}
