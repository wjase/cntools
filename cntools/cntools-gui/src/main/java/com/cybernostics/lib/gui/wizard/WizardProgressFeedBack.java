package com.cybernostics.lib.gui.wizard;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.cybernostics.lib.gui.control.StyledLabel;

public class WizardProgressFeedBack extends JPanel
{

	public static final String STEP_VALUE = "step";

	public static final String finishText = "Finish";
	public static final String nextText = "Next";

	BoxLayout layout = null;

	private int currentStep = 0;

	private Icon backGroundImage = null;

	ArrayList< StepPanel > steps = new ArrayList< StepPanel >();

	JPanel stepPanel = new JPanel();

	/**
	 * 
	 */
	private static final long serialVersionUID = 983779033717999921L;

	//	public static void main(String[] args)
	//	{
	//		SystemLook.set();
	//		JFrame jf = new JFrame( "WizardProgressPanel Test" );
	//		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	//		JPanel jp = new JPanel();
	//		DesignGridLayout dgl = new DesignGridLayout( jp );
	//
	//		final WizardProgressFeedBack wpf = new WizardProgressFeedBack();
	//
	//		ArrayList< StepPanel > steps = new ArrayList< StepPanel >();
	//
	//		WizardPanel wiz = new WizardPanel();
	//
	//		for ( int index = 0; index < 10; ++index )
	//		{
	//			steps.add( new ExampleStepPanel( wiz, "Step Number" + index ) );
	//		}
	//
	//		wpf.setSteps( steps );
	//
	//		dgl.row().grid().add( wpf );
	//
	//		ImageIcon backImage = new ImageIcon( "c:/temp/defaultWizard.png" );
	//		wpf.setBackGroundImage( backImage );
	//
	//		JButton jbNext = new JButton( nextText );
	//
	//		new WhenClicked( jbNext )
	//		{
	//
	//			@Override
	//			public void doThis(ActionEvent e)
	//			{
	//				wpf.gotoNext();
	//
	//			}
	//		};
	//
	//		JButton jbLast = new JButton( "Last" );
	//		new WhenClicked( jbLast )
	//		{
	//
	//			@Override
	//			public void doThis(ActionEvent e)
	//			{
	//				wpf.gotoLast();
	//
	//			}
	//		};
	//
	//		dgl.row().grid().add( jbLast, jbNext );
	//
	//		jf.getContentPane().add( jp );
	//		jf.setVisible( true );
	//		jf.pack();
	//	}

	WizardProgressFeedBack()
	{
		setLayout( new BorderLayout() );

		// JScrollPane jsp = new JScrollPane( stepPanel );
		// jsp.setOpaque( false );
		add(
			stepPanel,
			BorderLayout.CENTER );
		layout = new BoxLayout( stepPanel, BoxLayout.Y_AXIS );
		stepPanel.setLayout( layout );
		stepPanel.setOpaque( false );

		setOpaque( false );

		new com.cybernostics.lib.gui.declarative.events.WhenMadeVisible( this )
		{

			@Override
			public void doThis( AWTEvent e )
			{
				firePropertyChange(
					STEP_VALUE,
					null,
					getCurrentStep() );

			}
		};
	}

	// public void addStep( String stepName )
	// {
	// int stepNumber = getComponentCount() + 1;
	// String prefix = Integer.toString( stepNumber ) + ". ";
	// if ( stepNumber == 1 )
	// {
	// layout.row().grid().add( new StyledLabel( prefix + stepName,
	// StyledLabel.BOLD ) );
	// }
	// else
	// {
	// layout.row().grid().add( new StyledLabel( prefix + stepName ) );
	// }
	//
	// revalidate();
	// }

	public void addStep( StepPanel step )
	{
		steps.add( step );
		step.whenAdded();
		updateList();

	}

	public WizardStep getCurrentStep()
	{
		if (currentStep == -1 || steps.size() == 0)
		{
			return null;
		}
		return steps.get( currentStep );
	}

	public int getCurrentStepIndex()
	{
		return currentStep;
	}

	protected void gotoLast()
	{
		if (currentStep == 0)
		{
			return;
		}
		setCurrentToStyle( StyledLabel.NORMAL );
		setCurrentStep( getCurrentStepIndex() - 1 );
		setCurrentToStyle( StyledLabel.BOLD );

	}

	protected boolean gotoNext()
	{
		if (currentStep == stepPanel.getComponentCount() - 1)
		{
			return false;
		}
		setCurrentToStyle( StyledLabel.GREY );
		setCurrentStep( getCurrentStepIndex() + 1 );
		setCurrentToStyle( StyledLabel.BOLD );
		return true;
	}

	@Override
	protected void paintComponent( Graphics g )
	{
		if (backGroundImage != null)
		{
			backGroundImage.paintIcon(
				this,
				g,
				0,
				0 );
		}
		super.paintComponent( g );
	}

	public void removeSteps()
	{
		steps.clear();
		updateList();

	}

	public void removeSteps( WizardStep step )
	{
		int deleteAfter = -1;
		for (int index = 0; index < steps.size(); ++index)
		{
			if (steps.get( index ) == step)
			{
				deleteAfter = index + 1;
				break;
			}
		}

		if (deleteAfter != -1)
		{
			while (steps.size() > deleteAfter)
			{
				steps.remove( deleteAfter );
			}
			updateList();
		}

	}

	public void setBackGroundImage( Icon backGroundImage )
	{
		this.backGroundImage = backGroundImage;
		setPreferredSize( new Dimension( backGroundImage.getIconWidth(),
			backGroundImage.getIconHeight() ) );
	}

	public void setCurrentStep( int currentStep )
	{
		WizardStep old = getCurrentStep();
		this.currentStep = currentStep;
		WizardStep newStep = getCurrentStep();
		if (isVisible())
		{
			firePropertyChange(
				STEP_VALUE,
				old,
				newStep );
		}
	}

	private void setCurrentToStyle( int style )
	{
		StyledLabel sl = (StyledLabel) stepPanel.getComponent( currentStep );
		sl.setStyle( style );
	}

	public void setSteps( ArrayList< StepPanel > theSteps )
	{
		steps = theSteps;
		updateList();
	}

	public void updateList()
	{
		int componentCount = stepPanel.getComponentCount();
		int stepNumber = componentCount + 1;
		if (stepNumber == 1)
		{
			boolean firstOne = true;
			for (WizardStep eachStep : steps)
			{
				String prefix = Integer.toString( stepNumber ) + ". ";
				JLabel jl = new StyledLabel( prefix + eachStep.getName(),
					firstOne ? StyledLabel.BOLD
						: StyledLabel.NORMAL );
				jl.setFont( getFont() );
				stepPanel.add( jl );
				// layout.row().grid().add( jl );
				stepPanel.validate();
				firstOne = false;
				++stepNumber;
			}
			setCurrentStep( 0 );

		}
		else
		{
			// Steps is shorter than the displayed list
			if (componentCount > steps.size())
			{
				// start at the end of the list and chop surplus
				int highestIndex = componentCount - 1;
				for (int index = highestIndex; index >= steps.size(); --index)
				{
					stepPanel.remove( index );
					stepPanel.validate();

				}
			}
			else
				if (componentCount < steps.size())
				{
					for (int index = componentCount; index < steps.size(); ++index)
					{
						String prefix = Integer.toString( index + 1 ) + ". ";
						JLabel jl = new StyledLabel( prefix + steps.get(
							index )
							.getName() );
						jl.setFont( getFont() );
						// layout.row().grid().add( jl );
						stepPanel.add( jl );
						stepPanel.validate();
					}
				}
		}
		repaint();

	}
}
