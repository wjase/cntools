package com.cybernostics.lib.gui.wizard;

import java.util.ArrayList;

import javax.swing.JPanel;

import com.cybernostics.lib.gui.adapter.ComponentValueAdaptor;

/**
 * The StepPanel is an instance of Wizard Step which you can subclass to give
 * most wizard behaviour.
 * 
 * 
 * @author jasonw
 * 
 */
public class StepPanel extends JPanel
	implements
	NavigationController,
	NavigationListener,
	WizardStep
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8546145349660820516L;

	// /**
	// * Builds a composite qualified name in a dot separated form to identify a
	// component in the component hierarchy.
	// The
	// * name is based on the setName method being called on selected components
	// to get and set property values for
	// them.
	// *
	// * @param parentName
	// * @param currentName
	// * @return
	// */
	// private static String buildComponentName( String parentName, String
	// currentName )
	// {
	// return ( ( parentName.length() == 0 ) ? currentName : parentName
	// + ( ( ( currentName != null ) && ( currentName.length() > 0 ) ) ? "." +
	// currentName : "" ) );
	// }

	ArrayList< NavigationListener > listeners = new ArrayList< NavigationListener >();

	private Wizard theWizard;

	public StepPanel( Wizard theWizard )
	{
		setWizard( theWizard );
		setName( getClass().getSimpleName() );

	}

	public StepPanel( Wizard theWizard, String name )
	{
		setWizard( theWizard );
		setName( name );

	}

	@Override
	public void addNavigationListener( NavigationListener theListener )
	{
		listeners.add( theListener );
		initialiseNavigation();
	}

	public void addNextSteps( StepPanel... panels )
	{
		Wizard wiz = getWizard();
		wiz.removeSteps( this );

		for (StepPanel eachStep : panels)
		{
			eachStep.setWizard( wiz );
			wiz.addStep( eachStep );
		}
	}

	@Override
	public void canCancel( boolean flag )
	{
		for (NavigationListener eachNavigateListener : listeners)
		{
			eachNavigateListener.canCancel( flag );
		}
	}

	@Override
	public void canContinue( boolean flag )
	{
		for (NavigationListener eachNavigateListener : listeners)
		{
			eachNavigateListener.canContinue( flag );
		}

	}

	@Override
	public void canGoBack( boolean flag )
	{
		for (NavigationListener eachNavigateListener : listeners)
		{
			eachNavigateListener.canGoBack( flag );
		}

	}

	public Wizard getWizard()
	{
		return theWizard;
	}

	public void initialiseNavigation()
	{

	}

	protected void populateFromProperties()
	{
		ComponentValueAdaptor.recursiveSetProperties(
			"",
			this,
			getWizard().getProperties() );

	}

	// private void recursiveSetProperties( String name, JComponent component )
	// {
	// String componentName = component.getName();
	// String qualifiedName = buildComponentName( name, componentName );
	//
	// // System.out.println( qualifiedName );
	//
	// if ( component.getComponentCount() == 0 )
	// {
	// if ( ( componentName != null ) && ( componentName.length() > 0 ) )
	// {
	// // check if there is a value to set
	// if ( theWizard.getProperties().containsKey( qualifiedName ) )
	// {
	// Map< String, Object > props = theWizard.getProperties();
	// synchronized ( props )
	// {
	// Object valueToSet = props.get( qualifiedName );
	// // System.out.println( "Set component value: " + qualifiedName + " to " +
	// valueToSet );
	// ComponentValueAdaptor.setValue( component, valueToSet );
	// }
	//
	// }
	// }
	// return;
	// }
	// else
	// {
	// // I have children dive in and harvest their values
	// for ( int index = 0; index < component.getComponentCount(); ++index )
	// {
	// Component c = component.getComponent( index );
	// if ( c instanceof JComponent )
	// {
	// JComponent comp = ( JComponent ) c;
	// recursiveSetProperties( qualifiedName, comp );
	//
	// }
	// }
	// }
	//
	// }

	// private void recursiveGetProperties( String name, JComponent component )
	// {
	// String componentName = component.getName();
	// String qualifiedName = buildComponentName( name, componentName );
	//
	// if ( component.getComponentCount() == 0 )
	// {
	// if ( ( componentName != null ) && ( componentName.length() > 0 ) )
	// {
	// // // check if there is a value to set
	// // if ( theWizard.getProperties().containsKey( qualifiedName ) )
	// // {
	// Map< String, Object > props = theWizard.getProperties();
	// synchronized ( props )
	// {
	// Object currentValue = ComponentValueAdaptor.getValue( component );
	// // System.out.println( "Get property :" + qualifiedName + " to " +
	// currentValue );
	// props.put( qualifiedName, currentValue );
	// }
	// // }
	// }
	// return;
	// }
	// else
	// {
	// // I have children dive in and harvest their values
	// for ( int index = 0; index < component.getComponentCount(); ++index )
	// {
	// Component c = component.getComponent( index );
	// if ( c instanceof JComponent )
	// {
	// JComponent comp = ( JComponent ) c;
	// recursiveGetProperties( qualifiedName, comp );
	// }
	// }
	// }
	//
	// }

	@Override
	public void removeListeners()
	{
		listeners.clear();

	}

	public void setWizard( Wizard parent )
	{
		theWizard = parent;
	}

	protected void updatePropertyValues()
	{
		ComponentValueAdaptor.recursiveGetProperties(
			"",
			this,
			getWizard().getProperties() );

	}

	/**
	 * This is called when this step has been added to the active list of steps.
	 */
	public void whenAdded()
	{

	}

	/**
	 * This is called when this page is no longer shown
	 */
	public void whenHidden()
	{
		updatePropertyValues();
		removeListeners();

	}

	/**
	 * This is called by the wizard when the dialog is (re)displayed
	 */
	public void whenShown()
	{
		populateFromProperties();
		removeListeners();
		addNavigationListener( theWizard );

		canContinue( true );
	}
}
