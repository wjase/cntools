package com.cybernostics.lib.gui.wizard;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.cybernostics.lib.gui.lookandfeel.SystemLook;
import com.cybernostics.lib.gui.ButtonFactory;
import com.cybernostics.lib.gui.IconFactory;
import com.cybernostics.lib.gui.IconFactory.StdButtonType;
import com.cybernostics.lib.gui.declarative.events.WhenClicked;
import com.cybernostics.lib.gui.declarative.events.WhenMadeVisible;
import com.cybernostics.lib.gui.declarative.events.WhenPropertyChanges;
import com.cybernostics.lib.gui.declarative.events.WhenWindowShown;
import javax.swing.Box;

/**
 * A Wizard dialog with a series of steps.
 * 
 * <br>
 * Usage:
 * 
 * <code>
 * <br><br>
 * WizardPanel wiz = new WizardPanel();<br>
 * //build a list of ArrayList&lt; WizardStep &gt; steps<br>
 * wiz.getProgressPane().setSteps( steps );<br>
 * wiz.getProgressPane().setBackGroundImage( backImage );<br>
 * JDialog jd = createWizard( parentFrame, wiz, "Wizard Title Text" );<br>
 * jd.setVisible( true ); // show the dialog will block till the dialog is closed<br>
 * </code>
 * 
 * @author jasonw
 * 
 */
public class WizardPanel extends JPanel implements Wizard
{

	public static void main( String[] args )
	{
		SystemLook.set();

		final WizardPanel wiz = new WizardPanel();

		ArrayList< StepPanel > steps = new ArrayList< StepPanel >();
		steps.add( new ExampleStepPanel( wiz, "First Step" ) );
		steps.add( new ExampleStepPanel( wiz, "Second Step" ) );
		steps.add( new ExampleStepPanel( wiz, "Third Step" ) );
		steps.add( new ExampleStepPanel( wiz, "Fourth Step" ) );
		steps.add( new DefaultFinalStep( wiz, "Final Step" ) );
		wiz.getProgressPane()
			.setSteps(
				steps );

		ImageIcon backImage = new ImageIcon( "c:/temp/defaultWizard.png" );
		wiz.getProgressPane()
			.setBackGroundImage(
				backImage );

		JFrame jf = new JFrame( "Wizard Panel Test" );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		JButton jbShowWizard = new JButton( "Show Wizard" );
		jf.getContentPane()
			.add(
				jbShowWizard );
		jf.setVisible( true );
		jf.pack();

		final JDialog jd = createWizard(
			jf,
			wiz,
			"Test Wizard Dialog" );

		new WhenClicked( jbShowWizard )
		{

			@Override
			public void doThis( ActionEvent e )
			{
				jd.setVisible( true );
				System.out.println( "done" );

				Map< String, Object > props = wiz.getProperties();
				synchronized (props)
				{
					for (String eachKey : props.keySet())
					{
						System.out.println( eachKey + " was set to "
							+ props.get( eachKey ) );
					}

				}

			}
		};

	}

	/**
	 * A Map of properties set during the wizard process
	 */
	Map< String, Object > wizardProperties = new TreeMap< String, Object >();

	Dimension minimum = new Dimension( 800, 600 );

	private static final long serialVersionUID = 6574468673289694062L;

	/**
	 * Creates a modal dialog with the WizardPanel, parent frame and specified
	 * title
	 * 
	 * @param jf
	 *            - the parent frame
	 * @param wiz
	 *            - the WizardPanelWithProgress instance
	 * @param title
	 *            - the title of
	 * @return an instance of the dialog to show by calling setVisible()
	 */
	public static JDialog createWizard( final JFrame jf,
		final WizardPanel wiz,
		final String title )
	{
		final JDialog jd = new JDialog( jf, title )
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void dialogInit()
			{
				super.dialogInit();

				setModalityType( ModalityType.DOCUMENT_MODAL );
				new WhenWindowShown( this )
				{

					@Override
					public void doThis( WindowEvent e )
					{
						Container contentPane = getContentPane();
						contentPane.setLayout( new GridLayout() );
						getContentPane().add(
							wiz );
						setModalityType( ModalityType.APPLICATION_MODAL );
						// setMinimumSize( new Dimension( 600, 400 ) );
						pack();

						setLocationRelativeTo( null );
					}
				};
			}

		};

