package uk.ac.cardiff.raptor.ui.secure;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SAMLUserDetailsServiceImpl implements SAMLUserDetailsService {

	// Logger
	private static final Logger log = LoggerFactory.getLogger(SAMLUserDetailsServiceImpl.class);

	@Override
	public Object loadUserBySAML(final SAMLCredential credential) throws UsernameNotFoundException {

		// The method is supposed to identify local account of user referenced
		// by
		// data in the SAML assertion and return UserDetails object describing
		// the user.

		final String userID = credential.getNameID().getValue();

		log.info("SAML Loging from local entityID {} and remote entityID {}", credential.getLocalEntityID(),
				credential.getRemoteEntityID());

		credential.getAttributes().forEach(attr -> log.debug("NameId [{}] SAML attribute is [{}->{}]", userID,
				attr.getName(), attr.getFriendlyName()));

		log.info("User with namneID [{}] has authenticated and authorised", userID);
		final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		final GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
		authorities.add(authority);

		final RaptorUser user = new RaptorUser(userID, "null", true, true, true, true, authorities);
		user.setIdpEntityId(credential.getRemoteEntityID());

		// In a real scenario, this implementation has to locate user in a
		// arbitrary
		// dataStore based on information present in the SAMLCredential and
		// returns such a date in a form of application specific UserDetails
		// object.
		return user;
	}

}
