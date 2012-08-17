package com.cybernostics.lib.gui.wizard;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;

import com.cybernostics.lib.gui.declarative.events.WhenClicked;

public class ExampleStepPanel extends StepPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4926467660283237123L;

	public ExampleStepPanel( Wizard theWizard )
	{
		super( theWizard );
		jcb.setName( "ClickME" );
		add( jcb );

		new WhenClicked( jcb )
		{

			@Override
			public void doThis( ActionEvent e )
			{
				JCheckBox jcbThis = (JCheckBox) e.getSource();
				canContinue( jcbThis.getModel()
					.isSelected() );
			}
		};

	}

	public ExampleStepPanel( WizardPanel wiz, String name )
	{
		this( wiz );
		setName( name );
	}

	JCheckBox jcb = new JCheckBox( "Must Click me", false );

	@Override
	public void initialiseNavigation()
	{
		canContinue( jcb.getModel()
			.isSelected() );
	}

	@Override
	public void whenShown()
	{
		// TODO Auto-generated method stub
		super.whenShown();
		canContinue( false );
	}
}
