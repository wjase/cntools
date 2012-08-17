package com.cybernostics.lib.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import javax.swing.*;

/**
 * JDateChooser is a simple Date choosing component with similar functionality
 * to JFileChooser and JColorChooser. It can be used as a component, to be
 * inserted into a client layout, or can display it's own Dialog through use of
 * the {@link #showDialog(Component, String) showDialog} method. <p>
 * JDateChooser can be initialized to the current date using the no argument
 * constructor, or initialized to a predefined date by passing an instance of
 * Calendar to the constructor. <p> Using the JDateChooser dialog works in a
 * similar manner to JFileChooser or JColorChooser. The {@link #showDialog(Component, String) showDialog}
 * method returns an int that equates to the public variables ACCEPT_OPTION,
 * CANCEL_OPTION or ERROR_OPTION. <p> <tt> JDateChooser chooser = new
 * JDateChooser();<br> if (chooser.showDialog(this, "Select a date...") ==
 * JDateChooser.ACCEPT_OPTION) {<br> &nbsp;&nbsp;Calendar selectedDate =
 * chooser.getSelectedDate();<br> &nbsp;&nbsp;// process date here...<br> }<p>
 * To use JDateChooser as a component within a GUI, users should subclass
 * JDateChooser and override the {@link #acceptSelection() acceptSelection} and
 * {@link #cancelSelection() cancelSelection} methods to process the
 * corresponding user selection.<p> The current date can be retrieved by calling {@link #getSelectedDate() getSelectedDate}
 * method.
 */
