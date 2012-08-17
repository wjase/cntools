package com.cybernostics.examples.lib.gui.control;




import com.cybernostics.lib.gui.control.CLabel;
import javax.swing.JFrame;

/**
 * Encrypted jlabel only decrypts when painting
 * 
 * @author jasonw
 * 
 */
public class CLabelExample 
{

	public static void main( String[] args )
	{
		String sText = "[fc403fd7445d36453ea474041993e3f6bd7c5393222a25ef]";
		JFrame jf = new JFrame( "label test" );

		CLabel cl = new CLabel( sText );
		cl.setAspiration( "MyKey789" );
		jf.getContentPane().add( cl );
		jf.setSize( 400, 400 );
		jf.setVisible( true );

		System.out.println( cl.getText() );
	}


}
