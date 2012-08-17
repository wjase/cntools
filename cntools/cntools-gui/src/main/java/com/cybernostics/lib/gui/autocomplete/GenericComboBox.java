package com.cybernostics.lib.gui.autocomplete;

import com.cybernostics.lib.gui.ButtonFactory;
import com.cybernostics.lib.gui.IconFactory;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Popup;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * A custom component that mimics a combo box, displaying customised content
 * rather than a 'list'.
 * 
 */
public abstract class GenericComboBox extends JPanel
{

	/*
	 * Captures the 'comboBtn' action.
	 */
	private class ComboButtonActionListener implements ActionListener
	{

		public void actionPerformed( ActionEvent e )
		{
			if (popupShown == false)
			{
				showPopup();
			}
			else
			{
				hidePopup();
			}

		}
	}

	JComboBox hidden = new JComboBox();

	/*
	 * Captures user input in the 'combo box' Updates the Popup Based on current
	 * Text
	 */
	private class ComboEditorListener extends KeyAdapter
	{

		@Override
		public void keyTyped( KeyEvent e )
		{
			// todo : send some kind of notification to thepopup (maybe not
			// required)
		}
	}

	// private static JComboBox jcb = new JComboBox();

	/**
	 * 
	 */
	private static final long serialVersionUID = -830078267620667713L;

	private boolean popupShown = false;

	private static final Toolkit toolkit = Toolkit.getDefaultToolkit();

	private static final Dimension screenSize = toolkit.getScreenSize();

	// -- instance fields used with 'combo-box' panel
	private JPanel inputPanel = null;

	private JTextField input = null;

	private JButton comboBtn = new CustomComboArrowButton( SwingConstants.SOUTH );

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled( boolean enabled )
	{
		super.setEnabled( enabled );
		comboBtn.setEnabled( enabled );

	}

	// -- instance fields used with calendar panel
	private JPanel popUpContentPanel = null;

	private JWindowPopup popup;
	private Object current;

	/**
	 * Create a new generic combo-box object set with the given Object.
	 * 
	 * @param cal
	 *            a calendar object
	 */
	public GenericComboBox()
	{
		super();
		setEnabled( true );

	}

	public GenericComboBox( Object value )
	{
		this();
		setCurrent( value );

	}

	@Override
	public void addNotify()
	{
		super.addNotify();

		Border stdBorder = UIManager.getDefaults()
			.getBorder(
				"ComboBox.border" );
		setBorder( stdBorder );

		input = getComboTextComponent();

		( (CustomComboArrowButton) comboBtn ).setAssociatedComponent( input );

		// create the GUI elements and assign listeners
		buildInputPanel();

		popUpContentPanel = new JPanel();
		initPopupComponent( popUpContentPanel );
		registerListeners();

		validate();
	}

	/*
	 * Creates a field and 'combo box' button above the calendar to allow user
	 * input.
	 */
	private void buildInputPanel()
	{
		setLayout( new BorderLayout() );

		add(
			input,
			BorderLayout.CENTER );
		add(
			comboBtn,
			BorderLayout.EAST );
		//		GroupLayoutPlus glp = new GroupLayoutPlus( this );
		//		glp.setAutoCreateContainerGaps( false );
		//		glp.setAutoCreateGaps( false );
		//
		//		glp.setHorizontalGroup( SEQUENTIAL.group( SIZING.fill( input ), SIZING.fixed( comboBtn ) ) );
		//		glp.setVerticalGroup( PARALLEL.group( Alignment.CENTER, SIZING.fixed( input ), SIZING.fixed( comboBtn ) ) );
		validate();

		comboBtn.setActionCommand( "combo" );
	}

	@Override
	public int getBaseline( int width, int height )
	{
		int inputBaseLine = input.getBaseline(
			width,
			height );
		return inputBaseLine + input.getLocation().y;
	}

	abstract protected JTextField getComboTextComponent();

	/*
	 * Returns the currently selected date as a <code>Calendar</code> object.
	 * 
	 * @return Calendar the currently selected calendar date
	 */
	public Object getCurrent()
	{
		return current;
	}

	protected JPanel getInputPanel()
	{
		return inputPanel;
	}

	/*
	 * Gets a Popup to hold the calendar display and determines it's position on
	 * the screen.
	 */
	private Popup getPopup()
	{
		Point p = input.getLocationOnScreen();
		Dimension inputSize = input.getPreferredSize();

		Dimension popupSize = popUpContentPanel.getMinimumSize();

		popupSize.width = Math.max(
			popupSize.width,
			inputSize.width );

		popUpContentPanel.setMinimumSize( popupSize );
		popUpContentPanel.setMaximumSize( popupSize );

		popUpContentPanel.validate();

		if (( p.y + popupSize.height ) < screenSize.height)
		{
			// will fit below input panel
			// popup = factory.getPopup( input, popUpContentPanel, p.x, p.y +
			// inputSize.height );
			popup = (JWindowPopup) JWindowPopup.getPopup(
				input,
				popUpContentPanel,
				p.x,
				p.y + inputSize.height );
		}
		else
		{
			// need to fit it above input panel
			// popup = factory.getPopup( input, popUpContentPanel, p.x, p.y -
			// popupSize.height );
			popup = (JWindowPopup) JWindowPopup.getPopup(
				input,
				popUpContentPanel,
				p.x,
				p.y - popupSize.height );
		}

		popup.getInternalWindow()
			.setModalExclusionType(
				ModalExclusionType.APPLICATION_EXCLUDE );
		return popup;
	}

	protected void hidePopup()
	{
		if (popup != null)
		{
			popup.hide();
		}

		popupShown = false;
		popup = null;
	}

	/*
	 * Builds the panel to be displayed in the popup
	 */
	abstract protected void initPopupComponent( JPanel contentPanel );

	public boolean isPopupShown()
	{
		return popupShown;
	}

	/*
	 * Register all required listeners with appropriate components
	 */
	private void registerListeners()
	{

		ComboButtonActionListener btnListener = new ComboButtonActionListener();

		// 'Combo-box' listeners
		input.addKeyListener( new ComboEditorListener() );
		comboBtn.addActionListener( btnListener );

	}

	/**
	 * Sets the current date and updates the UI to reflect the new date.
	 * 
	 * @param newDate
	 *            the new date as a <code>Date</code> object.
	 * @see Date
	 * @author James Waldrop
	 */
	public void setCurrent( Object value )
	{
	}

	private void showPopup()
	{
		popup = (JWindowPopup) getPopup();
		popup.show();
		popupShown = true;
	}

}