public class JDateChooser extends JComponent
	implements
	ActionListener,
	DaySelectionListener
{

	public boolean isShowDateOnly()
	{
		return ShowDateOnly;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -5966430161544695415L;

	/**
	 * Value returned by {@link #showDialog(Component, String) showDialog} upon
	 * an error.
	 */
	public static final int ERROR_OPTION = 0;

	/**
	 * Value returned by {@link #showDialog(Component, String) showDialog} upon
	 * pressing the "okay" button.
	 */
	public static final int ACCEPT_OPTION = 2;

	/**
	 * Value returned by {@link #showDialog(Component, String) showDialog} upon
	 * pressing the "cancel" button.
	 */
	public static final int CANCEL_OPTION = 4;

	private JTextField dateText;

	private final Calendar calendar;

	private JButton previousYear;

	private JButton previousMonth;

	private JButton nextMonth;

	private JButton nextYear;

	private JButton okay;

	private JButton cancel;

	private int returnValue;

	private JDialog dialog;

	private JPanel days;

	private final boolean ShowDateOnly = true;

	/**
	 * This constructor creates a new instance of JDateChooser initialised to
	 * the current date.
	 */
	public JDateChooser()
	{
		this( Calendar.getInstance() );
	}

	/**
	 * Creates a new instance of JDateChooser initialised to the given Calendar.
	 */
	public JDateChooser( Calendar c )
	{
		super();
		this.calendar = c;
		this.calendar.setLenient( true );
		setup();
	}

	private void setup()
	{
		GridBagLayout g = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		JPanel header = new JPanel( g );
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets( 2, 0, 2, 0 );
		previousYear = (JButton) header.add( new JButton( "<<" ) );
		previousYear.addActionListener( this );
		previousYear.setToolTipText( "Previous Year" );
		g.setConstraints(
			previousYear,
			c );
		previousMonth = (JButton) header.add( new JButton( "<" ) );
		previousMonth.addActionListener( this );
		previousMonth.setToolTipText( "Previous Month" );
		c.gridx++;
		g.setConstraints(
			previousMonth,
			c );
		dateText = (JTextField) header.add( new JTextField( "",
			SwingConstants.CENTER ) );
		dateText.setBorder( new EtchedBorder( EtchedBorder.LOWERED ) );
		c.gridx++;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		g.setConstraints(
			dateText,
			c );
		nextMonth = (JButton) header.add( new JButton( ">" ) );
		nextMonth.addActionListener( this );
		nextMonth.setToolTipText( "Next Month" );
		c.gridx++;
		c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		g.setConstraints(
			nextMonth,
			c );
		nextYear = (JButton) header.add( new JButton( ">>" ) );
		nextYear.addActionListener( this );
		nextYear.setToolTipText( "Next Year" );
		c.gridx++;
		g.setConstraints(
			nextYear,
			c );

		updateCalendar( calendar );

		JPanel buttons = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
		okay = (JButton) buttons.add( new JButton( "Done" ) );
		okay.addActionListener( this );
		cancel = (JButton) buttons.add( new JButton( "Cancel" ) );
		cancel.addActionListener( this );

		dateText.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent e )
			{
				Date newTime = null;
				try
				{
					newTime = sdf.parse( dateText.getText() );
				}
				catch (ParseException e1)
				{
				}
				if (newTime != null)
				{
					calendar.setTime( newTime );
					updateCalendar( calendar );
				}
				else
				{
				}

			}

		} );

		dateText.addFocusListener( new FocusListener()
		{

			@Override
			public void focusGained( FocusEvent e )
			{
				// TODO Auto-generated method stub
			}

			@Override
			public void focusLost( FocusEvent e )
			{
				// TODO Auto-generated method stub
				Date newTime = null;
				try
				{
					newTime = sdf.parse( dateText.getText() );
				}
				catch (ParseException e1)
				{
				}
				if (newTime != null)
				{
					calendar.setTime( newTime );
					updateCalendar( calendar );
				}
				else
				{
				}

			}

		} );
		setLayout( new BorderLayout() );
		add(
			"North",
			header );
		add(
			"Center",
			days );
		add(
			"South",
			buttons );
	}

	private void updateCalendar( Calendar c )
	{
		if (days != null)
		{
			remove( days );
		}
		calendar.get( Calendar.DAY_OF_MONTH );
		calendar.get( Calendar.MONTH );
		calendar.get( Calendar.YEAR );
		days = new JPanel( new GridLayout( 7, 7 ) );
		Calendar setup = (Calendar) calendar.clone();
		setup.set(
			Calendar.DAY_OF_WEEK,
			setup.getFirstDayOfWeek() );
		int lastLayoutPosition = 0;
		for (int i = 0; i < 7; i++)
		{
			int dayInt = setup.get( Calendar.DAY_OF_WEEK );
			if (dayInt == Calendar.MONDAY)
			{
				days.add( new JLabel( "Mon" ) );
			}
			if (dayInt == Calendar.TUESDAY)
			{
				days.add( new JLabel( "Tue" ) );
			}
			if (dayInt == Calendar.WEDNESDAY)
			{
				days.add( new JLabel( "Wed" ) );
			}
			if (dayInt == Calendar.THURSDAY)
			{
				days.add( new JLabel( "Thu" ) );
			}
			if (dayInt == Calendar.FRIDAY)
			{
				days.add( new JLabel( "Fri" ) );
			}
			if (dayInt == Calendar.SATURDAY)
			{
				days.add( new JLabel( "Sat" ) );
			}
			if (dayInt == Calendar.SUNDAY)
			{
				days.add( new JLabel( "Sun" ) );
			}
			setup.roll(
				Calendar.DAY_OF_WEEK,
				true );
			lastLayoutPosition++;
		}
		setup = (Calendar) calendar.clone();
		setup.set(
			Calendar.DAY_OF_MONTH,
			1 );
		int first = setup.get( Calendar.DAY_OF_WEEK );
		// Pad days before the first one with blank labels
		for (int i = 0; i < ( first - 1 ); i++)
		{
			days.add( new JLabel( "" ) );
			lastLayoutPosition++;
		}
		setup.set(
			Calendar.DAY_OF_MONTH,
			1 );
		for (int i = 0; i < setup.getActualMaximum( Calendar.DAY_OF_MONTH ); i++)
		{
			DayButton button = new DayButton( setup.get( Calendar.DAY_OF_MONTH ) );
			button.addDaySelectionListener( this );
			days.add( button );
			setup.roll(
				Calendar.DAY_OF_MONTH,
				true );
			lastLayoutPosition++;
		}

		for (int i = lastLayoutPosition; i < 49; i++)
		{
			days.add( new JLabel( "" ) );
		}
		add(
			"Center",
			days );
		validate();

		if (dialog != null)
		{
			dialog.pack();
		}
		setup = null;
		updateLabel();
	}

	SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" );

	private void updateLabel()
	{
		Date date = calendar.getTime();
		dateText.setText( sdf.format( date ) );
	}

	/**
	 * Returns the currently selected Date in the form of a java.util.Calendar
	 * object. Typically called adter receipt of an {@link #ACCEPT_OPTION
	 * ACCEPT_OPTION} (using the {@link #showDialog(Component, String)
	 * showDialog} method) or within the {@link #acceptSelection()
	 * acceptSelection} method (using the JDateChooser as a component.) <p>
	 *
	 * @return java.util.Calendar The selected date in the form of a Calendar
	 * object.
	 */
	public Calendar getSelectedDate()
	{
		return calendar;
	}

	/**
	 * Pops up a Date chooser dialog with the supplied <i>title</i>, centered
	 * about the component <i>parent</i>.
	 *
	 * @return int An integer that equates to the static variables
	 * <i>ERROR_OPTION</i>, <i>ACCEPT_OPTION</i> or <i>CANCEL_OPTION</i>.
	 */
	public int showDialog( Component parent, String title )
	{
		returnValue = ERROR_OPTION;
		Frame frame = parent instanceof Frame ? (Frame) parent
			: (Frame) SwingUtilities.getAncestorOfClass(
				Frame.class,
				parent );
		dialog = new JDialog( frame, title, true );
		dialog.getContentPane()
			.add(
				"Center",
				this );
		dialog.pack();
		dialog.setLocationRelativeTo( parent );
		dialog.setVisible( true );
		return returnValue;
	}

	/**
	 * This method is called when the user presses the "okay" button. Users must
	 * subclass JDateChooser and override this method to use JDateChooser as a
	 * Component and receive accept selections by the user.
	 */
	public void acceptSelection()
	{
	}

	/**
	 * This method is called when the user presses the "cancel" button. Users
	 * must subclass JDateChooser and override this method to use JDateChooser
	 * as a Component and receive cancel selections by the user.
	 */
	public void cancelSelection()
	{
	}

	/**
	 * Used to process events from the previous month, previous year, next
	 * month, next year, okay and cancel buttons. Users should call
	 * super.actionPerformed(ActionEvent) if overriding this method.
	 */
	public void actionPerformed( ActionEvent e )
	{
		if (e.getSource() == okay)
		{
			returnValue = ACCEPT_OPTION;
			if (dialog != null)
			{
				dialog.dispose();
			}
			acceptSelection();
		}
		if (e.getSource() == cancel)
		{
			returnValue = CANCEL_OPTION;
			if (dialog != null)
			{
				dialog.dispose();
			}
			cancelSelection();
		}
		if (e.getSource() == previousYear)
		{
			calendar.roll(
				Calendar.YEAR,
				-1 );
			updateCalendar( calendar );
		}
		if (e.getSource() == previousMonth)
		{
			calendar.roll(
				Calendar.MONTH,
				-1 );
			updateCalendar( calendar );
		}
		if (e.getSource() == nextMonth)
		{
			calendar.roll(
				Calendar.MONTH,
				1 );
			updateCalendar( calendar );
		}
		if (e.getSource() == nextYear)
		{
			calendar.roll(
				Calendar.YEAR,
				1 );
			updateCalendar( calendar );
		}
	}

	/**
	 * Used to process day selection events from the user. This method resets
	 * resets the Calendar object to the selected day. Subclasses should make a
	 * call to super.daySelected() if overriding this method.
	 */
	public void daySelected( int d )
	{
		calendar.set(
			Calendar.DAY_OF_MONTH,
			d );
		updateLabel();
	}

}

class DayButton extends JButton implements ActionListener
{

	/**
	 *
	 */
	private static final long serialVersionUID = -6651143243727125601L;

	private final int day;

	private Vector< DaySelectionListener > listeners;

	Dimension preferredSize = new Dimension( 100, 100 );

	public DayButton( int d )
	{
		super( ( new Integer( d ) ).toString() );
		this.day = d;
		addActionListener( this );
		setBorder( BorderFactory.createEmptyBorder(
			10,
			10,
			10,
			10 ) );
		setPreferredSize( preferredSize );
	}

	public void actionPerformed( ActionEvent e )
	{
		if (listeners != null)
		{
			for (int i = 0; i < listeners.size(); i++)
			{
				( listeners.elementAt( i ) ).daySelected( day );
			}
		}
	}

	public void addDaySelectionListener( DaySelectionListener l )
	{
		if (listeners == null)
		{
			listeners = new Vector< DaySelectionListener >( 1, 1 );
		}
		listeners.addElement( l );
	}

	public void removeDaySelectionListener( DaySelectionListener l )
	{
		if (listeners != null)
		{
			listeners.removeElement( l );
		}
	}

	public void removeAllListeners()
	{
		listeners = new Vector< DaySelectionListener >( 1, 1 );
	}

}
