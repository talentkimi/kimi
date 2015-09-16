package core.net;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.io.file.TextFile;
import core.util.ClosableHandler;

/**
 * A Network Trust Manager.
 */
public final class NetTrustManager implements X509TrustManager {

	private static final Logger log = LoggerFactory.getLogger(NetTrustManager.class);


	public static final KeyStore loadKeysFromFile(String type, String filename, String password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
		KeyStore keyStore = KeyStore.getInstance(type);
		FileInputStream fis = new FileInputStream(new TextFile(filename));
		try{
			keyStore.load(fis, password.toCharArray());
		} finally {
			ClosableHandler.closeSafely(fis);
		}
		return keyStore;
	}

	/** The trust manager. */
	private final X509TrustManager sunJSSEX509TrustManager;
	/** The socket factory. */
	private final SSLSocketFactory socketFactory;
	/** Warnings enabled. */
	private boolean warningsEnabled = false;
	/** Strict validate. */
	private boolean strictValidation = false;

	/**
	 * Returns the socket factory.
	 * @return the socket factory.
	 */
	public SSLSocketFactory getSocketFactory() {
		return socketFactory;
	}

	/**
	 * Sets whether warnings are enabled.
	 * @param enable true to enable warnings.
	 */
	public void setWarningsEnabled(boolean enable) {
		this.warningsEnabled = enable;
	}

	/**
	 * Sets whether warnings are enabled.
	 * @param enable true to enable warnings.
	 */
	public void setStrictValidation(boolean strict) {
		this.strictValidation = strict;
	}

	/**
	 * Creates a new trust manager..
	 * @throws SecurityException
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 */
	public NetTrustManager(String filename, String password) throws IOException, GeneralSecurityException {
		KeyStore keyStore = loadKeysFromFile("JKS", filename, password);

		// Trust Managers
		TrustManagerFactory managerFactory = TrustManagerFactory.getInstance("SunX509", "SunJSSE");
		managerFactory.init(keyStore);
		TrustManager managers[] = managerFactory.getTrustManagers();
		for (int i = 0; i < managers.length; i++) {
			if (managers[i] instanceof X509TrustManager) {
				sunJSSEX509TrustManager = (X509TrustManager) managers[i];

				keyStore = loadKeysFromFile("PKCS12", "java/data/ssl/jetblue.pfx", "travelfusion");
				
				KeyManagerFactory keyFactory = KeyManagerFactory.getInstance("SunX509");
				keyFactory.init(keyStore, "travelfusion".toCharArray());
				KeyManager[] keyManagers = keyFactory.getKeyManagers();

				managers = new TrustManager[]{this};
				SSLContext context = SSLContext.getInstance("TLS");
				context.init(keyManagers, managers, null);
				this.socketFactory = context.getSocketFactory();
				return;
			}
		}

		// Failed
		throw new NoSuchProviderException("Unable to find a X509TrustManager in the SunX509/SunJSSE TrustManagerFactory");
	}

	/**
	 * Check the if the client certificate chain is trusted.
	 */
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		if (strictValidation || warningsEnabled) {
			try {
				sunJSSEX509TrustManager.checkClientTrusted(chain, authType);
			} catch (CertificateException exception) {
				if (strictValidation) {
					throw exception;
				}
				if (warningsEnabled) {
					if (log.isDebugEnabled()) log.debug ("[Warning]", exception);
				}
			}
		}
	}

	/*
	 * Delegate to the default trust manager.
	 */
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		if (strictValidation || warningsEnabled) {
			try {
				sunJSSEX509TrustManager.checkServerTrusted(chain, authType);
			} catch (CertificateException exception) {
				if (strictValidation) {
					throw exception;
				}
				if (warningsEnabled) {
					if (log.isDebugEnabled()) log.debug ("[Warning]", exception);
				}
			}
		}
	}

	/**
	 * Returns the accepted issuers.
	 */
	public X509Certificate[] getAcceptedIssuers() {
		return sunJSSEX509TrustManager.getAcceptedIssuers();
	}
}
