package com.cybernostics.lib.svg.editor;

import com.cybernostics.lib.gui.ParentHeightSizer;
import com.cybernostics.lib.gui.action.ButtonStyler;
import com.cybernostics.lib.gui.action.ChainableAction;
import com.cybernostics.lib.gui.shapeeffects.ShapedPanel;
import com.cybernostics.lib.media.icon.AttributedScalableIcon;
import com.cybernostics.lib.media.icon.NoImageIcon;
import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import com.cybernostics.lib.svg.SVGUtil;
import com.cybernostics.lib.svg.editor.actions.*;
import com.kitfox.svg.elements.SVGElement;
import com.kitfox.svg.elements.SVGElementListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.JToggleButton.ToggleButtonModel;

/**
 *
 * @author jasonw
 */
public class SVGEditor extends ShapedPanel
	implements
	EditController,
	PointScaler
{

	public static void main( String[] args )
	{
		JFrame jf = new JFrame( "SVGTest" );
		jf.setSize(
			600,
			400 );

		final SVGEditor sve = SVGEditor.create();

		ChainableAction[] actions =
		{
			new BrushAction( sve ),
			new RectangleAction( sve ),
			new EllipseAction( sve ),
			new ChooseImageAction( sve ),
			new GrowBrushAction( sve ),
			new ShrinkBrushAction( sve ),
			new StrokeFillModeAction( sve ),
			new StrokeColorChooserAction( sve ),
			new FillColorChooserAction( sve ),
			new BackgroundColorPickerAction( sve ),
			new SVGSaveAction( sve )
		};

		for (ChainableAction eachAction : actions)
		{
			eachAction.putValue(
				SVGEditorAction.STYLER_KEY,
				null );
			sve.addAction( eachAction );
		}

		AttributedScalableIcon trophy = NoImageIcon.get();

		ScalableSVGIcon svi = new ScalableSVGIcon( trophy.getURL() );
		svi.setFitToParent( true );

		//a.putValue( Action.LARGE_ICON_KEY, trophy );

		sve.getButtonBar()
			.setPreferredSize(
				new Dimension( 800, 80 ) );
		sve.getButtonBar()
			.validate();
		sve.add(
			sve.getButtonBar(),
			BorderLayout.SOUTH );
		//        buttonBar.add( getCompleteActionButton() );

		jf.getContentPane()
			.setLayout(
				new GridLayout() );
		jf.getContentPane()
			.add(
				sve );
		jf.getContentPane()
			.invalidate();
		jf.setVisible( true );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}

	private Map< String, Action > actions = new HashMap< String, Action >();

	private EditController currentTool = null;

	private ImagePanel drawingPane = null;

	private JPanel buttonBar = new JPanel();

	private StrokeSizeAdjuster strokeWidthAdjuster = null;

	public StrokeSizeAdjuster getStrokeWidthAdjuster()
	{
		return strokeWidthAdjuster;
	}

	private List< DrawingListener > listeners = new ArrayList< DrawingListener >();

	public void addDrawingListener( DrawingListener listener )
	{
		listeners.add( listener );
	}

	public void fireDrawingChanged()
	{
		for (DrawingListener eachListener : listeners)
		{
			eachListener.drawingChanged( theDrawing );
		}
	}

	public void setCurrentController( EditController toSet )
	{
		toSet.setDrawing( theDrawing );
		toSet.setPointScaler( this );

		complete();
		currentTool = toSet;
	}

	private ButtonGroup toolSelectors = new ButtonGroup();

	private JButton completeActionButton = new JButton( "Done!" );

	public JButton getCompleteActionButton()
	{
		return completeActionButton;
	}

	public Action getAction( String key )
	{
		return actions.get( key );
	}

	public void addDrawingControl( JComponent jc )
	{
		getButtonBar().add(
			jc );
	}

	public void addAction( ChainableAction a )
	{
		actions.put(
			a.getId(),
			a );

		JPanel buttonContainer = getButtonBar();
		ButtonStyler styler = (ButtonStyler) a.getValue( SVGEditorAction.STYLER_KEY );
		if (a instanceof ControlSelectAction)
		{
			AbstractButton b = createButton( a );
			b.setModel( new ToggleButtonModel() );

			buttonContainer.add( b );
			ParentHeightSizer.bind(
				buttonContainer,
				b );
			if (styler != null)
			{
				styler.apply( b );
			}
			toolSelectors.add( b );
		}
		else
		{
			AbstractButton b = createButton( a );

			buttonContainer.add( b );
			ParentHeightSizer.bind(
				buttonContainer,
				b );
			if (styler != null)
			{
				styler.apply( b );
			}
		}
	}

	public AbstractButton createButton( Action a )
	{
		return new JButton( a );
	}

	public JPanel getButtonBar()
	{
		return buttonBar;
	}

	public SVGDrawing getDrawing()
	{
		return theDrawing;
	}

	private SVGDrawing theDrawing = null;

	protected void initDrawing()
	{
		setDrawing( new SVGDrawing( "Drawing" ) );
	}

	protected SVGEditor()
	{
	}

	public static SVGEditor create()
	{
		SVGEditor svge = new SVGEditor();
		svge.init();
		svge.layoutElements();
		return svge;
	}

	public void layoutElements()
	{
		setLayout( new BorderLayout() );
		add(
			drawingPane,
			BorderLayout.CENTER );
		buttonBar.setLayout( new BoxLayout( buttonBar, BoxLayout.X_AXIS ) );

	}

	private SVGElementListener repainter = new SVGElementListener()
	{

		@Override
		public void elementUpdated( SVGElement changed )
		{
			if (changed == null)
			{
				//drawingPane.repaint();
				repaint();
			}
			else
			{
				Shape s = SVGUtil.getItemShape(
					changed,
					theDrawing.getDiagram() );
				Rectangle2D r = s.getBounds2D();
				double width = drawingPane.getWidth();
				double height = drawingPane.getHeight();
				double x = r.getMinX();
				double y = r.getMinY();
				double rwidth = r.getWidth();
				double rheight = r.getHeight();
				//System.out.printf( "%g,%g,%g,%g\n",x,y,rwidth,rheight );
				drawingPane.repaint(
					(int) ( x * width ),
					(int) ( y * width ),
					(int) ( rwidth * width ),
										(int) ( rheight * height ) );
				drawingPane.repaint( 5 );
			}
		}

	};

	@Override
	public void setDrawing( SVGDrawing theDrawing )
	{
		this.theDrawing = theDrawing;
		theDrawing.addElementListener( repainter );

		if (drawingPane != null)
		{
			drawingPane.repaint();
		}

		fireDrawingChanged();
	}

	@Override
	public void mouseClicked( MouseEvent e )
	{
		if (currentTool != null)
		{
			currentTool.mouseClicked( e );
		}
	}

	@Override
	public void mousePressed( MouseEvent e )
	{
		if (currentTool != null)
		{
			currentTool.mousePressed( e );
		}
	}

	@Override
	public void mouseReleased( MouseEvent e )
	{
		if (currentTool != null)
		{
			currentTool.mouseReleased( e );
		}

	}

	@Override
	public void mouseEntered( MouseEvent e )
	{
		if (currentTool != null)
		{
			currentTool.mouseEntered( e );
		}

	}

	@Override
	public void mouseExited( MouseEvent e )
	{
		if (currentTool != null)
		{
			currentTool.mouseExited( e );
		}
	}

	@Override
	public void mouseDragged( MouseEvent e )
	{
		if (currentTool != null)
		{
			currentTool.mouseDragged( e );
		}
	}

	@Override
	public void mouseMoved( MouseEvent e )
	{
		if (currentTool != null)
		{
			currentTool.mouseMoved( e );
		}
	}

	@Override
	public void complete()
	{
		if (currentTool != null)
		{
			currentTool.complete();
		}
	}

	@Override
	public void setPointScaler( PointScaler scaler )
	{
		this.scaler = scaler;
	}

	private final PointScaler defaultScaler = new PointScaler()
	{

		@Override
		public Point2D getPoint( Point p )
		{
			return new Point2D.Double( scaleX( p.x ), scaleY( p.y ) );
		}

		@Override
		public double scaleX( int x )
		{
			return 1.0 * x / drawingPane.getWidth();
		}

		@Override
		public double scaleY( int y )
		{
			return 1.0 * y / drawingPane.getHeight();
		}

	};

	private PointScaler scaler = defaultScaler;

	@Override
	public Point2D getPoint( Point p )
	{
		return scaler.getPoint( p );
	}

	@Override
	public double scaleX( int x )
	{
		return scaler.scaleX( x );
	}

	@Override
	public double scaleY( int y )
	{
		return scaler.scaleY( y );
	}

	protected void init()
	{
		if (theDrawing == null)
		{
			initDrawing();
		}
		drawingPane = new ImagePanel( theDrawing );

		drawingPane.addMouseListener( this );
		drawingPane.addMouseMotionListener( this );

		strokeWidthAdjuster = new StrokeSizeAdjuster( this );
		drawingPane.addMouseWheelListener( strokeWidthAdjuster );

	}

}
