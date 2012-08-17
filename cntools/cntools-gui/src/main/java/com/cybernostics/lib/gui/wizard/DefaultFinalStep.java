package com.cybernostics.lib.gui.wizard;

/**
 * Subclass this for final wizard steps. Once this step is made active then the
 * Next Button will change to "Finish"
 * 
 * @author jasonw
 * 
 */
public class DefaultFinalStep extends ExampleStepPanel implements FinalStep
{

	public DefaultFinalStep( Wizard theWizard )
	{
		super( theWizard );
	}

	public DefaultFinalStep( WizardPanel wiz, String name )
	{
		super( wiz, name );
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3053729950771543590L;

}
