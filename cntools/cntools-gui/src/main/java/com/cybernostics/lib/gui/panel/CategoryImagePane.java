package com.cybernostics.lib.gui.panel;

import com.cybernostics.lib.gui.lookandfeel.NimbusLook;
import com.cybernostics.lib.gui.shapeeffects.ShapedPanel;
import com.cybernostics.lib.gui.border.TubeBorder;

import com.cybernostics.lib.gui.control.ComboListModelAdapter;
import com.cybernostics.lib.gui.IconFactory;
import com.cybernostics.lib.gui.control.ListFillerTask;
import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

/**
 *
 * @author jasonw
 */
public class CategoryImagePane extends ShapedPanel
{

	public static void main( String[] args )
	{
		try
		{
			NimbusLook.set();
			//OyoahaLook.set( CategoryImagePane.class );
			JFrame jf = new JFrame( "Categories" );
			jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
			jf.setSize(
				500,
				500 );
			jf.getContentPane()
				.setLayout(
					new GridLayout() );

			final CategoryImagePane cip = CategoryImagePane.create();
			cip.addCategory( new URLImageCategory( "category1", new URL(
				"file:///C:/data/images/Photographs/2008/2008_01_17" ) ) );
			cip.addCategory( new URLImageCategory( "category2", new URL(
				"file:///C:/data/images/Photographs/2008/2008_01_19" ) ) );
			cip.addCategory( new URLImageCategory( "category3", new URL(
				"file:///C:/data/images/Photographs/2008/2008_01_23" ) ) );

			cip.setBorder( new TubeBorder( 30 ) );
			JComboBox picker = cip.getCategoryPickerCtrl();
			picker.setFont( new Font( "Comic Sans MS", Font.BOLD, 24 ) );
			cip.setBackground( Color.green.darker()
				.darker()
				.darker() );
			jf.getContentPane()
				.add(
					cip );
			jf.setVisible( true );

			cip.addPropertyChangeListener(
				ImagePane.SELECTION_CHANGED,
				new PropertyChangeListener()
			{

				@Override
				public void propertyChange( PropertyChangeEvent evt )
				{
					for (Object eachObj : cip.getSelected())
					{
						System.out.println( eachObj.toString() );
					}
				}
			} );
		}
		catch (MalformedURLException ex)
		{
			Logger.getLogger(
				CategoryImagePane.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
	}

	CustomComboBoxUI comboUI = null;

	public static CategoryImagePane create()
	{
		CategoryImagePane cip = new CategoryImagePane();
		cip.init();
		return cip;
	}

	private CategoryImagePane()
	{
		ScalableSVGIcon si = IconFactory.getStdIcon( IconFactory.StdButtonType.DOWN );
		si.setSize( new Dimension( 40, 40 ) );
		//comboUI = new CustomComboBoxUI( categories.getUI() );
		//comboUI.setCategoryButtonIcon( si );
		//categories.setUI( comboUI );
		//setBackgroundPainter( new ImageFillEffect( ResourcesRoot.getLoader().loadImage( "gui/images/tiles/cracked.png" ) ) );

	}

	public void setCategoryComboIcon( Icon toSet )
	{
		comboUI.setCategoryButtonIcon( toSet );
	}

	public void setCategories( JComboBox categories )
	{
		this.categories = categories;
	}

	public JComboBox getCategoryPickerCtrl()
	{
		return categories;
	}

	public void init()
	{
		JPanel topPanel = new JPanel();
		BoxLayout bl = new BoxLayout( topPanel, BoxLayout.X_AXIS );
		topPanel.setLayout( bl );
		topPanel.add( Box.createGlue() );
		topPanel.add( categories );
		topPanel.add( Box.createHorizontalStrut( 40 ) );
		topPanel.setOpaque( false );
		topPanel.setBorder( BorderFactory.createEmptyBorder(
			20,
			20,
			20,
			20 ) );

		imageList.setBorder( BorderFactory.createEmptyBorder(
			20,
			20,
			20,
			20 ) );

		//categories.setBackground( new Color( 0, 255, 0, 128 ) );
		setLayout( new BorderLayout() );
		add(
			topPanel,
			BorderLayout.NORTH );
		add(
			imageList,
			BorderLayout.CENTER );
		categories.setModel( dlm );

		imageList.setOpaque( true );
		//categories.setOpaque( false );

		final ListCellRenderer currentRenderer = categories.getRenderer();
		categories.setRenderer( new ListCellRenderer()
		{

			@Override
			public Component getListCellRendererComponent( JList list,
				Object value,
				int index,
				boolean isSelected,
				boolean cellHasFocus )
			{
				Component c = currentRenderer.getListCellRendererComponent(
					list,
					value,
					index,
					isSelected,
					cellHasFocus );
				if (value != null)
				{
					JLabel tc = (JLabel) c;
					ImageCategory ic = (ImageCategory) value;
					tc.setText( ic.getName() );
					//toPaint.setOpaque( false);
					tc.setHorizontalAlignment( SwingConstants.CENTER );

				}
				return c;
			}
		} );

		categories.addItemListener( new ItemListener()
		{

			@Override
			public void itemStateChanged( ItemEvent e )
			{
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					ImageCategory cat = (ImageCategory) dlm.getSelectedItem();
					imageList.setModel( cat.getItems() );
				}
			}
		} );

		if (dlm.getSize() > 0)
		{

			dlm.setSelectedItem( dlm.getElementAt( 0 ) );
		}

		imageList.addPropertyChangeListener(
			ImagePane.SELECTION_CHANGED,
			new PropertyChangeListener()
		{

			@Override
			public void propertyChange( PropertyChangeEvent evt )
			{
				firePropertyChange(
					ImagePane.SELECTION_CHANGED,
					false,
					true );
			}
		} );

	}

	public void addCategories( ListFillerTask filler )
	{

		filler.setCollection( new ComboListModelAdapter( dlm ) );
		filler.start();

	}

	public void addCategory( ImageCategory cat )
	{
		dlm.addElement( cat );
	}

	ImagePane imageList = new ImagePane();
	DefaultComboBoxModel dlm = new DefaultComboBoxModel();
	JComboBox categories = new JComboBox();

	public void setRenderer( ImagePanelRenderer imagePaneConceptRenderer )
	{
		imageList.setRenderer( imagePaneConceptRenderer );
	}

	public Set< Object > getSelected()
	{

		return imageList.getSelectedItems();
	}
}
