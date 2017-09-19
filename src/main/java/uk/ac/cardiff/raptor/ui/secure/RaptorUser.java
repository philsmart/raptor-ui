package uk.ac.cardiff.raptor.ui.secure;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * Extension to the normal {@link User} class to hold details specific to
 * Raptor.
 * 
 * @author philsmart
 *
 */
public class RaptorUser extends User {

	private String idpEntityId;

	/**
	 * Default serial UUID
	 */
	private static final long serialVersionUID = -7619227996530801609L;

	public RaptorUser(final String username, final String password, final boolean enabled,
			final boolean accountNonExpired, final boolean credentialsNonExpired, final boolean accountNonLocked,
			final Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

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

}
