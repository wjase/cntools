package com.cybernostics.lib.gui.grouplayoutplus;

import java.awt.Component;
import java.awt.Container;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

// import com.cybernostics.joeymail.JoeyMail;
// import com.cybernostics.joeymail.util.gui.OyoahaLook;
/**
 * The GroupLayoutPlus is a thin extension of the GroupLayout layout manager which enables a cleaner api.
 * 
 * Whereas a layout specification for GroupLayout uses lots of nested function calls, GroupLayout plus has the notion of
 * groups being "dummy components" so the structure of groups within groups is much easier to see without all the
 * 'noise' of function call text.
 * 
 * @author jasonw
 * 
 */
public class GroupLayoutPlus extends javax.swing.GroupLayout
{

	public static void main( String[] args )
	{
		JFrame jf = new JFrame( "Group Layout Plus Example" );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		JPanel jpTest = new JPanel();
		GroupLayoutPlus egl = new GroupLayoutPlus( jpTest );
		jpTest.setLayout( egl );

		JButton but1 = new JButton( "One" );
		JButton but2 = new JButton( "Two" );
		JButton but3 = new JButton( "Three" );
		JButton but4 = new JButton( "Four" );
		JButton but5 = new JButton( "Five" );

		egl.setVerticalGroup( SEQUENTIAL.group(
			but1,
			but2,
			but3,
			SIZING.fill( but4 ),
			but5 ) );
		egl.setHorizontalGroup( SEQUENTIAL.group(
			but1,
			PARALLEL.groupLinked(
				Alignment.TRAILING,
				but2,
				but3 ),
			SIZING
				.fill( but4 ),
			but5 ) );

		jf.getContentPane()
			.add(
				jpTest );
		jf.setVisible( true );
		jf.pack();
		jf.setMinimumSize( jf.getSize() );

	}

	public GroupLayoutPlus( Container host )
	{
		super( host );
		host.setLayout( this );
		setAutoCreateContainerGaps( true );
		setAutoCreateGaps( true );
	}

	public static void fillWithChild( Container parent, Component child )
	{
		GroupLayoutPlus glp = new GroupLayoutPlus( parent );

		glp.setHorizontalGroup( PARALLEL.group(
			Alignment.CENTER,
			child ) );
		glp.setVerticalGroup( PARALLEL.group(
			Alignment.CENTER,
			child ) );

	}

	public static void layoutCompactGrid( int rows,
		int columns,
		GroupLayoutPlus glp,
		List< ? extends JComponent > comps )
	{
		LAYOUTGROUP rowGroups = SEQUENTIAL.group( Alignment.CENTER );
		for (int rowIndex = 0; rowIndex < rows; ++rowIndex)
		{
			LAYOUTGROUP row = PARALLEL.group( Alignment.CENTER );
			for (int colIndex = 0; colIndex < columns; ++colIndex)
			{
				int item = ( rowIndex * columns ) + colIndex;
				row.addToGroup( comps.get( item ) );
			}
			rowGroups.addToGroup( row );
		}
		glp.setVerticalGroup( rowGroups );

		LAYOUTGROUP vertical = SEQUENTIAL.group( Alignment.CENTER );

		for (int colIndex = 0; colIndex < columns; ++colIndex)
		{
			LAYOUTGROUP column = PARALLEL.group( Alignment.CENTER );
			for (int rowIndex = 0; rowIndex < rows; ++rowIndex)
			{
				int item = ( rowIndex * columns ) + colIndex;
				column.addToGroup( comps.get( item ) );
			}
			vertical.addToGroup( column );
		}

		glp.setHorizontalGroup( vertical );
	}

	private void addComponentToGroup( Group g, Component comp )
	{
		if (comp instanceof SIZING)
		{
			SIZING rs = (SIZING) comp;
			g.addComponent(
				rs.getToAdd(),
				rs.getMin(),
				rs.getPreferred(),
				rs.getMax() );
			// Add a spacer between this and the edge of the component to be nice
		}
		else
			if (comp instanceof PARALLEL)
			{
				PARALLEL parGroup = (PARALLEL) comp;
				parallel(
					g,
					parGroup.isLinkSizes(),
					parGroup.getAlignment(),
					parGroup.getComps() );
			}
			else
				if (comp instanceof SEQUENTIAL)
				{
					SEQUENTIAL seqGroup = (SEQUENTIAL) comp;
					sequential(
						g,
						seqGroup.getComps() );
				}
				else
				{
					g.addComponent( comp );
				}
	}

	public Group parallel( Alignment alignment, Component... components )
	{

		return parallel(
			null,
			false,
			alignment,
			components );
	}

	public Group parallel( Group parent,
		boolean linked,
		Alignment alignment,
		Component... components )
	{
		ParallelGroup pg = createParallelGroup( alignment );

		if (linked)
		{
			linkSize( components );
		}

		for (Component eachOne : components)
		{
			addComponentToGroup(
				pg,
				eachOne );
		}

		if (parent != null)
		{
			parent.addGroup( pg );
		}
		return pg;
	}

	public ParallelGroup parallelLinked( Alignment alignment,
		Component... components )
	{
		ParallelGroup pg = createParallelGroup( alignment );

		parallel(
			pg,
			true,
			alignment,
			components );
		return pg;
	}

	public Group sequential( Component... components )
	{
		SequentialGroup sg = (SequentialGroup) sequential(
			null,
			components );
		// sg.addContainerGap();
		return sg;

	}

	public Group sequential( Group parent, Component... components )
	{
		SequentialGroup sg = createSequentialGroup();
		for (Component eachOne : components)
		{
			addComponentToGroup(
				sg,
				eachOne );
		}
		if (parent != null)
		{
			parent.addGroup( sg );
		}
		return sg;

	}

	public void setHorizontalGroup( LAYOUTGROUP group )
	{
		if (group instanceof PARALLEL)
		{
			setHorizontalGroup( parallel(
				group.getAlignment(),
				group.getComps() ) );
		}
		else
		{
			setHorizontalGroup( sequential( group.getComps() ) );
		}

	}

	public void setVerticalGroup( LAYOUTGROUP group )
	{
		if (group instanceof PARALLEL)
		{
			setVerticalGroup( parallel(
				group.getAlignment(),
				group.getComps() ) );
		}
		else
		{
			setVerticalGroup( sequential( group.getComps() ) );
		}

	}
}
