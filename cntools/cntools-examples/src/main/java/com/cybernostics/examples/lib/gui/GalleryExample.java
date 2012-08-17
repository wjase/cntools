/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.examples.lib.gui;

import com.cybernostics.app.conceptlist.ConceptBundleList;
import com.cybernostics.app.gamecore.GameImageSource;
import com.cybernostics.app.joeymail.res.JoeyMailResources;
import com.cybernostics.lib.animator.SVGBackgroundImage;
import com.cybernostics.lib.animator.ui.AnimatedScene;
import com.cybernostics.lib.animator.ui.Scene;
import com.cybernostics.lib.animator.ui.SceneLoader;
import com.cybernostics.lib.gui.ButtonFactory;
import com.cybernostics.lib.gui.IconFactory;
import com.cybernostics.lib.gui.ScreenRelativeDimension;
import com.cybernostics.lib.gui.declarative.events.WhenClicked;
import com.cybernostics.lib.gui.gallery.ArrayListModel;
import com.cybernostics.lib.gui.gallery.ComponentGallery;
import com.cybernostics.lib.gui.gallery.DefaultGalleryCellRenderer;
import com.cybernostics.lib.gui.shapeeffects.ShapeEffect;
import com.cybernostics.lib.gui.window.CNApplication;
import com.cybernostics.lib.gui.windowcore.ScreenStack;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.media.icon.NoImageIcon;
import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import com.cybernostics.lib.media.image.ImageLoader;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ListModel;

/**
 *
 * @author jasonw
 */
public class GalleryExample
{

    public static void main( String[] args )
    {
        ConceptBundleList.register();

        final AnimatedScene animpanel = new AnimatedScene();
        animpanel.setAssetLoader( new SceneLoader()
        {

            @Override
            public ShapeEffect loadBackGround()
            {
                return new SVGBackgroundImage( new ScalableSVGIcon( JoeyMailResources.getResource( 
                        "scenes/main/images/harbourzoo.svg" ) ) );
            }

            @Override
            public void loadAssets( Scene panel )
            {
                final ComponentGallery myList = ComponentGallery.create();
                myList.setInnerBackground( Color.red );

                DefaultGalleryCellRenderer dgcr = new DefaultGalleryCellRenderer();
                dgcr.setItemSize( new ScreenRelativeDimension( 0.1f, 0.1f ) );
                myList.setCellRenderer( dgcr );

                final ListModel model1 = ImageLoader.getIcons(
                        "file:///C:/data/images/Photographs/2008/2008_01_17/", ImageLoader.ScaleType.WIDTH_ONLY,
                        new ScreenRelativeDimension( 0.1f, 0.1f ) );
                final ListModel model2 = ImageLoader.getIcons(
                        "file:///C:/data/images/Photographs/2008/2008_01_16/", ImageLoader.ScaleType.WIDTH_ONLY,
                        new ScreenRelativeDimension( 0.1f, 0.1f ) );

                final ListModel model3 = new ArrayListModel( GameImageSource.getImages() );

                final ListModel[] models =
                {
                    model1, model2, model3
                };

                myList.setModel( models[0] );
                
                myList.setScrollLeftButton( ButtonFactory.getStdButton( IconFactory.StdButtonType.PREV, null ) );// getButton(
                // "images/Left.png",
                // "Left" ) );
                myList.setScrollRightButton( ButtonFactory.getStdButton( IconFactory.StdButtonType.NEXT, null ) );

                final JLabel theLabel = new JLabel( "Nothing Selected" );
                myList.addPropertyChangeListener( ComponentGallery.CURRENT_INDEX, new PropertyChangeListener()
                {

                    @Override
                    public void propertyChange( PropertyChangeEvent evt )
                    {
                        theLabel.setText( "Item " + evt.getNewValue().toString() );
                    }

                } );

                animpanel.addWithinBounds( myList, new Rectangle2D.Double( 0, 0, 1, .8 ) );


                JButton switchModel = new JButton( "Switch..." );

                new WhenClicked( switchModel )
                {

                    int index = 0;

                    @Override
                    public void doThis( ActionEvent e )
                    {
                        index = ( index + 1 ) % 3;
                        myList.setModel( models[index] );
                    }

                };

                animpanel.addWithinBounds( theLabel, new Rectangle2D.Double( 0, .8, 1, .1 ) );
                animpanel.addWithinBounds( switchModel, new Rectangle2D.Double( 0, .9, 1, .1 ) );
            }

        } );

        ScreenStack.get().register( "gallery", animpanel );
        CNApplication.testScreen( "gallery" );

    }

}
