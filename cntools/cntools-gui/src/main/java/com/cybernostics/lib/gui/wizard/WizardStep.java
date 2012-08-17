package com.cybernostics.lib.gui.wizard;

/**
 * Interface which represents a single step in a series of Wizard Steps
 * 
 * @author jasonw
 * 
 */
public interface WizardStep
{

	/**
	 * Returns the short name of this step. This is the label to be used in the
	 * progress panel to identify the current step.
	 * 
	 * @return
	 */
	public String getName();

}
