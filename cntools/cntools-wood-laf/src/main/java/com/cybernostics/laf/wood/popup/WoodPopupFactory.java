package com.cybernostics.laf.wood.popup;

import java.awt.Component;

import javax.swing.Popup;
import javax.swing.PopupFactory;

public class WoodPopupFactory extends javax.swing.PopupFactory
{

	private static WoodPopupFactory popupFactory = new WoodPopupFactory();

	private static PopupFactory backupPopupFactory;

	public static void install()
	{
		if (backupPopupFactory == null)
		{
			backupPopupFactory = getSharedInstance();
			setSharedInstance( popupFactory );
		}
	}

	public static void uninstall()
	{
		if (backupPopupFactory == null)
		{
			return;
		}
		setSharedInstance( backupPopupFactory );
		backupPopupFactory = null;
	}

	public Popup getPopup( Component owner,
		Component contents,
		int ownerX,
		int ownerY )
	{
		final Popup realPopup = super.getPopup(
			owner,
			contents,
			ownerX,
			ownerY );
		return new WoodPopup( owner, contents, ownerX, ownerY, realPopup );
	}
}