		return jd;

	}

	/**
	 * A JPanel which shows the progress of the Panel
	 */
	private WizardProgressFeedBack progressPane = new WizardProgressFeedBack();

	private JPanel rightPane = new JPanel();

	private JButton nextStepButton = ButtonFactory.getStdButton( StdButtonType.NEXT );

	private JButton prevStepButton = ButtonFactory.getStdButton( StdButtonType.PREV );

	private JButton cancelButton = ButtonFactory.getStdButton( StdButtonType.CANCEL );

	public WizardPanel()
	{
		// Set the layout
		setLayout( new BorderLayout() );

		// Set up the
		JPanel leftPane = new JPanel();
		BoxLayout bl = new BoxLayout( leftPane, BoxLayout.X_AXIS );
		leftPane.setLayout( bl );

		leftPane.add( progressPane );
		leftPane.add( new JSeparator( SwingConstants.VERTICAL ) );

		add(
			leftPane,
			BorderLayout.LINE_START );
		add(
			rightPane,
			BorderLayout.CENTER );

		// Set up the bar for prev next and cancel
		JPanel buttonBar = new JPanel();
		BoxLayout buttonBarLayout = new BoxLayout( buttonBar, BoxLayout.X_AXIS );
		buttonBar.setLayout( buttonBarLayout );
		buttonBar.add( Box.createGlue() );
		buttonBar.add( prevStepButton );
		buttonBar.add( nextStepButton );
		buttonBar.add( cancelButton );

		setMinimumSize( new Dimension( 600, 400 ) );
		add(
			buttonBar,
			BorderLayout.SOUTH );

		// progressPane.setBorder( BorderFactory.createEmptyBorder() );
		// rightPane.setBorder( BorderFactory.createEmptyBorder() );
		rightPane.setLayout( new BorderLayout() );
		revalidate();

		new WhenClicked( cancelButton )
		{

			@Override
			public void doThis( ActionEvent e )
			{
				if (JOptionPane.showConfirmDialog(
					WizardPanel.this,
					"Are you sure you want to cancel?",
					"Confirm Cancel",
					JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION)
				{
					getProperties().put(
						"cancelled",
						true );
					closeParentWindow();
				}
			}
		};

		new WhenClicked( nextStepButton )
		{

			@Override
			public void doThis( ActionEvent e )
			{
				if (!progressPane.gotoNext())
				{
					closeParentWindow();
				}
			}
		};

		new WhenClicked( prevStepButton )
		{

			@Override
			public void doThis( ActionEvent e )
			{
				progressPane.gotoLast();
			}
		};

		new WhenPropertyChanges( "step", progressPane )
		{

			@Override
			public void doThis( PropertyChangeEvent evt )
			{
				updateStepContents( (WizardStep) evt.getNewValue() );
			}
		};

		new WhenMadeVisible( this )
		{

			@Override
			public void doThis( AWTEvent e )
			{
				updateStepContents( progressPane.getCurrentStep() );
			}
		};
	}

	@Override
	public void addStep( StepPanel step )
	{
		progressPane.addStep( step );

	}

	@Override
	public void canCancel( boolean flag )
	{
		cancelButton.setEnabled( flag );

	}

	@Override
	public void canContinue( boolean flag )
	{
		WizardStep thisStep = progressPane.getCurrentStep();
		if (( thisStep instanceof FinalStep ))
		{
			//nextStepButton.setText( WizardProgressFeedBack.finishText );
			nextStepButton.setIcon( IconFactory.getStdIcon( StdButtonType.YES ) );
			nextStepButton.setToolTipText( WizardProgressFeedBack.finishText );
		}
		else
		{
			nextStepButton.setIcon( IconFactory.getStdIcon( StdButtonType.NEXT ) );
			nextStepButton.setToolTipText( WizardProgressFeedBack.nextText );
		}
		nextStepButton.setEnabled( flag );

	}

	@Override
	public void canGoBack( boolean flag )
	{
		prevStepButton.setEnabled( flag );

	}

	public void closeParentWindow()
	{
		updatePropertiesFromStep();
		WizardPanel.this.getRootPane()
			.getParent()
			.setVisible(
				false );
	}

	@Override
	public Dimension getMinimumSize()
	{

		return minimum;
	}

	@Override
	public Dimension getPreferredSize()
	{
		// TODO Auto-generated method stub
		return minimum;
	}

	public WizardProgressFeedBack getProgressPane()
	{
		return progressPane;
	}

	public Map< String, Object > getProperties()
	{
		return wizardProperties;
	}

	@Override
	public void removeSteps()
	{
		progressPane.removeSteps();
	}

	@Override
	public void removeSteps( StepPanel step )
	{
		progressPane.removeSteps( step );
	}

	protected void updatePropertiesFromStep()
	{
		if (rightPane.getComponentCount() > 0)
		{
			StepPanel old = (StepPanel) rightPane.getComponent( 0 );
			old.whenHidden();
		}
	}

	protected void updateStepContents( WizardStep currentStep )
	{
		if (currentStep == null)
		{
			return;
		}
		updatePropertiesFromStep();
		rightPane.removeAll();
		StepPanel newPanel = (StepPanel) currentStep;
		rightPane.add( newPanel );
		newPanel.whenShown();

		rightPane.revalidate();
		rightPane.repaint();
	}

	/**
	 * @param initialProperties
	 */
	public void setProperties( Map< String, Object > initialProperties )
	{
		wizardProperties = initialProperties;

	}
}
