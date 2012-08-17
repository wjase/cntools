package com.cybernostics.lib.gui.wizard;

/**
 * The Navigation Listener responds to methods from Wizard steps indicating what
 * navigation options are available.
 * 
 * As the status of navigation options changes as a result of control changes to
 * the wizard step, NavigationUpdate events are sent to all listeners.
 * 
 * Typically the main Listener will be the Wizard dialog which will then change
 * the status of the next previous and cancel buttons.
 * 
 * @author jasonw
 * 
 */
public interface NavigationListener
{

	/**
	 * Indicates whether or not the Wizard step be cancelled
	 * 
	 * @param flag
	 */
	public void canCancel( boolean flag );

	/**
	 * Indicates whether or not the step can proceed
	 * 
	 * @param flag
	 */
	public void canContinue( boolean flag );

	/**
	 * Indicates whether or not the step can go back
	 * 
	 * @param flag
	 */
	public void canGoBack( boolean flag );
}
