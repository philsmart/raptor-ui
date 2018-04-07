package uk.ac.cardiff.raptor.ui.secure;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import uk.ac.cardiff.raptor.ui.model.account.ServiceIDAuthZMapping;

/**
 * Extension to the normal {@link User} class to hold details specific to
 * Raptor.
 * 
 * @author philsmart
 *
 */
public class RaptorUser extends User {

	// TODO, is this redundent with the service mapping below?
	private String idpEntityId;

	/**
	 * Within the context of the {@link User} object, this class should not be
	 * modified.
	 */
	private final ServiceIDAuthZMapping serviceIdMappings;

	/**
	 * Default serial UUID
	 */
	private static final long serialVersionUID = -7619227996530801609L;

	public RaptorUser(final String username, final String password, final boolean enabled,
			final boolean accountNonExpired, final boolean credentialsNonExpired, final boolean accountNonLocked,
			final Collection<? extends GrantedAuthority> authorities, final ServiceIDAuthZMapping mappings) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.serviceIdMappings = mappings;

	}

	/**
	 * @return the idpEntityId
	 */
	public String getIdpEntityId() {
		return idpEntityId;
	}

	/**
	 * @param idpEntityId
	 *            the idpEntityId to set
	 */
	public void setIdpEntityId(final String idpEntityId) {
		this.idpEntityId = idpEntityId;
	}

	/**
	 * Defensive copy of the class, so this class held here is not mutable.
	 * 
	 * @return the serviceIdMappings
	 */
	public ServiceIDAuthZMapping getServiceIdMappings() {
		return new ServiceIDAuthZMapping(serviceIdMappings);
	}

}
