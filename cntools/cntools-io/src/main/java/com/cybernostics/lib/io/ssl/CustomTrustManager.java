/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.cybernostics.lib.io.ssl;

import com.cybernostics.lib.collections.ArrayUtils;
import com.cybernostics.lib.collections.IterableArray;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * This Trust Manager is "naive" because it trusts everyone.
 *
 */
public class CustomTrustManager implements X509TrustManager
{

	public CustomTrustManager()
	{
	}

	private static SingletonInstance< KeyStore > keys = new SingletonInstance< KeyStore >()
	{

		@Override
		protected KeyStore createInstance()
		{
			try
			{
				KeyStore ks = KeyStore.getInstance( KeyStore.getDefaultType() );
				File file = new File( "jssecacerts" );
				if (file.isFile() == false)
				{
					char SEP = File.separatorChar;
					File dir = new File( System.getProperty( "java.home" )
						+ SEP +
							"lib" + SEP + "security" );
					file = new File( dir, "jssecacerts" );
					if (file.isFile() == false)
					{
						file = new File( dir, "cacerts" );
					}
				}
				//System.out.println( "Loading KeyStore " + file + "..." );
				InputStream in = new FileInputStream( file );
				String pw = "changeit";
				ks.load(
					in,
					pw.toCharArray() );
				in.close();
				return ks;

			}
			catch (Exception ex)
			{
				throw new RuntimeException( ex );
			}
		}

	};

	private static ArrayList< X509Certificate > customCerts = new ArrayList< X509Certificate >();

	private static SingletonInstance< X509TrustManager > defaultManager = new SingletonInstance< X509TrustManager >()
	{

		@Override
		protected X509TrustManager createInstance()
		{
			try
			{
				KeyStore ts = keys.get();

				TrustManagerFactory tmf = TrustManagerFactory.getInstance(
						TrustManagerFactory.getDefaultAlgorithm() );
				tmf.init( ts );

				// acquire X509 trust manager from factory
				TrustManager tms[] = tmf.getTrustManagers();
				for (int i = 0; i < tms.length; ++i)
				{
					if (tms[ i ] instanceof X509TrustManager)
					{
						return (X509TrustManager) tms[ i ];
					}
				}
			}
			catch (KeyStoreException ex)
			{
				Logger.getLogger(
					CustomTrustManager.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex );
			}
			catch (NoSuchAlgorithmException ex)
			{
				Logger.getLogger(
					CustomTrustManager.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex );
			}

			return null;

		}

	};

	public static X509TrustManager getDefaultManager()
	{
		return defaultManager.get();
	}

	private static void updateIssuers()
	{
		acceptedIssuers = defaultManager.get()
			.getAcceptedIssuers();
		if (!customCerts.isEmpty())
		{
			X509Certificate[] dflt = defaultManager.get()
				.getAcceptedIssuers();
			X509Certificate[] combined = new X509Certificate[ acceptedIssuers.length
				+ customCerts.size() ];
			ArrayUtils.copy(
				dflt,
				combined );
			ArrayUtils.copy(
				customCerts,
				combined,
				dflt.length );
		}
	}

	public static void addCertificate( X509Certificate cert )
	{
		customCerts.add( cert );
		updateIssuers();
	}

	public static void addCertificate( InputStream is )
	{
		try
		{
			CertificateFactory cf = CertificateFactory.getInstance( "X.509" );
			customCerts.add( (X509Certificate) cf.generateCertificate( is ) );
			updateIssuers();
		}
		catch (CertificateException ex)
		{
			Logger.getLogger(
				CustomTrustManager.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}

	}

	private static X509Certificate[] acceptedIssuers;

	/**
	 * Doesn't throw an exception, so this is how it approves a certificate.
	 *
	 * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[], String)
	 *
	 */
	@Override
	public void checkClientTrusted( X509Certificate[] cert, String authType )
			throws CertificateException
	{
		try
		{
			defaultManager.get()
				.checkClientTrusted(
					cert,
					authType );
		}
		catch (CertificateException cex)
		{
		}
	}

	/**
	 * Doesn't throw an exception, so this is how it approves a certificate.
	 *
	 * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[], String)
	 *
	 */
	@Override
	public void checkServerTrusted( X509Certificate[] certs, String authType )
			throws CertificateException
	{
		try
		{
			defaultManager.get()
				.checkServerTrusted(
					certs,
					authType );
		}
		catch (Throwable cex)
		{
			boolean foundMatch = false;

			if (!customCerts.isEmpty())
			{
				for (X509Certificate eachCandidate : IterableArray.get( certs ))
				{
					try
					{
						CertificateVerifier.verifyCertificate(
							eachCandidate,
							customCerts );
						foundMatch = true;
						break;
					}
					catch (Exception ex)
					{
					}

				}

			}
			if (!foundMatch)
			{
				throw new CertificateException( cex );
			}
		}
	}

	/**
	 * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
	 *
	 */
	@Override
	public X509Certificate[] getAcceptedIssuers()
	{
		return acceptedIssuers;
	}

}