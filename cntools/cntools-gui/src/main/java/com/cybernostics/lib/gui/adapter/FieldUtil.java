package com.cybernostics.lib.gui.adapter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

public class FieldUtil
{

	public static void addLabelField( JPanel parent,
		String labelText,
		JTextComponent textField,
		String FieldId,
		GridBagConstraints gbc,
		String helpHint )
	{
		gbc = ( gbc == null ) ? new GridBagConstraints() : gbc;

		// namedComponents.put( FieldId, textField );
		textField.setName( FieldId );
		JPanel fieldPanel = parent;
		GridBagLayout gbl = null;

		if (!( fieldPanel.getLayout() instanceof GridBagLayout ))
		{
			fieldPanel = new JPanel( new GridBagLayout() );
		}

		gbl = (GridBagLayout) fieldPanel.getLayout();

		gbc.insets = new Insets( 2, 2, 2, 2 );

		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.weightx = 1;
		gbc.gridwidth = 1;
		JLabel jlDescription = new JLabel( labelText );
		fieldPanel.add( jlDescription );
		gbl.setConstraints(
			jlDescription,
			gbc );

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 100;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		fieldPanel.add( textField );
		textField.setToolTipText( helpHint );
		gbl.setConstraints(
			textField,
			gbc );

		if (fieldPanel != parent)
		{
			parent.add( fieldPanel );
		}
	}

	/**
	 * Returns the named component
	 * 
	 * @param sName
	 * @return
	 */
	public static JComponent getNamedComponent( String sName, Container jp )
	{
		for (Component eachComponent : jp.getComponents())
		{
			String name = eachComponent.getName();
			if (( name != null ) && name.equals( sName ))
			{
				return (JComponent) eachComponent;
			}
			if (eachComponent instanceof Container)
			{
				JComponent subComponent = getNamedComponent(
					sName,
					(Container) eachComponent );
				if (subComponent != null)
				{
					return subComponent;
				}
			}
		}
		return null;
	}

	public static JTextField getTextField( String sName, Container container )
	{
		JComponent jc = getNamedComponent(
			sName,
			container );
		if (( jc != null ) && ( jc instanceof JTextField ))
		{
			return (JTextField) jc;
		}
		return null;
	}

	public static JLabel getLabel( String sName, Container container )
	{
		JComponent jc = getNamedComponent(
			sName,
			container );
		if (( jc != null ) && ( jc instanceof JLabel ))
		{
			return (JLabel) jc;
		}
		return null;
	}

	public static boolean getCheckBoxState( String sName, Container container )
	{
		JComponent jc = getNamedComponent(
			sName,
			container );
		if (( jc != null ) && ( jc instanceof JCheckBox ))
		{
			return ( (JCheckBox) jc ).isSelected();
		}
		return false;
	}

	public static boolean getRadioBoxState( String sName, Container container )
	{
		JComponent jc = getNamedComponent(
			sName,
			container );
		if (( jc != null ) && ( jc instanceof JRadioButton ))
		{
			return ( (JRadioButton) jc ).isSelected();
		}
		return false;
	}

	public static void selectRadioButton( String sName,
		Container container,
		boolean state )
	{
		JComponent jc = getNamedComponent(
			sName,
			container );
		if (( jc != null ) && ( jc instanceof JRadioButton ))
		{
			( (JRadioButton) jc ).setSelected( state );
		}
	}

	public static void setCheckBoxState( String sName,
		Container container,
		boolean state )
	{
		JComponent jc = getNamedComponent(
			sName,
			container );
		if (( jc != null ) && ( jc instanceof JCheckBox ))
		{
			( (JCheckBox) jc ).setSelected( state );
		}
	}

	public static void main( String[] args )
	{
		JFrame jfFrame = new JFrame();
		jfFrame.setSize(
			400,
			400 );
		JPanel jp = new JPanel( new GridBagLayout() );
		jp.setBorder( BorderFactory.createCompoundBorder(
			new EmptyBorder( 20, 20, 20, 20 ),
			BorderFactory
				.createLineBorder( Color.blue ) ) );

		FieldUtil.addLabelField(
			jp,
			"Field Name",
			new JTextField(),
			"FieldName",
			null,
			"Help text for field" );
		FieldUtil.addLabelField(
			jp,
			"Field Name2",
			new JTextField(),
			"FieldName2",
			null,
			"Help text for field" );
		FieldUtil.getTextField(
			"FieldName",
			jp )
			.setText(
				"Fill me with text" );

		jfFrame.setVisible( true );

		jfFrame.getContentPane()
			.add(
				jp );
		jfFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

	}

	Map< String, JComponent > namedComponents = new HashMap< String, JComponent >();
}
