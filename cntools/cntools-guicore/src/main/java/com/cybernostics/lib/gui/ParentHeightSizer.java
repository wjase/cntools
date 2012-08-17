package com.cybernostics.lib.gui;

import com.cybernostics.lib.gui.declarative.events.WhenResized;
import com.cybernostics.lib.test.JFrameTest;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import javax.swing.*;

/**
 * Sets the preferred size based on a components height (taking insets into
 * account)
 *
 * @author jasonw
 */
public class ParentHeightSizer
{

	private static final double defaultRatio = .9;

	private static double aspectRatio = defaultRatio;

	public static void bind( final JComponent parent, final JComponent child )
	{
		bind(
			parent,
			child,
			aspectRatio );
	}

	public static void bind( final JComponent parent,
		final JComponent child,
		final double aspect )
	{
		final Dimension max = new Dimension();

		new WhenResized( parent )
		{

			@Override
			public void doThis( ComponentEvent e )
			{
				update(
					max,
					parent,
					child,
					aspect );
			}

		};
		update(
			max,
			parent,
			child,
			aspect );
	}

	private static void update( Dimension d,
		JComponent parent,
		JComponent child,
		double aspect )
	{
		Dimension dOld = child.getSize();
		Insets i = parent.getInsets();
		d.height = parent.getHeight() - i.bottom - i.top;
		d.width = (int) ( d.height / aspect );
		if (( dOld.width != d.width ) || ( dOld.height != d.height ))
		{
			child.setMaximumSize( d );
			child.setPreferredSize( d );
		}
		//parent.validate();

	}

	public static void main( String[] args )
	{
		JFrameTest jf = JFrameTest.create( "BoxTest" );
		// create your component
		JPanel tstObject = new JPanel();
		tstObject.setLayout( new BoxLayout( tstObject, BoxLayout.X_AXIS ) );
		jf.getContentPane()
			.setLayout(
				new GridLayout() );

		jf.getContentPane()
			.add(
				tstObject );

		JButton jb1 = new JButton();

		bind(
			tstObject,
			jb1 );
		tstObject.add( jb1 );
		tstObject.add( Box.createGlue() );
		JButton jb2 = new JButton();
		bind(
			tstObject,
			jb2,
			.5 );
		tstObject.add( jb2 );
		jf.pack();

		tstObject.setPreferredSize( new Dimension( 600, 100 ) );

		jf.go();
	}

}
