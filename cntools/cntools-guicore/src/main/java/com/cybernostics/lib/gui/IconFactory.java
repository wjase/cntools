package com.cybernostics.lib.gui;

import com.cybernostics.lib.Application.AppResources;
import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import com.cybernostics.lib.media.icon.ScreenRelativeIconSizer;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import com.cybernostics.lib.resourcefinder.Finder;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import java.awt.Dimension;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class IconFactory
{

	public enum StdButtonType
	{

		SHAPES, NEWITEM, STOP, CONFIG, LISTREMOVE, LISTADD, ABC, IMPORTANT, NEW_CONTACT, EXIT, OK, CANCEL, YES, NO, UP, DOWN, COPY, CLEAR, CLOSE, TRASH, PRINT, PAINT, CHEST, BACK, DICE, UNDO, CAMERA, PARENT, FIRST, LAST, NEXT, PREV, ADD, DELETE, ADDRESS_BOOK, OPEN, FOLDER, FOLDER_OPEN
	}

	private static SingletonInstance< Finder > loader = new SingletonInstance< Finder >()
	{
		@Override
		public Finder createInstance()
		{
			if (AppResources.get( "gui/control/icons/exit.svg" ) != null)
			{
				return AppResources.getFinder();
			}
			throw new RuntimeException( "You must call ResourcesRoot.register() before accessing common resources." );
		}
	};

	private static Finder getFinder()
	{
		return loader.get();
	}

	static Map< StdButtonType, URL > buttonURLs = null;

	public static URL getIconURL( StdButtonType toGet )
	{
		if (buttonURLs == null)
		{
			try
			{
				buttonURLs = new HashMap< StdButtonType, URL >();
				buttonURLs.put(
					StdButtonType.EXIT,
					loader.get()
						.getResource(
							"gui/control/icons/exit.svg" ) );
				buttonURLs
					.put(
						StdButtonType.CAMERA,
						loader.get()
							.getResource(
								"gui/control/icons/camera-photo.svg" ) );
				buttonURLs.put(
					StdButtonType.NEW_CONTACT,
					loader.get()
						.getResource(
							"gui/control/icons/contact-new.svg" ) );
				buttonURLs
					.put(
						StdButtonType.PRINT,
						loader.get()
							.getResource(
								"gui/control/icons/document-print.svg" ) );
				buttonURLs.put(
					StdButtonType.DELETE,
					loader.get()
						.getResource(
							"gui/control/icons/edit-delete.svg" ) );
				buttonURLs.put(
					StdButtonType.TRASH,
					loader.get()
						.getResource(
							"gui/control/icons/edit-delete.svg" ) );
				buttonURLs.put(
					StdButtonType.IMPORTANT,
					loader.get()
						.getResource(
							"gui/control/icons/emblem-important.svg" ) );
				buttonURLs.put(
					StdButtonType.ABC,
					loader.get()
						.getResource(
							"gui/control/icons/insert-text.svg" ) );
				buttonURLs.put(
					StdButtonType.LISTADD,
					loader.get()
						.getResource(
							"gui/control/icons/list-add.svg" ) );
				buttonURLs.put(
					StdButtonType.LISTREMOVE,
					loader.get()
						.getResource(
							"gui/control/icons/list-remove.svg" ) );
				buttonURLs.put(
					StdButtonType.CONFIG,
					loader.get()
						.getResource(
							"gui/control/icons/preferences-system.svg" ) );
				buttonURLs.put(
					StdButtonType.STOP,
					loader.get()
						.getResource(
							"gui/control/icons/process-stop.svg" ) );
				buttonURLs.put(
					StdButtonType.CLOSE,
					loader.get()
						.getResource(
							"gui/control/icons/window-close.svg" ) );
				buttonURLs.put(
					StdButtonType.CHEST,
					loader.get()
						.getResource(
							"gui/control/icons/chest.svg" ) );
				buttonURLs.put(
					StdButtonType.PAINT,
					loader.get()
						.getResource(
							"gui/control/icons/applications-graphics.svg" ) );
				buttonURLs.put(
					StdButtonType.NEWITEM,
					loader.get()
						.getResource(
							"gui/control/icons/new-item.svg" ) );
				buttonURLs.put(
					StdButtonType.SHAPES,
					loader.get()
						.getResource(
							"gui/control/icons/shapes.svg" ) );
				buttonURLs.put(
					StdButtonType.UNDO,
					loader.get()
						.getResource(
							"gui/control/icons/edit-undo.svg" ) );
				buttonURLs
					.put(
						StdButtonType.PARENT,
						loader.get()
							.getResource(
								"gui/control/icons/parent-folder.svg" ) );

				buttonURLs.put(
					StdButtonType.BACK,
					loader.get()
						.getResource(
							"gui/control/icons/go-previous.svg" ) );

				buttonURLs.put(
					StdButtonType.NO,
					loader.get()
						.getResource(
							"gui/control/icons/no.svg" ) );

				buttonURLs.put(
					StdButtonType.YES,
					loader.get()
						.getResource(
							"gui/control/icons/emblem-default.svg" ) );
				buttonURLs.put(
					StdButtonType.OK,
					loader.get()
						.getResource(
							"gui/control/icons/emblem-default.svg" ) );

				buttonURLs.put(
					StdButtonType.DICE,
					loader.get()
						.getResource(
							"gui/control/icons/dice.svg" ) );

				buttonURLs.put(
					StdButtonType.DOWN,
					loader.get()
						.getResource(
							"gui/control/icons/go-down.svg" ) );
				buttonURLs.put(
					StdButtonType.UP,
					loader.get()
						.getResource(
							"gui/control/icons/go-up.svg" ) );

				buttonURLs.put(
					StdButtonType.FIRST,
					loader.get()
						.getResource(
							"gui/control/icons/go-first.svg" ) );
				buttonURLs.put(
					StdButtonType.LAST,
					loader.get()
						.getResource(
							"gui/control/icons/go-last.svg" ) );
				buttonURLs.put(
					StdButtonType.NEXT,
					loader.get()
						.getResource(
							"gui/control/icons/go-next.svg" ) );
				buttonURLs.put(
					StdButtonType.PREV,
					loader.get()
						.getResource(
							"gui/control/icons/go-previous.svg" ) );

				buttonURLs.put(
					StdButtonType.ADDRESS_BOOK,
					loader.get()
						.getResource(
							"gui/control/icons/address-book-new.svg" ) );
				buttonURLs
					.put(
						StdButtonType.CANCEL,
						loader.get()
							.getResource(
								"gui/control/icons/window-close.svg" ) );
				buttonURLs.put(
					StdButtonType.OPEN,
					loader.get()
						.getResource(
							"gui/control/icons/open.svg" ) );
				buttonURLs.put(
					StdButtonType.FOLDER,
					loader.get()
						.getResource(
							"gui/control/icons/folder.svg" ) );
				buttonURLs.put(
					StdButtonType.FOLDER_OPEN,
					loader.get()
						.getResource(
							"gui/control/icons/folder_open.svg" ) );
				buttonURLs.put(
					StdButtonType.COPY,
					loader.get()
						.getResource(
							"gui/control/icons/edit-copy.svg" ) );
				buttonURLs.put(
					StdButtonType.CLEAR,
					loader.get()
						.getResource(
							"gui/control/icons/edit-clear.svg" ) );

			}
			catch (ResourceFinderException ex)
			{
				throw new RuntimeException( ex );
			}

		}
		URL result = buttonURLs.get( toGet );
		if (result == null)
		{
			throw new NullPointerException( toGet.toString() );
		}
		return result;
	}

	public static ScalableSVGIcon getStdIcon( StdButtonType toGet )
	{
		return getStdIcon( getIconURL( toGet ) );
	}

	public static ScalableSVGIcon getStdIcon( String path )
	{
		return getStdIcon( getFinder().getResource(
			path ) );
	}

	public static ScalableSVGIcon getStdIcon( String path, Dimension dSize )
	{
		ScalableSVGIcon svgI = getStdIcon( path );
		svgI.setSize( dSize );
		return svgI;
	}

	public static ScalableSVGIcon getStdIcon( URL path )
	{
		ScalableSVGIcon toCreate = new ScalableSVGIcon( path );
		ScreenRelativeIconSizer.setSize( toCreate );
		return toCreate;
	}
}
