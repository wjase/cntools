package com.cybernostics.lib.gui.adapter;

import java.awt.Component;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.text.JTextComponent;

/**
 * ComponentValueAdapter gets and sets values from Java forms.
 * The values get serialised into and out of XML
 * @author jasonw
 *
 */
public class ComponentValueAdaptor
{

	/**
	 * Builds a composite qualified name in a dot separated form to identify a
	 * component in the component hierarchy. The name is based on the setName
	 * method being called on selected components to get and set property values
	 * for them.
	 * 
	 * @param parentName
	 * @param currentName
	 * @return
	 */
	private static String buildComponentName( String parentName,
		String currentName )
	{
		return ( ( parentName.length() == 0 ) ? currentName
			: parentName
				+ ( ( ( currentName != null ) && ( currentName.length() > 0 ) ) ? "."
					+ currentName
					: "" ) );
	}

	/**
	 * Retrieves a value 
	 * @param component
	 * @return
	 */
	public static Object getValue( JComponent component )
	{
		if (component instanceof XMLPropertyComponent)
		{
			XMLPropertyComponent xpc = (XMLPropertyComponent) component;
			return xpc.getControlXMLProperty();
		}
		if (component instanceof JTextComponent)
		{
			JTextComponent jtc = (JTextComponent) component;
			return jtc.getText();
		}
		else
		{
			if (component instanceof JList)
			{
				JList theList = (JList) component;
				return theList.getSelectedValue();
			}
			if (component instanceof JCheckBox)
			{
				JCheckBox jcb = (JCheckBox) component;
				return jcb.getModel()
					.isSelected();
			}
			if (component instanceof JRadioButton)
			{
				JRadioButton jrb = (JRadioButton) component;
				return jrb.getModel()
					.isSelected();
			}

		}
		return null;
	}

	/**
	 * This method extracts values from a component hierachy based on their
	 * names and the names of the containers. If a component is contained in
	 * nested panels which have no names they are ignored in the naming scheme.
	 * 
	 * @param parentPath
	 *            a dot separated path representing the current components
	 *            container name. To set the names of the containers, you must
	 *            have called the setName() method for the containers
	 * @param component
	 *            the current component to examine
	 * @param propertyMap
	 *            a map to be populated with component values
	 */
	public static void recursiveGetProperties( String parentPath,
		JComponent component,
		Map< String, Object > propertyMap )
	{
		String componentName = component.getName();
		String qualifiedName = buildComponentName(
			parentPath,
			componentName );

		if (component.getComponentCount() == 0)
		{
			if (( componentName != null ) && ( componentName.length() > 0 ))
			{
				// // check if there is a value to set
				// if ( theWizard.getProperties().containsKey( qualifiedName ) )
				// {
				synchronized (propertyMap)
				{
					Object currentValue = ComponentValueAdaptor.getValue( component );
					// System.out.println( "Get property :" + qualifiedName +
					// " to " + currentValue );
					propertyMap.put(
						qualifiedName,
						currentValue );
				}
				// }
			}
			return;
		}
		else
		{
			// I have children dive in and harvest their values
			for (int index = 0; index < component.getComponentCount(); ++index)
			{
				Component c = component.getComponent( index );
				if (c instanceof JComponent)
				{
					JComponent comp = (JComponent) c;
					recursiveGetProperties(
						qualifiedName,
						comp,
						propertyMap );
				}
			}
		}

	}

	/**
	 * This method takes a property map and applies the properties to a
	 * hierarchy of components. <br>
	 * For text components the value is the contained text.<br>
	 * For radio buttons the value is 'true' if the item is selected, 'false'
	 * otherwise<br>
	 * For other components you need to implement XMLProperty Component to
	 * serialise and deserialise the components
	 * 
	 * @param parentPath
	 * @param component
	 * @param propertyMap
	 */
	public static void recursiveSetProperties( String parentPath,
		JComponent component,
		Map< String, Object > propertyMap )
	{
		String componentName = component.getName();
		String qualifiedName = buildComponentName(
			parentPath,
			componentName );

		// System.out.println( qualifiedName );

		if (component.getComponentCount() == 0)
		{
			if (( componentName != null ) && ( componentName.length() > 0 ))
			{
				// check if there is a value to set
				if (propertyMap.containsKey( qualifiedName ))
				{
					synchronized (propertyMap)
					{
						Object valueToSet = propertyMap.get( qualifiedName );
						// System.out.println( "Set component value: " +
						// qualifiedName + " to " + valueToSet );
						ComponentValueAdaptor.setValue(
							component,
							valueToSet );
					}

				}
			}
			return;
		}
		else
		{
			// I have children dive in and harvest their values
			for (int index = 0; index < component.getComponentCount(); ++index)
			{
				Component c = component.getComponent( index );
				if (c instanceof JComponent)
				{
					JComponent comp = (JComponent) c;
					recursiveSetProperties(
						qualifiedName,
						comp,
						propertyMap );

				}
			}
		}

	}

	// Applies a value to the components
	public static void setValue( JComponent component, Object object )
	{
		if (component instanceof XMLPropertyComponent)
		{
			XMLPropertyComponent xpc = (XMLPropertyComponent) component;
			xpc.setControlXMLProperty( (String) object );
			return;
		}
		if (component instanceof JTextComponent)
		{
			JTextComponent jtc = (JTextComponent) component;
			jtc.setText( (String) object );
			return;
		}
		else
		{
			if (component instanceof JList)
			{
				JList theList = (JList) component;
				theList.setSelectedValue(
					object,
					true );
				return;
			}
			if (component instanceof JCheckBox)
			{
				JCheckBox jcb = (JCheckBox) component;
				jcb.getModel()
					.setSelected(
						(Boolean) object );
				return;
			}
			if (component instanceof JRadioButton)
			{
				JRadioButton jrb = (JRadioButton) component;
				jrb.getModel()
					.setSelected(
						(Boolean) object );
				return;
			}

		}

	}

}
