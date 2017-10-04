package uk.ac.cardiff.raptor.ui.service;

import java.util.List;
import java.util.Optional;

import javax.xml.namespace.QName;

import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.OrganizationDisplayName;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.samlext.saml2mdui.Logo;
import org.opensaml.samlext.saml2mdui.UIInfo;
import org.opensaml.xml.XMLObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.stereotype.Service;

/**
 * Provides convience methods for working with the currently configured set of
 * {@link MetadataProvider}s used for Authentication. Does not work for name
 * lookup for formatting entityIds for display.
 * 
 * @author philsmart
 *
 */
@Service
@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
public class MetadataHelperService {

	private static final Logger log = LoggerFactory.getLogger(MetadataHelperService.class);

	public static final String[] SUPPORTED_SAML_SSO_PROTOCOLS = new String[] { SAMLConstants.SAML10P_NS,
			SAMLConstants.SAML11P_NS, SAMLConstants.SAML20P_NS };

	@Autowired
	private MetadataManager metadata;

	/**
	 * Returns the first organisation name from the Organisations element of the
	 * SAML Metadata relating to the entityId input.
	 * 
	 * @param entityId
	 *            the identifier of the SAML MetadataDescriptor
	 * @return and {@link Optional} of type {@link String} of the org name, or
	 *         null.
	 */
	public Optional<String> getOrgDisplayName(final String entityId) {
		try {
			final EntityDescriptor entity = metadata.getEntityDescriptor(entityId);

			if (entity.getOrganization() != null && entity.getOrganization().getDisplayNames() != null) {
				final List<OrganizationDisplayName> orgNames = entity.getOrganization().getDisplayNames();
				if (orgNames.isEmpty() == false) {
					return Optional.ofNullable(orgNames.get(0).getName().getLocalString());
				}
			}
		} catch (final Exception e) {

		}
		return Optional.empty();
	}

	/**
	 * 
	 * Chooses the largest sized (determined by height only) logo from the mdui
	 * SAML extensions if present, if not, empty Optional is returned.
	 * 
	 * @param entityId
	 *            the identifier of the Entity to lookup
	 * @return
	 * @throws MetadataProviderException
	 */

	public Optional<String> getMduiLogo(final String entityId) throws MetadataProviderException {

		final EntityDescriptor entity = metadata.getEntityDescriptor(entityId);

		Logo chosenLogo = null;

		Extensions extensions = null;

		/*
		 * even though a loop here, there should only be one-of these protocols
		 * supported per entity, so only every one will give reference to
		 * extensions.
		 */
		for (final String ssoProtocol : SUPPORTED_SAML_SSO_PROTOCOLS) {
			if (entity.getIDPSSODescriptor(ssoProtocol) != null) {
				extensions = entity.getIDPSSODescriptor(ssoProtocol).getExtensions();
			}
		}

		for (final String ssoProtocol : SUPPORTED_SAML_SSO_PROTOCOLS) {
			if (entity.getSPSSODescriptor(ssoProtocol) != null) {
				extensions = entity.getSPSSODescriptor(ssoProtocol).getExtensions();
			}
		}

		if (extensions != null) {
			final List<XMLObject> uiinfos = extensions.getUnknownXMLObjects(
					new QName(UIInfo.MDUI_NS, UIInfo.DEFAULT_ELEMENT_LOCAL_NAME, UIInfo.MDUI_PREFIX));
			for (final XMLObject uiinfo : uiinfos) {
				if (uiinfo instanceof UIInfo) {
					final UIInfo info = (UIInfo) uiinfo;
					final List<Logo> logos = info.getLogos();

					for (final Logo logo : logos) {
						log.trace("Logo URL found {}", logo.getURL());
						if (chosenLogo == null) {
							chosenLogo = logo;
						} else if (chosenLogo.getHeight() < logo.getHeight()) {
							chosenLogo = logo;
						}
					}
				}

			}
		}

		if (chosenLogo != null) {
			log.trace("Choosing logo [{}}", chosenLogo.getURL());
			return Optional.of(chosenLogo.getURL());
		}
		return Optional.empty();
	}

}
