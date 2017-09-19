package uk.ac.cardiff.raptor.ui.secure;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class SimpleAuthenticationProvider implements AuthenticationProvider {

	// Logger
	private static final Logger log = LoggerFactory.getLogger(SimpleAuthenticationProvider.class);

	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

		log.debug("Calling the Simple Authentication Provider with user details {}", authentication);

		final String name = authentication.getName();
		final String password = authentication.getCredentials().toString();

		if (name != null && password != null && name.equals("raptor") && password.equals("raptor")) {
			final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

			return new UsernamePasswordAuthenticationToken("raptor", "raptor", authorities);
		}

		// authZ failed
		return null;

	}

	@Override
	public boolean supports(final Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
