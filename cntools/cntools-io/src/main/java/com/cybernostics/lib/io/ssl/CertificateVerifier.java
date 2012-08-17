/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.cybernostics.lib.io.ssl;

import java.security.*;
import java.security.cert.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for building a certification chain for given certificate and verifying
 * it. Relies on a set of root CA certificates and intermediate certificates
 * that will be used for building the certification chain. The verification
 * process assumes that all self-signed certificates in the set are trusted
 * root CA certificates and all other certificates in the set are intermediate
 * certificates.
 *
 * @author Svetlin Nakov
 */
public class CertificateVerifier
{

	/**
	 * Attempts to build a certification chain for given certificate and to verify
	 * it. Relies on a set of root CA certificates and intermediate certificates
	 * that will be used for building the certification chain. The verification
	 * process assumes that all self-signed certificates in the set are trusted
	 * root CA certificates and all other certificates in the set are intermediate
	 * certificates.
	 *
	 * @param cert - certificate for validation
	 * @param additionalCerts - set of trusted root CA certificates that will be
	 * used as "trust anchors" and intermediate CA certificates that will be
	 * used as part of the certification chain. All self-signed certificates
	 * are considered to be trusted root CA certificates. All the rest are
	 * considered to be intermediate CA certificates.
	 * @return the certification chain (if verification is successful)
	 * @throws CertificateVerificationException - if the certification is not
	 * successful (e.g. certification path cannot be built or some
	 * certificate in the chain is expired or CRL checks are failed)
	 */
	public static PKIXCertPathBuilderResult verifyCertificate( X509Certificate cert,
																Collection< X509Certificate > additionalCerts )
	{
		try
		{

			// Prepare a set of trusted root CA certificates
			// and a set of intermediate certificates
			Set< X509Certificate > trustedRootCerts = new HashSet< X509Certificate >();
			Set< X509Certificate > intermediateCerts = new HashSet< X509Certificate >();
			for (X509Certificate additionalCert : additionalCerts)
			{
				if (isSelfSigned( additionalCert ))
				{
					trustedRootCerts.add( additionalCert );
				}
				else
				{
					intermediateCerts.add( additionalCert );
				}
			}

			// Attempt to build the certification chain and verify it
			PKIXCertPathBuilderResult verifiedCertChain =
										verifyCertificate(
											cert,
											trustedRootCerts,
											intermediateCerts );

			//			// Check whether the certificate is revoked by the CRL
			//			// given in its CRL distribution point extension
			//			CRLVerifier.verifyCertificateCRLs(cert);

			// The chain is built and verified. Return it as a result
			return verifiedCertChain;
		}
		catch (GeneralSecurityException ex)
		{
			Logger.getLogger(
				CertificateVerifier.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}

		return null;
	}

	/**
	 * Checks whether given X.509 certificate is self-signed.
	 */
	public static boolean isSelfSigned( X509Certificate cert )
			throws CertificateException, NoSuchAlgorithmException,
					NoSuchProviderException
	{
		try
		{
			// Try to verify certificate signature with its own public key
			PublicKey key = cert.getPublicKey();
			cert.verify( key );
			return true;
		}
		catch (SignatureException sigEx)
		{
			// Invalid signature --> not self-signed
			return false;
		}
		catch (InvalidKeyException keyEx)
		{
			// Invalid key --> not self-signed
			return false;
		}
	}

	/**
	 * Attempts to build a certification chain for given certificate and to verify
	 * it. Relies on a set of root CA certificates (trust anchors) and a set of
	 * intermediate certificates (to be used as part of the chain).
	 *
	 * @param cert - certificate for validation
	 * @param trustedRootCerts - set of trusted root CA certificates
	 * @param intermediateCerts - set of intermediate certificates
	 * @return the certification chain (if verification is successful)
	 * @throws GeneralSecurityException - if the verification is not successful
	 * (e.g. certification path cannot be built or some certificate in the
	 * chain is expired)
	 */
	private static PKIXCertPathBuilderResult verifyCertificate( X509Certificate cert,
																Set< X509Certificate > trustedRootCerts,
																Set< X509Certificate > intermediateCerts )
		throws
			GeneralSecurityException
	{

		// Create the selector that specifies the starting certificate
		X509CertSelector selector = new X509CertSelector();
		selector.setCertificate( cert );

		// Create the trust anchors (set of root CA certificates)
		Set< TrustAnchor > trustAnchors = new HashSet< TrustAnchor >();
		for (X509Certificate trustedRootCert : trustedRootCerts)
		{
			trustAnchors.add( new TrustAnchor( trustedRootCert, null ) );
		}

		// Configure the PKIX certificate builder algorithm parameters
		PKIXBuilderParameters pkixParams =
								new PKIXBuilderParameters( trustAnchors,
									selector );

		// Disable CRL checks (this is done manually as additional step)
		pkixParams.setRevocationEnabled( false );

		// Specify a list of intermediate certificates
		CertStore intermediateCertStore = CertStore.getInstance(
			"Collection",
																	new CollectionCertStoreParameters( intermediateCerts ),
																	Security.getProviders()[ 0 ] );
		pkixParams.addCertStore( intermediateCertStore );

		// Build and verify the certification chain
		CertPathBuilder builder = CertPathBuilder.getInstance(
			"PKIX",
			Security.getProviders()[ 0 ] );
		PKIXCertPathBuilderResult result =
									(PKIXCertPathBuilderResult) builder.build( pkixParams );
		return result;
	}

}
