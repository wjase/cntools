/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.cybernostics.lib.io.ssl;

import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class CustomSSLSocketFactory extends SSLSocketFactory
{

	private SSLSocketFactory factory;

	public CustomSSLSocketFactory()
	{
		//System.out.println( "CustomSSLSocketFactory instantiated" );
		try
		{

			SSLContext sslcontext = SSLContext.getInstance( "TLS" );
			sslcontext.init(
				null, // default KeyManager 
				new TrustManager[]
					{
						new CustomTrustManager()
					},
								null );
			factory = (SSLSocketFactory) sslcontext.getSocketFactory();
		}
		catch (Exception ex)
		{
			throw new RuntimeException( ex );
		}
	}

	private static SingletonInstance< CustomSSLSocketFactory > def = new SingletonInstance< CustomSSLSocketFactory >()
	{

		@Override
		protected CustomSSLSocketFactory createInstance()
		{
			return new CustomSSLSocketFactory();
		}

	};

	@Override
	public Socket createSocket() throws IOException
	{
		return factory.createSocket();
	}

	public static SocketFactory getDefault()
	{
		return def.get();
	}

	@Override
	public Socket createSocket( Socket socket, String s, int i, boolean flag )
			throws IOException
	{
		return factory.createSocket(
			socket,
			s,
			i,
			flag );
	}

	@Override
	public Socket createSocket( InetAddress inaddr, int i,
								InetAddress inaddr1, int j ) throws IOException
	{
		return factory.createSocket(
			inaddr,
			i,
			inaddr1,
			j );
	}

	@Override
	public Socket createSocket( InetAddress inaddr, int i ) throws
			IOException
	{
		return factory.createSocket(
			inaddr,
			i );
	}

	@Override
	public Socket createSocket( String s, int i, InetAddress inaddr, int j )
			throws IOException
	{
		return factory.createSocket(
			s,
			i,
			inaddr,
			j );
	}

	@Override
	public Socket createSocket( String s, int i ) throws IOException
	{
		return factory.createSocket(
			s,
			i );
	}

	@Override
	public String[] getDefaultCipherSuites()
	{
		return factory.getSupportedCipherSuites();
	}

	@Override
	public String[] getSupportedCipherSuites()
	{
		return factory.getSupportedCipherSuites();
	}

}