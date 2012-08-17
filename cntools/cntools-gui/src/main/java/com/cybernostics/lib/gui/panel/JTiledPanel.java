package com.cybernostics.lib.gui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * A small extension to JPanel, meant to allow the JPanel to support a tiling
 * image background. The tiled background is correctly drawn inside any Border
 * that the panel might have. Note that JTiledPanel containers are always
 * opaque. If you give the tiling image as null, then JTiledPanel behaves
 * exactly like an opaque JPanel.
 */
public class JTiledPanel extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image tileimage;
	private int tilewidth;
	private int tileheight;
	private final Rectangle rb;
	private final Insets ri;

	/**
	 * Create a JTiledPanel with the given image. The tile argument may be null,
	 * you can set it later with setTileImage(). Note that a JTiledPanel is
	 * always opaque.
	 */
	public JTiledPanel( Image tile )
	{
		super();
		setTileImage( tile );
		setOpaque( true );
		rb = new Rectangle( 0, 0, 1, 1 );
		ri = new Insets( 0, 0, 0, 0 );
	}

	/**
	 * Create a JTiledPanel with the given image and layout manager and double
	 * buffering status. Either or both of the first two arguments may be null.
	 */
	public JTiledPanel( Image tile, LayoutManager mgr, boolean isDB )
	{
		super( mgr, isDB );
		setTileImage( tile );
		setOpaque( true );
		rb = new Rectangle( 0, 0, 1, 1 );
		ri = new Insets( 0, 0, 0, 0 );
	}

	/**
	 * Get the current tiling image, or null if there isn't any right now.
	 */
	public Image getTileImage()
	{
		return tileimage;
	}

	/**
	 * Set the current tiling image. To prevent tiling, call this method with
	 * null. Note that this method does NOT call repaint for you; if you want
	 * the panel to repaint immediately, you must call repaint() yourself.
	 */
	public void setTileImage( Image tile )
	{
		tileimage = tile;
		tilewidth = 0;
		tileheight = 0;
		repaint();
	}

	/**
	 * Paint this component, including the tiled background image, if any.
	 */
	@Override
	public void paintComponent( Graphics g )
	{
		super.paintComponent( g );
		if (( tileimage != null ) && ( tilewidth <= 0 ))
		{
			tileheight = tileimage.getHeight( this );
			tilewidth = tileimage.getWidth( this );
		}
		if (( tileimage != null ) && ( tilewidth > 0 ))
		{
			Color bg = getBackground();
			getBounds( rb );
			Insets riv = getInsets( ri );
			rb.translate(
				riv.left,
				riv.top );
			rb.width -= ( riv.left + riv.right );
			rb.height -= ( riv.top + riv.bottom );
			Shape ccache = g.getClip();
			g.clipRect(
				rb.x,
				rb.y,
				rb.width,
				rb.height );
			int xp, yp;
			for (yp = rb.y; yp < rb.y + rb.height; yp += tileheight)
			{
				for (xp = rb.x; xp < rb.x + rb.width; xp += tilewidth)
				{
					g.drawImage(
						tileimage,
						xp,
						yp,
						bg,
						this );
				}
			}
			g.setClip( ccache );
		}
	}

	/**
	 * Small main to do a self-test. Tiles with a image file name taken from the
	 * command line. For example, if you have a directory named <tt>images</tt>
	 * and an image in it named <tt>tile1.gif</tt> then you would run this test
	 * main as <tt>java JTiledPanel images/tile1.gif</tt>.
	 */
	public static void main( String[] args )
	{

		JFrame f = new JFrame( "Test JTiledPanel " + "cougar1.gif" );
		// URL uBase = JTiledPanel.class.getResource( "cougar1.gif" );
		ImageIcon ic = new ImageIcon( "c:/temp/image.jpg" );
		JTiledPanel jtp = new JTiledPanel( ic.getImage() );
		jtp.setBorder( BorderFactory.createMatteBorder(
			3,
			4,
			5,
			6,
			Color.green ) );
		jtp.add( new JButton( "Press Me!" ) );
		jtp.add( new JButton( "Press Me Too!" ) );
		f.getContentPane()
			.add(
				jtp,
				BorderLayout.CENTER );
		f.setSize(
			350,
			290 );
		f.setVisible( true );
		f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
}
