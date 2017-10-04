package uk.ac.cardiff.raptor.ui.secure;

import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holds information about {@link MetadataProvider}s loaded from properties
 * files. Is then used to instantiate providers by the {@link SecurityConfig}
 * configuration class
 * 
 * @author philsmart
 *
 */
public class MetadataProviderInfo {

	private static final Logger log = LoggerFactory.getLogger(MetadataProviderInfo.class);

	private String metadataURL;

	private String keystoreSignatureAlias;

	private final boolean doTrustCheck = true;

	/**
	 * @return the doTrustCheck
	 */
	public boolean isDoTrustCheck() {
		return doTrustCheck;
	}

	/**
	 * @return the keystoreSignatureAlias
	 */
	public String getKeystoreSignatureAlias() {
		return keystoreSignatureAlias;
	}

	/**
	 * @param keystoreSignatureAlias
	 *            the keystoreSignatureAlias to set
	 */
	public void setKeystoreSignatureAlias(final String keystoreSignatureAlias) {
		this.keystoreSignatureAlias = keystoreSignatureAlias;
	}

	/**
	 * @return the metadataURL
	 */
	public String getMetadataURL() {
		return metadataURL;
	}

	/**
	 * @param metadataURL
	 *            the metadataURL to set
	 */
	public void setMetadataURL(final String metadataURL) {
		this.metadataURL = metadataURL;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("MetadataProviderInfo [metadataURL=");
		builder.append(metadataURL);
		builder.append(", keystoreSignatureAlias=");
		builder.append(keystoreSignatureAlias);
		builder.append(", doTrustCheck=");
		builder.append(doTrustCheck);
		builder.append("]");
		return builder.toString();
	}

}
