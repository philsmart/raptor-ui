package uk.ac.cardiff.raptor.ui.secure;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import uk.ac.cardiff.raptor.ui.model.AuthSystem;

public class SecurityContextHelper {

	private static final Logger log = LoggerFactory.getLogger(SecurityContextHelper.class);

	/**
	 * Retrieves the {@link RaptorUser} from the {@link SecurityContextHolder}. If
	 * the {@link Authentication} object is not a {@link RaptorUser} (should always
	 * be), an empty {@link Optional} is returned;
	 * 
	 * @return a {@link RaptorUser} object if one exists in the Security Context.
	 */
	public static Optional<RaptorUser> retrieveRaptorUser() {

		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getPrincipal() != null) {

			if (auth.getPrincipal() instanceof RaptorUser) {
				return Optional.of((RaptorUser) auth.getPrincipal());

			}
		}
		return Optional.empty();
	}

	@Nonnull
	public static List<String> retrieveRaptorUserAuthorisedServiceIds(final AuthSystem system) {
		final Optional<RaptorUser> user = retrieveRaptorUser();

		if (user.isPresent() == false) {
			return Collections.emptyList();
		} else {
			return user.get().getServiceIdMappings().getSystemToServiceIdMapping().get(system.name());
		}
	}

	@Nonnull
	public static String getRaptorUserUsernameOrDefault(final String defaultUsername) {
		final Optional<RaptorUser> user = retrieveRaptorUser();

		if (user.isPresent() == true) {
			return user.get().getUsername();
		}
		return defaultUsername;
	}

}
