package uk.ac.cardiff.raptor.ui.secure;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import uk.ac.cardiff.raptor.ui.model.AuthSystem;
import uk.ac.cardiff.raptor.ui.service.UnauthorisedSearchException;

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

	/**
	 * <p>
	 * Checks the user associated with the current thread of execution - found from
	 * the {@link SecurityContext} - is authorised for all the requested
	 * {@code serviceIds}
	 * </p>
	 * 
	 * <p>
	 * Will throw an {@link UnauthorisedSearchException} if the user does not have
	 * rights to *all* the input {@code serviceIds}.
	 * </p>
	 * 
	 * @param serviceIds
	 * @param system
	 */
	public static void checkUserRights(final List<String> serviceIds, final String system) {

		final List<String> allowedUserServiceIds = SecurityContextHelper.retrieveRaptorUserAuthorisedServiceIds(system);

		for (final String serviceId : serviceIds) {

			// any one that is not allowed throws the error
			if (allowedUserServiceIds.contains(serviceId) == false) {

				log.error("User " + SecurityContextHelper.getRaptorUserUsernameOrDefault("uknown")
						+ " is trying to find serviceId " + serviceId + " for authentication system " + system
						+ " for which they are not authorized");
				throw new UnauthorisedSearchException(
						"User " + SecurityContextHelper.getRaptorUserUsernameOrDefault("uknown")
								+ " is trying to find serviceId " + serviceId + " for authentication system " + system
								+ " for which they are not authorized");
			}
		}

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
	public static List<String> retrieveRaptorUserAuthorisedServiceIds(final String system) {
		final Optional<RaptorUser> user = retrieveRaptorUser();

		if (user.isPresent() == false) {
			return Collections.emptyList();
		} else {
			return user.get().getServiceIdMappings().getSystemToServiceIdMapping().get(system);
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
