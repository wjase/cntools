package com.cybernostics.lib.gui.window;

import com.cybernostics.lib.animator.paramaterised.LinearChange;
import com.cybernostics.lib.animator.track.*;
import com.cybernostics.lib.animator.track.ordering.TrackEndedListener;
import com.cybernostics.lib.concurrent.GUIEventThread;
import com.cybernostics.lib.gui.adapter.FieldUtil;
import com.cybernostics.lib.gui.ButtonFactory;
import com.cybernostics.lib.gui.IconFactory.StdButtonType;
import com.cybernostics.lib.gui.declarative.events.WhenClicked;
import com.cybernostics.lib.gui.dialogs.DialogBox;
import com.cybernostics.lib.gui.windowcore.DialogResponses;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class FullScreenFrame extends JFrame
{

	/**
	 *
	 */
	private static final long serialVersionUID = 7602422042567318004L;

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		final JFrame fsf = new JFrame( "myFrame" );
		//fsf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		fsf.getContentPane()
			.setBackground(
				Color.black );
		//fsf.slideIn();

		JButton jb = ButtonFactory.getStdButton(
			StdButtonType.CLOSE,
			null );
		//jb.setPreferredSize( new Dimension( 60, 40 ) );
		fsf.getContentPane()
			.setLayout(
				new FlowLayout() );
		fsf.getContentPane()
			.add(
				jb );

		new WhenClicked( jb )
		{

			@Override
			public void doThis( ActionEvent e )
			{
				fsf.setVisible( false );//closeIfAllowed();
			}

		};

		GUIEventThread.show( fsf );

	}

	static public void recurseSetEnabled( JComponent c, boolean flag )
	{
		c.setEnabled( flag );
		for (Component comp : c.getComponents())
		{
			if (comp instanceof JComponent)
			{
				recurseSetEnabled(
					(JComponent) comp,
					flag );
			}
		}
	}

	Map< String, JComponent > namedComponents = new HashMap< String, JComponent >();

	Timer t;

	TimerTask myTask;

	public FullScreenFrame( String name )
	{
		super( name );

		setBackground( Color.black );
		// Don't need TitleBars for kids app.
		//setUndecorated( true );

		// this.setMaximizedBounds(env.getMaximumWindowBounds());
		//this.setExtendedState( this.getExtendedState() | Frame.MAXIMIZED_BOTH );
		setResizable( false );
		//setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );

		//setUndecorated( true );
		Dimension size = Toolkit.getDefaultToolkit()
			.getScreenSize();
		setBounds(
			0,
			0,
			size.width,
			size.height );
		//setAlwaysOnTop( true );

		addWindowListener( new WindowAdapter()
		{

			@Override
			public void windowClosing( WindowEvent e )
			{
				closeIfAllowed();

			}

		} );

	}

	public boolean canCloseNow()
	{
		return DialogBox.getYesNoResponse(
			getClosePrompt(),
			"Close Check",
			null,
			false ) == DialogResponses.YES_ANSWER;
	}

	protected String getClosePrompt()
	{
		return "Are you sure  you want to close now?";
	}

	public void closeIfAllowed()
	{
		if (canCloseNow())
		{
			dispose();
		}

	}

	public JButton getButton( String name )
	{
		return (JButton) namedComponents.get( name );
	}

	public JTextField getTextField( String name )
	{
		JTextField jtf = (JTextField) namedComponents.get( name );

		if (jtf == null)
		{
			jtf = FieldUtil.getTextField(
				name,
				getContentPane() );
		}
		return jtf;
	}

	public void init()
	{
		// override me.
	}

	public void slideOut()
	{
		Rectangle r = getBounds();
		Track slider = slideWindow(
			0,
			r.width );
		Sequencer.get()
			.addAndStartTrack(
				slider );
		slider.addTrackEndedListener( new TrackEndedListener()
		{

			@Override
			public void trackEnded( Track source )
			{
				setVisible( false );
			}

		} );

	}

	private BasicTrack slideWindow( int start, int stop )
	{
		LinearChange windowPath = new LinearChange( start, stop );
		AdapterAnimatorTrack slideIn = new AdapterAnimatorTrack( "slidein",
			2000,
			windowPath,
			new AnimatedProperty< Double >()
			{

				@Override
				public void update( Double value )
				{
					setLocation(
						value.intValue(),
						0 );
				}

			} );
		return slideIn;

	}

	public void slideIn()
	{
		Rectangle r = getBounds();
		Track slider = slideWindow(
			r.width,
			0 );
		Sequencer.get()
			.addAndStartTrack(
				slider );
		setVisible( true );
		//        SwingWorker< Integer, Integer> mover = new SwingWorker< Integer, Integer>()
		//        {
		//
		//            @Override
		//            protected Integer doInBackground() throws Exception
		//            {
		//
		//                setVisible( true );
		//                Rectangle r = getBounds();
		//
		//                Integer location = new Integer( r.width );
		//                setLocation( location, 0 );
		//
		//                while (location > 10)
		//                {
		//                    publish( location );
		//                    Thread.sleep( 15 );
		//                    location = location - 10;
		//                }
		//
		//                return 0;
		//            }
		//
		//            @Override
		//            protected void done()
		//            {
		//                setLocation( 0, 0 );
		//            }
		//
		//            @Override
		//            protected void process( List< Integer> chunks )
		//            {
		//                setLocation( chunks.get( chunks.size() - 1 ), 0 );
		//            }
		//        };
		//
		//        mover.execute();

	}

}
