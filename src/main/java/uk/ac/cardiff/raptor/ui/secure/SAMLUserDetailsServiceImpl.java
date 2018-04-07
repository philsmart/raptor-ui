package uk.ac.cardiff.raptor.ui.secure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;

import uk.ac.cardiff.raptor.ui.model.AuthSystem;
import uk.ac.cardiff.raptor.ui.model.account.ServiceIDAuthZMapping;
import uk.ac.cardiff.raptor.ui.store.ServiceMappingsStorage;

@Service
public class SAMLUserDetailsServiceImpl implements SAMLUserDetailsService {

	// Logger
	private static final Logger log = LoggerFactory.getLogger(SAMLUserDetailsServiceImpl.class);

	/**
	 * Provides information about which serviceIDs this remoteEntityID is allowed to
	 * query.
	 */
	@Inject
	private ServiceMappingsStorage serviceIDMappings;

	/**
	 * Loads the user account details from the SAML assertions. Most information is
	 * dynamically taken from the SAML authentication object. ServiceIDs the user is
	 * entitled to view are loaded from the {@code serviceIDMappings} object. If no
	 * serviceIDs are specified by the existing set of mappings, a new one is
	 * created with the {@code remoteEntityID()} serving as the serviceId the user
	 * is authorised to view.
	 */
	@Override
	public Object loadUserBySAML(final SAMLCredential credential) throws UsernameNotFoundException {

		final String userID = credential.getNameID().getValue();

		log.info("SAML Loging from local entityID {} and remote entityID {}", credential.getLocalEntityID(),
				credential.getRemoteEntityID());

		credential.getAttributes().forEach(attr -> log.debug("NameId [{}] SAML attribute is [{}->{}]", userID,
				attr.getName(), attr.getFriendlyName()));

		final Optional<ServiceIDAuthZMapping> mapping = serviceIDMappings
				.findBySamlEntityId(credential.getRemoteEntityID());

		RaptorUser user = null;

		final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		final GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
		authorities.add(authority);

		if (mapping.isPresent() == false) {
			log.debug(
					"Logged in user [{}] has a remoteEntityID [{}] with no serviceID authZ mappings, creating new mapping record for Shibboleth AuthSystem type",
					credential.getNameID(), credential.getRemoteEntityID());
			final ServiceIDAuthZMapping newMapping = new ServiceIDAuthZMapping(credential.getRemoteEntityID(),
					AuthSystem.SHIBBOLETH_IDP, credential.getRemoteEntityID());

			log.debug("User login from [{}] has resulted in a new mapping being created [{}]",
					credential.getRemoteEntityID(), newMapping.getSystemToServiceIdMapping());

			serviceIDMappings.save(newMapping);
			log.debug("After saving, user mapping is now [{}]", newMapping);
			user = new RaptorUser(userID, "null", true, true, true, true, authorities, newMapping);

		} else {
			log.debug("Logged in user [{}] with remoteEntityId [{}] has previous serviceID authZ mappings [{}]",
					credential.getNameID(), credential.getRemoteEntityID(), mapping.get());
			user = new RaptorUser(userID, "null", true, true, true, true, authorities, mapping.get());

		}

		user.setIdpEntityId(credential.getRemoteEntityID());

		log.info("User with namneID [{}] has authenticated and authorised", userID);

		return user;
	}

}
