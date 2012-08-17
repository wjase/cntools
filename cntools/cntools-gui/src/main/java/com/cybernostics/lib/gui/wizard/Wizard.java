package com.cybernostics.lib.gui.wizard;

import java.util.Map;

/**
 * The Wizard interface implements the basic methods for<br>
 * <ul>
 * <li>adding/removing Wizard Steps</li>
 * <li>accessing a Wizard properties</li>
 * </ul>
 * map
 * 
 * @author jasonw
 * 
 */
public interface Wizard extends NavigationListener
{

	/**
	 * Adds the specified step to the Wizard
	 * 
	 * @param step
	 */
	public void addStep( StepPanel step );

	/**
	 * Returns the map of properties for the current wizard session
	 * 
	 * @return
	 */
	public Map< String, Object > getProperties();

	/**
	 * Clear all the steps from the wizard
	 * 
	 * @param step
	 */
	public void removeSteps();

	/**
	 * Remove steps after the specified one. Used when going back from a step
	 * which adds a bunch of steps.
	 * 
	 * @param step
	 */
	public void removeSteps( StepPanel step );
}
