package com.cybernostics.lib.gui.autocomplete;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Caret;
import javax.swing.text.Document;

import com.cybernostics.lib.gui.declarative.events.RunLater;
import com.cybernostics.lib.gui.declarative.events.WhenClicked;
import com.cybernostics.lib.gui.declarative.events.WhenKeyPressed;
import com.cybernostics.lib.gui.declarative.events.WhenKeyTyped;

/**
 * Like JtextField but pops up suggestions when a key character is typed. Like a
 * dot or slash for system paths
 * 
 * @author jasonw
 * 
 */
public class AutoCompleteTextField extends JTextField
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8964469766093422890L;

	private char popupTrigger = '.';

	JList popupList = new JList();

	/**
	 * Contains the list for the popup options
	 */
	JScrollPane popupScrollPane = new JScrollPane( popupList );

	private static final Toolkit toolkit = Toolkit.getDefaultToolkit();

	private static final Dimension screenSize = toolkit.getScreenSize();

	// try to use this instead of popup
	JWindow jwPopup = new JWindow();

	private AutoCompleteOptionSource autoCompleteOptionProvider = null;

	// Popup popup = null;

	// This tracks the last reported position of the editiing Caret
	Point lastCaretPos = new Point( 0, 0 );

	public AutoCompleteTextField()
	{
		super();
	}

	public AutoCompleteTextField( Document doc, String text, int columns )
	{
		super( doc, text, columns );
	}

	public AutoCompleteTextField( int columns )
	{
		super( columns );
	}

	public AutoCompleteTextField( String text )
	{
		super( text );
	}

	public AutoCompleteTextField( String text, int columns )
	{
		super( text, columns );
	}

	@Override
	public void addNotify()
	{
		super.addNotify();

		popupList.setModel( new DefaultListModel() );

		jwPopup.getContentPane()
			.setLayout(
				new GridLayout( 1, 1 ) );
		jwPopup.getContentPane()
			.add(
				popupScrollPane );
		jwPopup.setAlwaysOnTop( true );

		final char newlineChar = '\n';
		final char spaceChar = ' ';

		new WhenKeyPressed( this )
		{
			@Override
			public void doThis( KeyEvent e )
			{
				// For some reason the popup does not get keyboard focus. So we
				// cheat by forwarding up and down key
				// events to the scrollpane and ensure it scrolls correctly
				if (jwPopup.isVisible())
				{
					if (( e.getKeyCode() == KeyEvent.VK_UP ))
					{
						int selected = popupList.getSelectedIndex();
						if (selected > 0)
						{
							popupList.setSelectedIndex( --selected );
							popupList.scrollRectToVisible( popupList.getCellBounds(
								selected,
								selected ) );
						}

					}
					if (( e.getKeyCode() == KeyEvent.VK_DOWN ))
					{
						int selected = popupList.getSelectedIndex();
						if (selected < popupList.getModel()
							.getSize() - 1)
						{
							popupList.setSelectedIndex( ++selected );
							popupList.scrollRectToVisible( popupList.getCellBounds(
								selected,
								selected ) );
						}

					}
				}

			}
		};
		new WhenKeyTyped( this )
		{

			@Override
			public void doThis( KeyEvent e )
			{
				if (jwPopup.isVisible())
				{
					if (( e.getKeyChar() == newlineChar )
						|| ( spaceChar == e.getKeyChar() ))
					{
						autoCompleteTextSelected();
						return;
					}
					if (( e.getKeyChar() == KeyEvent.VK_BACK_SPACE )
						|| ( e.getKeyChar() == KeyEvent.VK_ESCAPE ))
					{
						hidePopup();
						return;
					}

					new RunLater()
					{

						@Override
						public void run( Object... args )
						{
							hidePopup();

						}
					};
					new RunLater()
					{

						@Override
						public void run( Object... args )
						{
							showPopup();

						}
					};
					//
					// }

				}
				else
				{
					if (e.getKeyChar() == popupTrigger)
					{
						new RunLater()
						{

							@Override
							public void run( Object... args )
							{
								showPopup();

							}
						};
					}
				}

			}
		};

		new WhenClicked( popupList )
		{

			@Override
			public void doThis( ActionEvent e )
			{
				autoCompleteTextSelected();
			}
		};

		getCaret().addChangeListener(
			new ChangeListener()
		{

			@Override
			public void stateChanged( ChangeEvent e )
			{
				// It seems that this periodically returns null as the Caret is
				// blinking
				Point p = ( (Caret) e.getSource() ).getMagicCaretPosition();
				if (p != null)
				{
					lastCaretPos = p;
				}

			}
		} );
	}

	/**
	 * This method is called when the popup text has been selected. The default
	 * behaviour is to hide the popup.
	 */
	protected void autoCompleteTextSelected()
	{

		hidePopup();

		new RunLater()
		{

			@Override
			public void run( Object... args )
			{
				int index = popupList.getSelectedIndex();
				if (index >= 0)
				{
					String element = (String) popupList.getModel()
						.getElementAt(
							index );
					// System.out.println( element );
					autoCompleteTextSelected( element );
				}

			}
		};
	}

	/**
	 * Override this method to handle what is done with the selected text from
	 * the auto-complete popup
	 * 
	 * @param selectedText
	 */
	protected void autoCompleteTextSelected( String selectedText )
	{
		setText( getText() + selectedText );
	}

	/*
	 * Gets a Popup to hold the calendar display and determines it's position on
	 * the screen. Tried to use a Popup and Popup Factory but found that it
	 * would not get focus if its bounds extended outside the container pane.
	 */
	private JWindow getFramePopup()
	{
		Point origin = getLocationOnScreen();

		JComponent popupList = getPopupList();

		Dimension popupSize = popupList.getPreferredSize();

		Point p = new Point( lastCaretPos.x, lastCaretPos.y );
		p.y += origin.y;
		p.y += getSize().height;
		p.x += origin.x;

		if (( p.y + popupSize.height ) < screenSize.height)
		{

			// will fit below input panel
			// popup = factory.getPopup( SwingUtilities.windowForComponent( this
			// ), popupList, p.x, p.y );
			jwPopup.setLocation(
				p.x,
				p.y );
			jwPopup.setSize( popupList.getPreferredSize() );
		}
		else
		{// need to fit it above input panel

			jwPopup.setLocation(
				p.x,
				p.y - popupSize.height );
			// popup = factory.getPopup( SwingUtilities.windowForComponent( this
			// ), popupList, p.x, p.y - popupSize.height );
		}
		return jwPopup;
	}

	public AutoCompleteOptionSource getOptionProvider()
	{
		return autoCompleteOptionProvider;
	}

	// /*
	// * Gets a Popup to hold the calendar display and determines it's position
	// on the screen.
	// */
	// private Popup getPopup()
	// {
	//
	// Point origin = getLocationOnScreen();
	//
	// JComponent popupList = getPopupList();
	//
	// Dimension popupSize = popupList.getPreferredSize();
	//
	// Point p = new Point( lastCaretPos.x, lastCaretPos.y );
	// p.y += origin.y;
	// p.y += getSize().height;
	// p.x += origin.x;
	//
	// if ( ( p.y + popupSize.height ) < screenSize.height )
	// {
	//
	// // will fit below input panel
	// popup = factory.getPopup( SwingUtilities.windowForComponent( this ),
	// popupList, p.x, p.y );
	// }
	// else
	// {
	// // need to fit it above input panel
	// popup = factory.getPopup( SwingUtilities.windowForComponent( this ),
	// popupList, p.x, p.y - popupSize.height );
	// }
	// return popup;
	// }

	private JComponent getPopupList()
	{
		DefaultListModel dlm = (DefaultListModel) popupList.getModel();
		dlm.clear();
		String[] options = autoCompleteOptionProvider.getOptions( getText() );
		if (options.length > 0)
		{
			for (String eacString : options)
			{
				dlm.addElement( eacString );
			}
		}
		else
		{
			dlm.addElement( "*No Options" );
		}

		popupList.setSelectedIndex( 0 );
		popupList.setPrototypeCellValue( "Index 12345678901234567890" );
		return popupScrollPane;
	}

	public char getPopupTrigger()
	{
		return popupTrigger;
	}

	private void hidePopup()
	{
		// if ( popup != null )
		// {
		// popup.hide();
		// }
		jwPopup.setVisible( false );
	}

	/**
	 * Sets the object to provide contextual autocomplete based on the current
	 * text content.
	 * 
	 * @param optionProvider
	 */
	public void setAutoCompleteOptionProvider( AutoCompleteOptionSource optionProvider )
	{
		this.autoCompleteOptionProvider = optionProvider;
	}

	public void setPopupTrigger( char popupTrigger )
	{
		this.popupTrigger = popupTrigger;
	}

	@Override
	public void setText( String t )
	{
		super.setText( t );
	}

	private void showPopup()
	{
		new RunLater()
		{

			@Override
			public void run( Object... args )
			{
				getFramePopup();
				jwPopup.pack();
				jwPopup.setVisible( true );

				// popup = getPopup();
				//
				// popup.show();

			}
		};

	}
}
